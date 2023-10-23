package org.example.sysdesign.api;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.example.sysdesign.api.util.PagedRatingResult;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.RatingInput;
import org.example.sysdesign.model.Rating;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/ratings")
public class RatingResource {
    /**
     * Handler method for when a new Rating is posted to the endpoint.
     * Notice the use of several JAX-RS and Quarkus annotations:
     * - @Path is used to define a sub-path for this operation.
     * - @POST defines the type of HTTP operation that is handled.
     * - @Consumes defines the expected Content-Type of the request body
     * - @Authenticated indicates that only authenticated users may call this
     * operation (but no specific role is required).
     * - @Context is used to inject the SecurityIdentity and UriInfo based on the
     * session.
     *
     * @param uriInfo          - Class representing the request URI, used to build
     *                         the new instance Location response header.
     * @param securityIdentity - Authentication info for the request.
     * @param input            - Input for the that rating to be created.
     * @return A Uni instance that will produce an HTTP Response.
     */
    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<Response> create(@Context UriInfo uriInfo, @Context SecurityIdentity securityIdentity, RatingInput input,
            @PathParam("id") String paintingId) {
        var newRating = input.createNewRating(
                securityIdentity.getPrincipal().getName(),
                paintingId);
        return Rating.persist(newRating).map(
                v -> Response.created(uriInfo.getRequestUriBuilder().path(newRating.id.toString()).build()).build());
    }

    /**
     * Handler method for listing the available ratings.
     * Notice the use of several JAX-RS and Quarkus annotations:
     * - @GET defines the type of HTTP operation that is handled.
     * - @Produces defines the Content-Type of the HTTP response.
     * - @QueryParam is used to inject query parameters as method arguments.
     * - @DefaultValue is used to set default values in case a query parameter is
     * omitted from the request.
     *
     * @param pageIndex - Index of what page of the result-set that should be
     *                  returned.
     * @param pageSize  - The maximum number of ratings that should be included in a
     *                  single result page.
     * @return A Uni instance that will produce a paged rating result.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedRatingResult> list(@QueryParam("pageIndex") @DefaultValue("0") Integer pageIndex,
            @QueryParam("pageSize") @DefaultValue(("25")) Integer pageSize) {
        var query = Rating.findAll().page(pageIndex, pageSize);
        return Uni.combine().all().unis(query.<Rating>list(), query.hasNextPage(), query.count())
                .asTuple()
                .map(results -> new PagedRatingResult(results.getItem1(), results.getItem2(), results.getItem3()));
    }

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedRatingResult> listAll() {
        var query = Rating.findAll();
        return Uni.combine().all().unis(query.<Rating>list(), query.hasNextPage(), query.count())
                .asTuple()
                .map(results -> new PagedRatingResult(results.getItem1(), results.getItem2(), results.getItem3()));
    }



    /**
     * Handler method for updating a specific rating.
     * Notice the use of several JAX-RS and Quarkus annotations:
     * - @Path is used to define a sub-path for this operation.
     * - @PUT defines the type of HTTP operation that is handled.
     * - @Consumes defines the expected Content-Type of the request body
     * - @RolesAllowed is used to enforce basic role-based access control: only
     * users with the admin role can execute this operation.
     * - @Context is used to inject the SecurityIdentity
     * - @PathParam is used to inject the ratingId based on a path parameter.
     *
     * @param securityIdentity - Authentication info for the request.
     * @param ratingId         - The id of the rating to update.
     * @param input            - Input for the update operation.
     * @return A Uni instance that will produce an HTTP Response.
     */
    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> update(@Context SecurityIdentity securityIdentity,
            @PathParam("id") String ratingId, RatingInput input) {
        return Rating.<Rating>findById(new ObjectId(ratingId))
                .onItem().transformToUni(rating -> Rating
                        .persistOrUpdate(input.updateRating(rating))
                        .map(result -> Response.noContent().build()));
    }

    /**
     * Handler method for retrieving a specific rating.
     * Notice the use of several JAX-RS and Quarkus annotations:
     * - @Path is used to define a sub-path for this operation.
     * - @GET defines the type of HTTP operation that is handled.
     * - @Produces defines the Content-Type of the HTTP response.
     * - @PathParam is used to inject the ratingId based on a path parameter.
     *
     * @param ratingId - The id of the rating to retrieve.
     * @return A Uni instance that will produce the requested rating (if it
     *         exists).
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Rating> get(@PathParam("id") String ratingId) {
        return Rating.findById(new ObjectId(ratingId));
    }

    /**
     * Handler method for deleting a specific rating.
     * Notice the use of several JAX-RS and Quarkus annotations:
     * - @Path is used to define a sub-path for this operation.
     * - @DELETE defines the type of HTTP operation that is handled.
     * - @RolesAllowed is used to enforce basic role-based access control: only
     * users with the admin role can execute this operation.
     * - @PathParam is used to inject the ratingId based on a path parameter.
     *
     * @param ratingId - The id of the rating to delete.
     * @return A Uni instance that will produce an HTTP Response.
     */
    @Path("{id}")
    @DELETE
    @RolesAllowed(Roles.ADMIN)
    public Uni<Response> delete(@PathParam("id") String ratingId) {

        return Rating.deleteById(new ObjectId(ratingId))
                .map(success -> success ? Response.noContent().build() : Response.notModified().build());
    }

    /**
     * Handler method for listing the available ratings for a specific painting.
     * - @Path is used to define a sub-path for this operation.
     * - @QueryParam is used to inject query parameters as method arguments.
     * - @DefaultValue is used to set default values in case a query parameter is omitted from the request.
     * @param pageIndex - Index of what page of the result-set that should be returned.
     * @param pageSize  - The maximum number of ratings that should be included in a  single result page.
     * @return A Uni instance that will produce a paged rating result.
     */
    @Path("/painting/{paintingId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PagedRatingResult> list(
        @QueryParam("pageIndex") @DefaultValue("0") Integer pageIndex, 
        @QueryParam("pageSize") @DefaultValue(("25")) Integer pageSize, 
        @PathParam("paintingId") String paintingId) {
        var lol  = Rating.find("paintingId", paintingId); 
        var query = lol.page(pageIndex, pageSize);
        return Uni.combine().all().unis(query.<Rating>list(), query.hasNextPage(), query.count())
                .asTuple()
                .map(results -> new PagedRatingResult(results.getItem1(), results.getItem2(), results.getItem3()));
    }
}
