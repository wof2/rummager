package pl.codo.rummager.model.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MonitoringMetricRepository implements PanacheRepository<MonitoringMetric> {


}