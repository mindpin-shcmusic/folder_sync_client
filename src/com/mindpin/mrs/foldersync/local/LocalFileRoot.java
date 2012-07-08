package com.mindpin.mrs.foldersync.local;

import java.util.HashMap;
import java.util.Map.Entry;

import com.mindpin.mrs.foldersync.obj.LocalFileMeta;



public class LocalFileRoot extends Putter {

	static class LocalFileRootHolder{
		static LocalFileRoot instance = new LocalFileRoot();
	}
	
	public static LocalFileRoot get_instance(){
		return LocalFileRootHolder.instance;
	}
	
	public LocalFileRoot(){
		this.children = new HashMap<String, LocalEntry>();
	}
	
	public static void main(String[] args) {
		LocalFileRoot root = LocalFileRoot.get_instance();
		root.put_path("/foo/bar/abc", new LocalFileMeta("/foo/bar/abc"));
		root.put_path("/foo/bar/abc2/txt", new LocalFileMeta("/foo/bar/abc"));
		root.put_path("/haha/bak", new LocalFileMeta("/foo/bar/abc"));
		System.out.println("------");
		for(Entry<String, LocalEntry> entry : root.children.entrySet()){
			System.out.println(entry.getKey() + ", " + entry.getValue() + entry.getValue().meta);
			_r(entry.getValue());
		}
	}
	
	private static void _r(LocalEntry aentry){
		for(Entry<String, LocalEntry> entry : aentry.children.entrySet()){
			System.out.println(entry.getKey() + ", " + entry.getValue() + entry.getValue().meta);
			_r(entry.getValue());
		}
	}
}
