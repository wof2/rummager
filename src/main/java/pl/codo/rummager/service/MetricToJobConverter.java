package pl.codo.rummager.service;

import org.quartz.Job;
import pl.codo.rummager.model.metric.Metric;

public interface MetricToJobConverter<M extends Metric, J extends Job> {

    Class<M> appliesTo();

    Class<J> getJobClass();

}
