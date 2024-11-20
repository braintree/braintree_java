mvn = "MAVEN_OPTS='-Dhttps.protocols=TLSv1.2' mvn"

task :default => :jar

task :clean do
  sh "#{mvn} clean"
  sh "rm *jar || true"
  sh "rm -rf doc"
end

task :compile do
  sh "#{mvn} compile"
end

namespace :test do

  # Usage:
  #   rake test:unit
  #   rake test:unit[ConfigurationTest]
  #   rake test:unit[ConfigurationTest,testStringEnvironmentConstructor]
  desc "run unit tests"
  task :unit, [:file_name, :test_name] do |task, args|
    if args.file_name.nil?
      sh "#{mvn} test"
    elsif args.test_name.nil?
      sh "#{mvn} test -Dtest=com.braintreegateway.unittest.#{args.file_name}"
    else
      sh "#{mvn} test -Dtest=com.braintreegateway.unittest.#{args.file_name}##{args.test_name}"
    end
  end

  # Usage:
  #   rake test:integration
  #   rake test:integration[PlanIT]
  #   rake test:integration[PlanIT,returnsAllPlans]
  desc "run integration tests"
  task :integration, [:file_name, :test_name] do |task, args|
    if args.file_name.nil?
      sh "#{mvn} verify -DskipUTs"
    elsif args.test_name.nil?
      sh "#{mvn} verify -DskipUTs -Dit.test=com.braintreegateway.integrationtest.#{args.file_name}"
    else
      sh "#{mvn} verify -DskipUTs -Dit.test=com.braintreegateway.integrationtest.#{args.file_name}##{args.test_name}"
    end
  end

  desc "run unit and integration tests"
  task :all do
    #sh "#{mvn} verify"
    sh "#{mvn} verify -DskipUTs -Dit.test=com.braintreegateway.integrationtest.CustomerSessionIT*"
  end
end

task :test => "test:all"

desc "compile, test, build a jar"
task :jar do
  sh "#{mvn} verify package"
  sh "cp target/braintree-java-*.jar ./"
end
