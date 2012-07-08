package com.mindpin.mrs.foldersync.local;

import java.util.HashMap;
import java.util.Set;

import com.mindpin.mrs.foldersync.obj.LocalFileMeta;


public abstract class Putter {

	public HashMap<String, LocalEntry> children;
	
	// remote_path as /foo/bar/abc
	public void put_path(String remote_path, LocalFileMeta meta){
		//System.out.println("remote_path: " + remote_path);
		
		String child_name = remote_path.split("/")[1]; 
		//System.out.println(child_name);
		
		String not_used_path = remote_path.substring(child_name.length() + 1);
		
		LocalEntry old_child = children.get(child_name);
		if( null == old_child ){
			LocalEntry new_child = new LocalEntry();
			_add_meta(new_child, child_name, not_used_path, meta);
			children.put(child_name, new_child);
		}else{
			_add_meta(old_child, child_name, not_used_path, meta);
		}
	}
	
	private void _add_meta(LocalEntry child, String child_name, String not_used_path, LocalFileMeta meta){
		boolean end = not_used_path.equals("");
		
		child.name = child_name;
		
		if(end){
			child.meta = meta;
		}else{
			child.put_path(not_used_path, meta);
		}
	}
	
	public Set<String> children_name_list(){
		return children.keySet();
	}
	
}
