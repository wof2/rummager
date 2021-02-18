package pl.codo.rummager.api;

import lombok.var;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.service.HostService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Path("/hosts")
@Produces(MediaType.APPLICATION_JSON)
public class HostsController {

    @Inject
    HostService hostService;

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    @Transactional
    public Response deleteHost(@PathParam("id") Long id) {
        var ok = hostService.deleteHostById(id);
        return ok ? Response.ok().build() : Response.notModified().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getHostById(@PathParam("id") Long id) {
        return Response.ok(hostService.getHostById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHosts() {
        return Response.ok(hostService.getAllHosts()).build();
    }


    @PATCH
    @Path("/id/{id}")
    public Response updateHost(@PathParam("id") Long id, @Valid Host host) {

       try {
           hostService.updateHost(id, host);
       }
       catch(NotFoundException exception) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       return Response.ok().build();
    }

    @POST
    public Response addHost(@Valid Host host, @Context UriInfo uriInfo) {
          hostService.registerHost(host);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(host.id));
       // LOGGER.debug("New hero created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

}