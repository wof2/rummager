package pl.codo.rummager.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.jboss.logging.Logger;
import org.quartz.*;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;
import pl.codo.rummager.model.MonitoringMetricResult;
import pl.codo.rummager.model.PingMonitoringMetricResult;
import pl.codo.rummager.model.resource.MonitoringMetricsResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
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
       return MonitoringMetricResult.list("MonitoringMetric_id", metricId);
    }
    public @Valid List<MonitoringMetricResult> getResultsByMetricId(Long metricId, int limit) {
        return MonitoringMetricResult.find("MonitoringMetric_id=?1 order by sampledAt DESC", metricId).page(0, limit).list();
    }

    public @Valid List<MonitoringMetricResult> getResultsForTodayByMetricId(Long metricId) {
        return MonitoringMetricResult.find("MonitoringMetric_id=?1 and sampledAt>=?2 order by sampledAt DESC", metricId, LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).list();
    }

}
