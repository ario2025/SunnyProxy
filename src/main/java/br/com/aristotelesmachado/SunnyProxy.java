package br.com.aristotelesmachado;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import javax.net.ssl.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.net.ssl.*;

import static org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;

public class SunnyProxy extends HttpServlet {
    String b = "## [SunnyProxy]: ";
    public void log(String texto){System.out.println(b+texto);}

    public void init() throws ServletException {
        //System.setProperty("javax.net.ssl.trustStore", "/path/to/my/cacerts");
        //System.setProperty("javax.net.ssl.trustStore","/tmp/keystore.jks");
        //System.out.println( SunnyProxy.class.getClassLoader().getResource("src/main/resources/custom-cacerts.jks").toString() );
        //System.setProperty("javax.net.ssl.trustStore", "src/main/resources/custom-cacerts.jks" );
        //System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        System.out.println("#################################################");
        System.out.println("## SunnyProxy 1.0                              ##");
        System.out.println("#################################################");

        System.setProperty("javax.net.debug", "all");
        System.setProperty("UseSunHttpHandler", "true");
        System.setProperty("ssl.debug", "true");
        System.setProperty("https.protocol", "TLSv1.2");

        /*
         -Djavax.net.debug=all
         -DUseSunHttpHandler=true
         -Dweblogic.ssl.JSSEEnabled=true
         -Dweblogic.security.SSL.enableJSSE=true
         -Dweblogic.security.SSL.verbose=true
         -Dweblogic.security.SSL.enable.renegotiation=true
         -Dsun.security.ssl.allowUnsafeRenegotiation=true
         -Dssl.debug=true
         -Dssl.SocketFactory.provider=sun.security.ssl.SSLSocketFactoryImpl
         -Dssl.ServerSocketFactory.provider=sun.security.ssl.SSLSocketFactoryImpl
         -Dweblogic.security.SSL.nojce=true
         -Dweblogic.ssl.JSSEEnabled=true
         -Dweblogic.security.SSL.enableJSSE=true
         */

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = request.getParameter("url");
        log("URL called: " + url);

        int ignoressl = 0;
        if ( request.getParameter("ignoressl") != null && request.getParameter("ignoressl").equals("1") ) { ignoressl = 1; } else { ignoressl = 0; }
        log("Ignore SSL: " + ( ignoressl == 1 ? "True" : "False" ));

        if ( url!=null && !url.trim().equals("") ) {



            // Http common / Apache.
            int clientversion = 3;


            if (clientversion == 1) {
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                //HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setInstanceFollowRedirects(true);
                con.setDoOutput(true);
                con.setConnectTimeout(1500); // TCP open port.
                con.setReadTimeout(10000);   // Application response.
                con.setRequestProperty("Content-Type","text/html");
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
                con.setUseCaches(true);
                con.setRequestMethod("GET");
                con.connect();
                int responseCode = con.getResponseCode();
                response.setStatus( con.getResponseCode() );
                response.setContentType( con.getContentType() );
                //response.setHeader( "Cache-Control", con.getHeaderField("Cache-Control") );
                //response.setHeader( "Pragma", con.getHeaderField("Pragma") );
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer responseBuffer = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    //responseBuffer.append(inputLine);
                    response.getWriter().write( inputLine );
                }
                in.close();
            }




            if (clientversion == 3) {

                SSLContext sslcontext = SSLContexts.createDefault(); //SSLContexts.createSystemDefault();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext) { /*new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},null,BROWSER_COMPATIBLE_HOSTNAME_VERIFIER*/
                    @Override
                    public Socket connectSocket(
                            int connectTimeout,Socket socket,HttpHost host,InetSocketAddress remoteAddress,
                            InetSocketAddress localAddress,HttpContext context) throws IOException, ConnectTimeoutException {
                        /*
                        if (socket instanceof SSLSocket) {
                            try {
                                System.setProperty(socket, "host", host.getHostName());
                                log(host.getHostName());
                            } catch (NoSuchMethodException ex) {
                            } catch (IllegalAccessException ex) {
                            } catch (InvocationTargetException ex) {
                            }
                        }
                        */
                        return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
                    }
                };

                SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(false).setTcpNoDelay(true).build();
                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(1500)
                        .setConnectionRequestTimeout(5000)
                        .setSocketTimeout(1000)
                        .setMaxRedirects(15)
                        .setContentCompressionEnabled(true)
                        .setCircularRedirectsAllowed(true)
                        .setRedirectsEnabled(true)
                        .setRelativeRedirectsAllowed(true)
                        .build();
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setSSLSocketFactory(sslsf)
                        .setDefaultRequestConfig(config)
                        .setDefaultSocketConfig(socketConfig)
                        .build();
                CloseableHttpResponse responseData = httpclient.execute(new HttpGet( url ));
                try {
                    log( "StatusLine: " + responseData.getStatusLine().toString() );
                    log( "Protocol Version: " + responseData.getProtocolVersion() );
                    final HttpEntity entity = responseData.getEntity();
                    final InputStream is = entity.getContent();

                   // EntityUtils.consume(responseData.getEntity());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8); // "iso-8859-1"
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    String resString = sb.toString(); // Result is here

                    //is.close(); // Close the stream


                    // resposta do proxy.
                    response.getWriter().write(resString);


                } finally {
                    responseData.close();
                }



            }


            if (clientversion == 2) {

                // Apache version
                log("org.apache httpClient version");
                log("Remote user:" + request.getRemoteUser());

                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(1500)
                        .setConnectionRequestTimeout(5000)
                        .setSocketTimeout(1000)
                        .setMaxRedirects(15)
                        .setContentCompressionEnabled(true)
                        .setCircularRedirectsAllowed(true)
                        .setRedirectsEnabled(true)
                        .setRelativeRedirectsAllowed(true)
                        .build();

                CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("Content-Type","text/html");
                httpget.setHeader("Accepted-Encoding","utf-8");

                try {

                    CloseableHttpResponse responseData = httpclient.execute(httpget);
                    final HttpEntity entity = responseData.getEntity();
                    final InputStream is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8); // "iso-8859-1"
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    String resString = sb.toString(); // Result is here

                    is.close(); // Close the stream


                    // resposta do proxy.
                    response.getWriter().write(resString);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(500);
                    response.getWriter().write("[ERROR]: " + e.getMessage() );
                }


            }
        } else {
            response.setStatus(405);
            response.getWriter().write("Parameter needed not found.");
            response.sendError(405,"Parameter needed not found.");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String protocol = request.getProtocol();
        response.getWriter().write("POST Method!");

        //response.getWriter().write( SunnyProxy.class.getClassLoader().getResource("src/main/resources/custom-cacerts.jks").toString() );
    }

    public void destroy() { }

    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            System.out.println("allHostsValid: " + hostname);
            return true;
        }
    };


}
