package pl.codo.rummager.model.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.codo.rummager.model.Host;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HostRepository implements PanacheRepository<Host> {



}