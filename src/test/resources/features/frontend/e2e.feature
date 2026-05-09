Feature: E2E - Full Flow Step 1 to 4

  Scenario: Complete shopping flow from login to order confirmation
    # Step 1 - Login
    Given user is on login page
    When user enters email "admin@admin.com" and password "admin123"
    Then user should be redirected to shop page

    # Step 2 - Shopping Cart
    When user adds "Dior J'adore" with quantity 2
    And user adds "Gucci Bloom Eau de" with quantity 3
    Then total cost should be correct
    And user clicks proceed to checkout button

    # Step 3 - Shipping Details
    When user fills shipping details
      | phone   | 0812345678  |
      | street  | 123 Main St |
      | city    | Bangkok     |
      | country | Thailand    |
    Then user should be able to submit order successfully

    # Step 4 - Order Confirmation
    Then address should be displayed in format "Street, City - Country"
    And address should be "123 Main St, Bangkok - Thailand."