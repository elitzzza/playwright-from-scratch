Feature: Product catalogue

  As an user i should be able to search inside the catalogue

  Rule: Customers should be able to search in the catalogue by product name
    Example: Sally searches for an Adjustable Wrench
      Given Sally is on the home page
      When she searches for "Adjustable Wrench"
      Then the "Adjustable Wrench" should be displayed

    Example: Sally searches for more general term
      Given Sally is on the home page
      When she searches for "saw"
      Then following items should be displayed
        | Wood Saw     |
        | Circular Saw |

    Example: Sally searches for more general term using a data table
      Given Sally is on the home page
      When she searches for "saw"
      Then following list of items should be displayed
        | Product      | Price  |
        | Wood Saw     | $12.18 |
        | Circular Saw | $80.19 |

    Example: Sally searches for a product that does not exist
      Given Sally is on the home page
      When she searches for "NotExisting-Product"
      Then no products should be found
      And the message "There are no products found." should be displayed

  Rule: Customers should be able to narrow down search by category
    Example: Sally only wants to see Hand Saws
      Given Sally is on the home page
      When she searches for "saw"
      And she filters by "Hand saw"
      Then following list of items should be displayed
        | Product  | Price  |
        | Wood Saw | $12.18 |

  Rule: Customers should be able to sort the items by various criteria
    Scenario Outline: : Sally sorts by different criteria
      Given Sally is on the home page
      When she sorts by "<Sorting Criterion>"
      Then the first displayed product should be "<First Product>"
      Examples:
        | Sorting Criterion  | First Product       |
        | Name (A - Z)       | Adjustable Wrench   |
        | Name (Z - A)       | Wood Saw            |
        | Price (High - Low) | Drawer Tool Cabinet |
        | Price (Low - High) | Washers             |