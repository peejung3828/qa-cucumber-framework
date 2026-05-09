Feature: Step3 - Shipping Details

  Background:
    Given user is logged in with "admin@admin.com" and "admin123"
    And user has added items to cart and proceeded to checkout

  # Positive Test Cases
  Scenario: Submit order with all required fields
    When user fills shipping details
      | phone   | 0812345678  |
      | street  | 123 Main St |
      | city    | Bangkok     |
      | country | Thailand    |
    Then user should be able to submit order successfully

  # Negative Test Cases
  Scenario: Cannot submit order with missing phone
    When user fills shipping details
      | phone   |             |
      | street  | 123 Main St |
      | city    | Bangkok     |
      | country | Thailand    |
    Then user should not be able to submit order

  Scenario: Cannot submit order with missing city
    When user fills shipping details
      | phone   | 0812345678  |
      | street  | 123 Main St |
      | city    |             |
      | country | Thailand    |
    Then user should not be able to submit order