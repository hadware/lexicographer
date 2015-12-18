# now start 3 config servers
echo "Starting config servers"
mkdir -p data/config/config-a data/config/config-b data/config/config-c 
mongod --logpath "cfg-a.log" --dbpath data/config/config-a --port 57040 --fork --configsvr --smallfiles
mongod --logpath "cfg-b.log" --dbpath data/config/config-b --port 57041 --fork --configsvr --smallfiles
mongod --logpath "cfg-c.log" --dbpath data/config/config-c --port 57042 --fork --configsvr --smallfiles
echo "sleeping for 10s"
sleep 10
