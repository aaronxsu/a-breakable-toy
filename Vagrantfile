# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.require_version ">= 1.8"

ANSIBLE_VERSION = "2.3.1.0"

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"

  config.vm.provider :virtualbox do |vb|
    vb.memory = 2048
    vb.cpus = 2
  end

  config.vm.provision "shell" do |s|
    s.inline = <<-SHELL
      if [ ! -x /usr/local/bin/ansible ] || ! ansible --version | grep #{ANSIBLE_VERSION}; then
        sudo apt-get update -qq
        sudo apt-get install python-pip python-dev -y
        sudo pip install --upgrade pip
        sudo pip install ansible==#{ANSIBLE_VERSION}
      fi

      cd /vagrant/deployment/ansible && \
      ruby ../vagrant/run_ansible_galaxy.rb
      ANSIBLE_FORCE_COLOR=1 PYTHONUNBUFFERED=1 ANSIBLE_CALLBACK_WHITELIST=profile_tasks \
      ansible-playbook -u vagrant -i 'localhost,' main.yml

    SHELL
  end
end
