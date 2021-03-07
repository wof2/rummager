package pl.codo.rummager.service;

import org.jboss.logging.Logger;
import pl.codo.rummager.model.DownloadTransferMetricResult;
import pl.codo.rummager.model.MetricResult;
import pl.codo.rummager.model.metric.DownloadTransferMetric;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.resource.MetricsResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class ResultsService {

    @Inject
    MetricsResource metricsResource;


    private static final Logger LOG = Logger.getLogger(ResultsService.class);

    public @Valid List<MetricResult> getAllResultsByMetricId(Long metricId) {
       return MetricResult.list("Metric_id", metricId);
    }
    public @Valid List<MetricResult> getResultsByMetricId(Long metricId, int limit) {
        return MetricResult.find("Metric_id=?1 order by sampledAt DESC", metricId).page(0, limit).list();
    }

    public @Valid List<MetricResult> getResultsForTodayByMetricId(Long metricId) {
        return MetricResult.find("Metric_id=?1 and sampledAt>=?2 order by sampledAt DESC", metricId, LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).list();
    }

    @Transactional
    public void registerResult(@Valid  MetricResult result, long metricId) {
        Metric metric = Metric.findById(metricId);
        if(result.getSuccess()) {
            LOG.info("Saving result - "+result);
        }else {
            LOG.warn("Saving result - "+result);
        }
        result.setMetric(metric);
        result.persist();
    }

    }
