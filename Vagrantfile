# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
	config.vm.box = "ARTACK/debian-jessie"
	config.vm.network "private_network", ip: "192.168.33.10"
	config.vm.network 'forwarded_port', guest: 27017, host: 27017
	config.vm.provision "ansible" do |ansible|
	    ansible.playbook = "playbook.yml"
    	end
end
