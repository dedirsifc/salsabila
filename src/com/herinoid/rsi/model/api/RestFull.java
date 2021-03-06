/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herinoid.rsi.model.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.herinoid.rsi.util.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author HEWRIE
 */
public class RestFull {
    private static ObjectMapper mapper;

    public static BaseResponse getValidasi(String ipws) throws Exception {
        mapper = new ObjectMapper();
        BaseResponse response = null;
        try {
            System.out.println("validasi get");
            URL url = new URL(ipws + "validasi");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + BasicAuth.auth());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            response = mapper.readValue(br.readLine(), BaseResponse.class);
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static BaseResponse postSimpanResep(String ipws,CreateResepRequest request) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(request);
            HttpPost httpPost = new HttpPost(ipws + "desktop/new");
            StringEntity params = null;
            params = new StringEntity(json);
            httpPost.addHeader("Authorization", "Basic " + BasicAuth.auth());
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(params);
            response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = rd.readLine()) != null) {
                builder.append(str);
            }
            String text = builder.toString();
            if (!Utils.isBlank(text)) {
                System.out.println(text);
                Gson gson = new Gson();
                return gson.fromJson(text, BaseResponse.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseResponse postResepValidasi(String ipws,ResepValidasiRequest request) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(request);
            HttpPost httpPost = new HttpPost(ipws + "validasi");
            StringEntity params = null;
            params = new StringEntity(json);
            httpPost.addHeader("Authorization", "Basic " + BasicAuth.auth());
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(params);
            response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = rd.readLine()) != null) {
                builder.append(str);
            }
            String text = builder.toString();
            if (!Utils.isBlank(text)) {
                System.out.println(text);
                Gson gson = new Gson();
                return gson.fromJson(text, BaseResponse.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static BaseResponse deleteResepValidasi(String ipws,String noresep,String depo) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            String url = ipws + "delete/validasi/"+noresep+"/"+depo;
            
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.addHeader("Authorization", "Basic " + BasicAuth.auth());
            httpDelete.addHeader("content-type", "application/json");
            response = httpClient.execute(httpDelete);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = rd.readLine()) != null) {
                builder.append(str);
            }
            String text = builder.toString();
            if (!Utils.isBlank(text)) {
                System.out.println(text);
                Gson gson = new Gson();
                return gson.fromJson(text, BaseResponse.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static BaseResponse deleteResep(String ipws,String noresep) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            HttpDelete httpDelete = new HttpDelete(ipws + "delete/"+noresep);
            httpDelete.addHeader("Authorization", "Basic " + BasicAuth.auth());
            httpDelete.addHeader("content-type", "application/json");
            response = httpClient.execute(httpDelete);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = rd.readLine()) != null) {
                builder.append(str);
            }
            String text = builder.toString();
            if (!Utils.isBlank(text)) {
                System.out.println(text);
                Gson gson = new Gson();
                return gson.fromJson(text, BaseResponse.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static BaseResponse postDetailPemberianObat(String ipws,CreateObatDetailRequest request) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(request);
            HttpPost httpPost = new HttpPost(ipws + "detail-pemberian-obat");
            StringEntity params = null;
            params = new StringEntity(json);
            httpPost.addHeader("Authorization", "Basic " + BasicAuth.auth());
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(params);
            response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = rd.readLine()) != null) {
                builder.append(str);
            }
            String text = builder.toString();
            if (!Utils.isBlank(text)) {
                System.out.println(text);
                Gson gson = new Gson();
                return gson.fromJson(text, BaseResponse.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getNoreg(String ipws,String kodePoli,String kodeDokter) throws Exception {
        mapper = new ObjectMapper();
        String nomer = null;
        try {
            URL url = new URL(ipws + "api/noreg/"+kodePoli+"/"+kodeDokter);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + BasicAuth.auth());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            nomer = br.readLine();
            if(nomer.equals("<!DOCTYPE html>")){
                nomer="wait";
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nomer;
    }

}
