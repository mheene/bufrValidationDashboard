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
<<<<<<< HEAD
Please note it is an inital commit and for discussion purpose only. Some value/functions are not implemented now (e.g. number of messages in a BUFR, MD5 Checksum, ...)


=======
Please note it is an inital commit and for discussion purpose only. Some value/functions are not implemente
d now (e.g. number of messages in a BUFR, MD5 Checksum, ...)
>>>>>>> 6e6da901f24d466562db1246581dc49a97fca3b5
