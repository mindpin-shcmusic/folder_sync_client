package com.mindpin.mrs.foldersync;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mindpin.lib.http.HttpDeleteRequest;
import com.mindpin.lib.http.HttpGetRequest;
import com.mindpin.lib.http.HttpPostRequest;
import com.mindpin.lib.http.HttpPutRequest;
import com.mindpin.lib.http.params.PostParamFile;
import com.mindpin.lib.http.params.PostParamText;
import com.mindpin.mrs.foldersync.obj.Delta;
import com.mindpin.mrs.foldersync.obj.Metadata;
import com.mindpin.mrs.foldersync.res.R;
import com.mindpin.mrs.foldersync.res.Util;

public class APIClient {
	
	private void _mkdir_when_download(String remote_path){
		String file_local_path = Util.local_path(remote_path);
		File dir = new File(file_local_path).getParentFile();
		dir.mkdirs();
	}
	
	private void _write_file(HttpResponse response, String remote_path){
		InputStream res_content = null;
		FileOutputStream output = null;
		byte[] buffer = new byte[1024];
		
		try {
			res_content = response.getEntity().getContent();
			output = new FileOutputStream(Util.local_path(remote_path));
						
            for (int length; (length = res_content.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (res_content != null) try { res_content.close(); } catch (IOException logOrIgnore) {}
		}
	}
	
	// ---------------------
	
	public void get_file(final String remote_path){	
		String url = R.FILE_GET_URL + remote_path;
		url = url.replace(" ", "%20");
		
		new HttpGetRequest<Void>(url){

			@Override
			public Void success(HttpResponse response) {
				System.out.println("DOWNLOAD: " + remote_path);
				_mkdir_when_download(remote_path);
				_write_file(response, remote_path);
				return null;
			};

			@Override
			public Void error(int status_code, HttpResponse response) {
				System.out.println(status_code + ": " + response_text());
				return null;
			}
			
		}.send();
	}
	
	public void put_file(String remote_path, File file){
		String url = R.FILE_PUT_URL + remote_path;
		url = url.replace(" ", "%20");
		
		new HttpPutRequest<Boolean>(url,
				new PostParamFile("file", file.getPath())
			) {

			@Override
			public Boolean success(HttpResponse response) {
				System.out.println(response_text());
				return null;
			}

			@Override
			public Boolean error(int status_code, HttpResponse response) {
				System.out.println(status_code + ": " + response_text());
				return null;
			}
			
		}.send();
	}
	
	public Delta delta(int cursor){
		String url = R.DELTA_URL;
		
		return new HttpGetRequest<Delta>(url//, 
			//new BasicNameValuePair("count", "5")
		) {

			@Override
			public Delta success(HttpResponse response) {
				String json_str = response_text();
				System.out.println(json_str);
				
				JsonElement delta_json = new Gson().fromJson(json_str, JsonElement.class);
				
				Delta delta = new Delta(delta_json);
				
				//System.out.println(delta.entries.get(0).path);
				
				return delta;
			}

			@Override
			public Delta error(int status_code, HttpResponse response) {
				System.out.println(status_code + " " + response_text());
				return null;
			}
		}.send();
	}
	
	public Metadata create_folder(String remote_path){		
		return new HttpPostRequest<Metadata>(R.CREATE_FOLDER_URL,
			new PostParamText("path", remote_path)
		) {

			@Override
			public Metadata success(HttpResponse response) {
				String json_str = response_text();
				System.out.println("文件夹资源远程创建成功: " + json_str);
				
				JsonElement meta_json = new Gson().fromJson(json_str, JsonElement.class);
				Metadata meta = new Metadata(meta_json);
				
				return meta;
			}

			@Override
			public Metadata error(int status_code, HttpResponse response) {
				System.out.println(status_code + " " + response_text());
				return null;
			}
		}.send();
	}
	
	public Metadata delete(String remote_path){
		return new HttpDeleteRequest<Metadata>(R.DELETE_URL,
			new BasicNameValuePair("path", remote_path)
		) {

			@Override
			public Metadata success(HttpResponse response) {
				String json_str = response_text();
				System.out.println("资源远程删除成功: " + json_str);
				
				JsonElement meta_json = new Gson().fromJson(json_str, JsonElement.class);
				Metadata meta = new Metadata(meta_json);
				
				return meta;
			}

			@Override
			public Metadata error(int status_code, HttpResponse response) {
				System.out.println(status_code + " " + response_text());
				return null;
			}
		}.send();
	}
}
