package service.resources;

import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("test")
public class JavaEE8Resource {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    EntityManager em;
    
    @GET
    public Response ping(){
        
        User u = em.find(User.class, 1);
        
        return Response
                .ok(u.getUsername())
                .build();
    }
}
