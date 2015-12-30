#!/bin/bash

my_ip=192.168.130.129

sh 1_replicat_set_init.sh   
sh 2_config_server_init.sh  
sh 3_mongos_init.sh $my_ip
sh 4_enable_sharding.sh
