#!/bin/bash -x
#
VPBASE=$(readlink $(which run_vpuml) | sed 's#launcher/run_vpuml##g')
VPRUN=$(which run_vpuml)
PID=$(ps ax | awk '/run_vpuml/ {print $1}' | head -n1)
if [ ! -z "$VPRUN" ]; then
	kill -15 ${PID}
	mkdir -p "${VPBASE}/plugins/ODR/lib"
        mkdir -p "${VPBASE}/plugins/ODR/images"

	mv "${VPBASE}resources/vpuicustomization.xml" "${VPBASE}resources/vpuicustomization.backup"
        mv "${VPBASE}resources/images/logo-red.png" "${VPBASE}plugins/ODR/images/logo-red.png"

	cp -r "vpuicustomization.xml" "${VPBASE}resources/vpuicustomization.xml"
        
        cp -r "images/icon-red.png" "${VPBASE}resources/images/icon-red.png"
        cp -r usersettings.xml "${VPBASE}plugins/ODR/usersettings.xml"
	cp -r target/ODR-VP-Plugin-0.2.4-SNAPSHOT.jar "${VPBASE}plugins/ODR/lib/"
	cp -r plugin.xml "${VPBASE}plugins/ODR/plugin.xml"
	cp -r images "${VPBASE}plugins/ODR/"

	run_vpuml

	rm -rf "${VPBASE}resources/vpuicustomization.xml"
	mv "${VPBASE}resources/vpuicustomization.backup" "${VPBASE}resources/vpuicustomization.xml"
else
	exit 0
fi
