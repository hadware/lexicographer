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
mkdir -p data/shard0/rs1 data/shard1/rs1
mongod --replSet s0 --logpath "s0-r1.log" --dbpath data/shard0/rs1 --port 47017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r1.log" --dbpath data/shard1/rs1 --port 47018 --fork --shardsvr --smallfiles
#mongod --replSet s1 --logpath "s1-r2.log" --dbpath data/shard1/rs2 --port 47019 --fork --shardsvr --smallfiles

sleep 10

# connect to one server and initiate the set
echo "Configuring s1 replica set"
mongo --port 47018 << 'EOF'
//Change this IP for different configuration !
my_ip = "192.168.130.129"
other_ip1 = "192.168.1.16"

config = { _id: "s1", members:[{ _id : 0, host : my_ip + ":47018" }]};
rs.initiate(config)
rs.add(other_ip1 + ":37018")
EOF

# connect to one server and initiate the set
echo "Updating Configuration s0 replica set"
mongo --host 192.168.1.16 --port 37017 << 'EOF'
//Change this IP for different configuration !
my_ip = "192.168.130.129"
rs.add(my_ip + ":47017")
EOF
