package br.com.aristotelesmachado;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.servlet.http.*;
import javax.servlet.ServletException;

public class SunnyProxy extends HttpServlet {
    public void init() throws ServletException {
        //System.setProperty("javax.net.ssl.trustStore", "/path/to/my/cacerts");
        //System.setProperty("javax.net.ssl.trustStore","/tmp/keystore.jks");
        //System.out.println( SunnyProxy.class.getClassLoader().getResource("src/main/resources/custom-cacerts.jks").toString() );
        System.out.println("[LOGGER] test");
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/custom-cacerts.jks" );
        System.setProperty("javax.net.ssl.trustStorePassword","changeit");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = request.getParameter("url");
        int ignoressl = 0;
        if ( request.getParameter("ignoressl") != null && request.getParameter("ignoressl").equals("1") ) { ignoressl = 1; } else { ignoressl = 0; }

        if ( url!=null && !url.trim().equals("") ) {
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setInstanceFollowRedirects(true);
            con.setConnectTimeout(1500); // TCP open port.
            con.setReadTimeout(18500);   // Application response.
            con.setUseCaches(true);

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
            int responseCode = con.getResponseCode();

            response.setStatus( con.getResponseCode() );
            response.setContentType( con.getContentType() );
            response.setHeader( "Cache-Control", con.getHeaderField("Cache-Control") );
            response.setHeader( "Pragma", con.getHeaderField("Pragma") );

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                //responseBuffer.append(inputLine);
                response.getWriter().write( inputLine );
            }
            in.close();
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

}
