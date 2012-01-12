#!/bin/bash
# I assume its installed in /Applications/VP\ Suite\ 5.3/
VPBASE="/Applications/VP Suite 5.3/"
VPRUN="launcher/run_vpuml.app"

PID=$(ps ax | awk '/run_vpuml/ {print $1}' | head -n1)
if [ ! -z "$VPRUN" ]; then
	kill -15 ${PID}
	mkdir -p "${VPBASE}/plugins/ODR/lib"
        mkdir -p "${VPBASE}/plugins/ODR/images"

	cp -r target/ODR-VP-Plugin-0.2.4-SNAPSHOT.jar "${VPBASE}plugins/ODR/lib/"
	cp -r plugin.xml "${VPBASE}plugins/ODR/plugin.xml"
        cp -r usersettings.xml "${VPBASE}plugins/ODR/usersettings.xml"
	cp -r images "${VPBASE}plugins/ODR/"
        
	open "/Applications/VP Suite 5.3/launcher/run_vpuml.app"
else
	exit 0
fi