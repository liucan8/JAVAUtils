package com.lc.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dicongyan on 2016/11/30.
 *
 */
public class MobileAddressUtil {
    private static final String apiUrl = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=%s";

    /**
     * @return 返回结果
     */
    private String getMobileAdress(String httpPhoneArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String httpUrl = apiUrl + "?phone=" + httpPhoneArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getMobileCity(String httpPhoneArg) throws IOException {
        String url = apiUrl;
        url = String.format(url, httpPhoneArg);
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.getElementsByClass("tdc2");
        String houseAddress = els.get(1).text();

        if(houseAddress.trim().equals("北京 ")||houseAddress.trim().equals("天津 ")
                ||houseAddress.trim().equals("上海 ")||houseAddress.trim().equals("重庆 ")) {
            return houseAddress.replaceAll(" ", "");
        } else {
            return houseAddress.replaceAll(" ", "省");
        }
    }
}
