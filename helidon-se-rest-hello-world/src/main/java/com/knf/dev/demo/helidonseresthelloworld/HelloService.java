package com.knf.dev.demo.helidonseresthelloworld;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import io.helidon.config.Config;
import io.helidon.webserver.Routing.Rules;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class HelloService implements Service {

	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
	private final AtomicReference<String> greeting = new AtomicReference<>();

	HelloService(Config config) {
		greeting.set(config.get("app.message").asString().orElse("Empty"));
	}

	@Override
	public void update(Rules rules) {
		rules.get("/", this::getDefaultMessageHandler);
	}

	private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
		sendResponse(response, "Knowledgefactory");
	}

	private void sendResponse(ServerResponse response, String name) {
		var message = String.format("%s %s!", greeting.get(), name);
		var returnObject = JSON.createObjectBuilder().add("message", message).build();
		response.send(returnObject);
	}

}