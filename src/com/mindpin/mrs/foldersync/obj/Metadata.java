package com.mindpin.mrs.foldersync.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Metadata {

	// {"bytes":0,"path":"/1/2/3/4","is_dir":true,"contents":[]}
	
	public int bytes;
	public String path;
	public boolean is_dir;
	public List<Metadata> contents;
	
	public Metadata(JsonElement json){
		JsonObject o = json.getAsJsonObject();
		
		this.bytes  = o.get("bytes").getAsInt();
		this.path   = o.get("path").getAsString();
		this.is_dir = o.get("is_dir").getAsBoolean();
		
		if(this.is_dir){
			JsonElement contents = o.get("contents");
			if(contents != null){
				this.contents = new ArrayList<Metadata>();
				for(JsonElement j : contents.getAsJsonArray()){
					this.contents.add(new Metadata(j));
				}
			}
		}
	}
}
