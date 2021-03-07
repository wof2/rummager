package pl.codo.rummager.model.metric;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.codo.rummager.model.MetricResult;
import pl.codo.rummager.model.validators.CronExpression;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@RegisterForReflection
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Metric extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;

    @NotNull
    private Boolean isEnabled = true;

    @CronExpression
    private String cronExpression;

    @Size(max=255)
    private String comment;

    @Min(value = 0)
    private int retentionDays = 30;


    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonGetter
    public String getType() {
        return this.getClass().getTypeName();
    }

    @JsonGetter
   // @Valid
    public List<MetricResult> getLastResults() {
        return MetricResult.find("metric_id = ?1 order by sampledAt DESC",  this.id).page(0,10).list();
    }

}
