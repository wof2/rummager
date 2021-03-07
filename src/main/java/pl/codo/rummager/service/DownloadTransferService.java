package pl.codo.rummager.service;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.DownloadTransferMetricResult;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.DownloadTransferMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class DownloadTransferService extends AbstractService {

    private static final Logger LOG = Logger.getLogger(DownloadTransferService.class);

    @Inject
    ResultsService resultsService;

    @AllArgsConstructor
    @Getter
    class Listener implements  ISpeedTestListener {
        long metricId;

        ResultsService resultsService;

        @Override
        public void onCompletion(SpeedTestReport report) {
            BigDecimal downloadKbps = report.getTransferRateBit().divide(BigDecimal.valueOf(1024));
            DownloadTransferMetricResult res = new DownloadTransferMetricResult(null,  downloadKbps.intValue(), true );
            resultsService.registerResult(res, metricId);
        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {
        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {
            DownloadTransferMetricResult res = new DownloadTransferMetricResult(null,  0, false );
            resultsService.registerResult(res, metricId);
        }
    }

    @Transactional
    @Override
    public void perform(Metric metric) {
        DownloadTransferMetric dtm = (DownloadTransferMetric) metric;
        startToMeasureDownload(dtm.getUrl(), dtm.getMaxDuration(), new Listener(metric.id, resultsService ));
    }

   private void startToMeasureDownload( String uri, int maxDuration, Listener listener) {
       SpeedTestSocket speedTestSocket = new SpeedTestSocket();
       speedTestSocket.addSpeedTestListener(listener);
       speedTestSocket.startFixedDownload(uri, maxDuration);
   }




}
