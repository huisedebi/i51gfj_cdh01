package com.i51gfj.www.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.i51gfj.www.manager.ModelManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ShpfUtil {
	public static String mDataBasesName = "ShpfUtil";

	/** 清除相应key的存储. */
	public static void remove(String ShpfKey) {
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
		Editor editor = user.edit();
		editor.remove(ShpfKey);
		editor.apply();
	}

	/**
	 * 清除所有
	 */
	public static void clear() {
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
		Editor editor = user.edit();
		editor.clear();
		editor.apply();
	}

	public static int getIntValue(String ShpfKey) {
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
	
			int value;
		value = user.getInt(ShpfKey, 0);
		return  value;
	}
	public static String getStringValue(String ShpfKey) {
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
		String value;
		value = user.getString(ShpfKey, "");
		return  value;
	}
	public static boolean getBooleanValue(String ShpfKey) {
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
		boolean value;
		value = user.getBoolean(ShpfKey, false);
		return  value;
	}

	/** 存储基本类型 int string boolean float. */
	public static void setValue(String ShpfKey, Object value) {
		if (ShpfKey == null || value == null) {
			return;
		}
		SharedPreferences user = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, 0);
		Editor editor = user.edit();
		if (value instanceof Integer) {
			editor.putInt(ShpfKey, Integer.parseInt(value.toString()));
		} else if (value instanceof Long) {
			editor.putLong(ShpfKey, Long.parseLong(value.toString()));
		} else if (value instanceof Boolean) {
			editor.putBoolean(ShpfKey, Boolean.parseBoolean(value.toString()));
		} else if (value instanceof String) {
			editor.putString(ShpfKey, value.toString());
		} else if (value instanceof Float) {
			editor.putFloat(ShpfKey, Float.parseFloat(value.toString()));
		}
		editor.apply();
	}

	/** 可以存储任何对象map list等. 对象必须实现序列化*/
	public static void setObject(String ShpfKey, Object object) {
		SharedPreferences preferences = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, Context.MODE_PRIVATE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String oAuth_Base64;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			if (object instanceof Bitmap) {
				ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		 ((Bitmap) object).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		 oAuth_Base64 = new String(Base64.encode(outputStream.toByteArray(),
				 Base64.DEFAULT));
			}else
			{
				oos.writeObject(object);
			 oAuth_Base64 = new String(Base64.encode(baos.toByteArray(),
					 Base64.DEFAULT));
			}
			
			Editor editor = preferences.edit();
			editor.putString(ShpfKey, oAuth_Base64);
			editor.apply();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 获取存储的对象. */
	public static <T> T getObject(String ShpfKey) {
		Object object = null;
		SharedPreferences preferences = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, Context.MODE_PRIVATE);
		String string = preferences.getString(ShpfKey, "");
		if (string.equals("")) {
			return null;
		}
		
		byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			ObjectInputStream bis = new ObjectInputStream(bais);
			object = bis.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) object;
	}
	/** 获取bitmap对象. */
	public static Bitmap getBitmap(String ShpfKey) {
		Object object = null;
		SharedPreferences preferences = ModelManager.getInstance().getContext()
				.getSharedPreferences(mDataBasesName, Context.MODE_PRIVATE);
		String string = preferences.getString(ShpfKey, "");
		if (string.equals("")) {
			return null;
		}
		byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		Bitmap bitmap = null;
		try {
			 bitmap = BitmapFactory.decodeStream(bais);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
	/** 获取bitmap的uri */
	public static Uri getBitmapUri(String ShpfKey) {
		Uri uri;
		uri = Uri.parse(MediaStore.Images.Media.insertImage(ModelManager.getInstance().getContext().getContentResolver(), getBitmap(ShpfKey), null, null));
		return uri;
	}


		public static final String MYFIRST = "FIRST";
		public static final String KEY_DISPALY_WIDTH = "WIDTH";
		public static final String KEY_DISPALY_HEIGHT = "HEIGHT";
		public static final String LOGIN = "login";
		public static final String CropBitmap="cropbitmap";
		public static final String BitmapCamera="BitmapCamera";
		public static final String reshfetime="reshfetime";


}
