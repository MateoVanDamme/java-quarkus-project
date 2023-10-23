package org.example.sysdesign.api;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.sysdesign.model.Rating;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.PathParam;

@Path("/ratings")
@RegisterRestClient
public interface RatingClient {
    
    /**
     * Use the Rating endpoint to get a single rating
     */
    @Path("{id}")
    @GET
    Uni<Rating> getRating(@PathParam("id") String id);
}
