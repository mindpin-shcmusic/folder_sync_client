package com.mindpin.lib.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;

public abstract class HttpDeleteRequest<TResult> extends HttpBaseRequest<TResult> {
    public HttpDeleteRequest(final String request_url, final NameValuePair ... nv_pairs) {
    	String url_with_params = request_url + build_params_string(nv_pairs);
        
        this.http_uri_request = new HttpDelete(url_with_params);
    }
}
