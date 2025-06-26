package dev.playground.postcaster;

import com.fasterxml.jackson.annotation.JsonProperty;

class FacebookUploadSessionRequest {

	// could be an enum, but keeping it simple for now
	@JsonProperty("upload_phase")
	public String uploadPhase;

	@JsonProperty("access_token")
	public String accessToken;

}
