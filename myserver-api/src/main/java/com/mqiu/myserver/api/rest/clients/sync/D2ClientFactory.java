package com.mqiu.myserver.api.rest.clients.sync;

import com.linkedin.d2.balancer.D2Client;
import com.linkedin.d2.balancer.D2ClientBuilder;
import java.util.concurrent.TimeUnit;


public class D2ClientFactory {

  public D2Client createInstance(String zkHosts,
      long zkSessionTimeoutInMillisecond,
      long zkStartupTimeoutInMillisecond,
      long lbWaitTimeoutInMillisecond,
      String flagFile,
      String basePath,
      String fsBasePath)  {
         D2Client d2Client = new D2ClientBuilder()
             .setZkHosts(zkHosts)
             .setZkSessionTimeout(zkSessionTimeoutInMillisecond, TimeUnit.MILLISECONDS)
             .setZkStartupTimeout(zkStartupTimeoutInMillisecond, TimeUnit.MILLISECONDS)
             .setLbWaitTimeout(lbWaitTimeoutInMillisecond, TimeUnit.MILLISECONDS)
             .setFlagFile(flagFile)
             .setBasePath(basePath)
             .setFsBasePath(fsBasePath)
             .build();
         return d2Client;
  }
}
