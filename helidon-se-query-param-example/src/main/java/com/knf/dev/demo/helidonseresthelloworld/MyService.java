package com.knf.dev.demo.helidonseresthelloworld;

import java.util.Collections;

import javax.json.Json;
import javax.json.JsonBuilderFactory;

import io.helidon.config.Config;
import io.helidon.webserver.Routing.Rules;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class MyService implements Service {

	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

	MyService(Config config) {
	}

	@Override
	public void update(Rules rules) {
		rules.get("/", this::getDefaultMessageHandler);
	}

	private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
		var params = request.queryParams().toMap();
		System.out.println("name: " + params.get("name").get(0));
		System.out.println("email: " + params.get("email").get(0));

		sendResponse(response, params.get("name").get(0));
	}

	private void sendResponse(ServerResponse response, String name) {

		var returnObject = JSON.createObjectBuilder().add("name", name).build();
		response.send(returnObject);
	}

}