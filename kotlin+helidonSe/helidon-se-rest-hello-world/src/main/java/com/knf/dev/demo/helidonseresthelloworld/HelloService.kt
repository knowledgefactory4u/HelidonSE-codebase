package com.knf.dev.demo.helidonseresthelloworld

import io.helidon.config.Config
import io.helidon.webserver.*
import java.util.concurrent.atomic.AtomicReference
import javax.json.Json

class HelloService internal constructor(config: Config) : Service {
    private val greeting = AtomicReference<String>()

    init {
        greeting.set(config["app.message"].asString().orElse("Empty"))
    }

    override fun update(rules: Routing.Rules) {
        rules["/", Handler { request: ServerRequest, response: ServerResponse ->
            getDefaultMessageHandler(
                request,
                response
            )
        }]
    }

    private fun getDefaultMessageHandler(request: ServerRequest, response: ServerResponse) {
        sendResponse(response, "Knowledgefactory")
    }

    private fun sendResponse(response: ServerResponse, name: String) {
        val message = String.format("%s %s!", greeting.get(), name)
        val returnObject = JSON.createObjectBuilder().add("message", message).build()
        response.send(returnObject)
    }

    companion object {
        private val JSON = Json.createBuilderFactory(emptyMap<String, Any>())
    }
}
