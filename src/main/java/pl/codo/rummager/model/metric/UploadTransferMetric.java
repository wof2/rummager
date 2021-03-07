package pl.codo.rummager.model.metric;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


@RegisterForReflection
@Entity
@Setter
@Getter
public class UploadTransferMetric extends Metric {
    private static final String validUrlRegex = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
   // @Pattern(regexp="^("+ validUrlRegex +")$")
    private String url;

    @Min(value = 1)
    private int maxDuration = 10000;


    @Min(value = 1)
    private int fileSizeOctet = 10000;






}
