#!/bin/bash

# Initialization of the replica set s2
echo "Configuring s2 replica set"
mongo --port 37019 << 'EOF'
//Change this IP for different configuration !
ip = "10.0.0.6"
config = { _id: "s0", members:[{ _id : 0, host : ip + ":37019" }]};
rs.initiate(config)
EOF

