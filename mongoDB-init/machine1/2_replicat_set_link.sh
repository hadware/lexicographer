echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s1
echo "Updating Configuration s1 replica set"
mongo --host 192.168.130.129 --port 47018 << 'EOF'
//Change this IP for different configuration !
ip = "192.168.1.16"
rs.add(ip + ":37018")
EOF
