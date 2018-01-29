#!/bin/bash
cd "$(dirname "$0")"
java -Xmx256m -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -jar sem-mac-app.jar --installer


