# Authentication and authorization via the Quarkus Keycloak integration

## What is Keycloak

[Keycloak](https://www.keycloak.org) is an open source identity and access management solution. The project has a large community and is used by companies worldwide, including at Red Hat, Accenture, Capgemini, CloudNative Inc., etc.

The benefit of using Keycloak is that it can do the heavy lifting in terms of security concerns, this includes being compatible with the latest security specifications (such as OpenID Connect, UMA 2.0, OAuth 2.0) and maintaining the software with frequent patches to mitigate new attack vectors and vulnerabilities.

Keycloak is a standalone service that runs along your application. Requests sent to your application are redirected to Keycloak for access control, and then either blocked, or redirected back again to the application (if access is granted).

By abstracting a crosscutting concern such as security away from the application, into a separate service, the application implementation can focus on the actual business logic. This makes it easier to read and maintain the application codebase.

## Configuring Quarkus to use Keycloak

When the following dependencies are added to your Quarkus project, Keycloak will automatically be started and configured to be used from the Quarkus application, when running in development (or test) mode via the Dev Service feature:

```kotlin
implementation("io.quarkus:quarkus-oidc")
implementation("io.quarkus:quarkus-keycloak-authorization")
```

To configure Keycloak integration with the Quarkus application, the following properties need to be specified:

```properties
# A URL pointing to the realm to use on a specific Keycloak server
quarkus.oidc.auth-server-url=http://keycloak-example:8543/realms/quarkus
# The name of the client that was registered on Keycloak to represent this application
quarkus.oidc.client-id=backend-service
# The secret associated with this client
quarkus.oidc.credentials.secret=secret
```

Note that you can prefix configuration properties with `%prod.` so that these entries are only activated when running in production mode (e.g. `%prod.quarkus.oidc.auth-server-url=...`). This way you can specify a production configuration, while still having the benefit of being able to use the automatic Dev Service feature during local development (in development mode).

## Authenticating via Keycloak

The [walkthrough](./using-the-demonstrator.md#authenticating) for the demo application guides you through the authentication process.
Note that we are using built-in accounts that are intended for demonstration purposes. Keycloak supports creating accounts on the server (which can be managed by a user with Keycloak administrator permissions), but also allows integrating OpenID providers. E.g. it is straightforward to connect Keycloak with the Google OpenID servers[^1], making it possible that anyone with a Google account can authenticate with your application. For more information on this particular feature, we refer to the Keycloak documentation at https://www.keycloak.org/docs/latest/server_admin/index.html#_google

[^1]: Or other Social Identity Providers. Out-of-the-box, Keycloak comes with support for Bitbucket, Facebook, Github, Gitlab, Google, LinkedIn, Microsoft, OpenShift, PayPal, Stack overflow, Twitter and Instagram.

## Authorisation of Web endpoints

There are two ways in which Keycloak can be used to implement authorisation of Web endpoints. The simplest way, and the approach that is used in the demo application, is by letting the framework (Quarkus) evaluate the permissions based on the security identity that is provided via Keycloak. After successful authentication, Keycloak will mint a signed JWT access token that represents the security context of the authenticated user. This access token is then sent along with each request to the application, where it is intercepted by the Quarkus HTTP handling framework. By adding annotations to a Web handler method, you can customize how these permissions should be evaluated.

The following example demonstrates the basic auth annotation that can be applied:

* `@Authenticated` indicates that only authenticated users may access the endpoint.
* `@RolesAllowed` indicates that only authenticated users that have at least one of the specified roles can access the endpoint.
* `@PermitAll` indicates that the given endpoint is accessible by any caller, authenticated or not.
* `@DenyAll` disallows any access regardless of whether the call is authenticated. This could be useful in a situation where you want to prevent a public method in a HTTP Resource class from being available over HTTP (e.g. in some specific inheritance cases).

The other approach to implementing authorisation via Keycloak, is by authoring a security policy within Keycloak (via the management interface). The details of this approach are out-of-scope for this demo application, but instructions are available from the Keycloak documentation: https://www.keycloak.org/docs/latest/authorization_services/#_overview

When using this approach, the above annotations are ignored. Instead, the Quarkus Keycloak extension will be responsible to map the URIs of the protected resources you have in Keycloak and evaluate the permissions accordingly, granting or denying access depending on the permissions that will be granted by Keycloak.

To start using this approach, you have to configure the Quarkus Keycloak extension to enable the policy-enforcer. This is done via the following property:

```properties
quarkus.keycloak.policy-enforcer.enable=true
```

## Reference

For a complete overview of the Quarkus Keycloak integration and security features, check the following guides:

* https://quarkus.io/guides/security-keycloak-authorization
* https://quarkus.io/guides/security
