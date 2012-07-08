package com.mindpin.lib.http.params;

import org.apache.http.entity.mime.content.ContentBody;

// 用于包装文件以及文件MIME类型的小类
public interface PostParam {
    public String get_name();
    public ContentBody get_body();
}