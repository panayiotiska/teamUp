---
title: teamUp API Documentation

language_tabs: # must be one of https://git.io/vQNgJ
  - http

toc_footers:
  - <a href='https://github.com/lord/slate'>Documentation Powered by Slate</a>

includes:
  - errors

search: true
---

# Introduction

Welcome to the teamUp API! You can use our API to access teamUp API endpoints, which can get information on various users, games, and user ratings in our database.

# Authentication
> To authorize, use this code:

```http
POST /api/v1/users/auth HTTP/1.1
Host: dev.api.teamup.local

```