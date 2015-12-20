#!/bin/bash

# clean everything up
echo "killing mongod and mongos"
sudo killall mongod
sudo killall mongos
echo "removing data files"
rm -rf data/config
rm -rf data/shard*


# start a replica set and tell it that it will be shard0(pc-robin 192.168.1.3)
echo "starting servers for shard 0"
mkdir -p data/shard0/rs0 data/shard0/rs1 data/shard0/rs2
mongod --replSet s0 --logpath "s0-r0.log" --dbpath data/shard0/rs0 --port 37017 --fork --shardsvr --smallfiles
mongod --replSet s0 --logpath "s0-r1.log" --dbpath data/shard0/rs1 --port 37018 --fork --shardsvr --smallfiles
mongod --replSet s0 --logpath "s0-r2.log" --dbpath data/shard0/rs2 --port 37019 --fork --shardsvr --smallfiles

sleep 5
# connect to one server and initiate the set
echo "Configuring s0 replica set"

mongo --port 37017 << 'EOF'

//Change this IP for different configuration !
ip = "192.168.1.16"

config = { _id: "s0", members:[
          { _id : 0, host : ip + ":37017" },
          { _id : 1, host : ip + ":37018" },
          { _id : 2, host : ip + ":37019" }]};
rs.initiate(config)
EOF
