package com.vertx.starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {
  private final static Logger log = LoggerFactory.getLogger(WorkerVerticle.class);

  // the purpose of worker blocker is when a network is down or a messaging taking a long time etc...
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.debug("Start "+ getClass().getName());
    startPromise.complete();
    Thread.sleep(5000);
    log.debug("Blocking worker done! ");
  }
}
