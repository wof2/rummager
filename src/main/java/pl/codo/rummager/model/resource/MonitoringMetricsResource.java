package pl.codo.rummager.model.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;

public interface MonitoringMetricsResource extends PanacheEntityResource<MonitoringMetric, Long> {
}