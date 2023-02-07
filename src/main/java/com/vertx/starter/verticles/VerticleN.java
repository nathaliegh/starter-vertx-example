package com.vertx.starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleN extends AbstractVerticle {

  private final static Logger log = LoggerFactory.getLogger(VerticleN.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.debug("Start "+ getClass().getName()
      + " has config "+config().toString()
      + " on thread "+Thread.currentThread().getName());
    startPromise.complete();
  }
}
