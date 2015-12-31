# add shards and enable sharding on the test db
mongo << 'EOF'

sh.addShard("s0/10.0.0.4:37017")
sh.addShard("s1/10.0.0.5:47018")
sh.addShard("s2/10.0.0.6:67019")
sh.enableSharding("epub")
sh.shardCollection("epub.books",{"_id" : 1})
EOF
