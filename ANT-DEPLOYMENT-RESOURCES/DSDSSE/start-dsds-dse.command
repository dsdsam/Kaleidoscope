#!/bin/bash
cd "$(dirname "$0")"
java -Xmx256m -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -jar dsds-dse-mac-app.jar --installer