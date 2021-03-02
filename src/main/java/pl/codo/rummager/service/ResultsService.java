package pl.codo.rummager.service;

import org.jboss.logging.Logger;
import pl.codo.rummager.model.MonitoringMetricResult;
import pl.codo.rummager.model.resource.MonitoringMetricsResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class ResultsService {

    @Inject
    MonitoringMetricsResource metricsResource;


    private static final Logger LOG = Logger.getLogger(ResultsService.class);

    public @Valid List<MonitoringMetricResult> getAllResultsByMetricId(Long metricId) {
       return MonitoringMetricResult.list("Metric_id", metricId);
    }
    public @Valid List<MonitoringMetricResult> getResultsByMetricId(Long metricId, int limit) {
        return MonitoringMetricResult.find("Metric_id=?1 order by sampledAt DESC", metricId).page(0, limit).list();
    }

    public @Valid List<MonitoringMetricResult> getResultsForTodayByMetricId(Long metricId) {
        return MonitoringMetricResult.find("Metric_id=?1 and sampledAt>=?2 order by sampledAt DESC", metricId, LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).list();
    }

}
