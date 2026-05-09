Feature: Step4 - Order Confirmation

  Scenario: Validate address format is displayed correctly
    Given user is logged in with "admin@admin.com" and "admin123"
    And user has added items to cart and proceeded to checkout
    When user fills shipping details
      | phone   | 0812345678  |
      | street  | 123 Main St |
      | city    | Bangkok     |
      | country | Thailand    |
    And user submits the order
    Then address should be displayed in format "Street, City - Country"
    And address should be "123 Main St, Bangkok - Thailand."