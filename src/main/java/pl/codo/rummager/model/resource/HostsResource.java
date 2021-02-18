package pl.codo.rummager.model.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import pl.codo.rummager.model.Host;

public interface HostsResource extends PanacheEntityResource<Host, Long> {
}