package de.weinbock;


import javax.servlet.ServletContext;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.session.mgt.SessionManager;

import com.google.inject.Key;
import com.google.inject.binder.AnnotatedBindingBuilder;


public class WeinbockShiroWebModule extends ShiroWebModule {

	public WeinbockShiroWebModule(ServletContext servletContext) {
		super(servletContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureShiroWeb() {
		bindRealm().to(AuthorizingRealm.class).asEagerSingleton();
		bind(Authenticator.class).toInstance(new ModularRealmAuthenticator());
		Key<BasicAccessAuthFilter> customFilter = Key.get(BasicAccessAuthFilter.class);
		addFilterChain("/account/profile", customFilter);
	}
	
	@Override 
    protected void bindSessionManager(final AnnotatedBindingBuilder<SessionManager> bind) {
            bind.to(WebSessionManager.class); 
    } 
}
