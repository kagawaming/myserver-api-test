package com.newsoftvalley.myserver.jettysimulator;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class DevServer {
    private static final String WEB_APP_PATH =
            DevServer.class.getClassLoader()
                    .getResource(".").toString()
                    .replace("build/classes/main/", "")
                    + "src/main/webapp";
    private static final String WEB_XML_PATH =
            WEB_APP_PATH + "/WEB-INF/web.xml";
    private static final String CONTEXT_PATH = "/myserver-api";

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        WebAppContext webApp = new WebAppContext(WEB_APP_PATH, CONTEXT_PATH);
        webApp.setDescriptor(WEB_XML_PATH);
        webApp.setParentLoaderPriority(true);
        server.setHandler(webApp);

        HttpConnectionFactory httpConnectionFactory =
                new HttpConnectionFactory(new HttpConfiguration());
        ServerConnector connector =
                new ServerConnector(server, httpConnectionFactory);
        connector.setHost("0.0.0.0");
        connector.setPort(8080);
        server.addConnector(connector);

        server.start();
        server.join();
    }
}
