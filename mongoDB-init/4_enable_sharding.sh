# add shards and enable sharding on the test db
mongo <<'EOF'
sh.addShard("s0/192.168.1.3:37017")
sh.addShard("s1/192.168.1.2:47017")
sh.enableSharding("epub")
sh.shardCollection("epub.books",{"_id" : 1})
EOF
