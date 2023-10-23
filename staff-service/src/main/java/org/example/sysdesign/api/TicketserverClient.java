package org.example.sysdesign.api;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.sysdesign.model.Rating;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.example.sysdesign.api.util.AnswerConstruct;

@Path("/tickets")
@RegisterRestClient
public interface TicketserverClient {

    @POST
    @Path("/availability")
    Uni<Response> putTicketserver(AnswerConstruct answerConstruct);
}
