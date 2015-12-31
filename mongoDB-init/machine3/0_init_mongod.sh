#!/bin/bash

# clean everything up
echo "killing mongod and mongos"
sudo killall mongod
sudo killall mongos
echo "removing data files"
rm -rf data/config
rm -rf data/shard*


# start instance mongo on 3 different shards
echo "machine 3 is starting mongod instances on different shards"
mkdir -p data/shard0/rs2 data/shard1/rs2 data/shard2/rs2
mongod --replSet s0 --logpath "s0-r2.log" --dbpath data/shard0/rs2 --port 67017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r2.log" --dbpath data/shard1/rs2 --port 67018 --fork --shardsvr --smallfiles
mongod --replSet s2 --logpath "s2-r2.log" --dbpath data/shard2/rs2 --port 67019 --fork --shardsvr --smallfiles

sleep 10
