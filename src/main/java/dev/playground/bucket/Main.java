package dev.playground.bucket;

import java.util.UUID;

import io.resi.central.client.v3.DestinationGroupClient;
import io.resi.central.jaxrs.bean.Encoder;
import io.resi.client4j.reactive.jakarta.ReactiveJakartaClientBuilder;

public class Main {
	public static void main(String[] args) {
		var client = ReactiveJakartaClientBuilder.newBuilder("test")
				.withTarget("http://localhost:8080")
				.build(DestinationGroupClient.class);

		var authHeader = "X-Bearer token";

		var encoder = new Encoder();
		encoder.setRequestedStatus("start");
		var response = client.listDestinationGroups(UUID.fromString("20cda2f8-b874-4955-aea5-28dd131ee5f7"), true, authHeader)
				.collectList()
				.block();

		System.out.println("Hello, World!");
	}
}
