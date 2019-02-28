package io.vertx.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;


public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);


    @Override
    public void start(Future<Void> startFuture) throws Exception {
//        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(WikiDatabaseVerticle.class.getName(), wikiComplete->{
            if(wikiComplete.succeeded()) {
                vertx.deployVerticle(HttpServerVerticle.class.getName(),new DeploymentOptions().setInstances(1), httpComplete->{
                    if(httpComplete.succeeded()) {
                        startFuture.complete();
                    }else {
                        startFuture.fail(httpComplete.cause());
                    }
                });
            }else {
                startFuture.fail(wikiComplete.cause());
            }
        });

//        dbVerticleDeployment.compose(id -> {
//            Future<String> httpVerticleDeployment = Future.future();
//            vertx.deployVerticle(HttpServerVerticle.class.getName(), new DeploymentOptions().setInstances(1),
//                httpVerticleDeployment.completer());
//            return httpVerticleDeployment;
//        }).setHandler(ar -> {
//            if (ar.succeeded()) {
//                startFuture.complete();
//            } else {
//                startFuture.fail(ar.cause());
//            }
//        });

    }
    
    public static void main(final String... args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }

}
