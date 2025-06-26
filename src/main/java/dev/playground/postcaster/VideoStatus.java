package dev.playground.postcaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoStatus {

	@JsonProperty("video_status")
	public String videoStatus;

	@JsonProperty("uploading_phase")
	public Phase uploadingPhase;

	@JsonProperty("processing_phase")
	public Phase processingPhase;

	@JsonProperty("publishing_phase")
	public Phase publishingPhase;

}
