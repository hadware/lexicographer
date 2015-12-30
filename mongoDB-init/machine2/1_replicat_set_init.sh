#!/bin/bash

# Initialization of the replica set s0
echo "Configuring s1 replica set"
mongo --port 47018 << 'EOF'
//Change this IP for different configuration !
my_ip = "192.168.130.129"
config = { _id: "s1", members:[{ _id : 0, host : my_ip + ":47018" }]};
rs.initiate(config)
EOF
