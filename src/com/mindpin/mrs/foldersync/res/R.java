package com.mindpin.mrs.foldersync.res;

import java.io.File;

public class R {
	
	public static String FOLDER_LOCAL_PATH = "d:\\资源同步盘";
	public static File FOLDER_LOCAL = new File(FOLDER_LOCAL_PATH);
	
	final public static String SITE_URL = "http://192.168.1.26:3000";
	
	//final public static String SITE_URL = "http://192.168.1.13:3000";
	
	final public static String FILE_GET_URL = SITE_URL + "/api/file";
	final public static String FILE_PUT_URL = SITE_URL + "/api/file_put";
	final public static String DELTA_URL    = SITE_URL + "/api/delta";
	
	final public static String CREATE_FOLDER_URL = SITE_URL + "/api/fileops/create_folder";
	final public static String DELETE_URL        = SITE_URL + "/api/fileops/delete";
}
