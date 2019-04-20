# SunnyProxy

SunnyProxy is a first minimalist proxy-like servlet to serve all requests where commonly blocked for some
reason in common circunstances.

Weblogic
--------

Add in args this values:
-DUseSunHttpHandler=true
-Dweblogic.ssl.JSSEEnabled=true
-Dweblogic.security.SSL.enableJSSE=true
-Dweblogic.security.SSL.verbose=true
-Dweblogic.security.SSL.enable.renegotiation=true
-Dsun.security.ssl.allowUnsafeRenegotiation=true

For debug SSL certs:
-Djavax.net.debug=ssl

- ATTENTION!
------------
This project it's under development and are not working yet as expected, so, don't mind to
use it in your production environment!