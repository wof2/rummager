package pl.codo.rummager.service;

import lombok.var;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pl.codo.rummager.model.MetricResult;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.PingMetricResult;
import pl.codo.rummager.model.metric.PingMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PingService extends AbstractService {

    private static final Logger LOG = Logger.getLogger(PingService.class);


    @Transactional(Transactional.TxType.MANDATORY)
    public void perform(Metric metric) {
        PingMetric pm = (PingMetric) metric;
        List<MetricResult> results = new ArrayList<>();
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost (pm.getAddress());
        // delegate
        for(int i=0; i<pm.getSamplesPerRun();i++ ) {
            final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
            PingMetricResult res = new PingMetricResult(pm, response.getRtt(), response.getSuccessFlag());
            res.persist();
            results.add(res);

        }
        PingService.LOG.info("PingService finished " + metric.getName() + ". Results are:" + results.stream().map(String::valueOf)
                .collect(Collectors.joining(", ")));
        pm.persist();

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
