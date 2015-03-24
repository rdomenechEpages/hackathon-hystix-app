package com.epages.hackathon;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import ratpack.handling.HandlerDecorator;

public class HackModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(),
                HandlerDecorator.class).addBinding().toInstance(HandlerDecorator.prepend(new LoggingHandler()));
    }
}
