package com.mindpin.lib.http;


import org.apache.http.client.methods.HttpPost;

import com.mindpin.lib.http.params.PostParam;

public abstract class HttpPostRequest<TResult> extends HttpBaseRequest<TResult> {
    public HttpPostRequest(final String request_url, final PostParam... param) {
        HttpPost http_post = new HttpPost(request_url);
        http_post.setEntity(build_entity(param));
        
        this.http_uri_request = http_post;
    }
}