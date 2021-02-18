package pl.codo.rummager.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RegisterForReflection
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PingMonitoringMetricResult extends MonitoringMetricResult {

    @Min(value = 1)
    private Integer pingRTT;

    public PingMonitoringMetricResult(@Valid MonitoringMetric parent, @Min(value = 1) Integer pingRTT, Boolean isSuccess) {
        super();
        this.setMonitoringMetric(parent);
        this.pingRTT = pingRTT;
        this.setSuccess(isSuccess);
    }
}
