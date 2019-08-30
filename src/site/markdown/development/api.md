# API

## Authentication

The ARSnova API uses _JSON Web Tokens_ to authenticate users statelessly.
To receive a JWT initially, a client has to login using an endpoint specific for the authentication provider.
As response a JSON object containing the JWT is sent:

```json
{
	"userID": "fe5d046b30c64e3e931094865d3415c4",
	"loginId": "arsnova-user@example.com",
	"authProvider": "ARSNOVA",
	"token": "eyJ0eXAiOi...eLvinH9rZ0"
}
```

To get authorization to API endpoints the JWT is sent as part of HTTP Basic Authentication with `Bearer` scheme as described in
[RFC 6750](https://tools.ietf.org/html/rfc6750).
The `Authorization` header is set for all further requests:

```
Authorization: Bearer eyJ0eXAiOi...eLvinH9rZ0
```

## REST Endpoints

## Query Endpoint

## Management Endpoints
