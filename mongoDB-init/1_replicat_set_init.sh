#!/bin/bash

# clean everything up
echo "killing mongod and mongos"
sudo killall mongod
sudo killall mongos
echo "removing data files"
rm -rf data/config
rm -rf data/shard*


# start a replica set and tell it that it will be shard0(pc-amadou 192.168.1.2)
echo "starting servers for shard 1"
mkdir -p data/shard1/rs0 data/shard1/rs1 data/shard1/rs2
mongod --replSet s1 --logpath "s1-r0.log" --dbpath data/shard1/rs0 --port 47017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r1.log" --dbpath data/shard1/rs1 --port 47018 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r2.log" --dbpath data/shard1/rs2 --port 47019 --fork --shardsvr --smallfiles

sleep 5
# connect to one server and initiate the set
echo "Configuring s1 replica set"

mongo --port 47017 << 'EOF'

//Change this IP for different configuration !
ip = "192.168.130.129"

config = { _id: "s1", members:[
          { _id : 0, host : ip + ":47017" },
          { _id : 1, host : ip + ":47018" },
          { _id : 2, host : ip + ":47019" }]};
rs.initiate(config)
EOF

