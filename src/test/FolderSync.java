package test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mindpin.mrs.foldersync.APIClient;
import com.mindpin.mrs.foldersync.obj.Delta;
import com.mindpin.mrs.foldersync.obj.Delta.DeltaEntry;
import com.mindpin.mrs.foldersync.obj.LocalFileMeta;
import com.mindpin.mrs.foldersync.obj.Metadata;
import com.mindpin.mrs.foldersync.res.R;
import com.mindpin.mrs.foldersync.res.Util;


public class FolderSync {
	
	APIClient client = new APIClient(); 
	int cursor;
	HashMap<String, LocalFileMeta> local_files;
		
	private void test_upload(){
		List<File> files = Util.get_files_recursion(R.FOLDER_LOCAL);
		for(File file : files){
			String path = file.getPath();
			path = path.substring(R.FOLDER_LOCAL_PATH.length());
			path = path.replace("\\", "/");
			
			//System.out.println(path);
			client.put_file(path, file);
		}
	}
	
	private void test_download(){
		List<File> files = Util.get_files_recursion(R.FOLDER_LOCAL);
		
		for(File file : files){
			String path = file.getPath();
			path = path.substring(R.FOLDER_LOCAL_PATH.length());
			path = path.replace("\\", "/");
			
			client.get_file(path);
		}
	}
	
	private void test_delta(){
		client.delta(0);
	}

	
	// -------------------
	private void test_sync(){
		this.local_files = new HashMap<String, LocalFileMeta>();
		this.cursor = 0;
		
		while(true){
			//cursor = remote_delta(cursor);
			check_local_modified();
			check_local_removed();
			
			try {
				Thread.sleep(5000);
				System.out.println(".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int remote_delta(int cursor){
		Delta delta = this.client.delta(cursor);
		
		for(DeltaEntry delta_entry : delta.entries){
			String remote_path = delta_entry.remote_path;
			Metadata meta = delta_entry.metadata;
			
			String local_path = Util.local_path(remote_path);
			
//			unless meta
//	          if File.exists?(local)
//	            log "remove #{local}"
//	            FileUtils.remove_entry(local)
//	            @local_files.reject! { |k,v| k == local }
//	            if File.directory?(local)
//	              @local_files.reject! { |k,v| /^#{local}\/.*/ =~ k }
//	            end
//	          end
//	          next
//	        end
			
			if(meta.is_dir){
				new File(local_path).mkdirs();
			}else{
				System.out.println("DOWNLOAD FILE: " + remote_path);
				client.get_file(remote_path);
			}
			
			this.local_files.put(local_path, new LocalFileMeta(local_path));
		}
		
		return delta.cursor;
	}
	
	private void check_local_modified(){
		// check local modified
		
		for(File file : Util.get_files_and_dirs_recursion(R.FOLDER_LOCAL)){
			System.out.println();
			
			String local_path = file.getPath();
			String remote_path = Util.remote_path(local_path);
			
			if(null == this.local_files.get(local_path)){
				// new file
				LocalFileMeta meta = new LocalFileMeta(local_path);
				this.local_files.put(local_path, meta);
				
				if(meta.is_dir){
					// 创建文件夹
					System.out.println("CREATE UNCAUGHT FOLDER: " + remote_path);
					client.create_folder(remote_path);
				}else{
					// 上传文件
					System.out.println("UPLOAD UNCAUGHT FILE: " + remote_path);
					client.put_file(remote_path, file);
				}
			}else{
				// modified
				if(file.isFile() && file.lastModified() > this.local_files.get(local_path).modified){
					this.local_files.put(local_path, new LocalFileMeta(local_path));
					System.out.println("UPLOAD MODIFIED FILE: " + remote_path);
					client.put_file(remote_path, file);
				}
			}
		}
	}
	
	private void check_local_removed(){
		// check removed
		List<String> removed_local_paths = new ArrayList<String>();
		
		for(Entry<String, LocalFileMeta> entry : this.local_files.entrySet()){
			String local_path = entry.getKey();
			String remote_path = Util.remote_path(local_path);
			
			if(false == new File(local_path).exists()){
				System.out.println("DELETE FILE: " + remote_path);
				client.delete(remote_path);
				removed_local_paths.add(local_path);
			}
		}
		
		for(String removed_local_path : removed_local_paths){
			this.local_files.remove(removed_local_path);
		}
	}
	
	public static void main(String[] args) {
		
		//System.out.println(Config.FOLDER_LOCAL);
		
		FolderSync sync = new FolderSync();
		
		// 先上传
		//sync.test_upload();
		
		// 后下载
		//sync.test_download();
		
		//delta
		//sync.test_delta();
		
		sync.test_sync();
	}
	
}