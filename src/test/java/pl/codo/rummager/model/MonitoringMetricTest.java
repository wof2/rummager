package pl.codo.rummager.model;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import pl.codo.rummager.model.validators.CronExpression;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MonitoringMetricTest {

    public void testValidationParamPublic(@CronExpression String expr) {
       String r = expr.toLowerCase(Locale.ROOT);
    }

    public void testValidationValidPublic(@Valid MonitoringMetric metric) {
        MetricType type = metric.getMetricType();

    }

    public void testHost(@Valid Host host )
    {
        String type = host.getName();


    }
    public void testPublicPublic(@Valid MonitoringMetric metric )
    {
        testValidationValidPublic(metric);

    }


    @CronExpression
    public String testReturn(String x){
        return x;
    }

    public String testNotNull(@NotNull String x){
        return x;
    }
    @Test
    void runTestHost() {
        MonitoringMetric mm = new MonitoringMetric();
        mm.setCronExpression("* 0 0 ? * * *");
     //   mm.setCronExpression("bad");
        mm.setMetricType(MetricType.Ping);
        mm.setSamplesPerRun(-1);
        Host host = new Host();
        host.setAddress("xxx");
        host.setName("Name");
        host.setMonitoringMetrics(new ArrayList<>());
        host.getMonitoringMetrics().add(mm);
       // testHost(host);
        assertThrows(ConstraintViolationException.class, () -> testValidationValidPublic(mm));
        assertThrows(ConstraintViolationException.class, () -> testPublicPublic(mm));
        assertThrows(ConstraintViolationException.class, () -> testHost(host));
    }


    @Test
    void testNotNullParam() {
        assertThrows(ConstraintViolationException.class, () -> testNotNull(null));
    }

    @Test
    void runTestPublicPublic() {
        MonitoringMetric metric = new MonitoringMetric();
        metric.setCronExpression("tes");
        assertThrows(Exception.class, () -> testPublicPublic(metric));
    }

    @Test
    void testReturn() {
        assertThrows(Exception.class, () -> testReturn("test"));
    }

    @Test
    void testValidationParamPublic() {
        assertThrows(Exception.class, () -> testValidationParamPublic("test"));
    }

    @Test
    void testValidationValidPublic() {
        MonitoringMetric metric = new MonitoringMetric();
        metric.setCronExpression("tes");
        assertThrows(Exception.class, () -> testValidationValidPublic(metric));

    }
}