#!/bin/bash

while true
do

rsync -avz "./WebInterface/src/main/webapp/" "./OpenDecisionRepository/target/gfdeploy/WebInterface-0.1_war"
rsync -avz "./WebInterface/src/main/resources/nl/rug/search/odr/localization/" "./OpenDecisionRepository/target/gfdeploy/WebInterface-0.1_war/WEB-INF/classes/nl/rug/search/odr/localization"

# use either enter to redeploy or do it every 5 seconds
read line
# sleep 5
done
