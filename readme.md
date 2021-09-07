This is a Java implementation of the Cloud Audition Project
It is built using Spring Boot taking advantage of Spring Boot's built-in web server and dependency injection framework.
The application provides HTTP endpoints (described below) to manage messages. The server listens on `http://localhost:8080`  All endpoints accept and return JSON.
Messages are stored in memory.  The application implements optimistic locking and will fail a request when attempting to update a resource that has been modified by another request.

_Other notes:_

* Authentication/authorization currently not supported
* Pagination query parameters are accepted as part of `GET /api/messages` but the feature has not yet been implemented
* Application is currently not instrumented for application monitoring
* Tests are written in JUnit and include integration/component tests for `MessageController` and unit tests for everything else

# API Endpoints

## /api/messages
#### Supported Methods
##### GET
Returns a list of messages

Sample response:
```
{
    "messages": [
        {
            "id": 1,
            "content": "aabcc",
            "version": 1,
            "palindrome": false
        },
        {
            "id": 2,
            "content": "aabaa",
            "version": 1,
            "palindrome": true
        }
    ],
    "total": 2,
    "page": 1,
    "links": {
        "next": "http://linktonextpage"
    }
}
```

_Optional Query Strings_
| Name        | Description           | Type  |
| ------------- |-------------| -----|
| page      | page number to return | Integer |
| per_page     | number of items per page      |   Integer |
| sort_by | message field to sort by      |    Integer |
##### POST
Creates a new message

_Request Body Arguments_
| Name        | Required | Description           | Type  |
| -------------| --- |-------------| -----|
| content      | yes | message content | String |

Example Request:
```
{
    "content": "aabaa" 
}
```

Example Response:
```
{
    "id": 2,
    "content": "aabaa",
    "version": 1,
    "palindrome": true
}
```

## /api/messages/{id}
#### Supported Methods
##### GET

Get a message 

Example Response:
```
{
    "id": 2,
    "content": "aabaa",
    "version": 1,
    "palindrome": true
}
```
##### PUT

Update a message

_Request Body Arguments_
| Name        | Required | Description           | Type  |
| -------------| --- |-------------| -----|
| content      | yes | message content | String |
| version     | yes | version of entity when retrieved.  error is returned if version does not match current stored version      |   Integer |
##### DELETE

Deletes a message

# Development

### Prerequisites
[Java 11](https://www.azul.com/downloads/?package=jdk#download-openjdk)

### To build the application
From repository directory run:
`./gradlew build`

### Running the application
First build the project, then staying in the repository directory run:
`java -jar build/libs/qlik-assignment-0.0.1-SNAPSHOT.jar`

### Running automated tests
From repository root directory run:
`./gradlew test`

The test report can be found at `<repo>/build/reports/tests/test/index.html`

### Manual Testing

A Postman collection has been included for manual testing and can be found at `<repo>/postman/Messages.postman_collection.json`
