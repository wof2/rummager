package pl.codo.rummager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import pl.codo.rummager.model.metric.Metric;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RegisterForReflection
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
   indexes = {@Index(name = "sampledAt_IDX", columnList = "Metric_id, sampledAt")}
)
public abstract class MonitoringMetricResult extends PanacheEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Metric metric;

    @NotNull
    @JsonFormat (shape = JsonFormat.Shape.STRING)
    private LocalDateTime sampledAt = LocalDateTime.now();

    @NotNull
    @Type(type="boolean")
    private Boolean success;




}
