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

require 'simplecov'
require 'factory_girl'
require 'yaml'
require_relative '../lib/Shared/EntityClasses/baseEntity'

SimpleCov.start do
  add_filter '/spec/'
  add_filter '/config/'
  add_filter '/vendor/'

  add_group 'WorldDefinition', '/lib/WorldDefinition/'
  add_group 'EntityCreation', '/lib/EntityCreation/'
  add_group 'OutputGeneration', '/lib/OutputGeneration/'
  add_group 'Shared', '/lib/Shared/'
  add_group 'Driver' do |src_file|
    File.basename(src_file.filename) == 'odin.rb' || File.basename(src_file.filename) == "driver.rb"
  end
end

# This file was generated by the `rspec --init` command. Conventionally, all
# specs live under a `spec` directory, which RSpec adds to the `$LOAD_PATH`.
# Require this file using `require "spec_helper"` to ensure that it is only
# loaded once.
#
# See http://rubydoc.info/gems/rspec-core/RSpec/Core/Configuration
RSpec.configure do |config|

  config.before(:each) do
    SimpleCov.command_name "RSpec #{rand(100000)}"
  end

  config.treat_symbols_as_metadata_keys_with_true_values = true
  config.run_all_when_everything_filtered = true
  config.filter_run :focus

  config.fail_fast = true
  
  # Run specs in random order to surface order dependencies. If you find an
  # order dependency and want to debug it, you can fix the order by providing
  # the seed, which is printed after each run.
  #     --seed 1234
  config.order = 'random'
end



FactoryGirl.find_definitions

def get_spec_scenario
  { 'BATCH_SIZE' => 1 }
end

# monkey patch StringIO so it can be used more like a file for tests
class StringIO
  def path
    "testfile"
  end
end

BaseEntity.initializeDemographics(
    File.join( "#{File.dirname(__FILE__)}/", "../scenarios/defaults/demographics.yml"),
    File.join( "#{File.dirname(__FILE__)}/", "../scenarios/defaults/choices.yml"))
BaseEntity.set_scenario(YAML.load_file("#{File.dirname(__FILE__)}/../scenarios/defaults/base_scenario"))
