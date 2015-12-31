echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s1
echo "Updating Configuration s1 replica set"
mongo --host 10.0.0.5 --port 47018 << 'EOF'
ip = "10.0.0.4"
rs.add(ip + ":37018")
EOF

# Add this machine to replica set s2
echo "Updating Configuration s2 replica set"
mongo --host 10.0.0.6 --port 67019 << 'EOF'
ip = "10.0.0.4"
rs.add(ip + ":37019")
EOF

