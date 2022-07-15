#!/bin/sh
	while true
	do
		clear
		java -Xmx400M -jar R_Socket.jar
		for i in 5 4 3 2 1
		do
			echo -ne "."
			sleep 1
		done
	done