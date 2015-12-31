echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s0
echo "Updating Configuration s0 replica set"
mongo --host 10.0.0.4 --port 37017 << 'EOF'
my_ip = "10.0.0.5"
rs.add(my_ip + ":47017")
EOF

# Add this machine to replica set s2
echo "Updating Configuration s2 replica set"
mongo --host 10.0.0.6 --port 67019 << 'EOF'
my_ip = "10.0.0.5"
rs.add(my_ip + ":47019")
EOF

