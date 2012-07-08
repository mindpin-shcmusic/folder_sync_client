package com.mindpin.lib.http;


import org.apache.http.client.methods.HttpPut;

import com.mindpin.lib.http.params.PostParam;

public abstract class HttpPutRequest<TResult> extends HttpBaseRequest<TResult> {
    public HttpPutRequest(final String request_url, final PostParam ... param) {
    	HttpPut http_put = new HttpPut(request_url);
        http_put.setEntity(build_entity(param));
        
        this.http_uri_request = http_put;
    }
}
