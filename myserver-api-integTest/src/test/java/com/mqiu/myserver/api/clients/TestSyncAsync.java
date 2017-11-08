package com.mqiu.myserver.api.clients;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.D2Client;
import com.linkedin.d2.balancer.D2ClientBuilder;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import com.linkedin.restli.client.ParSeqRestClient;
import com.linkedin.restli.client.RestClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nam.e.spa.ce.Abc;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestSyncAsync {

  private D2Client _d2Client;
  private com.mqiu.myserver.api.rest.clients.sync.AbcClient _abcClient;

  @BeforeMethod
  public void setup() {
    _d2Client = new D2ClientBuilder()
        .setZkHosts("localhost:21 81")
        .setZkSessionTimeout(5000, TimeUnit.MILLISECONDS)
        .setZkStartupTimeout(5000, TimeUnit.MILLISECONDS)
        .setLbWaitTimeout(5000, TimeUnit.MILLISECONDS)
        .setFlagFile("/tmp/suppressZkFlag")
        .setBasePath("/d2")
        .setFsBasePath("/tmp/backup")
        .build();
    RestClient restClient = new RestClient(_d2Client, "d2://");
    _abcClient = new com.mqiu.myserver.api.rest.clients.sync.AbcClient(restClient);

    _d2Client.start(new Callback<None>() {
      @Override
      public void onError(Throwable e) {
        System.err.println("failed to connect to zookeeper");
        System.exit(-1);
      }

      @Override
      public void onSuccess(None result) {
        System.out.println("***********************************");
        System.out.println("Successfully connected to zookeeper");
        System.out.println("***********************************");
      }
    });
  }

  @AfterMethod
  public void tearDown() {
    _d2Client.shutdown(new Callback<None>() {
      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onSuccess(None result) {

      }
    });
  }

  @Test
  public void testSync() throws Exception {
    _abcClient.get("123");
    _abcClient.get("1234");
    _abcClient.get("12345");
    _abcClient.get("123456");
    _abcClient.get("1234567");
    _abcClient.get("12345678");
  }

  @Test
  public void testAsync() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(6);
    startThread(latch, "abc");
    startThread(latch, "abcd");
    startThread(latch, "abcde");
    startThread(latch, "abcdef");
    startThread(latch, "abcdefg");
    startThread(latch, "abcdefgh");
    latch.await();
  }

  private void startThread(CountDownLatch countDownLatch, String longUrl) {
    Executors.newSingleThreadExecutor().submit(() -> {
      try {
        _abcClient.get(longUrl);
      } catch (Exception e) {
        throw new RuntimeException(longUrl);
      }
      countDownLatch.countDown();
    });
  }

  @Test
  public void testTaskSeq() throws InterruptedException {
    RestClient restClient = new RestClient(_d2Client, "d2://");
    ParSeqRestClient parSeqRestClient = new ParSeqRestClient(restClient);
    AbcClient abcClient =
        new AbcClient(parSeqRestClient);

    // 你需要跑6个task
    Task<Abc> task1 = abcClient.get("abc");
    Task<Abc> task2 = abcClient.get("abcd");
    Task<Abc> task3 = abcClient.get("abcde");
    Task<Abc> task4 = abcClient.get("abcdef");
    Task<Abc> task5 = abcClient.get("abcdefg");
    Task<Abc> task6 = abcClient.get("abcdefgh");

    // 你的算法如何把这个6个task拼图
    Task task = task1.flatMap(result1 ->
        task2.flatMap(result2 ->
            task3.flatMap(result3 ->
                task4.flatMap(result4 ->
                    task5.flatMap(result5 ->
                        task6)))));
    // engine背后做事
    runTask(task);

    Assert.assertEquals(task1.get(), new Abc().setLongUrl("abc").setShortUrl("cba"));
    Assert.assertEquals(task2.get(), new Abc().setLongUrl("abcd").setShortUrl("dcba"));
    Assert.assertEquals(task3.get(), new Abc().setLongUrl("abcde").setShortUrl("edcba"));
    Assert.assertEquals(task4.get(), new Abc().setLongUrl("abcdef").setShortUrl("fedcba"));
    Assert.assertEquals(task5.get(), new Abc().setLongUrl("abcdefg").setShortUrl("gfedcba"));
    Assert.assertEquals(task6.get(), new Abc().setLongUrl("abcdefgh").setShortUrl("hgfedcba"));
  }

  @Test
  public void testTaskPar() throws InterruptedException {
    RestClient restClient = new RestClient(_d2Client, "d2://");
    ParSeqRestClient parSeqRestClient = new ParSeqRestClient(restClient);
    AbcClient abcClient =
        new AbcClient(parSeqRestClient);

    // 你需要跑6个task
    Task<Abc> task1 = abcClient.get("abc");
    Task<Abc> task2 = abcClient.get("abcd");
    Task<Abc> task3 = abcClient.get("abcde");
    Task<Abc> task4 = abcClient.get("abcdef");
    Task<Abc> task5 = abcClient.get("abcdefg");
    Task<Abc> task6 = abcClient.get("abcdefgh");

    // 你的算法如何把这个6个task拼图
    Task task = Task.par(task1, task2, task3, task4, task5, task6);

    // 你不需要做，Enigine会优化的
    runTask(task);

    Assert.assertEquals(task1.get(), new Abc().setLongUrl("abc").setShortUrl("cba"));
    Assert.assertEquals(task2.get(), new Abc().setLongUrl("abcd").setShortUrl("dcba"));
    Assert.assertEquals(task3.get(), new Abc().setLongUrl("abcde").setShortUrl("edcba"));
    Assert.assertEquals(task4.get(), new Abc().setLongUrl("abcdef").setShortUrl("fedcba"));
    Assert.assertEquals(task5.get(), new Abc().setLongUrl("abcdefg").setShortUrl("gfedcba"));
    Assert.assertEquals(task6.get(), new Abc().setLongUrl("abcdefgh").setShortUrl("hgfedcba"));
  }

  @Test
  public void testParSeq() throws InterruptedException {
    RestClient restClient = new RestClient(_d2Client, "d2://");
    ParSeqRestClient parSeqRestClient = new ParSeqRestClient(restClient);
    AbcClient abcClient =
        new AbcClient(parSeqRestClient);

    // 工作中这样写
    Task<Abc> task1 = abcClient.get("abc");
    Task<Abc> task2 = abcClient.get("abcd");
    Task<Abc> task3 = Task.par(task1, task2).flatMap((abc1, abc2) ->
        abcClient.get(abc1.getLongUrl() + "_" + abc2.getLongUrl()));

    // engine 背后跑的
    runTask(task3);

    Assert.assertEquals(task3.get(), new Abc()
        .setLongUrl("abc_abcd").setShortUrl("dcba_cba"));
  }

  private <T> void runTask(Task<T> task) throws InterruptedException {
    ExecutorService taskScheduler = Executors.newFixedThreadPool(1);
    ScheduledExecutorService timerScheduler = Executors.newSingleThreadScheduledExecutor();
    Engine engine = new EngineBuilder()
        .setTaskExecutor(taskScheduler)
        .setTimerScheduler(timerScheduler)
        .build();

    engine.run(task);
    task.await(1, TimeUnit.MINUTES);
  }

}
