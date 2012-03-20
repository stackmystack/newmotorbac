#!/bin/sh
java -classpath lib/'*' -jar newmotorbac.jar >> ./logs/log.$(date +%F%H%M%S) 2>&1
