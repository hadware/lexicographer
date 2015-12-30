# add shards and enable sharding on the test db
mongo << 'EOF'

sh.addShard("s0/192.168.1.16:37017")
sh.addShard("s1/192.168.130.129:47018")
sh.enableSharding("epub")
sh.shardCollection("epub.books",{"_id" : 1})
EOF
