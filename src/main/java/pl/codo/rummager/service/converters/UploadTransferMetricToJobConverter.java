package pl.codo.rummager.service.converters;

import pl.codo.rummager.model.metric.DownloadTransferMetric;
import pl.codo.rummager.model.metric.UploadTransferMetric;
import pl.codo.rummager.service.DownloadTransferService;
import pl.codo.rummager.service.UploadTransferService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UploadTransferMetricToJobConverter implements MetricToJobConverter<UploadTransferMetric, UploadTransferService> {


    @Override
    public Class<UploadTransferMetric> appliesTo() {
        return UploadTransferMetric.class;
    }

    @Override
    public Class<UploadTransferService> getJobClass() { return  UploadTransferService.class;
    }
}
