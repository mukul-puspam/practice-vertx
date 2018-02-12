package com.pokuri.services;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(getRouter(vertx)::accept).listen(8080);
        LOGGER.info("WebVerticle deployed");
    }

    private Router getRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        registerHandlers(router);
        return router;
    }

    private void registerHandlers(Router router) {
        /*router.route("/*").handler(routingContext -> {
            LOGGER.info("For every request");
            LOGGER.info("routingContext = [" + routingContext + "]");
            LOGGER.info("thread = [" + Thread.currentThread().getName() + "]");
            routingContext.next();
        }).failureHandler(ErrorHandler.create());*/

        /*router.route("/").handler(routingContext -> {
            LOGGER.info("For request having path: /");
            LOGGER.info("routingContext = [" + routingContext + "]");
            LOGGER.info("thread = [" + Thread.currentThread().getName() + "]");
            routingContext.next();
        });

        router.route("/").handler(StaticHandler.create().setIndexPage("/index.html"));*/

        router.route("/foo").blockingHandler(routingContext -> {
            LOGGER.info("For request having path: /foo");
            LOGGER.info("routingContext = [" + routingContext + "]");
            LOGGER.info("thread = [" + Thread.currentThread().getName() + "]");
            waitForSometime().setHandler(ar -> routingContext.next());
        }, false);

        router.route("/bar").blockingHandler(routingContext -> {
            LOGGER.info("For request having path: /bar");
            LOGGER.info("routingContext = [" + routingContext + "]");
            LOGGER.info("thread = [" + Thread.currentThread().getName() + "]");
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            routingContext.response().end("BAR");
        });

        router.route("/wibble").blockingHandler(routingContext -> {
            throw new RuntimeException("Failed to server");
        }, false);
    }

    private Future waitForSometime() {
        Future f = Future.future();
        try {
            Thread.currentThread().sleep(10000);
            f.complete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return f;
    }

}
