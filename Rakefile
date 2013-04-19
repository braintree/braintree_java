ENV['TEST_JDK_VERSION'] ||= "1.5"

task :default => :test

task :clean do
  sh "mvn clean"
  sh "rm -rf doc"
end

task :compile do
  sh "mvn compile"
end

task :test do
  sh "mvn test"
end

desc "build a jar"
task :jar => :compile do
  sh "mvn package"
end

# e.g. rake single_test testclass=com.braintreegateway.TransactionTest
desc "run a single unit test class"
task :single_test do
  sh "mvn test -Dtest=#{ENV['testclass']}"
end

desc "generate javadoc"
task :javadoc do
  excludes = [
    "com.braintreegateway.util",
    "com.braintreegateway.org"
  ]
  sh "javadoc -sourcepath src -subpackages com.braintreegateway -exclude #{excludes.join(":")} -d doc -overview overview.html"
end

def src_files
  Dir.glob("src/**/*.java").join(" ")
end

def test_files
  Dir.glob("test/**/*.java").join(" ")
end

def jar_name
  "braintree-java-#{version}.jar"
end

def version
  contents = File.read('src/com/braintreegateway/BraintreeGateway.java')
  version = contents.slice(/VERSION = "(.*)"/, 1)
  raise "Cannot read version" if version.empty?
  version
end
