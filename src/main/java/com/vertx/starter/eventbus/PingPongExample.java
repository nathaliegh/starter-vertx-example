package com.vertx.starter.eventbus;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vertx.starter.model.Ping;
import com.vertx.starter.model.Pong;


public class PingPongExample {

   private final static Logger log = LoggerFactory.getLogger(PingPongExample.class);

   public static void main(String[] args){
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new PingVerticle(),logOnError());
        vertx.deployVerticle(new PongVerticle(),logOnError());
    }

    private static Handler<AsyncResult<String>> logOnError(){
         return  ar -> {
             if(ar.failed()) log.error("message send failed "+ar.cause());
         };
    }

    static class PingVerticle extends AbstractVerticle {
         private final static Logger log = LoggerFactory.getLogger(PingVerticle.class);
         static final String ADDRESS = PingVerticle.class.getName();
         @Override
         public void start(Promise<Void> startPromise) throws Exception {
          var eventBus =vertx.eventBus();
          eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
          Ping message = new Ping("Hello", true);
          log.debug("Sending {}: "+message);
          eventBus.<Pong>request(ADDRESS,message, reply -> {
              if(reply.failed()){
                  log.error("failed "+reply.cause());
                  return;
              }
              log.debug("Response {}: " + reply.result().body());
          });
          startPromise.complete();
         }
    }

    static class PongVerticle extends AbstractVerticle{
        private final static Logger log = LoggerFactory.getLogger(PongVerticle.class);

        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
            vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message -> {
               log.debug("Message received {}: "+message.body());
               message.reply(new Pong(1));
            }).exceptionHandler(error -> {
                log.error("error", error);
            });
            startPromise.complete();
        }

    }


}
