package nl.probot.vertx.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/producer")
public class EventbusProducer {

    static final Logger log = LoggerFactory.getLogger(EventbusProducer.class);

    @Inject
    EventBus bus;

    @POST
    @Consumes(TEXT_PLAIN)
    public Uni<String> send(String msg) {
        log.info("Sending: {}", msg);
        return bus.<String>request("hello", msg)
                .onItem().transform(m -> m.body().toUpperCase());
    }
}
