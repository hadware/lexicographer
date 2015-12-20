# add shards and enable sharding on the test db
mongo << 'EOF'

//The IP address of all the other machine in the sharding
my_ip="192.168.130.129"
other_ip1="192.168.1.16"

sh.addShard("s0/" + other_ip1 + ":37017")
sh.addShard("s1/" + my_ip + ":47017")
sh.enableSharding("epub")
sh.shardCollection("epub.books",{"_id" : 1})
EOF
