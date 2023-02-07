package com.vertx.starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new Sender());
        vertx.deployVerticle(new Receiver());
    }
    static class Sender extends AbstractVerticle {
        private final static Logger log = LoggerFactory.getLogger(Sender.class);

        static final String ADDRESS = Sender.class.getName();

        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            var message="Salut coucou!";
            log.debug("Sending a message!!");
            vertx.setPeriodic(1000, id -> {
                vertx.eventBus().send(ADDRESS, message);
            });
        }
    }
    static class Receiver extends AbstractVerticle {
        private final static Logger log = LoggerFactory.getLogger(Receiver.class);
        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            vertx.eventBus().<String>consumer(PointToPointExample.Sender.ADDRESS, message -> {
             log.debug("Receive a message "+message.body());
            });
        }
    }
}
