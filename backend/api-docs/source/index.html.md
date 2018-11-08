---
title: teamUp API Documentation

language_tabs: # must be one of https://git.io/vQNgJ
  - http

toc_footers:
  - <a href='https://github.com/lord/slate'>Documentation Powered by Slate</a>

# includes:
#   - errors

search: true
---

# Introduction

Welcome to the teamUp API. You can use our API to access teamUp API resources.
All resources are using JSON as the format to receive and send data.

# Authentication

In order to consume the API endpoints, a user has to authenticate with the API first.

# Users
Use the `/users` resource to create, update, and show details for users.

## Create user
`POST /api/v1/users`

Creates a user account. In the JSON request body, include user account details. A user **must** have an id, first and last name, and optionally a mobile phone number.

Request body

> Sample request

```http
POST /api/v1/users HTTP/1.1
Host: http://localhost:3000/api/v1/
Content-Type: application/json
cache-control: no-cache

{
  "id": "100000273909112",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+86 764 564 9052"
}
```

> Sample response

```http
HTTP/1.1 201 Created
X-Powered-By: Express
Content-Type: application/json; charset=utf-8
Content-Length: 99
ETag: W/"63-U+3pylDx44qevKKpDSJ/98oGNAQ"
Date: Wed, 07 Nov 2018 23:22:25 GMT
Connection: keep-alive

{
  "result": {
      "status": "success",
      "error": null
  },
  "payload": {
      "id": "100000273909112",
      "firstName": "John",
      "lastName": "Doe",
      "phoneNumber": "+86 764 564 9052"
    }
}
```

## Get user
`GET /api/v1/users/{id}`

Retrieves user account details.

## Update user
`PATCH /api/v1/users/{id}`

Updates user account details.

## Delete user
`DELETE /api/v1/users/{id}`

Deletes a user account.

# Games

Use the `/games` resource to create, update and show details for games.

## Create game

`POST /api/v1/games/`

Request body

<aside class="notice">
You must replace `{YOUR-AUTH-TOKEN}` with your personal authentication key.
</aside>

> Sample request

```http
POST /api/v1/games/ HTTP/1.1
Content-Type: application/json
auth-token: {YOUR-AUTH-TOKEN}
cache-control: no-cache
User-Agent: PostmanRuntime/7.3.0
Accept: */*
Host: localhost:3000
Accept-Encoding: gzip, deflate
Content-Length: 364
Connection: close

{
	"name": "John's Game",
	"type": 1,
	"size": 5,
	"opponents": true,
	"eventDate": "2018-11-25T17:00:00.000Z",
	"description": "We can win as we always do.",
	"location": {
		"city": "Thessaloniki",
		"address": "Makedonikis Aminis 1",
		"country": "Greece",
		"countryCode": "GR",
		"postalCode": "54631",
		"latitude": 40.6378543,
		"longitude": 22.9447702
	}
}
```

> Sample response

```http

```