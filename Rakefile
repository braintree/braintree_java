require "rubygems"
load File.dirname(__FILE__) + "/cruise.rake"

task :default => :test

task :init do
  sh "mkdir -p classes"
  sh "mkdir -p test-classes"
end

task :clean do
  sh "rm -f *.jar"
  sh "rm -rf classes/*"
  sh "rm -rf test-classes/*"
end

task :compile => :init do
  sh "javac -target 1.5 -d classes -cp #{lib_classpath} -Xlint:deprecation #{src_files}"
  cp "VERSION", "classes"
end

task :jar => :compile do
  sh "jar cvf #{jar_name} -C classes . > /dev/null"
end

task :compile_tests => [:init, :clean, :jar] do
  sh "javac -target 1.5 -Xlint:deprecation -d test-classes -cp #{jar_name}:#{lib_classpath} #{test_files}"
  cp_r "test/script", "test-classes"
  cp_r "test/ssl", "test-classes"
end

task :test => :compile_tests do
  sh "java -cp #{jar_name}:test-classes:#{lib_classpath} org.junit.runner.JUnitCore com.braintreegateway.AllTests"
end

def lib_classpath
  Dir.glob("lib/*.jar").join(":")
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
  File.read('VERSION').strip
end
