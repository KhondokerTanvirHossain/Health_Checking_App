package com.example.v.bluetooth;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class QeryUtils {
    static private URL url = null;
    static private JSONObject jsonresponse = null;
    private QeryUtils(){

    }
    static JSONObject makeHttpRequest(String urlString,String bmp, String so2, String tem, Context context){
        try{

            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            if (bmp != null && so2 != null & tem != null){
                jsonresponse = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(URLEncoder.encode("bmp", "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(bmp, "UTF-8"));
                stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode("so2", "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(so2, "UTF-8"));
                stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode("tem", "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(tem, "UTF-8"));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                out.write(stringBuilder.toString());
                out.flush();
                out.close();

            }
            connection.connect();
            jsonresponse = new JSONObject(readJason(connection));

        }catch (MalformedURLException e){
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            return jsonresponse;
        }

    }
    private static String readJason(HttpURLConnection connection) throws IOException{
        BufferedReader br;
        br = new BufferedReader((new InputStreamReader(connection.getInputStream())));
        StringBuilder builder = new StringBuilder("");
        String str = br.readLine();
        while (str != null && str.length() > 0){
            builder.append(str);
            str = br.readLine();
        }
        return new String(builder);
    }
}
