package com.vertx.starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PublishSubscribeExample {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new Publisher());
        vertx.deployVerticle(new Subscriber1());
        vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
    }

    static class Publisher extends AbstractVerticle{
        private final static Logger log = LoggerFactory.getLogger(Publisher.class);
        static final String ADDRESS = Publisher.class.getName();
        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            startPromise.complete();
            vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id -> {
                vertx.eventBus().publish(ADDRESS,"A message for everyone !");
            });
        }
    }

    public static class Subscriber1 extends AbstractVerticle{
        private final static Logger log = LoggerFactory.getLogger(Subscriber1.class);
        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            vertx.eventBus().<String>consumer(Publisher.ADDRESS, response-> {
                log.debug(" Response 1 {} :"+response.body());
            });
        }
    }

    public static class Subscriber2 extends AbstractVerticle{
        private final static Logger log = LoggerFactory.getLogger(Subscriber2.class);
        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            vertx.eventBus().<String>consumer(Publisher.ADDRESS, response-> {
                log.debug(" Response 2 {} :"+response.body());
            });
        }
    }
}
