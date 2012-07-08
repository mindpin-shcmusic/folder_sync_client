package com.mindpin.mrs.foldersync.obj;

import java.io.File;

public class LocalFileMeta {
	public String path;
	public long modified;
	public boolean is_dir;
	
	public LocalFileMeta(String local_path){
		this.path = local_path;
		
		File file = new File(local_path);
		
		this.modified = file.lastModified();
		this.is_dir = file.isDirectory();
	}
}
