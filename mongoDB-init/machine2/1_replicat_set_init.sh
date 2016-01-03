#!/bin/bash

# Initialization of the replica set s1
echo "Configuring s1 replica set"
mongo --port 37018 << 'EOF'
//Change this IP for different configuration !
my_ip = "10.0.0.5"
config = { _id: "s1", members:[{ _id : 0, host : my_ip + ":37018" }]};
rs.initiate(config)
EOF
