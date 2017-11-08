package com.mqiu.myserver.api.listeners;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.D2Client;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class MyServletContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    D2Client d2Client = (D2Client) webApplicationContext.getBean("d2Client");

    CountDownLatch latch = new CountDownLatch(1);
    try {
      Executors.newSingleThreadScheduledExecutor().submit(() -> d2Client.start(new Callback<None>() {
        @Override
        public void onError(Throwable e) {
          System.out.println("Failed to connect to zookeeper");
          System.exit(-1);
        }

        @Override
        public void onSuccess(None result) {
          System.out.println("***********************************");
          System.out.println("Successfully connected to zookeeper");
          System.out.println("***********************************");
        }
      })).get(5000, TimeUnit.MILLISECONDS);
      latch.countDown();
    } catch (InterruptedException e) {
      System.exit(-1);
    } catch (ExecutionException e) {
      System.exit(-1);
    } catch (TimeoutException e) {
      System.exit(-1);
    }

    try {
      latch.await();
    } catch (InterruptedException e) {

    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    D2Client d2Client = (D2Client) webApplicationContext.getBean("d2Client");
    d2Client.shutdown(new Callback<None>() {
      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onSuccess(None result) {

      }
    });

  }
}
