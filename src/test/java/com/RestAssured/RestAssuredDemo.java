package com.RestAssured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quinbay.model.Example;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class RestAssuredDemo {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://reqres.in/";
       // String body = "{\n" + "    \"name\": \"morpheus\",\n" + "    \"job\": \"leader\"\n" + "}";
       /** given()
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .body(body)
                .post("/api/users")
                .then()
                .statusCode(201)
                .assertThat()
                .body("name", equalTo("morpheus"))
                .assertThat()
                .body("job", equalTo("leader"))
                .log()
                .all();

        **/

        Response response = given()
                .queryParam("page", 2)
                .header("content-type", "application/json")
                .log()
                .all()
                .when()
                .get("/api/users");
        System.out.println("------------Response----------");
        response.prettyPrint();

        assertThat("Response code is not 200", response.getStatusCode(), equalTo(200));
        String responsestr = response.asString();

       /** ObjectMapper objectMapper = new ObjectMapper(); **/

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            Example pojo = objectMapper.readValue(responsestr, Example.class);
            assertThat("Page is not matching", pojo.getPage(),equalTo(2));
            assertThat("Page is not matching", pojo.getData().size(),equalTo(6));
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
        }
        assertThat("Duration taken is more than Expected", response.timeIn(TimeUnit.SECONDS),lessThan(10L));
    }
}
