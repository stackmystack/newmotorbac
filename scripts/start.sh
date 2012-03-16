#!/bin/sh
java -classpath lib/'*' -jar motorbac2.jar >> ./logs/log.$(date +%F%H%M%S) 2>&1
