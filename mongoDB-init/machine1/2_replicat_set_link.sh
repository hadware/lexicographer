echo "Sleep 5 to wait other during creation of other replica set"
sleep 5

# Add this machine to replica set s1
echo "Updating Configuration s1 replica set"
mongo --host 10.0.0.5 --port 47018 << 'EOF'
//Change this IP for different configuration !
ip = "10.0.0.4"
rs.add(ip + ":37018")
EOF
