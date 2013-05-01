#!/usr/bin/env ruby
require 'webrick'
require 'webrick/https'
require 'openssl'

my_dir = File.dirname(__FILE__)
#project_root = File.expand_path("#{my_dir}/../..")
#test_resources_dir = "#{project_root}/src/test/resources"
res_dir = File.dirname(my_dir)
private_key_file = "#{res_dir}/privateKey.key"
cert_file = "#{res_dir}/certificate.crt"

pkey = OpenSSL::PKey::RSA.new(File.read(private_key_file))
cert = OpenSSL::X509::Certificate.new(File.read(cert_file))

pid_file = ARGV[0]

s = WEBrick::HTTPServer.new(
  :Port => 9443,
  :Logger => WEBrick::Log::new(nil, WEBrick::Log::ERROR),
  :DocumentRoot => File.join(File.dirname(__FILE__)),
  :ServerType => WEBrick::Daemon,
  :SSLEnable => true,
  :SSLVerifyClient => OpenSSL::SSL::VERIFY_NONE,
  :SSLCertificate => cert,
  :SSLPrivateKey => pkey,
  :SSLCertName => [ [ "CN",WEBrick::Utils::getservername ] ],
  :StartCallback => proc { File.open(pid_file, "w") { |f| f.write $$.to_s }}
)
trap("INT"){ s.shutdown }
s.start
