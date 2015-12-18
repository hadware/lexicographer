# clean everything up
echo "killing mongod and mongos"
sudo killall mongod
sudo killall mongos
echo "removing data files"
rm -rf data/config
rm -rf data/shard*

echo "---------------sleeping for 5s-------------------"
sleep 5

# start a replicate set and tell it that it will be a shard1 (pc-amadou 192.168.1.2)
echo "starting servers for shard 1"
mkdir -p data/shard1/rs0 data/shard1/rs1 data/shard1/rs2
mongod --replSet s1 --logpath "s1-r0.log" --dbpath data/shard1/rs0 --port 47017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r1.log" --dbpath data/shard1/rs1 --port 47018 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r2.log" --dbpath data/shard1/rs2 --port 47019 --fork --shardsvr --smallfiles

echo "---------------sleeping for 10s--------------"
sleep 10

echo "Configuring s1 replica set"
mongo --port 47017 << 'EOF'
config = { _id: "s1", members:[
          { _id : 0, host : "192.168.1.2:47017" },
          { _id : 1, host : "192.168.1.2:47018" },
          { _id : 2, host : "192.168.1.2:47019" }]};
rs.initiate(config)
EOF
