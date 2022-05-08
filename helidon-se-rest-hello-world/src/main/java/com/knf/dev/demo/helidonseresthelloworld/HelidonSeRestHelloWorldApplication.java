package com.knf.dev.demo.helidonseresthelloworld;

import java.util.logging.Logger;
import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

public class HelidonSeRestHelloWorldApplication {

	private static final Logger LOGGER = Logger.getLogger(HelidonSeRestHelloWorldApplication.class.getName());

	public static void main(final String[] args) {
		startServer();
	}

	private static Single<WebServer> startServer() {

		// Load the default configuration using the create() method.
		var config = Config.create();

		/*
		 * Helidon's web server is an asynchronous and reactive API that runs on top of
		 * Netty. The WebServer interface includes support for configuration, routing,
		 * error handling, and building metrics and health endpoints.
		 */
		var server = WebServer.builder(createRouting(config)).config(config.get("server"))
				.addMediaSupport(JsonpSupport.create()).build();

		// Start web server
		var webserver = server.start();

		webserver.thenAccept(ws -> {
			LOGGER.info("Web server started! http://localhost:" + ws.port() + "/hello");
			ws.whenShutdown().thenRun(() -> LOGGER.info("Web server is down!"));
		}).exceptionallyAccept(t -> {
			LOGGER.severe("Web startup failed: " + t.getMessage());
		});

		return webserver;
	}

	private static Routing createRouting(Config config) {

		var helloService = new HelloService(config);

		return Routing.builder()

				.register("/hello", helloService).build();
	}
}
