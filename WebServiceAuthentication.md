

# Overview #

The ODR [provides a RESTful web service](https://www.decisionrepository.com/webservice.html) for machine-to-machine communication. As the architecture documentation also states, it makes use of HTTP basic authentication. The authentication mechanism was implemented using RESTeasy request interceptors.

# Implementation #
Implemented through [nl.rug.search.odr.ws.security.AuthenticationInterceptor](http://code.google.com/p/opendecisionrepository/source/browse/trunk/sources/web-service/src/main/java/nl/rug/search/odr/ws/security/AuthenticationInterceptor.java).

# Restricting access #

## Allow all incoming requests ##
```
package nl.rug.search.odr.ws.resource;

// ...

@Path("/")
public class DecisionStateResource {
    
    // ...
    
    @GET
    @Path("/decisionstates")
    @SkipAuthentication
    @SkipGroupVerification
    public DecisionStateDTOList getCommonDecisionStates() {
        // ...
    }
}
```

## Only allow requests of authorized users ##
```
package nl.rug.search.odr.ws.resource;

// ...

@Path("/user/")
public class UserResource {
    @GET
    @Path("projects")
    @SkipGroupVerification
    public ProjectOverviewDTOList getProjectOverview(@Context HttpServletRequest request) {
        // ...
    }
}
```

## Only allow requests of group members ##
```
package nl.rug.search.odr.ws.resource;

// ...

@Path("/projects/{" + PathParameter.PROJECT_ID + "}")
public class ProjectResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ProjectDTO getProjectDetails(@Context HttpServletRequest request) {
        // ...
    }

    // ...
}
```