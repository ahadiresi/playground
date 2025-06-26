package dev.playground.postcaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Phase {

	public String status;
	public Error error;

}
