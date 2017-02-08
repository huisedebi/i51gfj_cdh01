package com.i51gfj.www.util;

import android.content.Context;


import com.i51gfj.www.manager.ModelManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.HashMap;
import com.nostra13.universalimageloader.utils.StorageUtils;


/**
 * 缓存数据对象到本地文件工具类
 * @author ly
 *
 */
public class FileUtils {

	public static Object lock = new Object();
	/**
	 * 保存对象
	 * @param obj
	 * @param key
	 * @param isCanClear 是否会被clearCache_obj（）方法清掉  注意：获取数据时的此属性必须和保存时值一致 否则取不到数据
	 * @return
	 */
	public  static boolean  wrightObjectToFile(Object obj,String key,boolean isCanClear){
		synchronized(lock)
		{
			boolean isSave = false;
			HashMap<String, Object> hashMap = null;
			ObjectInputStream objectInputStream = null;
			FileInputStream input = null;
			Context context = ModelManager.getInstance().getContext();
			try {
				if (isCanClear) {
					input = context.openFileInput("cache_obj");
				}else {
					input = context.openFileInput("cache_obj_noclear");
				}
				objectInputStream = new ObjectInputStream(input);
				hashMap = (HashMap<String, Object>) objectInputStream.readObject();
			} catch (FileNotFoundException e) {
	//			e.printStackTrace();
				Logger.errord(e.getMessage());
			} catch (IOException e) {
				Logger.errord(e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
	//			e.printStackTrace();
				Logger.errord(e.getMessage());
			}finally{
				try {
					if (objectInputStream!=null) {
						objectInputStream.close();
					}
					if (input!=null) {
						input.close();
					}
				} catch (IOException e) {
	//				e.printStackTrace();
				}
			}
			ObjectOutputStream objout=null;
			FileOutputStream out = null;
			try {
				if (isCanClear) {
					out = context.openFileOutput("cache_obj",
							Context.MODE_PRIVATE);
				}else {
					out = context.openFileOutput("cache_obj_noclear",
							Context.MODE_PRIVATE);
					
				}
				objout = new ObjectOutputStream(out);
				if (hashMap==null) {
					hashMap = new HashMap<String,Object>();
					hashMap.put(key, obj);
				}else {
					hashMap.put(key, obj);
				}
			} catch (FileNotFoundException e1) {
	//			e1.printStackTrace();
				Logger.errord(e1.getMessage());
			} catch (IOException e1) {
	//			e1.printStackTrace();
				Logger.errord(e1.getMessage());
			}
			try {
				objout.writeObject(hashMap);
				isSave =true;
				
			} catch (IOException e) {
				Logger.errord(e.getMessage());
			}finally{
				try {
					if (objectInputStream!=null) {
						objectInputStream.close();
					}
					if (objout!=null) {
						objout.close();
					}
					if (out!=null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return isSave;
		}
	}
	/**获取本地存储对象
	 * @param key （注意：key最好写成final 且全局类里 避免重复key导致数据存储不正常）
	 * @param context
	 * @param isCanClear 是否会被clearCache_obj（）方法清掉  注意：获取数据时的此属性必须和保存时值一致 否则取不到数据
	 * @return
	 */
	public static <T> T getObjectFromFile(String key,boolean isCanClear){
		HashMap<String, Object> hashMap = null;
		ObjectInputStream objectInputStream = null;
		FileInputStream input = null;
		Context context = ModelManager.getInstance().getContext();
		try {
			if (isCanClear) {
				input = context.openFileInput("cache_obj");
			}else {
				input = context.openFileInput("cache_obj_noclear");
			}
			objectInputStream = new ObjectInputStream(input);
			hashMap = (HashMap<String, Object>) objectInputStream.readObject();
		} catch (StreamCorruptedException e) {
			Logger.errord(e.getMessage());
//			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.errord(e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				if (objectInputStream!=null) {
					objectInputStream.close();
				}
				if (input!=null) {
					input.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (hashMap!=null) {
			Object o = hashMap.get(key);
			if (o!=null) {
				return (T) o;
			}
		}
		return null;
		
		
	}
	
	/**获取数据缓存文件及其他图片缓存文件总大小
	 * LoadCacheImage缓存文件fileCache,ImageLoader缓存文件imageloader/Cache,数据缓存文件来自CacheData(cache_obj)
	 * @return kb
	 */
	public static String getAllCacheFileSise(){
		Context context = ModelManager.getInstance().getContext();
		DecimalFormat df = new DecimalFormat("#.000");
		return df.format(getCacheFileSise()+getDirSize(
				StorageUtils.getOwnCacheDirectory(context.getApplicationContext(), "imageloader/Cache")));
	}
	/**获取数据缓存文件大小
	 * @param context
	 * @return KB
	 */
	public static double getCacheFileSise(){
		File file = new File("./data/data/"+PhoneUtils.getPackageName()+"/files/", "cache_obj");
		if (file!=null) {
			if (file.exists() && file.isFile()) {
				return file.length() / 1024;
			}
		}
		return 0.0;
	}
	/**获取目录下总大小
	 * @param file
	 * @return kb
	 */
	public static double getDirSize(File file) {
		if (file==null) {
			return 0.0;
		}
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();
                double size = 0;     
                if (children!=null) {
					for (File f : children)
						size += getDirSize(f);
				}
				return size/1024;     
            } else {
                double size = (double) file.length() / 1024;        
                return size;     
            }     
        } else {     
            Logger.LOG(true,"文件或者文件夹不存在，请检查路径是否正确！");     
            return 0.0;     
        }     
    }  
	/**
	 * 删除某个缓存对象  cache_obj or cache_obj_noclear
	 * @param key
	 * @param context
	 */
	public static void removeObjectFromFile(String key,boolean isCanClear){
		HashMap<String, Object> hashMap = null;
		ObjectInputStream objectInputStream = null;
		FileInputStream input = null;
		Context context = ModelManager.getInstance().getContext();
		try {
			if (isCanClear) {
				input = context.openFileInput("cache_obj");
			}else {
				input = context.openFileInput("cache_obj_noclear");
			}
			objectInputStream = new ObjectInputStream(input);
			hashMap = (HashMap<String, Object>) objectInputStream.readObject();
		} catch (StreamCorruptedException e) {
			Logger.errord(e.getMessage());
//			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		}finally{
			try {
				if (objectInputStream!=null) {
					objectInputStream.close();
				}
				if (input!=null) {
					input.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (hashMap!=null) {
			FileOutputStream out = null;
			ObjectOutputStream objout = null;
			try {
				if (isCanClear) {
					out = context.openFileOutput("cache_obj", Context.MODE_PRIVATE);
				}else {
					out = context.openFileOutput("cache_obj_noclear", Context.MODE_PRIVATE);
				}
				objout = new ObjectOutputStream(out);
				hashMap.remove(key);
				objout.writeObject(hashMap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if (objout!=null) {
						objout.close();
					}
					if (out!=null) {
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	/**
	 * 删除某个缓存对象  cache_obj
	 * @param key
	 * @param context
	 */
	public static void removeObjectFromFile(String key){
		HashMap<String, Object> hashMap = null;
		ObjectInputStream objectInputStream = null;
		FileInputStream input = null;
		Context context = ModelManager.getInstance().getContext();
		try {
			input = context.openFileInput("cache_obj");
			objectInputStream = new ObjectInputStream(input);
			hashMap = (HashMap<String, Object>) objectInputStream.readObject();
		} catch (StreamCorruptedException e) {
			Logger.errord(e.getMessage());
//			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Logger.errord(e.getMessage());
		}finally{
			try {
				if (objectInputStream!=null) {
					objectInputStream.close();
				}
				if (input!=null) {
					input.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (hashMap!=null) {
			FileOutputStream out = null;
			ObjectOutputStream objout = null;
			try {
				out = context.openFileOutput("cache_obj", Context.MODE_PRIVATE);
				objout = new ObjectOutputStream(out);
				hashMap.remove(key);
				objout.writeObject(hashMap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if (objout!=null) {
						objout.close();
					}
					if (out!=null) {
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	/**
	 * 清空缓存文件"cache_obj"
	 * @param context
	 */
	public static void clearCache_obj(){
		FileOutputStream out = null;
		ObjectOutputStream objout = null;
		try {
			Context context = ModelManager.getInstance().getContext();
			out = context.openFileOutput("cache_obj", Context.MODE_PRIVATE);
			objout = new ObjectOutputStream(out);
			objout.writeObject(new HashMap<String, Object>());
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}finally{
			try {
				if (objout!=null) {
					objout.close();
				}
				if (out!=null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static  boolean isDelete;
	/**删除缓存所有目录 
	 * @param dirFile
	 * @return
	 */
	public static boolean deleteDirectory(File dirFile) {
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    isDelete = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	        	isDelete =files[i].delete();  
	            if (!isDelete) break;  
	        } //删除子目录  
	        else {  
	        	isDelete = deleteDirectory(files[i]);  
	            if (!isDelete) break;  
	        }  
	    }  
	    if (!isDelete) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
	/**删除当前路径缓存文件目录下所有文件
	 * @param dirFile 目录
	 * @return
	 */
	public static boolean deleteCache(File dirFile) {
		//如果dir对应的文件不存在，或者不是一个目录，则退出  
		if (!dirFile.exists() || !dirFile.isDirectory()) {  
			return false;  
		}  
		isDelete = true;  
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {  
			//删除子文件  
			if (files[i].isFile()) {  
				isDelete =files[i].delete();  
				if (!isDelete) break;  
			} 
		}  
		return true;
	}
	/**删除当前路径缓存文件
	 * 不删所有目录
	 * @param file 文件
	 * @return
	 */
	public static boolean deleteCacheFile(File file) {
		//如果dir对应的文件不存在，或者不是一个目录，则退出  
		if (!file.exists() || !file.isFile()) {  
			return false;  
		}  
		file.delete();
		return true;
	}
	
	/**写文件
	 * @param path
	 * @param content
	 */
	public static void writeFile(String path, String content) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter writer = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(content, 0, content.length());
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.close();
						writer = null;
					}
					if (fos != null) {
						fos.close();
						fos = null;
					}
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**读文件
	 * @param filePathAndName
	 * @return
	 */
	public static String readFile(String filePathAndName) {
		String fileString = "";
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(filePathAndName);
			if (file.isFile() && file.exists()) {
				inputStreamReader = new InputStreamReader(new FileInputStream(
						file), "UTF-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					fileString += line;
				}
				inputStreamReader.close();
				inputStreamReader = null;
				bufferedReader.close();
				bufferedReader = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return fileString;
	}
}
