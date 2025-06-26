package dev.playground.mailgun;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.TEXT_PLAIN)
public class PlainTextMessageBodyReader implements MessageBodyReader<MailgunErrorResponse> {

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return type == MailgunErrorResponse.class && MediaType.TEXT_PLAIN_TYPE.isCompatible(mediaType);
	}

	@Override
	public MailgunErrorResponse readFrom(final Class<MailgunErrorResponse> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders,
			final InputStream entityStream) throws IOException, WebApplicationException {
		System.out.println("from body reader");
		var response = new MailgunErrorResponse();
		response.setMessage(new String(entityStream.readAllBytes()));
		return response;
	}

}
