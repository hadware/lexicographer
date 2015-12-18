#!/bin/bash

sh 1_replicat_set_init.sh   
sh 2_config_server_init.sh  
sh 3_mongos_init.sh 
sh 4_enable_sharding.sh
