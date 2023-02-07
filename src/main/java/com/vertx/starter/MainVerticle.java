package com.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class MainVerticle extends AbstractVerticle {

  private final static Logger log = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Soltia!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.debug("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
