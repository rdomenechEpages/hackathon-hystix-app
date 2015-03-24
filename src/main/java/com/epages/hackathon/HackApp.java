package com.epages.hackathon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

public class HackApp {

    private final static Logger LOGGER = LoggerFactory.getLogger(HackApp.class);

    public static void main(String[] args) {
        try {
            RatpackServer.of(b -> b
                    .serverConfig(ServerConfig.findBaseDirProps())
                            .registry(Guice.registry(bindingSpec -> bindingSpec.add(new HackModule())))
                            .handlers(chain -> chain
                                            .prefix("services", nested -> {
                                                nested
                                                        //.handler(new LoggingHandler())
                                                        .handler("a", new ParameterizedHandler(new ConfigurableServiceImpl("a")))
                                                        .handler("b", new ParameterizedHandler(new ConfigurableServiceImpl("b")));
                                            })
                                            .prefix("static", nested -> nested.assets("public/images"))
                                            .handler(context -> context.render("root handler!"))
                            )
            ).start();
        } catch (Exception e) {
            LOGGER.error("", e);
            System.exit(1);
        }
    }
}
