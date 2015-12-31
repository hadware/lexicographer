echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s0
echo "Updating Configuration s0 replica set"
mongo --host 10.0.0.4 --port 37017 << 'EOF'
ip = "10.0.0.6"
rs.add(ip + ":67017")
EOF

# Add this machine to replica set s1
echo "Updating Configuration s1 replica set"
mongo --host 10.0.0.5 --port 47018 << 'EOF'
ip = "10.0.0.6"
rs.add(ip + ":67018")
EOF

