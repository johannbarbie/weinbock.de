package de.weinbock;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.restnucleus.PersistenceConfiguration;
import org.restnucleus.filter.PaginationFilter;
import org.restnucleus.filter.PersistenceFilter;
import org.restnucleus.filter.QueryFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import de.weinbock.pojo.AccountPolicy;

public class WeinbockServletConfig extends GuiceServletContextListener {
	public static String basePath;
	public static String resPath;
	public static String captchaPubKey;
	public static String captchaSecKey;
	public static Logger log = LoggerFactory.getLogger(WeinbockServletConfig.class);
	public static Injector injector;
	static {
		basePath = System.getProperty("basePath");
		resPath = System.getProperty("resPath");
		captchaPubKey = System.getProperty("captchaPubKey");
		captchaSecKey = System.getProperty("captchaSecKey");
	}
	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
		final Injector i = getInjector();
	}

	@Override
	protected Injector getInjector() {
		injector = Guice.createInjector(new ServletModule() {
			        	
			@Provides @Singleton @SuppressWarnings("unused")
			PersistenceManagerFactory providePersistence(){
				PersistenceConfiguration pc = new PersistenceConfiguration();
				pc.createEntityManagerFactory();
				return pc.getPersistenceManagerFactory();
			}
			
			@Provides @Singleton @SuppressWarnings("unused")
			AccountPolicy providePolicy(){
				return new AccountPolicy()
					.setEmailMxLookup(true);
			}
        	
        	@Provides @Singleton @SuppressWarnings("unused")
        	public Cache provideCache(){
        		//Create a singleton CacheManager using defaults
        		CacheManager manager = CacheManager.create();
        		//Create a Cache specifying its configuration.
        		Cache testCache = new Cache(new CacheConfiguration("cache", 1000)
        		    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        		    .eternal(false)
        		    .timeToLiveSeconds(7200)
        		    .timeToIdleSeconds(3600)
        		    .diskExpiryThreadIntervalSeconds(0));
        		  manager.addCache(testCache);
        		  return testCache;
        	}
        	
			@Override
			public void configureServlets() {
            	filter("/*").through(GuiceShiroFilter.class);
            	filter("/*").through(PersistenceFilter.class);
            	filter("/*").through(QueryFilter.class);
            	filter("/*").through(PaginationFilter.class);
            	filter("/*").through(CorsFilter.class);
			}

		},new WeinbockShiroWebModule(this.servletContext));
		return injector;
	}
	
    public void deregisterJdbc(){
        // This manually deregisters JDBC driver, which prevents Tomcat 7 from complaining about memory leaks wrto this class
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.info(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
            	log.info(String.format("Error deregistering driver %s", driver));
                e.printStackTrace();
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            log.warn("SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }    	
    }

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Injector injector = (Injector) sce.getServletContext().getAttribute(Injector.class.getName());
		injector.getInstance(PersistenceManagerFactory.class).close();
		deregisterJdbc();
		super.contextDestroyed(sce);
		log.info("ServletContextListener destroyed");
	}

}
