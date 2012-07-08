package com.mindpin.mrs.foldersync.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Delta {
	public List<DeltaEntry> entries;
	public boolean reset;
	public boolean has_more;
	public int cursor; 
	
	public Delta(JsonElement delta_json){
		JsonObject o = delta_json.getAsJsonObject();
		
		this.entries = new ArrayList<DeltaEntry>();
		for(JsonElement delta_entry_json : o.getAsJsonArray("entries")){
			entries.add(new DeltaEntry(delta_entry_json));
		}
		
		this.reset    = o.get("reset").getAsBoolean();
		this.has_more = o.get("has_more").getAsBoolean();
		this.cursor   = o.get("cursor").getAsInt();
	}
	
	public class DeltaEntry{
		public String remote_path;
		public Metadata metadata;
		
		public DeltaEntry(JsonElement delta_entry_json){
			JsonArray a = delta_entry_json.getAsJsonArray();
			
			this.remote_path     = a.get(0).getAsString();
			this.metadata = new Metadata(a.get(1));
		}
	}
}
