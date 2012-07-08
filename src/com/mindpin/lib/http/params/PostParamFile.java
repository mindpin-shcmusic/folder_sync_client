package com.mindpin.lib.http.params;

import java.io.File;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

public class PostParamFile implements PostParam {
    public String param_name;
    public String file_path;

    public PostParamFile(String param_name, String file_path) {
        this.param_name = param_name;
        this.file_path = file_path;
    }

    @Override
    public ContentBody get_body() {
        File file = new File(file_path);
        return new FileBody(file);
    }

    @Override
    public String get_name() {
        return param_name;
    }

}
