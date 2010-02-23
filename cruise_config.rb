Project.configure do |project|
  project.scheduler.polling_interval = 1.minute

  case project.name
  when "client_library_java_integration_master"
    project.build_command = "CRUISE_BUILD=#{project.name} GATEWAY_PORT=4010 SPHINX_PORT=4322 rake cruise"
    project.triggered_by :gateway_master
  end
end

