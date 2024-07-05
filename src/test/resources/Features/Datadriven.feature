Feature: Data Driven Exception
  Scenario: Execute all apis in the excel
    Given I have "Dev" environment
    When I have data in "RestAssuredDataDriven" excel file and data is in "APIs_DATA"