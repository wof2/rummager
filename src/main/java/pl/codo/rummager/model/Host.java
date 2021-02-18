package pl.codo.rummager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.monitor.Monitor;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Host extends PanacheEntity {

    private static final String validIpAddressRegex = "((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))";
    private static final String validHostnameRegex = "((([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9]))";
    @Pattern(regexp="^("+validIpAddressRegex+"|"+validHostnameRegex+")$")
    private String address;

    @NotNull
    private Boolean isEnabled = true;

    @NotNull
    @Size(min = 2, max = 255)
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name= "host_id")
    @Valid
    private List<MonitoringMetric> monitoringMetrics = new ArrayList<>();

}
