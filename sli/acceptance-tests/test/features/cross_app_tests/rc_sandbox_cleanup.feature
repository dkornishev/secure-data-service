@RALLY_US4835
@rc
@sandbox
Feature:  RC Integration CleanUp Tests

Background:
  Given I have an open web browser
  And I am running in Sandbox mode

Scenario: App developer deletes installed app
  When I navigate to the Portal home page
  And I was redirected to the "Simple" IDP Login page
  When I submit the credentials "<DEVELOPER_SB_EMAIL>" "<DEVELOPER_SB_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  And under System Tools, I click on "Apps"
  Then I am redirected to the Application Registration Tool page
  Then I have clicked on the button 'Delete' for the application named "NotTheAppYoureLookingFor"
  And I got warning message saying 'You are trying to remove this application from inBloom. By doing so, you will prevent any active user to access it. Do you want to continue?'
  When I click 'Yes'
  Then the application named "NotTheAppYoureLookingFor" is removed from the SLI
  Then I have clicked on the button 'Delete' for the application named "Schlemiel"
  And I got warning message saying 'You are trying to remove this application from inBloom. By doing so, you will prevent any active user to access it. Do you want to continue?'
  When I click 'Yes'
  Then the application named "Schlemiel" is removed from the SLI

Scenario: slcoperator deletes SB Dev1 and Dev2
    When I navigate to the Portal home page
    And I was redirected to the "Simple" IDP Login page
    When I submit the credentials "<DEVELOPER_SB_EMAIL>" "<DEVELOPER_SB_EMAIL_PASS>" for the "Simple" login page
    Then I should be on Portal home page
    And under System Tools, I click on "Manage Developer Accounts"
   Then I delete the user "RCTestDev PartTwo"
