package com.mindpin.mrs.foldersync.local;

import java.util.HashMap;

import com.mindpin.mrs.foldersync.obj.LocalFileMeta;



public class LocalEntry extends Putter {
	public String name;
	public LocalFileMeta meta;
	
	public LocalEntry(){
		this.children = new HashMap<String, LocalEntry>();
		this.meta = new LocalFileMeta("");
	}
}
