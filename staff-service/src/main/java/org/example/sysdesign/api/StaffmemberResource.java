package org.example.sysdesign.api;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.example.sysdesign.api.util.Roles;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.annotation.security.RolesAllowed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.sysdesign.api.TicketserverClient;

import java.util.Arrays;
import java.util.ArrayList;
// import java.util.ArrayArrayList;
import java.util.Date;

import org.example.sysdesign.api.util.PagedStaffmemberResult;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.StaffmemberInput;
import org.example.sysdesign.api.util.AnswerConstruct; 
import org.example.sysdesign.model.Staffmember;
/**
 * This class implements the '/staff' HTTP endpoints.
 * 
 * 
 */

@Path("/staff")
public class StaffmemberResource {


    @RestClient
    TicketserverClient ticketserverClient;

    /**
     * Handler method for when a new staffmember is posted to the endpoint. This send the dates of the new staffmember to the ticketserver.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> create(@Context UriInfo uriInfo, @Context SecurityIdentity securityIdentity, StaffmemberInput input){
        var newStaffmember = input.createNewStaffmember();
        AnswerConstruct answerConstruct = new AnswerConstruct(newStaffmember.getPlanned(), 1);
        return ticketserverClient.putTicketserver(answerConstruct).chain(item -> Staffmember.persist(newStaffmember).map(
            v -> Response.created(uriInfo.getRequestUriBuilder().path(newStaffmember.id.toString()).build()).build()));
    }

    /**
     * Handler method for listing the available staffmembers.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedStaffmemberResult> list(@QueryParam("pageIndex") @DefaultValue("0") Integer pageIndex,
            @QueryParam("pageSize") @DefaultValue(("25")) Integer pageSize) {
        var query = Staffmember.findAll().page(pageIndex, pageSize);
        return Uni.combine().all().unis(query.<Staffmember>list(), query.hasNextPage(), query.count())
                .asTuple()
                .map(results -> new PagedStaffmemberResult(results.getItem1(), results.getItem2(), results.getItem3()));
    }

    /**
     * Handler method for updating a specific staffmember. It filters the dates of the staffmember that changed and sends the changed dates to the ticketserver.
     * 
     * input.getPlanned() is the new list of dates
     * Staffmember.findById(new ObjectId(staffmemberId)).getPlanned() is a uni that contains the old list of dates
     */


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> update(@PathParam("id") String staffmemberId, StaffmemberInput input) {

        return Staffmember.<Staffmember>findById(new ObjectId(staffmemberId)).onItem().transformToUni(oldStaffmember -> sendToTicket(oldStaffmember, input.getPlanned(), staffmemberId, input));

    }

    private Uni<Response> sendToTicket(Staffmember oldStaffmember, Date newDates, String staffmemberId, StaffmemberInput input) {
        
        /* 
        if(newDates != null){
            AnswerConstruct answerConstruct = new AnswerConstruct(newDates, 1);
            ticketserverClient.putTicketserver(answerConstruct);
        }

        if(oldStaffmember.getPlanned() != null){
            AnswerConstruct answerConstruct = new AnswerConstruct(oldStaffmember.getPlanned(), -1);
            ticketserverClient.putTicketserver(answerConstruct);
        }
        */

        return Staffmember.<Staffmember>findById(new ObjectId(staffmemberId))
        .onItem().transformToUni(staffmember -> Staffmember
                .persistOrUpdate(input.updateStaffmember(staffmember)))
                .map(result -> Response.noContent().build());
    }

    /**
     * Handler method for retrieving a specific staffmember.
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Staffmember> getStaffmember(@PathParam("id") String id) {
        return Staffmember.findById(new ObjectId(id));
    }

    /**
     * Handler method for deleting a specific staffmember.
     */
    @Path("{id}")
    @DELETE
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> delete(@PathParam("id") String staffmemberId) {

        return Staffmember.deleteById(new ObjectId(staffmemberId))
                .map(success -> success ? Response.noContent().build() : Response.notModified().build());
    }


    /**
     * Handler method for retrieving the count of the stafmembers
     */
    @Path("/Capacity")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Long> count() {
        return Staffmember.count();
    }
}

