package com.magic.wogia.huanxin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

 
/** 
 * Http请求工具类 
 * @version v1.0.1 
 */
public class HttpRequestUtil {
    static boolean proxySet = false;
    static String proxyHost = "127.0.0.1";
    static int proxyPort = 8087;
    /** 
     * 编码 
     * @param source 
     * @return 
     */ 
    public static String urlEncode(String source,String encode) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source,encode);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return "0";  
        }  
        return result;  
    }
    public static String urlEncodeGBK(String source) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source,"GBK");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return "0";  
        }  
        return result;  
    }
    /** 
     * 发起http请求获取返回结果 
     * @param req_url 请求地址 
     * @return 
     */ 
    public static String getUserHttpRequest(String req_url,Map<String,String> hearderMap) {
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(req_url);  
             
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            //解决 https SSL 问题
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] tmArr= {new X509TrustManager() {
				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
			}};
            
            sc.init(null, tmArr, new SecureRandom());
            httpUrlConn.setSSLSocketFactory(sc.getSocketFactory());
            
            httpUrlConn.setUseCaches(false);  
            if(hearderMap != null){
            	Set<String> set = hearderMap.keySet();
            	for (String key : set) {
            		httpUrlConn.setRequestProperty(key,hearderMap.get(key));
				}
            }
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            
   
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
   
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
   
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return buffer.toString();  
    }  
    /** 
     * 发起http请求获取返回结果 
     * @param req_url 请求地址 
     * @return 
     */ 
    public static String httpRequest(String req_url) {
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(req_url);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
   
            httpUrlConn.setDoOutput(false);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
   
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
   
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
   
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
   
        } catch (Exception e) {  
            System.out.println(e.getStackTrace());  
        }  
        return buffer.toString();  
    }  
       
    /** 
     * 发送http请求取得返回的输入流 
     * @param requestUrl 请求地址 
     * @return InputStream 
     */ 
    public static InputStream httpRequestIO(String requestUrl) {  
        InputStream inputStream = null;  
        try {  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            // 获得返回的输入流  
            inputStream = httpUrlConn.getInputStream();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return inputStream;  
    }
     
     
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL  所代表远程资源的响应结果
     */
    @SuppressWarnings("unused")
	public static String sendGet(String url, String param,Map<String,String> hearderMap) {
        String result = "";
        BufferedReader in = null;
        try {
        	String urlNameString = url;
        	if(null!=param){
        		urlNameString=urlNameString+ "?" + param;
        	}
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            //解决 https SSL 问题
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] tmArr= {new X509TrustManager() {
				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
			}};
            
            sc.init(null, tmArr, new SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());
            
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if(hearderMap != null){
            	Set<String> set = hearderMap.keySet();
            	for (String key : set) {
            		connection.setRequestProperty(key,hearderMap.get(key));
				}
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
           /* for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 向指定URL发送PUT方法的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    @SuppressWarnings("unused")
	public static String sendPut(String url, String param,Map<String,String> hearderMap) {
	    OutputStreamWriter out = null;
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            //解决 https SSL 问题
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] tmArr= {new X509TrustManager() {
				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
			}};
            
            sc.init(null, tmArr, new SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());
            
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");  
            
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if(hearderMap != null){
            	Set<String> set = hearderMap.keySet();
            	for (String key : set) {
            		connection.setRequestProperty(key,hearderMap.get(key));
				}
            }
            // 建立实际的连接
            connection.connect();
            
            
            
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
           /* for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy 是否使用代理模式
     * @param hearderMap 协议头
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,boolean isproxy,Map<String,String> hearderMap) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpsURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            if(isproxy){//使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                conn = (HttpsURLConnection) realUrl.openConnection(proxy);
            }else{
                conn = (HttpsURLConnection) realUrl.openConnection();
                //解决 https SSL 问题
                SSLContext sc = SSLContext.getInstance("SSL");
                TrustManager[] tmArr= {new X509TrustManager() {
    				
    				@Override
    				public X509Certificate[] getAcceptedIssuers() {
    					return null;
    				}
    				
    				@Override
    				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
    						throws CertificateException {
    					
    				}
    				
    				@Override
    				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
    						throws CertificateException {
    					
    				}
    			}};
                
                sc.init(null, tmArr, new SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
            }
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法
             
            // 设置通用的请求属性
             
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            if(hearderMap != null){
            	Set<String> set = hearderMap.keySet();
            	for (String key : set) {
            		conn.setRequestProperty(key,hearderMap.get(key));
				}
            }
            
            conn.connect();
            
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if(param != null){
            	out.write(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	try {
				System.out.println(conn.getResponseCode());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
    
    
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy 是否使用代理模式
     * @param hearderMap 协议头
     * @return 所代表远程资源的响应结果
     */
    public static String sendDelete(String url, String param,boolean isproxy,Map<String,String> hearderMap) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpsURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            if(isproxy){//使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                conn = (HttpsURLConnection) realUrl.openConnection(proxy);
            }else{
                conn = (HttpsURLConnection) realUrl.openConnection();
                //解决 https SSL 问题
                SSLContext sc = SSLContext.getInstance("SSL");
                TrustManager[] tmArr= {new X509TrustManager() {
    				
    				@Override
    				public X509Certificate[] getAcceptedIssuers() {
    					return null;
    				}
    				
    				@Override
    				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
    						throws CertificateException {
    					
    				}
    				
    				@Override
    				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
    						throws CertificateException {
    					
    				}
    			}};
                
                sc.init(null, tmArr, new SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
            }
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("DELETE");
             
            // 设置通用的请求属性
             
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            if(hearderMap != null){
            	Set<String> set = hearderMap.keySet();
            	for (String key : set) {
            		conn.setRequestProperty(key,hearderMap.get(key));
				}
            }
            
            conn.connect();
            
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if(param != null){
            	out.write(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 DELETE 请求出现异常！"+e);
            e.printStackTrace();
            try {
				return conn.getResponseCode()+"";
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
     
    public static void main(String[] args) {
        Map<String,String> hearderMap = new HashMap<String, String>();
        hearderMap.put("Content-Type","application/json");
        String param = "{\"grant_type\":\"client_credentials\",\"client_id\":\""+EasemobConfig.getInstance().getCLIENT_ID()+"\",\"client_secret\":\""+EasemobConfig.getInstance().getCLIENT_SECRET()+"\"}";
        String sr = HttpRequestUtil.sendPost(EasemobConfig.getInstance().get_SERVICE()+"token",param,false,hearderMap);
        System.out.println(sr);
//        hearderMap.put("Authorization","Bearer "+Const.TOURIST_TOKEN);
//        String param = "{\"username\":\"18782215442\",\"password\":\"123456\"}";
//        String sr= HttpRequestUtil.sendPost(Const._SERVER+"users",param,false,hearderMap);
    }
     
}