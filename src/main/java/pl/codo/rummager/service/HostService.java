package pl.codo.rummager.service;

import com.cronutils.validation.Cron;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.quartz.*;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.repository.HostRepository;
import pl.codo.rummager.model.resource.HostsResource;
import pl.codo.rummager.model.validators.CronExpression;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HostService {

    @Inject
    org.quartz.Scheduler quartz;

    @Inject
    HostsResource hostsResource;

    @Inject
    MonitoringMetricService metricService;

    private static final Logger LOG = Logger.getLogger(HostService.class);

    public @Valid Host getHostById(long id) {
        return Host.findById(id);
    }

    public Boolean deleteHostById(Long id) {
       return Host.deleteById(id);
    }

    public @Valid List<Host> getAllHosts() {
        return Host.listAll();
    }
    @Transactional()
    public Host updateHost(Long id, @Valid Host host) {
        Host persistentHost = Host.findById(id);
        if(persistentHost == null) throw new NotFoundException();

        host.setMonitoringMetrics(persistentHost.getMonitoringMetrics());
        return hostsResource.update(id, host);

    }

    @Transactional()
    public void registerHost(@Valid Host host) {
        LOG.info("Registering host: "+host.getName());
      //  host.getMonitoringMetrics().forEach(metric -> metric.getLastResults().clear());

        host.persist();
        host.getMonitoringMetrics().stream().filter(m->m.getIsEnabled()).forEach(mm ->{mm.setHost(host); metricService.scheduleMonitoringMetric(mm);});
        LOG.info("Registering host [finished]: "+host.getName());

    }




}
