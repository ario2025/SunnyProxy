# SunnyProxy

SunnyProxy is a first minimalist proxy-like servlet to serve all requests where commonly blocked for some
reason in common circumstances.


Weblogic
--------

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


** ATTENTION!
-------------

This project it's under development and are not working yet as expected, so, don't mind to
use it in your production environment!