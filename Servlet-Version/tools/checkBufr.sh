#!/bin/bash

curl -F uploadFile=@$1 -X POST http://bufr-allinone.7e14.starter-us-west-2.openshiftapps.com/dashboard/uploadFile?output=text
