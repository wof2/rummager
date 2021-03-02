package pl.codo.rummager.service;

import org.icmp4j.tool.Ping;
import org.quartz.Job;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.PingMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
