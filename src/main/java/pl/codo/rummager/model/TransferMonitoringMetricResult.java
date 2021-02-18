package pl.codo.rummager.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

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
public class TransferMonitoringMetricResult extends MonitoringMetricResult {

    @Min(value = 0)
    private Integer uploadKbps;

    @Min(value = 0)
    private Integer downloadKbps;

    public TransferMonitoringMetricResult(@Valid MonitoringMetric parent, @Min(value = 0) Integer uploadKbps,  @Min(value = 0) Integer downloadKbps, Boolean isSuccess ) {
        super();
        this.setMonitoringMetric(parent);
        this.uploadKbps = uploadKbps;
        this.downloadKbps = downloadKbps;
        this.setSuccess(isSuccess);
    }
}
