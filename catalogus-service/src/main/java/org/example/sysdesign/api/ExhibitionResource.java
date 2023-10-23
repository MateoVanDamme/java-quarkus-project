package org.example.sysdesign.api;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.example.sysdesign.api.util.PagedExhibitionResult;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.ExhibitionInput;
import org.example.sysdesign.model.ChatMessage;
import org.example.sysdesign.model.Topic;
import org.example.sysdesign.model.Exhibition;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.annotation.security.RolesAllowed;

/**
 * This class implements the '/catalogus/exhibitions' HTTP endpoints.
 * <p>
 * Endpoint implementations are annotated with JAX-RS @(Http operation) annotations such as @GET, @POST.
 * By annotating the class with @Path, these endpoints are automatically picked up and added to the HTTP server by Quarkus.
 */
@Path("/catalogus/exhibitions")
public class ExhibitionResource {

    /**
     * Create a POST method to create a new exhibition.
     * 
     * @param uriInfo - The URI information of the request.
     * @param securityIdentity - The security identity of the request.
     * @param input - The input data for the new exhibition.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> createExhibition(@Context UriInfo uriInfo, @Context SecurityIdentity securityIdentity, ExhibitionInput input) {
        var newItem = input.createNewExhibition();
        return Exhibition.persist(newItem)
            .map(
                m -> Response.created(uriInfo.getRequestUriBuilder().path(newItem.id.toString()).build()).build()
            );
    }

    /**
     * Create a GET method to retrieve a single exhibition.
     * 
     * @param id - The id of the exhibition to retrieve.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Exhibition> getExhibition(@PathParam("id") String id) {
        return Exhibition.findById(new ObjectId(id));
    }

    /**
     * Create a PUT method to update an existing exhibition.
     * 
     * @param id - The id of the exhibition to update.
     * @param input - The input data for the updated exhibition.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> updateCatalogusItem(@PathParam("id") String id, ExhibitionInput input) {
        return Exhibition.<Exhibition>findById(new ObjectId(id))
                .onItem()
                .transformToUni(item -> Exhibition
                        .persistOrUpdate(input.updateExhibition(item))
                        .map(result -> Response.noContent().build())
                );
    }

    /**
     * Create a DELETE method to delete an existing exhibition.
     * 
     * @param id - The id of the exhibition to delete.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> deleteExhibition(@PathParam("id") String id) {
        return Exhibition.findById(new ObjectId(id)).onItem().transformToUni(exhibition -> {
            if (exhibition == null) {
                return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).build());
            }
            return exhibition.delete().onItem().transform(v -> Response.ok().build());
        });
    }

    /**
     * Create a GET method to retrieve all exhibitions.
     * 
     * @param page - The page number to retrieve.
     * @param size - The number of items per page.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedExhibitionResult> getAllExhibitions(@QueryParam("page") @DefaultValue("0") int page, 
                                        @QueryParam("size") @DefaultValue("10") int size) {
        var query = Exhibition.findAll().page(page, size);
        return Uni.combine().all().unis(query.<Exhibition>list(), query.hasNextPage(), query.count())
                .asTuple()
                .map(tuple -> new PagedExhibitionResult(tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));
    }
}
