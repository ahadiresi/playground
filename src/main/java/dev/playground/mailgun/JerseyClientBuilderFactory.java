package dev.playground.mailgun;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import jakarta.ws.rs.client.ClientBuilder;

public class JerseyClientBuilderFactory {
	private JerseyClientBuilderFactory() {}

	public static ClientBuilder grizzlyClientBuilder() {
		ClientConfig config = new ClientConfig()
				.connectorProvider(new GrizzlyConnectorProvider());

		return defaultClientBuilder(config)
				// We've seen issues with Google's load balancer and Grizzly with Transfer-Chunked encoding; disable it
				.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);
	}

	public static ClientBuilder defaultClientBuilder() {
		return defaultClientBuilder(null);
	}

	private static ClientBuilder defaultClientBuilder(final ClientConfig config) {
		ClientBuilder builder = ClientBuilder.newBuilder();
		if (config != null) {
			builder = builder.withConfig(config);
		}
		// Disable unused providers; see https://github.com/eclipse-ee4j/jersey/issues/5281
		return builder.property(CommonProperties.PROVIDER_DEFAULT_DISABLE, "ALL");
	}
}
