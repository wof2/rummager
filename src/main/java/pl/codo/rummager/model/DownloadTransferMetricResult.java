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
@ToString(callSuper = true)
public class DownloadTransferMetricResult extends MetricResult {

    @Min(value = 0)
    private Integer downloadKbps;

    public DownloadTransferMetricResult(@Valid Metric parent, @Min(value = 0) Integer downloadKbps, Boolean isSuccess ) {
        super();
        this.setMetric(parent);
        this.downloadKbps = downloadKbps;
        this.setSuccess(isSuccess);
    }
}
