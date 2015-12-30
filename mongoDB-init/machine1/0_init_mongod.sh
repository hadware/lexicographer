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
mkdir -p data/shard0/rs0 data/shard1/rs0
mongod --replSet s0 --logpath "s0-r0.log" --dbpath data/shard0/rs0 --port 37017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r0.log" --dbpath data/shard1/rs0 --port 37018 --fork --shardsvr --smallfiles
#mongod --replSet s0 --logpath "s0-r2.log" --dbpath data/shard0/rs2 --port 37019 --fork --shardsvr --smallfiles

sleep 10
