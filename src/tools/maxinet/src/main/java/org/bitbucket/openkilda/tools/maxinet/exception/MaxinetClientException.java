package org.bitbucket.openkilda.tools.maxinet.exception;

public class MaxinetClientException extends MaxinetWebException {

	public MaxinetClientException(String message, int status) {
		super(message, status);
	}

}
