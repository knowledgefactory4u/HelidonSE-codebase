package com.knf.dev.demo.helidonseresthelloworld

import io.helidon.common.reactive.Single
import io.helidon.config.Config
import io.helidon.media.jsonp.JsonpSupport
import io.helidon.webserver.Routing
import io.helidon.webserver.WebServer
import java.util.logging.Logger

object HelidonSeRestHelloWorldApplication {
    private val LOGGER = Logger.getLogger(
        HelidonSeRestHelloWorldApplication::class.java.name
    )

    @JvmStatic
    fun main(args: Array<String>) {
        startServer()
    }

    private fun startServer(): Single<WebServer> {

        // Load the default configuration using the create() method.
        val config = Config.create()

        /*
		 * Helidon's web server is an asynchronous and reactive API that runs on top of
		 * Netty. The WebServer interface includes support for configuration, routing,
		 * error handling, and building metrics and health endpoints.
		 */
        val server = WebServer.builder(createRouting(config)).config(config["server"])
            .addMediaSupport(JsonpSupport.create()).build()

        // Start web server
        val webserver = server.start()
        webserver.thenAccept { ws: WebServer ->
            LOGGER.info("Web server started! http://localhost:" + ws.port() + "/hello")
            ws.whenShutdown().thenRun { LOGGER.info("Web server is down!") }
        }.exceptionallyAccept { t: Throwable -> LOGGER.severe("Web startup failed: " + t.message) }
        return webserver
    }

    private fun createRouting(config: Config): Routing {
        val helloService = HelloService(config)
        return Routing.builder()
            .register("/hello", helloService).build()
    }
}
