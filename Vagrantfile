# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.require_version ">= 1.8"

ANSIBLE_VERSION = "2.3.1.0"

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"

  config.vm.synced_folder ".", "/vagrant", disabled: true
  config.vm.synced_folder ".", "/opt/a-breakable-toy", type: "rsync"

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

      cd /opt/a-breakable-toy/deployment/ansible && \
      ruby ../vagrant/run_ansible_galaxy.rb
      ANSIBLE_FORCE_COLOR=1 PYTHONUNBUFFERED=1 ANSIBLE_CALLBACK_WHITELIST=profile_tasks \
      ansible-playbook -u vagrant -i 'localhost,' main.yml
      cd /opt/a-breakable-toy

      su vagrant ./scripts/bootstrap
    SHELL
  end
end
