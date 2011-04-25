#!/bin/bash

while true
do

rsync -avz "../sources/web-interface/src/main/webapp/" "../sources/startup/target/gfdeploy/nl.rug.search_startup_ear_0.1/web-interface-0.1_war"

# use either "enter" to redeploy or do it every 5 seconds
read line
# sleep 5
done
