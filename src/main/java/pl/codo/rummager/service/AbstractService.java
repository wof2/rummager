package pl.codo.rummager.service;

import io.quarkus.panache.common.Parameters;
import lombok.var;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pl.codo.rummager.model.MetricResult;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.PingMetric;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractService implements Job {


    @ConfigProperty(name = "rummager.metric.cleanOldResults")
    boolean cleanOldResults;

    private static final Logger LOG = Logger.getLogger(AbstractService.class);
    public static final String MetricIdAttributeName = "MM_ID";;


    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        AbstractService.LOG.info("Executing job " + context.getJobDetail().getKey());
        var mmId = context.getMergedJobDataMap().getLong(MetricIdAttributeName);
        Metric metric = Metric.findById(mmId);

        if(cleanOldResults) {
            long num = cleanOldResults(metric);
            AbstractService.LOG.info("Finished cleaning "+num+" old results for metric " + metric.id);

        }

        perform(metric);
        AbstractService.LOG.info("Executing job [finished] " + context.getJobDetail().getKey());


    }

    private long cleanOldResults(Metric metric) {
       return MetricResult.delete("sampledAt < :date",  Parameters.with("date" , LocalDateTime.now().minusDays(metric.getRetentionDays())));

    }

    public abstract void perform(Metric metric);
}
