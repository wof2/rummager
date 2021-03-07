package pl.codo.rummager.service.converters;

import pl.codo.rummager.model.metric.PingMetric;
import pl.codo.rummager.service.PingService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PingMetricToJobConverter implements MetricToJobConverter<PingMetric, PingService> {


    @Override
    public Class<PingMetric> appliesTo() {
        return PingMetric.class;
    }

    @Override
    public Class<PingService> getJobClass() {
        return  PingService.class;
    }
}
