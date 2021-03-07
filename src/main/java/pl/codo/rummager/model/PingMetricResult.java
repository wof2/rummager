package pl.codo.rummager.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import pl.codo.rummager.model.metric.Metric;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RegisterForReflection
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PingMetricResult extends MetricResult {

    @Min(value = 1)
    private Integer pingRTT;

    public PingMetricResult(@Valid Metric parent, @Min(value = 1) Integer pingRTT, Boolean isSuccess) {
        super();
        this.setMetric(parent);
        this.pingRTT = pingRTT;
        this.setSuccess(isSuccess);
    }
}
