package pl.codo.rummager.api;

import org.jboss.logging.Logger;
import pl.codo.rummager.service.ResultsService;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
public class ResultsController {

    private static final Logger LOG = Logger.getLogger(ResultsController.class);

    @Inject
    ResultsService resultsService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metric/id/{id}")
    public Response getAllMetricsByMetricId(@PathParam("id") Long metric_id) {
        return Response.ok(resultsService.getAllResultsByMetricId(metric_id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metric/id/{id}/limit/{limit}")
    public Response getMetricsByMetricId(@PathParam("id") Long metric_id, @Min(value = 1) @PathParam("limit") int limit) {
        return Response.ok(resultsService.getResultsByMetricId(metric_id, limit)).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metric/id/{id}/today")
    public Response getMetricsForTodayByMetricId(@PathParam("id") Long metric_id) {
        return Response.ok(resultsService.getResultsForTodayByMetricId(metric_id)).build();
    }



}