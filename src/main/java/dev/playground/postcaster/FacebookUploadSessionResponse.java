package dev.playground.postcaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUploadSessionResponse {

	@JsonProperty("video_id")
	public String videoId;

	@JsonProperty("upload_url")
	public String uploadUrl;

}
