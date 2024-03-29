package pl.codo.rummager.api;

import lombok.var;
import org.jboss.logging.Logger;
import pl.codo.rummager.model.metric.Metric;
import pl.codo.rummager.model.metric.PingMetric;
import pl.codo.rummager.model.metric.DownloadTransferMetric;
import pl.codo.rummager.model.metric.UploadTransferMetric;
import pl.codo.rummager.service.MetricService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/metrics")
@Produces(MediaType.APPLICATION_JSON)
public class MetricsController {

    private static final org.jboss.logging.Logger LOG = Logger.getLogger(MetricsController.class);

    @Inject
    MetricService metricService;

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
    public Response getAllMetrics() {
        return Response.ok(metricService.getAllMetrics()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getMetricById(@PathParam("id") Long id) {
        return Response.ok(metricService.getMetricById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/{type}")
    public Response getMetricsByType(@PathParam("type") MetricType type) {
        return Response.ok(metricService.getMetricsByType(type)).build();
    }



    @PATCH
    @Path("/ping/id/{id}")
    public Response updatePingMetric(@PathParam("id") Long id, @Valid PingMetric metric) {

       try {
           metricService.updateMetric(id, metric);
       }
       catch(NotFoundException exception) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       return Response.ok().build();
    }

    @PATCH
    @Path("/transfer/download/id/{id}")
    public Response updateDownloadTransferMetric(@PathParam("id") Long id, @Valid DownloadTransferMetric metric) {

        try {
            metricService.updateMetric(id, metric);
        }
        catch(NotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    @PATCH
    @Path("/transfer/upload/id/{id}")
    public Response updateUploadTransferMetric(@PathParam("id") Long id, @Valid UploadTransferMetric metric) {

        try {
            metricService.updateMetric(id, metric);
        }
        catch(NotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/ping")
    public Response addPingMetric(@Valid PingMetric metric, @Context UriInfo uriInfo) {

        metricService.registerMetric(metric);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(metric.id));
        LOG.debug("New metric created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @POST
    @Path("/transfer/download")
    public Response addDownloadTransferMetric(@Valid DownloadTransferMetric metric, @Context UriInfo uriInfo) {

        metricService.registerMetric(metric);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(metric.id));
        LOG.debug("New download metric created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @POST
    @Path("/transfer/upload")
    public Response addUploadTransferMetric(@Valid UploadTransferMetric metric, @Context UriInfo uriInfo) {

        metricService.registerMetric(metric);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(metric.id));
        LOG.debug("New upload metric created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

}