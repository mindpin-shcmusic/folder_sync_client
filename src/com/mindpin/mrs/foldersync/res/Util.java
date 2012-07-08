package com.mindpin.mrs.foldersync.res;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	// 类似 ruby 语言里的 File.join
	public static String join(String ... strs){
		if(strs.length == 0) return "";
		
	    File f = null;
		for(String str : strs){
			if(null == f){
				f = new File(str);
			}else{
				f = new File(f, str);
			}
		}
		
		return f.getPath();
	}
	
	// 获得某个文件夹下面所有的文件列表
	public static List<File> get_files_recursion(File dir){
		List<File> files = new ArrayList<File>();
		
		for(File child : dir.listFiles()){
			if(child.isDirectory()){
				for(File f : get_files_recursion(child)){ files.add(f); }
			}else{
				files.add(child);
			}
		}
		
		return files;
	}
	
	// 获得某个文件夹下面所有的文件和文件夹列表
	public static List<File> get_files_and_dirs_recursion(File dir){
		List<File> files = new ArrayList<File>();
		
		for(File child : dir.listFiles()){
			files.add(child);
			if(child.isDirectory()){
				for(File f : get_files_and_dirs_recursion(child)){ files.add(f); }
			}
		}
		
		return files;
	}
	
	public static String remote_path(String local_path){
		return local_path
				.substring(R.FOLDER_LOCAL_PATH.length())
				.replace("\\", "/");
	}
	
	public static String local_path(String remote_path){
		return Util.join(R.FOLDER_LOCAL_PATH, remote_path);
	}
	
	public static void local_mkdirs(String remote_path){
		String local_path = local_path(remote_path);
		new File(local_path).mkdirs();
	}
}
