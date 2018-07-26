#!/bin/bash

curl -F uploadFile=@$1 -X POST http://bufr-wildfly01.193b.starter-ca-central-1.openshiftapps.com/dashboard/uploadFile?output=text
