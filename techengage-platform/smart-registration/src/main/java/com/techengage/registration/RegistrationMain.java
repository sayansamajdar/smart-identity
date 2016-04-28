package com.techengage.registration;

import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import com.techengage.registration.utility.Bootstrap;
import com.techengage.registration.utility.ImageUtils;
import com.techengage.registration.utility.Utils;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class RegistrationMain extends Bootstrap {

    Vertx vertx;

    protected RegistrationMain start() {
	ClusterManager manager = new HazelcastClusterManager();
	VertxOptions options = new VertxOptions();
	options.setClusterManager(manager);

	String hzClusterHost = System.getProperty("hzClusterHost");
	System.err.println("-------------->>>> hzClusterHost=" + hzClusterHost);

	options.setClusterHost(hzClusterHost);
	Vertx.clusteredVertx(options, new Handler<AsyncResult<Vertx>>() {
	    public void handle(AsyncResult<Vertx> result) {
		if (result.succeeded()) {
		    vertx = result.result();
		    HashSet<AbstractVerticle> verticles = Utils.getVerticles();
		    for (final AbstractVerticle verticle : verticles) {
			vertx.deployVerticle(verticle, new Handler<AsyncResult<String>>() {
			    public void handle(AsyncResult<String> stringAsyncResult) {
				System.out.println("Deployed: " + verticle.getClass().getName());

				try {
				    ImageUtils.display(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("defaultFace.png")));
				} catch (IOException e) {
				    e.printStackTrace();
				}
			    }
			});
		    }
		}
	    }
	});

	return this;
    }

    protected RegistrationMain stop() {
	for (final AbstractVerticle verticle : Utils.getVerticles()) {
	    vertx.undeploy(verticle.getClass().getName(), new Handler<AsyncResult<Void>>() {
		public void handle(AsyncResult<Void> stringAsyncResult) {
		    System.out.println("Undeployed: " + verticle.getClass().getName());
		}
	    });
	}
	return this;
    }

    public static void main(String args[]) {
	final RegistrationMain registrationMain = new RegistrationMain().start();
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    public void run() {
		System.out.println("bye...");
		registrationMain.stop();
	    }
	});
    }
}
