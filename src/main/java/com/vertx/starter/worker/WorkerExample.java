package com.vertx.starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private final static Logger log = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args){
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  // the purpose of worker blocker is when a network is down or a messaging taking a long time etc...
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerExample(),
            new DeploymentOptions()
                    .setWorker(true)
                    .setWorkerPoolSize(1)
                    .setWorkerPoolName("my-worker-verticle"));
    log.debug("Start "+ getClass().getName());
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode(){
      vertx.executeBlocking(event -> {
          log.debug("Executing blocking code");
          try {
              Thread.sleep(5000);
              event.complete();
          } catch (InterruptedException e) {
              log.error("Failed: "+e);
              event.fail(e);
          }
      }, result -> {
          if(result.succeeded()) log.debug("Blocking call done");
          else log.error("Blocking call failed "+result.cause());
      });
  }
}
