Feature: Step1 - Login Shop

  Background:
    Given user is on login page

  # Positive Test Cases
  Scenario: Login success with valid credentials
    When user enters email "admin@admin.com" and password "admin123"
    Then user should be redirected to shop page

  # Negative Test Cases
  Scenario: Login fail with invalid email
    When user enters email "wrong@wrong.com" and password "admin123"
    Then user should see error message

  Scenario: Login fail with invalid password
    When user enters email "admin@admin.com" and password "wrongpass"
    Then user should see error message

  Scenario: Login fail with empty email
    When user enters email "" and password "admin123"
    Then user should see error message

  Scenario: Login fail with empty password
    When user enters email "admin@admin.com" and password ""
    Then user should see error message

  Scenario: Login fail with empty all fields
    When user enters email "" and password ""
    Then user should see error message