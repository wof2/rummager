package pl.codo.rummager.service;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.jboss.logging.Logger;
import org.quartz.*;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;
import pl.codo.rummager.model.resource.HostsResource;
import pl.codo.rummager.model.resource.MonitoringMetricsResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class MonitoringMetricService {

    @Inject
    Scheduler quartz;

    @Inject
    MonitoringMetricsResource metricsResource;


    private static final Logger LOG = Logger.getLogger(MonitoringMetricService.class);

    public @Valid Host getMetricById(long id) {
        return MonitoringMetric.findById(id);
    }

    public Boolean deleteMetricById(Long id) {
       return MonitoringMetric.deleteById(id);
    }

    public @Valid List<MonitoringMetric> getAllMetricsByHostId(Long host_id) {
       return MonitoringMetric.list("Host.id = :host_id", host_id);
    }
    @Transactional()
    public MonitoringMetric updateMetric(Long id, @Valid MonitoringMetric metric) {
        MonitoringMetric persistentMetric = MonitoringMetric.findById(id);
        if(persistentMetric == null) throw new NotFoundException();

      //  metric.setLastResults(persistentMetric.getLastResults());
        return metricsResource.update(id, metric);

    }

    @Transactional()
    public void registerMetric(@Valid MonitoringMetric metric, Long host_id) {
        LOG.info("Registering metric: "+metric);
        metric.setHost(Host.findById(host_id));
        metric.persist();
        scheduleMonitoringMetric(metric);

        LOG.info("Registering metric [finished]: "+metric);

    }

    @SneakyThrows
    public void scheduleMonitoringMetric(MonitoringMetric mm)  {
        String name = mm.getHost().getName()+"_"+mm.getMetricType()+"_"+mm.getCreatedAt();
        Class jobClass = null;
        switch (mm.getMetricType()) {
            case Ping:
                jobClass = PingService.class;
                break;

            case TransferSpeed:
                throw new NotImplementedException("TransferSpeed");
                //TODO: strategy pattern
        }

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(name, "pingServiceGroup")
                .build();
        job.getJobDataMap().put("HOST_ID", mm.getHost().id);
        job.getJobDataMap().put("MM_ID", mm.id);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name+"_Trigger", "pingServiceGroup")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(mm.getCronExpression()))
                .build();
        quartz.scheduleJob(job, trigger);
    }


}
