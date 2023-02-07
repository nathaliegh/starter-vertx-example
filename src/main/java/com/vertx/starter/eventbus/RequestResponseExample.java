package com.vertx.starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {


   public static void main(String[] args){
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestVerticle());
        vertx.deployVerticle(new ResponseVerticle());

    }

    static class RequestVerticle extends AbstractVerticle {
         private final static Logger log = LoggerFactory.getLogger(RequestVerticle.class);
         static final String ADDRESS = "my.request.address";
         @Override
         public void start(Promise<Void> startPromise) throws Exception {
          startPromise.complete();
          var eventBus =vertx.eventBus();
          var message = new JsonObject()
                  .put("message","Hello World!")
                  .put("version",1);
          log.debug("Sending {}: "+message);
          eventBus.<JsonArray>request(ADDRESS,message, reply ->
              log.debug("Response {}: "+reply.result().body())
          );
        }

    }

    static class ResponseVerticle extends AbstractVerticle{
        private final static Logger log = LoggerFactory.getLogger(ResponseVerticle.class);

        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            vertx.eventBus().<JsonObject>consumer(RequestVerticle.ADDRESS, message -> {
               log.debug("Message received {}: "+message.body());
               message.reply(new JsonArray().add("one").add("two").add("three"));
            });
        }

    }


}
