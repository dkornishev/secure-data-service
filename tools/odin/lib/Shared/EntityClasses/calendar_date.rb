=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end

require_relative "enum/CalendarEventType.rb"

# creates calendar date
class CalendarDate < BaseEntity

  attr_accessor :date, :ed_org_id;

  def initialize(date, event, ed_org_id)
      @date      = date.to_s
    @event     = event
    @ed_org_id = ed_org_id
  end

  def event
      CalendarEventType.to_string(@event)
  end

end
