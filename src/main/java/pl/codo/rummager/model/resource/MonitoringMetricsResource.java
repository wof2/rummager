package pl.codo.rummager.model.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.PingMetric;

public interface MonitoringMetricsResource extends PanacheEntityResource<PingMetric, Long> {
}