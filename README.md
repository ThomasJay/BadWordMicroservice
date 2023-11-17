# Bad Word Microservice
Bad Word Rest API Microservice using Spring Boot and Java as a simple service end point that includes a list of Bad Words

In the modern landscape of chat integrations, it's essential to have a reliable method for filtering content that enters these systems.

Systems like ChatGPT consume tokens so limiting the number of tokens used can help reduce cost.

This service will save tokens and also provide a cleaner interaction for chat users.


## Build

Use the command:

```
mvn clean compile package -DskipTests
```
   

This will generate the following file in the target folder:
```
BadWordCheckerService-1.0.0.jar
```
    



## Run the Microservice

```
  java -jar BadWordCheckerService-1.0.0.jar --spring.config.location=file:///Users/home/application.properties

```
 

## Settings

In the resources/applications.properties file, there are a few values that need to be changed for your environment.

```
server.port=8123
badword.file.location=<path-and-file-name>
apikey=2banapi

```


#### Server port is the listing port when you run the application
   server.port = 8123



#### Badword file is the text file with all the bad words
   badword.file.location=/Users/tjay/Desktop/master_bad_words.txt

There is a sample master_bad_words.txt that is included in the project, this should be used external to the jar as it is NOT compiled and included.
This file has been created from vaious lists that I found online.


#### The apiKey is used when calling the services, this is a header value

   apikey=2banapi



## The system will attempt to load the bad word file on start
No need to call async loading if the bad word file does not change.


## Async loading of the words
You can load the words without bring down the service with the loadwords API call. Note this is a Post, no body content is required.
```
curl --location --request POST 'http://localhost:8123/api/v1/loadwords'
```
## Usage:

To use the service, you typically send a POST request to the endpoint /api/v1/checksentence. This request should include a JSON body containing the sentence you want to check. You'll receive a response: if it's a 200-OK, you can review the 'count' and 'matches' in the response. A 'count' of zero (0) indicates that no offensive words were found. If the 'count' is more than zero, the 'matches' array will list the detected words.

```
curl --location 'http://localhost:8123/api/v1/checksentence' \
--header 'apiKey: 2banapi' \
--header 'Content-Type: application/json' \
--data '{
    "sentence" : "bad word for me"
}'
```

Success Response:

```
{
    "status": "Success",
    "messages": "No bad words found",
    "count": 0,
    "badWords": []
}
```

Bad Response:

```
Note: response edited out bad words themself since it is public on GitHub and I did not want bad words on the README file
{
    "status": "Failed",
    "messages": "Bad words found",
    "count": 2,
    "badWords": [
        "***",
        "***"
    ]
}
```

### Note: apiKey header is required.

The apiKey value is defined in the application.properties file


## Open API / Swagger Docs
```
http://localhost:8123/swagger-ui/index.html
```

## Java Docs
Yes, there are Java Docs

You can build them as follows:

```
mvn javadoc:javadoc
```

### Please Follow Me

If you like this then checkout my YouTube and Medium links:

Udemy classes: https://www.udemy.com/user/tomjay2

Website: https://www.thomasjayconsulting.com/

Twitter: https://twitter.com/startupdev

YouTube: https://www.youtube.com/@fastandsimpledevelopment?sub_confirmation=1


