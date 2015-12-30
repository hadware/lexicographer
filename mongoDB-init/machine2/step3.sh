#!/bin/bash

my_ip=192.168.130.129

sh 3_config_server_init.sh  
sh 4_mongos_init.sh $my_ip
sh 5_enable_sharding.sh
