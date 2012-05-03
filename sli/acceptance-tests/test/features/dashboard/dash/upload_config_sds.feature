Feature: Upload View Configurations

As a admin, I'm able to upload view configuration

Background:
Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go

@integration
Scenario: Invalid User Login
When I login as "linda.kim" "linda.kim1234"
When I enter the Configuration Area
Then I am informed that "SLI Exception Message"

@integration
Scenario: Upload valid config file
When I login as "jstevenson" "jstevenson1234"
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And I paste Valid json config into the text box
And click Save
Then I should be shown a success message
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
When I login as "linda.kim" "linda.kim1234"
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
Then I see a list of 28 students
Then I should have a dropdown selector named "viewSelect"
And I should have a selectable view named "Uploaded Custom View"
Then I should see a table heading "My Student"
Then I should see a table heading "Absence Count"
Then I should see a table heading "Tardy Count"

@integration
Scenario: Upload invalid config file
When I login as "jstevenson" "jstevenson1234"
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And I paste Invalid json config into the text box
And click Save
Then I should be shown a failure message
And I reset custom config
And click Save
Then I should be shown a success message
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
When I login as "linda.kim" "linda.kim1234"
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
Then I see a list of 28 students
Then I should have a dropdown selector named "viewSelect"
And I should have a selectable view named "Middle School ELA View"
