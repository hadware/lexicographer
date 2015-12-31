#!/bin/bash

my_ip=10.0.0.5

sh 3_config_server_init.sh  
sh 4_mongos_init.sh $my_ip
sh 5_enable_sharding.sh
