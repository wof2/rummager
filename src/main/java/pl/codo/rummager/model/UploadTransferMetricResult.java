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
public class UploadTransferMetricResult extends MetricResult {

    @Min(value = 0)
    private Integer uploadKbps;

    public UploadTransferMetricResult(@Valid Metric parent, @Min(value = 0) Integer uploadKbps, Boolean isSuccess ) {
        super();
        this.setMetric(parent);
        this.uploadKbps = uploadKbps;
        this.setSuccess(isSuccess);
    }
}
