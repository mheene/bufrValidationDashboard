# bufrValidationDashboard

![Build Status](https://api.travis-ci.org/mheene/bufrValidationDashboard.svg?branch=master)

With the help of the BUFR validation dashboard users could now easily check a BUFR with different decoders and receive warnings/errors of each decoder. An online demonstrator is available here

[Online Demonstrator, openshift cloud](http://bufr-wildfly01.193b.starter-ca-central-1.openshiftapps.com/dashboard)

![Dashboard](https://raw.githubusercontent.com/mheene/bufrValidationDashboard/master/Servlet-Version/docs/gui.png)

Command Line Interface
======================
With the shell script [checkBufr.sh](https://raw.githubusercontent.com/mheene/bufrValidationDashboard/master/Servlet-Version/tools/checkBufr.sh) you can easily check a BUFR with the different decoders from the command line.

checkBufr.sh bufrFile

![Screenshot](https://raw.githubusercontent.com/mheene/bufrValidationDashboard/master/Servlet-Version/docs/cli.png)

Supported Decoders
==================
Currently the following decoders are included:

 * BUFR Tools (DWD) [Online version provided by DWD](https://kunden.dwd.de/bufrviewer)
 * ecCodes (ECMWF) [Online version provided by ECMWF](http://apps.ecmwf.int/codes/bufr/validator/)
 * PyBufrKit (Yang Wang) [Online version provided by Yang](http://aws-bufr-webapp.s3-website-ap-southeast-2.amazonaws.com)
 * TrollBufr (Alex Maul) [Online version provided by Alex](http://flask-bufr-flasked-bufr.193b.starter-ca-central-1.openshiftapps.com) 
 * libECBUFR (Meteorological Service of Canada) [Self created online version](http://dev-bufr.1d35.starter-us-east-1.openshiftapps.com/libecBufrX)
 * Geo::BUFR (Pål Sannes) [Self created online version](http://geobufr-geobufr.a3c1.starter-us-west-1.openshiftapps.com/geobufr)
 * BUFRDC (ECMWF) [Self created online version](http://bufrd-bufrdc.193b.starter-ca-central-1.openshiftapps.com/bufrdc)


Docker
======
If you have docker installed simply type

docker pull "mheene/bufrvalidationdashboard"
docker run -it --rm -p 8888:8080 "mheene/bufrvalidationdashboard"

Now open in a browser the [dashboard](http://localhost:8888/dashboard)

That's it. The dashboard runs in a tomcat container on your computer on port 8888.

You can find the image [here](https://hub.docker.com/r/mheene/bufrvalidationdashboard)

![Docker Build](https://img.shields.io/docker/build/mheene/bufrvalidationdashboard.svg)
![Docker Pulls](https://img.shields.io/docker/pulls/mheene/bufrvalidationdashboard.svg)

Installation
============
In case you want to compile the dashboard by yourself you can use the provided ant script (build.xml). The directory Servlet-Version contains the BUFR Validation Dashboard and the build enviornment.

Copy the file build.properties-template to build.properties and change the content to your local installation.
At least you need to change catalina.home to your local installation. If you need a proxy to acces the internet configure the proxy setting in file /web/WEB-INF/web.xml

The dashboard is tested with Tomcat, Jetty and Wildfly application servers.

Create Distribution
===================
Create the distribution with

ant dist

and you will find the distribution in the directory dist. If you run tomcat and changed in the build.properties the variable catalina.home then

ant deploy

will deploy the application.


Notes:
======
Please note that this is an inital commit for discussion purposes only. Some values/functions are not fully implemented now (e.g. number of messages in a BUFR, ...)

[WMO Paper IPET-CM II](https://www.wmo.int/pages/prog/www/ISS/Meetings/IPET-CM_Offenbach2018/Documents/IPET-CM-II_Doc7-3-1_BUFR-Validation-Dashboard.docx)

[Online Demonstrator, openshift cloud](http://bufr-wildfly01.193b.starter-ca-central-1.openshiftapps.com/dashboard)

[Online Demonstrator based on primefaces, IRIMO](http://bufrchk.irimo.ir)

Your feedback is highly welcomed.