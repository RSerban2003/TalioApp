# Rubric: Technology

The students are not supposed to submit a specific assignment, instead, you are supposed to look into their code base.

### Dependency Injection

Application uses dependency injection to connect dependent components. No use of static fields in classes.

- *Sufficient:* There is one example in the client code and one in the server code that uses dependency injection to connect dependent components. Static fields and methods are only sparely used to access other components.


### Spring Boot

Application makes good use of the presented Spring built-in concepts to configure the server and maintain the lifecycle of the various server components.

- *Good:* The application contains example of @Controller, @RestController, and a JPA repository.
Feedback: * I think that you could consider adding the @Service classes, as it will make the code more clear and easier to test.

### JavaFX

Application uses JavaFX for the client and makes good use of available features (use of buttons/images/lists/formatting/…). The connected JavaFX controllers are used with dependency injection.

- *Sufficient:* Application uses JavaFX for the client.

### Communication

Application uses communication via REST requests and Websockets. The code is leveraging the canonical Spring techniques for endpoints and websocket that have been introduced in the lectures. The client uses libraries to simplify access.

- *Excellent:* The server defines all REST and webservice endpoints through Spring and uses a client library like Jersey (REST) or Stomp (Webservice) to simplify the server requests.

### Data Transfer

Application defines meaningful data structures and uses Jackson to perform the de-/serialization of submitted data.

- *Excellent:* Jackson is used implicitly by Spring or the client library. No explicit Jackson calls are required in the application.

