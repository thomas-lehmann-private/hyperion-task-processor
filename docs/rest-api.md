# REST api

## Introduction

Using the **serve** command (CLI) you can run the Hyperion command processor
as a server application (standalone). The next sections explain what requests
are supported

## Live documentation and testing of REST api

There are two requests available:

 - **/swagger-ui** provides the Swagger ui where you can see documentation and
   where you can try out the requests (see also: https://swagger.io/resources/open-api/).
 - **/redoc** provides open api documentation (see also: https://github.com/Redocly/redoc).

## Send a document request

### Request

| HTTP Verb | Request | Query Parameters | Body
| --------- | ------- | ---------------- | ----
|  POST      | /documents | tag (string, optional, repeatable) - filtering tasks| string
|            |            | timeout (int, optional) - timeout for each task group | string

### Response

 * **Status**: **200** (OK)
 * Unique id for the processing of the posted document.


## Check document request result

### Request

| HTTP Verb | Request | Query Parameters | Body
| --------- | ------- | ---------------- | ----
|  GET      | /documents/\<id\> | none | none

### Response

 - **Status**
   - **200** (OK) when there is a document result for given id.
   - **404** (NOT FOUND) when there is no document result for given id.
    
The body contains a simple json with the fields:
 - **success** - boolean success value true or false:
 - **started** - the timestamp (UTC) when the processing has started
 - **finished** - the timestamp (UTC) when the processing has finished

```
{
    "success": true,
    "started": "2021-08-16T03:33:01Z",
    "finished": "2021-08-16T03:33:01Z"
}
```
