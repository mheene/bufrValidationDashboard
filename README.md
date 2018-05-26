# bufrValidationDashboard
With the help of the BUFR validation dashboard the user could now easily check the BUFR with different decoders and would receive warnings/errors of each decoder.

Installation
============
The directory Servlet-Version contains an initial prototype version for the BUFR Validation Dashboard.

Copy the file build.properties-template to build.properties and change the to your local installation.
At least you need to change catalina.home to your local installation.

Create Distribution
===================
ant -f build.properties deploy

You will find the distribution in the directory dist.


Notes:
======
Please note that this is an inital commit for discussion purposes only. Some values/functions are not fully implemented now (e.g. number of messages in a BUFR, ...)
