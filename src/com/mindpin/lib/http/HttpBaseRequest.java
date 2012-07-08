package com.mindpin.lib.http;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.mindpin.lib.http.params.PostParam;

public abstract class HttpBaseRequest<TResult> {
    protected HttpUriRequest http_uri_request;
    private HttpResponse response;
    
    // 公共方法，构造一个http_client实例，会自动设置cookie到其中
    final static public DefaultHttpClient get_httpclient_instance() {
        HttpParams params = new BasicHttpParams();
        HttpClientParams.setRedirecting(params, false);
        DefaultHttpClient client = new DefaultHttpClient(params);

        return client;
    }

    // 主方法 send
	public TResult send() {
    	TResult re = null;
    	System.out.println(http_uri_request.getMethod() + " " + http_uri_request.getURI());
    	
    	try {
			response = get_httpclient_instance().execute(http_uri_request);
	        int status_code = response.getStatusLine().getStatusCode();
	
	        switch (status_code) {
	            case HttpStatus.SC_OK:
	                re = success(response);
	                break;
	            default:
	                re = error(status_code, response);
	                break;
	        }
	        
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.http_uri_request.abort();
		}
    	
    	return re;
    }

    // 此方法为 status_code = 200 时 的处理方法，由用户自己定义
    public abstract TResult success(HttpResponse response);
    
    // 此方法为 status_code != 200 时 的处理方法，由用户自己定义
    public abstract TResult error(int status_code, HttpResponse response);

    // ---------------
    
    // GET, DELETE 请求, url参数
    protected String build_params_string(NameValuePair ... nv_pairs) {
    	if(nv_pairs.length == 0){ return ""; }
    	
        String params_string = "?";

        for (NameValuePair pair : nv_pairs) {
        	String name = pair.getName();
        	String value = pair.getValue();
        	
        	try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        	
        	
            params_string += (name + "=" + value + "&");
        }

        return params_string;
    }
    
    // POST, PUT 请求，body参数
    protected HttpEntity build_entity(PostParam ... param) {
        MultipartEntity entity = new MultipartEntity();
        for (PostParam param_file : param) {
            entity.addPart(param_file.get_name(), param_file.get_body());
        }
        return entity;
    }
    
    // 获取 response_text 这个方法只能调用一次，因为流已关闭
    protected String response_text(){
    	String re = "";
    	
    	try {
    		
    		InputStream res_content = response.getEntity().getContent();
			re = IOUtils.toString(res_content);
			res_content.close();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return re;
    }
}