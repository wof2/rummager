package pl.codo.rummager.service;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.UploadTransferMetricResult;
import pl.codo.rummager.model.UploadTransferMetricResult;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.UploadTransferMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class UploadTransferService extends AbstractService {

    private static final Logger LOG = Logger.getLogger(UploadTransferService.class);

    @Inject
    ResultsService resultsService;

    @AllArgsConstructor
    @Getter
    class Listener implements  ISpeedTestListener {
        long metricId;

        ResultsService resultsService;

        @Override
        public void onCompletion(SpeedTestReport report) {
            BigDecimal uploadKbps = report.getTransferRateBit().divide(BigDecimal.valueOf(1024));
            UploadTransferMetricResult res = new UploadTransferMetricResult(null,  uploadKbps.intValue(), true );
            resultsService.registerResult(res, metricId);
        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {
        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {
            UploadTransferMetricResult res = new UploadTransferMetricResult(null,  0, false );
            resultsService.registerResult(res, metricId);
        }
    }

    @Transactional
    @Override
    public void perform(Metric metric) {
        UploadTransferMetric utm = (UploadTransferMetric) metric;
        startToMeasureUpload(utm.getUrl(), utm.getFileSizeOctet(), utm.getMaxDuration(), new Listener(metric.id, resultsService ));
    }

    private void startToMeasureUpload( String uri, int fileSizeOctet, int maxDuration, Listener listener) {
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        speedTestSocket.addSpeedTestListener(listener);
        speedTestSocket.startFixedUpload(uri, fileSizeOctet, maxDuration);
    }




}
