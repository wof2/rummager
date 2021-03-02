package pl.codo.rummager.service;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import lombok.var;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import org.jboss.logging.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.MonitoringMetricResult;
import pl.codo.rummager.model.PingMonitoringMetricResult;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TransferService /*implements  */{/* implements Job */



    @PreDestroy
    public void onDestroy() {
      //  speedTestSocket.shutdownAndWait();
    }

    private static final Logger LOG = Logger.getLogger(TransferService.class);

    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LOG.info("Executing job "+context.getJobDetail().getKey());
        var mmId = context.getMergedJobDataMap().getLong("MM_ID");

        Metric metric = Metric.findById(mmId);
   //     List<MonitoringMetricResult> results = measureTransfer(metric.getHost().getAddress(), );
     //   LOG.info("Executing job [finished] "+context.getJobDetail().getKey()+ ". Results are:"+results.stream().map(String::valueOf)
     //           .collect(Collectors.joining(", ")));

    }


   public void measureTransfer(String uri, int maxDuration, ISpeedTestListener listener) {
       SpeedTestSocket speedTestSocket = new SpeedTestSocket();
       speedTestSocket.startFixedDownload(uri, maxDuration);
       speedTestSocket.addSpeedTestListener(listener);

   }
   public void measureTransfer(String uri, int maxDuration) {
      measureTransfer(uri, maxDuration, new ISpeedTestListener() {
           @Override
           public void onCompletion(SpeedTestReport report) {
               LOG.info(Thread.currentThread().getName() + " - URI:"+uri+" , mbps:"+ report.getTransferRateBit().divide(BigDecimal.valueOf(1024*1024)));
           }

           @Override
           public void onProgress(float percent, SpeedTestReport report) {
           }

           @Override
           public void onError(SpeedTestError speedTestError, String errorMessage) {
           }
       });

   }




}
