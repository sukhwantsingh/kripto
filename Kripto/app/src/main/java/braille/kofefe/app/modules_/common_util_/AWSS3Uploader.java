package braille.kofefe.app.modules_.common_util_;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class AWSS3Uploader {
    public AWSS3Uploader() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });
    }

    public boolean uploadObject(String requestURL, File uploadFile) {

        try {
            URL url = new URL(requestURL);
            byte[] data = Files.toByteArray(uploadFile);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode >= 200 && httpResponseCode < 300) {
                // log success
                return true;
            } else {
                System.out.println("failure " + httpResponseCode);
                return false;
            }
        } catch (IOException ex) {
            // log failure
            ex.printStackTrace();
            return false;
        }
    }
}