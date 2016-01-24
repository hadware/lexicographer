# add shards and enable sharding on the test db
mongo << 'EOF'

sh.addShard("s0/10.0.0.4:37017")
sh.addShard("s1/10.0.0.5:37018")
sh.addShard("s2/10.0.0.6:37019")
sh.enableSharding("epub")
sh.shardCollection("epub.books",{"_id" : 1})
sh.shardCollection("epub.booksStat",{"_id" : 1})
sh.shardCollection("epub.authors",{"_id" : 1})
sh.shardCollection("epub.subjects",{"_id" : 1})
EOF
