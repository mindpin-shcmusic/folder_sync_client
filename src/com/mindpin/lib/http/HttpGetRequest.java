package com.mindpin.lib.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;

public abstract class HttpGetRequest<TResult> extends HttpBaseRequest<TResult> {
    public HttpGetRequest(final String request_url, final NameValuePair ... nv_pairs) {
        String url_with_params = request_url + build_params_string(nv_pairs);
        
        this.http_uri_request = new HttpGet(url_with_params);
    }
}
