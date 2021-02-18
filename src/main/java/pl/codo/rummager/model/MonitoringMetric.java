package pl.codo.rummager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.Where;
import pl.codo.rummager.model.validators.CronExpression;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringMetric extends PanacheEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Host host;

    @NotNull
    private Boolean isEnabled = true;

    @CronExpression
    private String cronExpression;

    @Size(max=255)
    private String comment;

    @Min( value = 1)
    private int samplesPerRun = 1;

    @NotNull
    private MetricType metricType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonGetter
    @Valid
    public List<MonitoringMetricResult> getLastResults() {
        return MonitoringMetricResult.find("MonitoringMetric_id = ?1 order by sampledAt DESC",  this.id).page(0,10).list();
    }

}
