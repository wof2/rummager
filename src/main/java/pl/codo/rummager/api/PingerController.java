package pl.codo.rummager.api;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.icmp4j.IcmpPingUtil;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import pl.codo.rummager.service.PingService;

@Path("/pinger")
public class PingerController {

    @Inject
    PingService pingService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String pingHost(@QueryParam("host") @DefaultValue("192.168.0.1") String host) {
        return String.valueOf(pingService.pingHost(host));

    }
}