# bufrValidationDashboard
With the help of the BUFR validation dashboard a user could now easily check the BUFR with different decoders and receives warnings/errors of each decoder.

Docker
======
If you have docker installed simply type

docker run -it --rm -p 8888:8080 "mheene/bufrvalidationdashboard"

Now open in a browser the [dashboard](http://localhost:8888/dashboard)

That's it. The dashboard runs in a tomcat container on your computer on port 8888.

Installation
============
The directory Servlet-Version contains the BUFR Validation Dashboard.

Copy the file build.properties-template to build.properties and change the to your local installation.
At least you need to change catalina.home to your local installation. If you need a proxy to acces the internet configure the proxy setting in file /web/WEB-INF/web.xml


Create Distribution
===================
ant dist

You will find the distribution in the directory dist. If you run tomcat and changed in the build.properties the variable catalina.home then

ant deploy

will deploy the application.


Notes:
======
Please note that this is an inital commit for discussion purposes only. Some values/functions are not fully implemented now (e.g. number of messages in a BUFR, ...)

[WMO Paper IPET-CM II](https://www.wmo.int/pages/prog/www/ISS/Meetings/IPET-CM_Offenbach2018/Documents/IPET-CM-II_Doc7-3-1_BUFR-Validation-Dashboard.docx)

[Online Demonstrator, openshift cloud](http://bufr-wildfly01.193b.starter-ca-central-1.openshiftapps.com/dashboard)

[Online Demonstrator based on primefaces, IRIMO](http://forecast.irimo.ir/BufrValidation/)
