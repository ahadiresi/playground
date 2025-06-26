package dev.playground.postcaster;

import static dev.playground.postcaster.Constants.FB_PAGE_TOKEN;
import static dev.playground.postcaster.Constants.FB_TESTING_PAGE_ID;
import static dev.playground.postcaster.Constants.FB_VIDEO_URL;

import java.time.Duration;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import dev.playground.mailgun.JerseyClientBuilderFactory;
import io.resi.jakartars.filter.client.ClientRequestLoggingFilter;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

class PostCaster {

	private static final Client CLIENT = JerseyClientBuilderFactory.grizzlyClientBuilder()
			.register(new JacksonJsonProvider(new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)))
			.register(ClientRequestLoggingFilter.class)
			.build();

	public static void main(String[] args) throws InterruptedException {
		postReelToPage();
	}

	private static void postReelToPage() throws InterruptedException {
		System.out.println("Initializing video upload session...");
		var uploadSession = initializeUploadSession();

		System.out.println("Uploading video...");
		var uploadResponse = uploadVideo(uploadSession, FB_VIDEO_URL);
		if (!uploadResponse.isSuccess()) {
			throw new RuntimeException("Video upload failed");
		}

		// sometimes fb is a slowpoke so we need to wait a bit so that the reel post is ready
		Thread.sleep(Duration.ofSeconds(2));

		System.out.println("Publishing reel...");
		var publishResponse = publish(uploadSession);
		if (!publishResponse.isSuccess()) {
			throw new RuntimeException("Reel publishing failed");
		}

		System.out.println("Checking reel status...");
		int count = 0;
		boolean keepChecking = true;
		while (keepChecking) {
			var statusResponse = checkStatus(uploadSession);
			var uploadingStatus = statusResponse.status.uploadingPhase.status;
			var processingStatus = statusResponse.status.processingPhase.status;
			var publishingStatus = statusResponse.status.publishingPhase.status;
			System.out.printf("%s. Upload: %s; Processing: %s; Publishing: %s%n", ++count, uploadingStatus, processingStatus, publishingStatus);

			if ("complete".equals(publishingStatus)) {
				keepChecking = false;
			}

			Thread.sleep(Duration.ofSeconds(1));
		}
	}

	private static FacebookUploadSessionResponse initializeUploadSession() {
		var request = new FacebookUploadSessionRequest();
		request.uploadPhase = "start";
		request.accessToken = FB_PAGE_TOKEN;

		try (Response response = CLIENT.target("https://graph.facebook.com/v23.0/%s/video_reels".formatted(FB_TESTING_PAGE_ID))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(request))) {

			if (response.getStatus() != 200) {
				var error = response.readEntity(new GenericType<HashMap<String, Object>>() { });
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			return response.readEntity(FacebookUploadSessionResponse.class);
		}
	}

	private static FacebookUploadVideoResponse uploadVideo(final FacebookUploadSessionResponse uploadSession, final String videoUrl) {
		try (Response response = CLIENT.target(uploadSession.uploadUrl)
				.request()
				.header("Authorization", "OAuth " + FB_PAGE_TOKEN)
				.header("offset", 0)
				.header("file_url", videoUrl)
				.post(Entity.json(new Object()))) {

			if (response.getStatus() != 200) {
				var error = response.readEntity(new GenericType<HashMap<String, Object>>() { });
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			return response.readEntity(FacebookUploadVideoResponse.class);
		}
	}

	private static FacebookPublishResponse publish(final FacebookUploadSessionResponse uploadSession) {
		try (Response response = CLIENT.target("https://graph.facebook.com/v23.0/%s/video_reels".formatted(FB_TESTING_PAGE_ID))
				.queryParam("access_token", FB_PAGE_TOKEN)
				.queryParam("video_id", uploadSession.videoId)
				.queryParam("upload_phase", "finish")
				.queryParam("video_state", "PUBLISHED")
				.queryParam("description", "Java did this!")
				.request()
				.post(Entity.json(new Object()))) {

			if (response.getStatus() != 200) {
				var error = response.readEntity(new GenericType<HashMap<String, Object>>() { });
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			return response.readEntity(FacebookPublishResponse.class);
		}
	}

	private static FacebookReelStatusResponse checkStatus(final FacebookUploadSessionResponse uploadSession) {
		try (Response response = CLIENT.target("https://graph.facebook.com/v23.0/%s".formatted(uploadSession.videoId))
				.queryParam("fields", "status")
				.queryParam("access_token", FB_PAGE_TOKEN)
				.request()
				.get()) {

			if (response.getStatus() != 200) {
				var error = response.readEntity(new GenericType<HashMap<String, Object>>() { });
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			return response.readEntity(FacebookReelStatusResponse.class);
		}
	}



}
