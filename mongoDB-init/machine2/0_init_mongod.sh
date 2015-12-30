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
