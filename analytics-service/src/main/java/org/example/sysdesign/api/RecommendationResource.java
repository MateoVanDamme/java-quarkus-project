package org.example.sysdesign.api;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.model.CatalogusItem;
import org.example.sysdesign.model.Rating;
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
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.sysdesign.api.RatingClient;
import org.example.sysdesign.api.CatalogusClient;

import java.util.List;




/**
 * This class implements the '/recommend' HTTP endpoints.
 * <p>
 * Endpoint implementations are annotated with JAX-RS @(Http operation) annotations such as @GET, @POST.
 * By annotating the class with @Path, these endpoints are automatically picked up and added to the HTTP server by Quarkus.
 */
@Path("/recommend")
public class RecommendationResource {

    // Inject the client that is used to communicate with the rating service
    @RestClient 
    RatingClient ratingClient;

    // Inject the client that is used to communicate with the catalogus service
    @RestClient
    CatalogusClient catalogusItemClient;

    /**
     * Create a GET method that returns a Rating object when you send the ID of that rating to the endpoint, to test if the ratingClient works
     * @param id - the ID of the rating
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<Rating> getRating(@PathParam("id") String id) {
        return ratingClient.getRating(id);
    }

    /**
     * A method to compare the similarity of two catalogusitems. The method returns a similarity score based on the style, artist and exhibitionID of the items.
     * @param item1 - the first catalogusitem to be compared
     * @param item2 - the second catalogusitem to be compared
     */
    public int calculateSimilarityScore(CatalogusItem item1, CatalogusItem item2){
        int similarityScore = 0;
        if(item1.getStyle().equals(item2.getStyle())){
            similarityScore++;
        }
        if(item1.getArtist().equals(item2.getArtist())){
            similarityScore++;
        }
        if(item1.getExhibitionID().equals(item2.getExhibitionID())){
            similarityScore++;
        }
        return similarityScore;
    }

    /**
     * A method that iterates over the list of catalogusitems and returns the most similar item to the catalogusitem that is sent to the method.
     * @param catalogusItem - the catalogusitem that is sent to the endpoint
     * @param catalogus - the list of all catalogusitems
     */
    public CatalogusItem findSimilarItem(CatalogusItem catalogusItem, List<CatalogusItem> catalogus){
        CatalogusItem similarItem = null;
        int maxSimilarityScore = 0;
        for(CatalogusItem item : catalogus){
            int similarityScore = calculateSimilarityScore(catalogusItem, item);

            if(similarityScore == 3){
                return item;
            }
            if (similarityScore > maxSimilarityScore){
                similarItem = item;
                maxSimilarityScore = similarityScore;
            }
        }
        return similarItem;
    }

    /**
     * A method that iterates over the list of catalogusitems and returns the least similar item to the catalogusitem that is sent to the method.
     * @param catalogusItem - the catalogusitem that is sent to the endpoint
     * @param catalogus - the list of all catalogusitems
     */
    public CatalogusItem findDissimilarItem(CatalogusItem catalogusItem, List<CatalogusItem> catalogus){
        CatalogusItem dissimilarItem = null;
        int maxSimilarityScore = 3;
        for(CatalogusItem item : catalogus){
            int similarityScore = calculateSimilarityScore(catalogusItem, item);

            if(similarityScore == 0){
                return item;
            }
            if (similarityScore < maxSimilarityScore){
                dissimilarItem = item;
                maxSimilarityScore = similarityScore;
            }
        }
        return dissimilarItem;
    }

    /**
     * A method that returns a recommendation for a catalogusitem based on a rating sent to the endpoint.
     * @param ratingScore - the score of the rating
     * @param paintingId - the ID of the catalogusitem that was rated
     */
    @Path("/simple/{paintingId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<CatalogusItem> getRecommendationSimple(@QueryParam("ratingScore") Integer ratingScore, @PathParam("paintingId") String paintingId){
        return catalogusItemClient.getAllCatalogusItemsAsAList().onItem().transformToUni(catalogus -> {
           
            CatalogusItem reviewedCatalogusItem = null;
            for(CatalogusItem item : catalogus){
                if(item.id.toString().equals(paintingId)){
                    reviewedCatalogusItem = item;
                    break;
                }
            }
            if(paintingId == null){
                return null;
            }
            if(ratingScore > 3){
                return Uni.createFrom().item(findSimilarItem(reviewedCatalogusItem, catalogus));
            }
            else{
                return Uni.createFrom().item(findDissimilarItem(reviewedCatalogusItem, catalogus));
            }
        });
        
    }



    // Returns a recommendation for a user based on all his previous ratings\
    
    /**
     * A method that returns a recommendation for a catalogusitem based on all the ratings of a user.
     * @param userid - the ID of the user
     */
    @Path("/advanced{userid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Uni<CatalogusItem> getRecommendationAdvanced(@PathParam("userid") String userid){
        return null; 
    }

}
