package com.epages.hackathon;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class HandlerB implements Handler {

    private final ServiceB service = new ServiceB();

    public HandlerB() {

    }

    @Override
    public void handle(Context context) {
        context.getResponse().send("service value: " + service.getValue());
    }
}
