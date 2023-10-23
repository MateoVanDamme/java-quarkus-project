package org.example.sysdesign.api;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.example.sysdesign.api.util.PagedCatalogusItemResult;
import org.example.sysdesign.api.util.PagedExhibitionResult;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.CatalogusItemInput;
import org.example.sysdesign.model.CatalogusItem;
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
import java.util.List;

/**
 * This class implements the 'catalogus/paintings' HTTP endpoints.
 * <p>
 * Endpoint implementations are annotated with JAX-RS @(Http operation) annotations such as @GET, @POST.
 * By annotating the class with @Path, these endpoints are automatically picked up and added to the HTTP server by Quarkus.
 */
@Path("/catalogus/paintings")
public class CatalogusItemResource {

    /**
     * Create a POST method to create a new catalogusItem.
     * 
     * @param uriInfo - The URI information of the request.
     * @param securityIdentity - The security identity of the request.
     * @param input - The input data for the new catalogusItem.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> createCatalogusItem(@Context UriInfo uriInfo, @Context SecurityIdentity securityIdentity, CatalogusItemInput input) {
        var newItem = input.createNewCatalogusItem();
        return CatalogusItem.persist(newItem)
            .map(
                m -> Response.created(uriInfo.getRequestUriBuilder().path(newItem.id.toString()).build()).build()
            );
    }

    /**
     * Create a GET method to retrieve a single catalogusItem.
     * 
     * @param id - The ID of the catalogusItem to retrieve.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CatalogusItem> getCatalogusItem(@PathParam("id") String id) {
        return CatalogusItem.findById(new ObjectId(id));
    }

    /**
     * Create a PUT method to update an existing catalogusItem.
     * 
     * @param id - The ID of the catalogusItem to update.
     * @param input - The input data for the updated catalogusItem.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> updateCatalogusItem(@PathParam("id") String id, CatalogusItemInput input) {
        return CatalogusItem.<CatalogusItem>findById(new ObjectId(id))
                .onItem()
                .transformToUni(item -> CatalogusItem
                        .persistOrUpdate(input.updateCatalogusItem(item))
                        .map(result -> Response.noContent().build())
                );
    }

    /**
     * Create a DELETE method to delete an existing catalogusItem.
     * 
     * @param id - The ID of the catalogusItem to delete.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> deleteCatalogusItem(@PathParam("id") String id) {
        return CatalogusItem.deleteById(new ObjectId(id))
        .map(success -> success ? Response.noContent().build() : Response.notModified().build());
    }

    /**
     * Create a GET method to retrieve all catalogusItems.
     * 
     * @param page - The page number to retrieve.
     * @param size - The number of catalogusItems to retrieve.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedCatalogusItemResult> getAllCatalogusItems(@QueryParam("page") @DefaultValue("0") int page,
                                                             @QueryParam("size") @DefaultValue("10") int size) {
        var query = CatalogusItem.findAll().page(page, size);
        return Uni.combine().all().unis(query.<CatalogusItem>list(), query.hasNextPage(),query.count())
                .asTuple()
                .map(tuple -> new PagedCatalogusItemResult(tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));
    }

    /**
     * Create a GET method to retrieve all catalogusItems as a list, instead of a paged result.
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<CatalogusItem>> getAllCatalogusItemsAsAList() {
        return CatalogusItem.findAll().list();
    }
    
}
