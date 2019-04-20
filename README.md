# SunnyProxy

SunnyProxy is a first minimalist proxy-like servlet to serve all requests where commonly blocked for some
reason in common circumstances.


Weblogic 10.3.6
---------------

Steps to prepare in Weblogic environment:

1. Select your server in Weblogic Console

2. Go to tab: SSL, click on Advanced link to expand

3. Find out "Enable JSSE" option and mark as true.

4. Add in args this values:
   -DUseSunHttpHandler=true
   -Dweblogic.ssl.JSSEEnabled=true
   -Dweblogic.security.SSL.enableJSSE=true
   -Dweblogic.security.SSL.verbose=true
   -Dweblogic.security.SSL.enable.renegotiation=true
   -Dsun.security.ssl.allowUnsafeRenegotiation=true

5. Restart your weblogic environment.

* For debug SSL certs:

-Djavax.net.debug=ssl

* Or for more detailed information:

-Djavax.net.debug=all


Build
-----

Apache Maven 3.3.9 with Java JDK 1.7 or JDK 1.6.
Weblogic 10.3.6.
** Plan to work on TomEE also.


** ATTENTION!
-------------

This project it's under development and are not working yet as expected, so, don't mind to
use it in your production environment!

* Solved issues
---------------

Some issues with httpClient from Apache, are working.
- Wildcard SSL certs are working - now with HttpURLConnection.
- Redirect from http to https in 301/302 to switch protocols, as transparent redirect. Not working with HttpURLConnection.



* Active issues
---------------

- TLS 1.2 not working on websites with SSL certs only for TLS1.2.
- Some websites like https://www.avianca.com.br/ not working for unknown reason yet.


* Future features
-----------------

- POST mechanism.
- X-Forwarded-For header.
- Stream connections.
- REST/SOAP Content-Type.
- Interop between ISO-8859-1 and UTF-8.