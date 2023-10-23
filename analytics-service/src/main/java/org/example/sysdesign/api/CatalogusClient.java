package org.example.sysdesign.api;

import org.example.sysdesign.model.CatalogusItem;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.sysdesign.model.Rating;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;


@Path("/catalogus/paintings/all")
@RegisterRestClient
public interface CatalogusClient {
    
    /**
     * Use the Catalogus endpoint to get all the catalogus items as a list
     */
    @GET 
    Uni<List<CatalogusItem>> getAllCatalogusItemsAsAList();

}
