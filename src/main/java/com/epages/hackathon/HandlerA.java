package com.epages.hackathon;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class HandlerA implements Handler {

    private final ServiceA service = new ServiceA();

    public HandlerA() {

    }

    @Override
    public void handle(Context context) {
        context.getResponse().send("service value: " + service.getValue());
    }
}
