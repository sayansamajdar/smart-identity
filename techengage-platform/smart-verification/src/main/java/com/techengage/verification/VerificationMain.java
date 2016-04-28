package com.techengage.verification;

import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import com.techengage.verification.utility.Bootstrap;
import com.techengage.verification.utility.LoginForm;
import com.techengage.verification.utility.Utils;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class VerificationMain extends Bootstrap {

    Vertx vertx;

    protected VerificationMain start() {
	ClusterManager manager = new HazelcastClusterManager();
	VertxOptions options = new VertxOptions();
	options.setClusterManager(manager);
	String hzClusterHost = System.getProperty("hzClusterHost");
	System.err.println("-------------->>>> hzClusterHost=" + hzClusterHost);

	System.out.println("mbrola.base " + System.getProperty("mbrola.base"));

	options.setClusterHost(hzClusterHost);
	Vertx.clusteredVertx(options, new Handler<AsyncResult<Vertx>>() {
	    @Override
	    public void handle(AsyncResult<Vertx> result) {
		if (result.succeeded()) {
		    vertx = result.result();
		    HashSet<AbstractVerticle> verticles = Utils.getVerticles();
		    for (final AbstractVerticle verticle : verticles) {
			vertx.deployVerticle(verticle, new Handler<AsyncResult<String>>() {
			    public void handle(AsyncResult<String> stringAsyncResult) {
				System.out.println("Deployed: " + verticle.getClass().getName());
				try {
					LoginForm.display(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("defaultFace.png")));
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

    protected VerificationMain stop() {
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
	final VerificationMain verificationMain = new VerificationMain().start();
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    public void run() {
		System.out.println("bye...");
		verificationMain.stop();
	    }
	});
    }
}
