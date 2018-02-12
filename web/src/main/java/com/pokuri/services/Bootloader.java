package com.pokuri.services;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Bootloader {

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions();
        vertx.deployVerticle(WebVerticle.class.getName(), options);
    }

}
