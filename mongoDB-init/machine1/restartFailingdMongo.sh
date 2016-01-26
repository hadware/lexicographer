#!/bin/bash

# clean everything up
echo "killing mongod and mongos"
sudo killall mongod
sudo killall mongos
echo "removing config and shard directories"
rm -rf data/config
rm -rf data/shard*

# restart instance mongo on 3 different shards
echo "machine 1 is restarting mongod instances on different shards"
mkdir -p data/shard0/rs0 data/shard1/rs0 data/shard2/rs0
mongod --replSet s0 --logpath "s0-r0.log" --dbpath data/shard0/rs0 --port 37017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r0.log" --dbpath data/shard1/rs0 --port 37018 --fork --shardsvr --smallfiles
mongod --replSet s2 --logpath "s2-r0.log" --dbpath data/shard2/rs0 --port 37019 --fork --shardsvr --smallfiles

sleep 10

# now restart a config servers
echo "Copy a good configServer from other nodes"
scp -r teamdata@10.0.0.5:lexicographer/mongoDB-init/machine2/data/config data/config

sleep 5

echo "Starting mongod config server"
mongod --logpath "cfg.log" --dbpath data/config --port 57040 --fork --configsvr --smallfiles
echo "sleeping for 10s"
sleep 10

# now restart the mongos
mongos --logpath "mongos.log" --configdb 10.0.0.4:57040,10.0.0.5:57040,10.0.0.6:57040 --fork



