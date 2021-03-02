package pl.codo.rummager.service;

import lombok.var;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.MonitoringMetricResult;
import pl.codo.rummager.model.PingMonitoringMetricResult;
import pl.codo.rummager.model.metric.PingMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PingService implements Job {

    private static final Logger LOG = Logger.getLogger(PingService.class);
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
       // long hostId = context.getMergedJobDataMap().getLong("HOST_ID");
        LOG.info("Executing job "+context.getJobDetail().getKey());
        var mmId = context.getMergedJobDataMap().getLong("MM_ID");

        PingMetric metric = Metric.findById(mmId);
        List<MonitoringMetricResult> results = pingHost(metric);
        LOG.info("Executing job [finished] "+context.getJobDetail().getKey()+ ". Results are:"+results.stream().map(String::valueOf)
                .collect(Collectors.joining(", ")));

    }
    @Transactional(Transactional.TxType.MANDATORY)
    private List<MonitoringMetricResult> pingHost(PingMetric metric) {
        List<MonitoringMetricResult> results = new ArrayList<>();
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost (metric.getAddress());
        // delegate
        for(int i=0; i<metric.getSamplesPerRun();i++ ) {
            final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
            PingMonitoringMetricResult res = new PingMonitoringMetricResult(metric, response.getRtt(), response.getSuccessFlag());
            res.persist();
            results.add(res);

        }
        metric.persist();
        return results;

    }
    private List<Integer> pingHost(String url, int count) {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost (url);
        // delegate
        final List<IcmpPingResponse> response = IcmpPingUtil.executePingRequests(request, count+1); // bad implementation need to add 1
        return Collections.unmodifiableList(response.stream().map(r -> r.getRtt()).collect(Collectors.toList()));

    }

        /**
         * Returns ping rtt in milliseconds
         * @param url
         * @return
         */
    public int pingHost(String url) {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost (url);
        // delegate
        final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
        // log

       return response.getRtt();

    }
}
