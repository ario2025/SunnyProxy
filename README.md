# SunnyProxy

SunnyProxy is a Java based proxy reverse minimalist "proxy-like" servlet to serve all requests where commonly blocked for some reason in common circumstances to work on focus in Oracle Weblogic Server.


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
 <Error> <HTTP> <BEA-101019> <[ServletContext@41846426[app:SunnyProxy module:SunnyProxy.war path:/SunnyProxy spec-version:2.5]] Servlet failed with IOException
javax.net.ssl.SSLException: Received fatal alert: protocol_version
        at com.sun.net.ssl.internal.ssl.Alerts.getSSLException(Alerts.java:190)
        at com.sun.net.ssl.internal.ssl.Alerts.getSSLException(Alerts.java:136)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.recvAlert(SSLSocketImpl.java:1806)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:986)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1170)
        Truncated. see log file for complete stacktrace
>

- Some websites like https://www.avianca.com.br/ not working for unknown reason yet.
Detail:
<Error> <HTTP> <BEA-101019> <[ServletContext@41846426[app:SunnyProxy module:SunnyProxy.war path:/SunnyProxy spec-version:2.5]] Servlet failed with IOException
javax.net.ssl.SSLException: java.lang.RuntimeException: Could not generate DH keypair
        at com.sun.net.ssl.internal.ssl.Alerts.getSSLException(Alerts.java:190)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1731)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1692)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.handleException(SSLSocketImpl.java:1675)
        at com.sun.net.ssl.internal.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1204)
        Truncated. see log file for complete stacktrace
Caused By: java.lang.RuntimeException: Could not generate DH keypair
        at com.sun.net.ssl.internal.ssl.DHCrypt.<init>(DHCrypt.java:106)
        at com.sun.net.ssl.internal.ssl.ClientHandshaker.serverKeyExchange(ClientHandshaker.java:556)
        at com.sun.net.ssl.internal.ssl.ClientHandshaker.processMessage(ClientHandshaker.java:183)
        at com.sun.net.ssl.internal.ssl.Handshaker.processLoop(Handshaker.java:593)
        at com.sun.net.ssl.internal.ssl.Handshaker.process_record(Handshaker.java:529)
        Truncated. see log file for complete stacktrace
Caused By: java.security.InvalidAlgorithmParameterException: Prime size must be multiple of 64, and can only range from 512 to 1024 (inclusive)
        at com.sun.crypto.provider.DHKeyPairGenerator.initialize(DashoA13*..)
        at java.security.KeyPairGenerator$Delegate.initialize(KeyPairGenerator.java:627)
        at com.sun.net.ssl.internal.ssl.DHCrypt.<init>(DHCrypt.java:100)
        at com.sun.net.ssl.internal.ssl.ClientHandshaker.serverKeyExchange(ClientHandshaker.java:556)
        at com.sun.net.ssl.internal.ssl.ClientHandshaker.processMessage(ClientHandshaker.java:183)
        Truncated. see log file for complete stacktrace
>



* Future features
-----------------

- POST mechanism.
- X-Forwarded-For header.
- Stream connections.
- REST/SOAP Content-Type.
- Interop between ISO-8859-1 and UTF-8.
