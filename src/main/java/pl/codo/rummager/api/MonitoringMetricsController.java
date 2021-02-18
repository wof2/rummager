package pl.codo.rummager.api;

import lombok.var;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.Host;
import pl.codo.rummager.model.MonitoringMetric;
import pl.codo.rummager.service.HostService;
import pl.codo.rummager.service.MonitoringMetricService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/metrics")
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringMetricsController {

    private static final org.jboss.logging.Logger LOG = Logger.getLogger(MonitoringMetricsController.class);

    @Inject
    MonitoringMetricService metricService;

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    @Transactional
    public Response deleteMetric(@PathParam("id") Long id) {
        var ok = metricService.deleteMetricById(id);
        return ok ? Response.ok().build() : Response.notModified().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getMetricById(@PathParam("id") Long id) {
        return Response.ok(metricService.getMetricById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/host/id/{id}")
    public Response getAllMetricsByHostId(@PathParam("id") Long host_id) {
        return Response.ok(metricService.getAllMetricsByHostId(host_id)).build();
    }


    @PATCH
    @Path("/id/{id}")
    public Response updateMetric(@PathParam("id") Long id, @Valid MonitoringMetric metric) {

       try {
           metricService.updateMetric(id, metric);
       }
       catch(NotFoundException exception) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       return Response.ok().build();
    }

    @POST
    @Path("/host/id/{id}")
    public Response addMetric(@PathParam("id") Long host_id, @Valid MonitoringMetric metric, @Context UriInfo uriInfo) {

        metricService.registerMetric(metric, host_id);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(metric.id));
        LOG.debug("New metric created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

}