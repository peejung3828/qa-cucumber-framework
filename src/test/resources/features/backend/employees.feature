Feature: API - Employees

  # POST /api/v1/employees
  # Positive Test Cases
  Scenario: Create employee success should return 201
    When user sends POST to "/api/v1/employees" with valid body
      | firstName | John             |
      | lastName  | Doe              |
      | email     | john@example.com |
    Then response status code should be 201

  # Negative Test Cases
  Scenario: Create employee with invalid email should return 400
    When user sends POST to "/api/v1/employees" with invalid email
      | firstName | John        |
      | lastName  | Doe         |
      | email     | invalid-email |
    Then response status code should be 400
    And response should contain defaultMessage "must be a well-formed email address"

  # GET /api/v1/employees/{id}
  # Positive Test Cases
  Scenario: Get employee with existing id should return 200
    Given an employee exists in the system
    When user sends GET to "/api/v1/employees/{id}"
    Then response status code should be 200

  # Negative Test Cases
  Scenario: Get employee with non existing id should return 404
    When user sends GET to "/api/v1/employees/99999"
    Then response status code should be 404
    And response body message should be "Employee not found with ID 99999"