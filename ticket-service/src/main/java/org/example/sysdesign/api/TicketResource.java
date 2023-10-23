package org.example.sysdesign.api;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import org.bson.types.ObjectId;
import org.example.sysdesign.api.util.AvailabilityInput;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.TicketInput;
import org.example.sysdesign.model.Availability;
import org.example.sysdesign.model.Ticket;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This class implements the '/tickets' HTTP endpoints.
 */
@Path("/tickets")
public class TicketResource {



    /**
     * GET ticket status.
     * @param id - Ticket id.
     * @return A Uni instance with a HTTP Response containing the ticket information.
     */
    @Path("/purchase/{id}")
    @GET
    @Authenticated
    public Uni<Response> getVerified(@PathParam("id") String id){
        Uni<Ticket> ticket = Ticket.findById(new ObjectId(id));
        return ticket.onItem().transformToUni(item -> handleResult(item));
        
    }

    private Uni<Response> handleResult(Ticket ticket){
        if(ticket == null){
            return Uni.createFrom().item(Response.status(404).entity("This ticket does not exist.").build());
        }else{
            return Uni.createFrom().item(Response.ok(ticket).build());
        }
    }

    /**
     * PUT new availability into system. Consumes JSON.
     * @param date - Date to be altered.
     * @param amount - Amount to be altered by.
     * @return A Uni instance with a HTTP Response containing the total availability for that day.
     */

    @Path("/availability")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> postAvailability(@Context UriInfo uriInfo, AvailabilityInput ava ) {
        Availability availability = ava.createNewAvailability();
        return Availability.find("date", availability.getDate()).firstResult()
        .onItem().transformToUni(item -> setAvailability(item, availability,uriInfo));
    }

    private Uni<Response> setAvailability(ReactivePanacheMongoEntityBase entityBase, Availability newAva, UriInfo uriInfo){
        Availability availability = (Availability) entityBase;
        if(availability == null){
            return Availability.persist(newAva).map(v -> Response.ok(newAva.getAmount()).build());
        }else{
            availability.setAmount(availability.getAmount()+(newAva.getAmount()*10));
            return Availability.update(availability).onItem().transformToUni(item -> Uni.createFrom().item(Response.ok(availability.getAmount()).build()));
        }
    }

    /**
     * POST new ticket to system. Consumes JSON.
     * @param ticket - Ticket to be verified.
     * @return A Uni instance with a HTTP Response.
     */
    @Path("/purchase")
    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> list(@Context UriInfo uriInfo, TicketInput ticketInput) {
        Ticket ticket = ticketInput.createNewTicket();
        return Availability.find("date", ticketInput.date().toString()).firstResult()
        .onItem().transformToUni(item -> setVerified(item, ticket, uriInfo));
    }


    private Uni<Response> setVerified(ReactivePanacheMongoEntityBase entityBase, Ticket ticketInput, UriInfo uriInfo){
        Availability availability = (Availability) entityBase;
        if(availability == null){
            return Uni.createFrom().item(Response.serverError().entity("This date is not available for booking online, please contact the Museum for this booking.").build());
        }else{
            if(availability.getAmount() >= ticketInput.getAmount()){
                ticketInput.setVerified(Ticket.Status.VERIFIED);
                availability.setAmount(availability.getAmount()-ticketInput.getAmount());
                availability.update();

            }else{
                ticketInput.setVerified(Ticket.Status.DENIED);
            }
            Ticket.persist(ticketInput);
            return Ticket.persist(ticketInput)
            .chain(item -> Availability.persistOrUpdate(availability)
            //.map(v -> Response.created(UriBuilder.fromPath(uriInfo.getBaseUri().toString()+"/{id}").build(ticketInput.id)).build()));
            .map(v -> Response.created(uriInfo.getRequestUriBuilder().path(ticketInput.id.toString()).build()).build()));
        }
    }
}
