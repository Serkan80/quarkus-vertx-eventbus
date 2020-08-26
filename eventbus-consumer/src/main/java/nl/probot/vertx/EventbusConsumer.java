package nl.probot.vertx;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class EventbusConsumer {

    static final Logger log = LoggerFactory.getLogger(EventbusConsumer.class);

    @Inject
    Vertx vertx;

    public void init(@Observes StartupEvent ev) {
        log.info("this.vertx.isClustered() = {}", vertx.isClustered());
        vertx.eventBus().consumer("hello")
                .handler(h -> {
                    log.info("received: {}", h.body());
                    h.reply("Hello back from consumer: " + h.body());
                });
//                .completionHandler(res -> {
//                    if (res.succeeded()) {
//                        log.info("in completed");
////                        log.info(res.result().toString());
//                    } else if (res.failed()) {
//                        log.error("in failed");
//                    } else {
//                        log.error("in else...");
//                    }
//                });
    }

    @ConsumeEvent("hello")
    public Uni<String> receive(String msg) {
        log.info("Received: {}", msg);
        return Uni.createFrom().item(() -> msg.toUpperCase());
    }
}
