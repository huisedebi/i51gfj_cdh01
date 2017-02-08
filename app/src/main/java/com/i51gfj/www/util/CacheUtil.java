package com.i51gfj.www.util;

/*
 * 最好不要用于存储图片等大数据
 * 该存储是放在安装目录*/


/**
 * 数据缓存
 * @author ly
 *1.getInstance获取实例
 *2.功能操作
 */
public class CacheUtil {


	/**
	 * 蛋糕
	 */
	public static String GWC_CAKE = "GWC_CAKE";
	/**
	 * 购物车
	 */
	public static String Cart = "GWC_CACHE";
	/**
	 * 默认地址
	 */
	public static String MY_ADDRESS = "MY_ADDRESS";
	/**
	 * 缓存地址
	 */
	public static String MY_ADDRESS_BUFFER = "MY_ADDRESS_BUFFER";
	private static CacheUtil cacheUtil;
	private final String TAG = "cacheUtil";
	private SoftCache softCache;
	public CacheUtil() {
		softCache = SoftCache.getInstance();
	}
	public static CacheUtil getInstance(){
		if (cacheUtil==null) {
			cacheUtil = new CacheUtil();
		}
		return cacheUtil;
	}
	/**缓存数据
	 * @param cachObj
	 * @param key
	 * @param isCanClear 是否是可清除存储的数据
	 */
	public <T> void  cacheData(T cachObj,String key,boolean isCanClear){
		FileUtils.wrightObjectToFile(cachObj, key, isCanClear);
		softCache.cacheObject(cachObj, key);
	}
	/**获取数据
	 * @param key
	 * @param isCanClear 是否是可清除存储的数据
	 * @return 无时null
	 */
	public <T> T getCacheData(String key,boolean isCanClear){
		T t = null;
		try {
			if (softCache.getObject(key)==null) {
				t= FileUtils.getObjectFromFile(key,isCanClear);
				Logger.errord(TAG, "本地:"+t+"");
				if (t!=null) {
					softCache.cacheObject(t, key);
				}
			}else {
				t=softCache.getObject(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			t=null;
		}
		Logger.LOG(TAG, t+"");
		return t;
	}
	/**获取数据缓存文件大小
	 * @return kb
	 */
	public double getCacheSize(){
		return FileUtils.getCacheFileSise();
	}
	/**清除特定缓存*/
	public void clear(String key,boolean isCanClear){
		softCache.deleteCache(key);
		FileUtils.removeObjectFromFile(key,isCanClear);
	}
	/**
	 * 清除全部缓存
	 */
	public void clear(){
		softCache.clearCache();
		FileUtils.clearCache_obj();
	}
}
