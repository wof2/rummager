package pl.codo.rummager;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.service.HostService;

import java.util.List;

@ApplicationScoped
public class ApplicationLifecycle {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");
    
    @Inject
    private HostService hostService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        List<Host> hosts = hostService.getAllHosts();
        hosts.stream().filter(h -> h.getIsEnabled()).forEach(host -> hostService.registerHost(host));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}
