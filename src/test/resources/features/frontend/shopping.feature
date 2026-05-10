Feature: Step2 - Shopping Cart

  Background:
    Given user is logged in with "admin@admin.com" and "admin123"

  # Positive Test Cases
  Scenario: Select items and validate total cost
    When user adds "Dior J'adore" with quantity 2
    And user adds "Gucci Bloom Eau de" with quantity 3
    Then total cost should be correct
    And user clicks proceed to checkout button

