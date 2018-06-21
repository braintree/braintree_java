ENV['TEST_JDK_VERSION'] ||= "1.5"
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
  desc "run unit tests"
  task :unit do
    sh "#{mvn} verify -DskipITs"
  end

  desc "run unit and integration tests"
  task :all do
    sh "#{mvn} verify"
  end
end

task :test => "test:all"

desc "compile, test, build a jar"
task :jar do
  sh "#{mvn} verify package"
  sh "cp target/braintree-java-*.jar ./"
end

# e.g. rake single_test testclass=com.braintreegateway.integrationtest.TransactionIT
# OR rake single_test testclass=com.braintreegateway.integrationtest.TransactionIT#sale_is_success
desc "run a single unit or integration test class"
task :single_test do
  test_class = ENV['testclass']
  if test_class.include? ".integrationtest."
    sh "#{mvn} verify -Dit.test=#{test_class}"
  else
    sh "#{mvn} test -Dtest=#{test_class}"
  end
end

desc "generate javadoc"
task :javadoc do
  excludes = [
    "com.braintreegateway.util",
    "com.braintreegateway.org"
  ]
  sh "javadoc -sourcepath src -subpackages com.braintreegateway -exclude #{excludes.join(":")} -d doc -overview overview.html"
end

def jar_name
  "braintree-java-#{version}.jar"
end
