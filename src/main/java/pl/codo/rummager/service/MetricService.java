package pl.codo.rummager.service;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.jboss.logging.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.PingMetric;
import pl.codo.rummager.model.resource.MonitoringMetricsResource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MetricService {

    @Inject
    Scheduler quartz;

    @Inject
    MonitoringMetricsResource metricsResource;

    @Inject
    Instance<MetricToJobConverter<? extends Metric,? extends Job>> metricToJobConverters;

    HashMap<Class<Metric>, Class<Job>> metricToJob = new HashMap<>();

    @PostConstruct
    void init() {
        for (MetricToJobConverter c: metricToJobConverters) {
            metricToJob.put(c.appliesTo(), c.getJobClass());
        }
    }

    private static final Logger LOG = Logger.getLogger(MetricService.class);

    public @Valid Metric getMetricById(long id) {
        return Metric.findById(id);
    }

    public Boolean deleteMetricById(Long id) {
       return Metric.deleteById(id);
    }

    public List<Metric> getAllMetrics() {
        return Metric.listAll();
    }

    @Transactional
    public void registerAllEnabledMetrics() {
        getAllMetrics().stream().filter(m -> m.getIsEnabled()).forEach(m -> registerMetric(m));
    }


    @SneakyThrows
    @Transactional()
    public Metric updateMetric(Long id, @Valid Metric metric) {
        Metric persistentMetric = Metric.findById(id);
        if(persistentMetric == null) throw new NotFoundException();
        boolean wasEnabled = persistentMetric.getIsEnabled();


      //  metric.setLastResults(persistentMetric.getLastResults());
        PingMetric updated = metricsResource.update(id, (PingMetric) metric);
        if(wasEnabled && !updated.getIsEnabled()) {
            descheduleMonitoringMetric(updated);
        }
        if(!wasEnabled && updated.getIsEnabled()) {
            registerMetric(updated);
        }

        return updated;

    }

    @Transactional()
    public void registerMetric(@Valid Metric metric) {
        LOG.info("Registering metric: "+metric);

        metric.persist();
        scheduleMonitoringMetric(metric);

        LOG.info("Registering metric [finished]: "+metric);

    }

    public void descheduleMonitoringMetric(Metric mm) throws SchedulerException {
        Class jobClass = metricToJob.get(mm.getClass());
        Set<JobKey> jobKeys = quartz.getJobKeys(GroupMatcher.jobGroupEquals(getJobGroupName(jobClass)));
        for (JobKey jobKey : jobKeys) {
            quartz.deleteJob(jobKey);
        }


    }
    @SneakyThrows
    public void scheduleMonitoringMetric(Metric mm)  {
        String name = mm.getName()+"_"+mm.getClass().getSimpleName()+"_"+mm.getCreatedAt();

        Class jobClass = metricToJob.get(mm.getClass());

        if(jobClass == null) throw new NotImplementedException("no implementation of job for "+mm.getClass().getSimpleName());

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(name, getJobGroupName(jobClass))
                .build();


        job.getJobDataMap().put("MM_ID", mm.id);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name+"_Trigger", getJobGroupName(jobClass))
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(mm.getCronExpression()))
                .build();
        quartz.scheduleJob(job, trigger);
    }

    private static String getJobGroupName(Class jobClass) {
        return jobClass.getSimpleName()+"Group";
    }


}
