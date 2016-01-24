# now start the mongos on a standard port
mongos --logpath "mongos.log" --configdb 10.0.0.4:57040,10.0.0.5:57040,10.0.0.6:57040 --fork
