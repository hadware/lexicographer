# now start 3 config servers
echo "Starting config servers"
mkdir -p data/config
mongod --logpath "cfg.log" --dbpath data/config --port 57040 --fork --configsvr --smallfiles
echo "sleeping for 10s"
sleep 10
