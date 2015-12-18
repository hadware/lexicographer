# # now start the mongos on a standard port
mongos --logpath "mongos-1.log" --configdb 192.168.1.2:57040,192.168.1.2:57041,192.168.1.2:57042 --fork
