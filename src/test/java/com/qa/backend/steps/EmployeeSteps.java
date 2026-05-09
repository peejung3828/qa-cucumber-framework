package com.qa.backend.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.List;
import java.util.Map;

public class EmployeeSteps {

    private Response response;
    private int createdEmployeeId;
    private static final String BASE_URL = "http://localhost:8887";

    @Given("an employee exists in the system")
    public void anEmployeeExistsInTheSystem() {
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String body = "{\"firstName\":\"Test\"," +
                      "\"lastName\":\"User\"," +
                      "\"email\":\"" + email + "\"," +
                      "\"dob\":\"1990-01-01\"}";

        // POST สร้าง employee
        response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/api/v1/employees");

        System.out.println("POST Status: " + response.getStatusCode());

        // GET all employees แล้วเอา ID ล่าสุด
        Response getAll = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/employees");

        System.out.println("GET All Response: " + getAll.getBody().asString());

        List<Map<String, Object>> employees = getAll.jsonPath().getList("$");
        if (employees != null && !employees.isEmpty()) {
            // เอา employee ตัวสุดท้าย
            Map<String, Object> lastEmployee = employees.get(employees.size() - 1);
            createdEmployeeId = (int) lastEmployee.get("id");
            System.out.println("Found Employee ID: " + createdEmployeeId);
        }
    }

    @When("user sends POST to {string} with valid body")
    public void userSendsPOSTWithValidBody(String endpoint, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String body = "{\"firstName\":\"" + data.get("firstName") + "\"," +
                      "\"lastName\":\"" + data.get("lastName") + "\"," +
                      "\"email\":\"" + data.get("email") + "\"," +
                      "\"dob\":\"1990-01-01\"}";

        response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(body)
                .when()
                .post(endpoint);

        System.out.println("POST Response Status: " + response.getStatusCode());
        System.out.println("POST Response Body: " + response.getBody().asString());
    }

    @When("user sends POST to {string} with invalid email")
    public void userSendsPOSTWithInvalidEmail(String endpoint, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String body = "{\"firstName\":\"" + data.get("firstName") + "\"," +
                      "\"lastName\":\"" + data.get("lastName") + "\"," +
                      "\"email\":\"" + data.get("email") + "\"," +
                      "\"dob\":\"1990-01-01\"}";

        response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(body)
                .when()
                .post(endpoint);

        System.out.println("POST Invalid Response Status: " + response.getStatusCode());
        System.out.println("POST Invalid Response Body: " + response.getBody().asString());
    }

    @When("user sends GET to {string}")
    public void userSendsGETTo(String endpoint) {
        String url = endpoint.replace("{id}", String.valueOf(createdEmployeeId));
        System.out.println("GET URL: " + url);

        response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(url);

        System.out.println("GET Response Status: " + response.getStatusCode());
        System.out.println("GET Response Body: " + response.getBody().asString());
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int expectedCode) {
        Assert.assertEquals(expectedCode, response.getStatusCode());
    }

    @And("response should contain defaultMessage {string}")
    public void responseShouldContainDefaultMessage(String expectedMessage) {
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        Assert.assertTrue(
            "Expected: " + expectedMessage + " in: " + responseBody,
            responseBody.contains(expectedMessage)
        );
    }

    @And("response body message should be {string}")
    public void responseBodyMessageShouldBe(String expectedMessage) {
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        Assert.assertTrue(
            "Expected 'Employee not found' in: " + responseBody,
            responseBody.contains("Employee not found")
        );
    }
}