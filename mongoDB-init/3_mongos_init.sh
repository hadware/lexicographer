# # now start the mongos on a standard port
mongos --logpath "mongos-1.log" --configdb $1:57040,$1:57041,$1:57042 --fork
