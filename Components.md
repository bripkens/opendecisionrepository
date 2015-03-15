
# Overview #
The following component diagram provides an overview over the components which make up the Open Decision Repository server component, the plug-in and integration tests. A description of the components can be found in the following sections. A version of this diagram is also available in [higher resolution](http://opendecisionrepository.googlecode.com/svn/wiki/images/components.png).

Components illustrated not inside the OpenDecisionRepository component are not part of the server component. This may be the case for standalone applications, e.g., web-interface-test, integration-test, and for the plug-in.

All components listed here are realized using [Maven](http://maven.apache.org/) and therefore this diagram can be reconstructed using the components' declared dependencies (see [Maven POM](http://maven.apache.org/pom.html)).

![![](http://opendecisionrepository.googlecode.com/svn/wiki/images/components.png)](http://opendecisionrepository.googlecode.com/svn/wiki/images/components.png)

# Component descriptions #

## testutil ##
As the name suggests, the testutil component contains utility classes for testing. Besides additional assertion statements, also database cleaning, i.e., truncating all tables, and functionality for dropping all tables exist.

## entities ##
The entities components contains JPA entities and other functionality, e.g., password encryption and email validation, which can easily be unit-tested. The component was solely created because, upon build, the decision-management component executes integration tests which take way longer.

## decision-management ##
Business logic is located in the decision-management component. It contains Enterprise JavaBeans which implement the [Data Access Object Pattern](http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html), TeX export functionality and a facade for accessing the [Open Pattern Repository](http://www.patternrepository.com/) web service. Additionally, it provides an extension point for other web services.

## web-interface ##
This component contains the web interface as it visible to the user.

## web-service-client ##
The web-service-client is a utility component which can be used to access the ODR's web service. It's available in [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22nl.rug.search%22%20AND%20a%3A%22web-service-client%22) and can [easily be added to your project](https://www.decisionrepository.com/webservice.html). A demo project is available on [GitHub](https://github.com/bripkens/Example-ODR-web-service-client), [JavaDoc is also available](http://opendecisionrepository.googlecode.com/svn/web-service-client-javadoc-2.3/index.html).

Following is a class diagram of the web-service-client component ([also available in higher resolution](http://opendecisionrepository.googlecode.com/svn/wiki/images/web-service-client-class-diagram.png)).

![![](http://opendecisionrepository.googlecode.com/svn/wiki/images/web-service-client-class-diagram.png)](http://opendecisionrepository.googlecode.com/svn/wiki/images/web-service-client-class-diagram.png)

## web-service ##
Machine-to-machine communication is realized through the web-service component. It implements a RESTful web service and provides an API for the most commonly used functionality. You may simply use the aforementioned web-service-client component to access it or implement it yourself. Further information about the web-service is available in [each deployed ODR version](https://www.decisionrepository.com/webservice.html).

## startup ##
The startup component bundles the web-interface, web-service and decision-management component into one enterprise application. It does not provide any further functionality.

## `OpenDecisionRepository` ##
All the server-side components are aggregated in the `OpenDecisionRepository` component. This allows simple build processes, central dependency management and common properties.

## web-interface-test ##
This component was created to integration test the web interface. Due to changes of the ODR, those tests don't work properly any longer and need to be revamped.

## Visual Paradigm plug-in ##
The Visual Paradigm plug-in with all the functionality and graphical user interface.

## integration-tests ##
To test the web service, the integration-tests component was created. In contract to the web-interface-test component, this one is maintained and verifies the web service's functionality.