package dev.playground.postcaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookReelStatusResponse {

	public VideoStatus status;

}
