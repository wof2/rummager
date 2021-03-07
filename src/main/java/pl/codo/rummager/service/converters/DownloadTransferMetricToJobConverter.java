package pl.codo.rummager.service.converters;

import pl.codo.rummager.model.metric.DownloadTransferMetric;
import pl.codo.rummager.model.metric.PingMetric;
import pl.codo.rummager.service.DownloadTransferService;
import pl.codo.rummager.service.PingService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DownloadTransferMetricToJobConverter implements MetricToJobConverter<DownloadTransferMetric, DownloadTransferService> {


    @Override
    public Class<DownloadTransferMetric> appliesTo() {
        return DownloadTransferMetric.class;
    }

    @Override
    public Class<DownloadTransferService> getJobClass() {
        return  DownloadTransferService.class;
    }
}
