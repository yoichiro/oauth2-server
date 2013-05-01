oauth2-server
===================

This project is an implementation for OAuth 2.0 Specification.
Especially, the protocol of the server area is covered by this project.

How to use
----------

This project is supporting some common processes to issue tokens.
To use this framework, you have to provide an implementation of a
DataHandler interface. The implementation class connects this framework
to your databases or such storages.

Also, you have to implement a DataHandlerFactory interface to
create your DataHandler instance.

Classes you have to implement are only above.

A class to handle a request to issue an access token is Token class.
But, the Token class needs some helper classes. Therefore, you have to
provide their instances to the Token instance. If you're using Spring Framework
DI Container, you can inject them to the Token instance.
Refer the applicationContext-token-schenario.xml file.

The way to use the Token class is simple. You can use it as the following snippet:

```java
HttpServletRequest request = ...; // Provided by Servlet Container
HttpServletRequestAdapter adapter = new HttpServletRequestAdapter(request);
Token token = ...; // Injected
Token.Response response = token.handleRequest(adapter);
int code = response.getCode(); // 200, 400, 401, ...
String body = response.getBody(); // {"token_type":"Bearer","access_token":"...", ...}
```

An code for an integration test has the request and response contents of each grant type.
Refer the test code TokenScenarioTest.

To check the request to access to each API endpoints, you can use
ProtectedResource class. The following code snippet represents how to use its
class:

```java
HttpServletRequest request = ...; // Provided by Servlet Container
HttpServletRequestAdapter adapter = new HttpServletRequestAdapter(request);
ProtectedResource protectedResource = ...; // Injected
try {
    ProtectedResource.Response response = protectedResource.handleRequest(adapter);
    String remoteUser = response.getRemoteUser(); // User ID
    String clientId = response.getClientId(); // Client ID
    String scope = response.getScope(); // Scope string delimited by whitespace
    // do something
} catch(OAuthError e) {
    int httpStatusCode = e.getCode();
    String errorType = e.getType();
    String description = e.getDescription();
    // do something
}
```

If you build your application with Servlet API, then you can use the code above
in your Filter class.