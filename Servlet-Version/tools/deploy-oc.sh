#!/bin/bash

# Check if oc exists
command -v oc >/dev/null 2>&1 || { echo >&2 "I require co but it's not installed. Download link: https://www.openshift.org/download.html"; exit 1; }

# Check args passed to the script
if [ "$#" -ne 4 ]; then
    echo "Illegal number of parameters";
    echo "Usage: $0 <username> <password> <oc-app> <war-file>"
    exit 1;
fi

# oc login
oc login --username=$1 --password=$2

# oc deploy
oc start-build $3 --from-file=$4
