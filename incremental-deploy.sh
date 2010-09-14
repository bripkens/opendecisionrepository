#!/bin/bash

while true
do

rsync -avz "./WebInterface/src/main/webapp/" "./OpenDecisionRepository/target/gfdeploy/WebInterface-0.1_war"

sleep 5
done
