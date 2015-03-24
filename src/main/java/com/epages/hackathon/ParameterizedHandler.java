package com.epages.hackathon;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class ParameterizedHandler implements Handler {

	private final Service service;
	
	public ParameterizedHandler(Service service) {
		this.service = service;
	}
	@Override
	public void handle(Context context) throws Exception {
        context.getResponse().send("service value: " + service.getValue());
	}

}
