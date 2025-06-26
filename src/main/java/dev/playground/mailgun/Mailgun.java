package dev.playground.mailgun;

import java.util.concurrent.TimeUnit;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import io.resi.jakartars.filter.client.ClientRequestLoggingFilter;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class Mailgun {

	public static final String RESI_EMAIL_DOMAIN = "resi.io";
	public static final String NO_REPLY = "Resi <noreply@" + RESI_EMAIL_DOMAIN + ">";

	public static void main(String[] args) {
		var mapper = new ObjectMapper();
		var client = JerseyClientBuilderFactory.grizzlyClientBuilder()
				.register(new JacksonJsonProvider(mapper))
				.register(PlainTextMessageBodyReader.class)
				.register(ClientRequestLoggingFilter.class)
				.connectTimeout(1000, TimeUnit.MILLISECONDS)
				.readTimeout(1000, TimeUnit.MILLISECONDS)
				.register(HttpAuthenticationFeature.basic("api", "apiKey"))
				.build();

		var form = new Form();
		form.param("to", "ardian.hadiyanto@resi.io");
		form.param("from", NO_REPLY);
		form.param("subject", "subject");
		form.param("html", "ardian");

		try (Response response = client.target("https://api.mailgun.net/v3/resi.io/messages")
				.request()
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE))){
			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				System.out.println("Error sending email: [%s] [%s]".formatted(response.getStatus(), response.readEntity(MailgunErrorResponse.class).getMessage()));
			}
		}

		System.out.println("done");
	}

}
