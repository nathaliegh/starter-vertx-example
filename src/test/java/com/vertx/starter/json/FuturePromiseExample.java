package com.vertx.starter.json;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
    private final static Logger log = LoggerFactory.getLogger(FuturePromiseExample.class);

    @Test
    void promise_success(Vertx vertx, VertxTestContext vertxTestContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("Start");
        vertx.setTimer(500, id -> {
            promise.complete("success");
            log.debug("success");
            vertxTestContext.completeNow();
        });
        log.debug("End");
    }

    @Test
    void promise_failure(Vertx vertx, VertxTestContext vertxTestContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("Start");
        vertx.setTimer(500, id -> {
            promise.fail(new RuntimeException("Failed!"));
            log.error("Failed");
            vertxTestContext.completeNow();
        });
        log.debug("End");
    }

    @Test
    void future_success(Vertx vertx, VertxTestContext vertxTestContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("Start");
        vertx.setTimer(500, id -> {
            promise.complete("success");
            log.debug("Timer done.");
        });
        final Future<String> future = promise.future();
        future.onSuccess(result -> {
             log.debug("Result {} :"+result);
             vertxTestContext.completeNow();
        }).onFailure(vertxTestContext::failNow);
    }

    @Test
    void future_failure(Vertx vertx, VertxTestContext vertxTestContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("Start");
        vertx.setTimer(500, id -> {
            promise.fail(new RuntimeException("Failed!"));
            log.debug("Timer done.");
        });
        final Future<String> future = promise.future();
        future.onSuccess(result -> {
            log.debug("Result {} :"+result);
            vertxTestContext.completeNow();
        }).onFailure(failure -> {
            log.error("Failure {} :"+failure);
            vertxTestContext.failNow("Failure exception");
        });
    }

    @Test
    void future_map(Vertx vertx, VertxTestContext vertxTestContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("Start");
        vertx.setTimer(500, id -> {
            promise.complete("success");
            log.debug("Timer done.");
        });
        final Future<String> future = promise.future();
        future.map(asString -> {
            log.debug("result as map {}",asString);
            return new JsonObject().put("key",asString);
        }).map(jsonObject -> {
            log.debug("result as json Object {}",jsonObject);
            return new JsonArray().add(jsonObject);
        }).onSuccess(result -> {
            log.debug("Result {} of type {}",result, result.getClass().getTypeName());
            vertxTestContext.completeNow();
        }).onFailure(vertxTestContext::failNow);
    }

    @Test
    void future_coordinate(Vertx vertx, VertxTestContext vertxTestContext) {
        vertx.
                createHttpServer()
                .requestHandler(request -> log.debug("request {}", request))
                .listen(10_000)
                .compose(server -> {
                   log.info("Another task");
                   return Future.succeededFuture(server);
                }).compose(server -> {
                   log.info("Even more");
                   return Future.succeededFuture(server);
                }).onFailure(vertxTestContext::failNow)
                .onSuccess(server -> {
                    log.debug("Server started successfully on port {} ",server.actualPort());
                    vertxTestContext.completeNow();
                });
    }

        @Test
        void future_composition(Vertx vertx, VertxTestContext vertxTestContext) {
            var one = Promise.<Void>promise();
            var two = Promise.<Void>promise();
            var three = Promise.<Void>promise();

            var futureOne = one.future();
            var futureTwo = two.future();
            var futureThree = three.future();

            CompositeFuture
                    .all(futureOne,futureTwo,futureThree)
                    .onFailure(vertxTestContext::failNow)
                    .onSuccess(result -> {
                        log.debug("success");
                        vertxTestContext.completeNow();
                    });

            vertx.setTimer(5, id -> {
                one.complete();
                two.complete();
                three.complete();
            });


        }
}
