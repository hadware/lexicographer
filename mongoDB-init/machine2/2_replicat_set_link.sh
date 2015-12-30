echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s0
echo "Updating Configuration s0 replica set"
mongo --host 192.168.1.16 --port 37017 << 'EOF'
//Change this IP for different configuration !
my_ip = "192.168.130.129"
rs.add(my_ip + ":47017")
EOF
