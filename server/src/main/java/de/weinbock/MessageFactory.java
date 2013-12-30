package de.weinbock;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MessageFactory {
	// where to find the templates when running in web container
	public static final String RESOURCE_PATH = "/WEB-INF/templates/";
	// where to find the templates when outside web container
	public static final String LOCAL_RESOURCE_PATH = "src/main/webapp/WEB-INF/templates/";
	public static final String CT_TEXT_HTML = "text/html";
	public static final String CT_PLAIN_TEXT = "text/plain";
	public static final String TEXT_FOLDER = "text/";
	public static final String HTML_FOLDER = "email/";

	private final Configuration cfg;
	private final ServletContext servletContext;
	private ResourceBundle rb;

	public MessageFactory() {
		this(null);
	}

	public MessageFactory(ServletContext servletContext) {
		cfg = new Configuration();
		this.servletContext = servletContext;
		if (servletContext == null) {
			try {
				cfg.setDirectoryForTemplateLoading(new File(LOCAL_RESOURCE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			cfg.setServletContextForTemplateLoading(servletContext,
					RESOURCE_PATH);
		}
	}

	private void prepare(DataSet rsp) throws MalformedURLException {
		if (null == rsp.getResBundle()) {
			ClassLoader loader = null;
			if (null==servletContext){
				File file = new File(LOCAL_RESOURCE_PATH+"../classes");
				URL[] urls = {file.toURI().toURL()};
				loader = new URLClassLoader(urls);
			}else{
				loader = MessageFactory.class.getClassLoader();
			}
			rb = ResourceBundle.getBundle(rsp.getService(),rsp.getLocale(),loader);
			rsp.setResBundle(new ResourceBundleModel(rb, new BeansWrapper()));
		}
	}

	public String constructHtml(DataSet rsp)
			throws IOException, TemplateException {
		prepare(rsp);
		return processTemplate(rsp, HTML_FOLDER);
	}

	public String constructJson(DataSet rsp,String file)
			throws IOException, TemplateException {
		prepare(rsp);
		rsp.setService(file);
		List<String> data = new ArrayList<>();
		Enumeration<String> keys = rb.getKeys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			if (key.indexOf("web")==0){
				data.add(key);
			}
		}
		rsp.setPayload(data);
		return processTemplate(rsp, null);
	}
	
	public String constructTxt(DataSet rsp)
			throws IOException, TemplateException {
		prepare(rsp);
		return processTemplate(rsp, TEXT_FOLDER);
	}

	public String constructSubject(DataSet rsp) throws IOException, TemplateException {
		prepare(rsp);
		String subjectPrefix= rsp.getAction().getText();
		Template template = new Template("name", rb.getString(subjectPrefix+"Subject"),new Configuration()); 
		Writer out = new StringWriter(); 
		template.process(rsp, out); 
		return out.toString();
	}
	
	public String getText(String key, DataSet data) throws IOException, TemplateException{
		prepare(data);
		Template template = new Template("name", rb.getString(key),new Configuration()); 
		Writer out = new StringWriter(); 
		template.process(data, out);
		return out.toString();
	}

	public String processTemplate(DataSet rsp, String folder) throws IOException,
			TemplateException {
		String filePath = null;
		if (null!=folder){
			filePath = folder + rsp.getAction().getText() + ((folder.contains(HTML_FOLDER))?".html":".txt");
		}else{
			filePath = rsp.getService();
		}
		// make email template
		Template template = cfg.getTemplate(filePath);

		Writer stringWriter = null;

		// create html mail part
		stringWriter = new StringWriter();
		template.process(rsp, stringWriter);

		stringWriter.flush();
		return stringWriter.toString();
	}

}
