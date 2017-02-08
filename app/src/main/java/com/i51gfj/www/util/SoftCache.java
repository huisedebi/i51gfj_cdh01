package com.i51gfj.www.util;

import android.graphics.Bitmap;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class SoftCache {
	private static  SoftCache cache;// 一个Cache实例      
    private Hashtable hashtable;// 用于Cache内容的存储
    private ReferenceQueue queue;// 垃圾Reference的队列
       
    // 继承SoftReference，使得每一个实例都具有可识别的标识。      
    // 并且该标识与其在HashMap内的key相同。      
    private class  MySoftReference<T> extends SoftReference<T> {

	private String _key = "";
       
       public MySoftReference(T t, ReferenceQueue<? super T> q,String key) {
           super(t, q);      
           _key = key;      
       }      
    }      
       
    // 构建一个缓存器实例      
    @SuppressWarnings("rawtypes")
	private SoftCache() {      
    	hashtable = new Hashtable();
    	queue = new ReferenceQueue();
    }      
       
    // 取得缓存器实例      
    public static SoftCache getInstance() {      
       if (cache == null) {      
           cache = new SoftCache();      
       }      
       return cache;      
    }      
       
    // 以软引用的方式对一个对象的实例进行引用并保存该引用      
    /**缓存对象
     * @param t 带缓存对象
     * @param key
     */
    @SuppressWarnings("unchecked")
	public  <T> void cacheObject(T t,String key) {
       cleanCache();// 清除垃圾引用      
       MySoftReference<T> ref =  new MySoftReference<T>(t, queue, key);    
       hashtable.put(ref._key, ref);
       t = null;
    }      
       
    // 依据所指定的ID号，重新获取相应Object对象的实例      
    /**获取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public  <T> T getObject(String key) {
       // 缓存中是否有该Object实例的软引用，如果有，从软引用中取得。      
    	T t = null;
       if (hashtable.containsKey(key)) {      
    	   MySoftReference<T> ref = (MySoftReference<T>) hashtable.get(key);      
           try {
			t = (T)ref.get();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.errord(e.getMessage());
		}      
       }      
       return t;      
    }      
       
    // 清除那些所软引用的Object对象已经被回收的ObjectRef对象      
    @SuppressWarnings("rawtypes")
	private void cleanCache() {    
    	MySoftReference ref = null;      
       while ((ref = (MySoftReference) queue.poll()) != null) {      
           hashtable.remove(ref._key);      
       }      
    }      
       
    /**
     * 清除Cache内存的全部内容      
     */
    public void clearCache() {      
       cleanCache();      
       hashtable.clear();      
       System.gc();
       System.runFinalization();
    }      
    /**
     * 清除特定cache
     * @param key
     */
    public void deleteCache(String key) {
    	cleanCache();      
    	hashtable.remove(key);      
    	System.gc();
    	System.runFinalization();
    }      
    /**
     * 清除特定cacheBitmap
     * @param key
     */
    public void deleteCacheBitmap(String key) {
    	try {
			if (hashtable.containsKey(key)) {      
			   MySoftReference<Bitmap> ref = (MySoftReference<Bitmap>) hashtable.get(key);
			   Bitmap bitmap = ref.get();
			   Logger.recycleDebugLoggger(bitmap, "SoftCache");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	try {
			hashtable.remove(key);      
			cleanCache();      
			System.gc();
			System.runFinalization();
		} catch (Exception e) {
			e.printStackTrace();
		}      
    }      
}
