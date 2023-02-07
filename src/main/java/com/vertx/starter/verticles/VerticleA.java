package com.vertx.starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {
  private final static Logger log = LoggerFactory.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.debug("Start "+ getClass().getName());
    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      log.debug("Deployed "+VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result());
    });
    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      log.debug("Deployed "+VerticleAB.class.getName());
    });
    startPromise.complete();
  }
}
