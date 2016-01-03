#!/bin/bash

# Initialization of the replica set s0
echo "Configuring s0 replica set"
mongo --port 37017 << 'EOF'
//Change this IP for different configuration !
ip = "10.0.0.4"
config = { _id: "s0", members:[{ _id : 0, host : ip + ":37017" }]};
rs.initiate(config)
EOF

