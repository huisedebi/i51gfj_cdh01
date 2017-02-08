package com.i51gfj.www.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.i51gfj.www.manager.ModelManager;


public class PhoneUtils {
	/**
	 * 获取本机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getMyPhoneNumber() {
		Context context = ModelManager.getInstance().getContext();
		String tel = "";
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceid = tm.getDeviceId();
			tel = tm.getLine1Number();
			String imei = tm.getSimSerialNumber();
			String imsi = tm.getSubscriberId();
			Logger.errord("tel:" + tel + "; imei:" + imei + "; imsi:" + imsi
					+ "; deviceid:" + deviceid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tel;
	}

	// dip转像素
	public static int dipToPixels(float dip) {
		final float SCALE = ModelManager.getInstance().getContext().getResources()
				.getDisplayMetrics().density;
		float valueDips = dip;
		int valuePixels = (int) (valueDips * SCALE + 0.5f);
		return valuePixels;
	}

	// 像素转dip
	public static float pixelsToDip(int pixels) {

		final float SCALE = ModelManager.getInstance().getContext().getResources()
				.getDisplayMetrics().density;
		float dips = pixels / SCALE;
		return dips;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode() {
		Context context = ModelManager.getInstance().getContext();
		int versionCode = 0;
		try {
			// 获取软件版本号，
			versionCode = context.getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName() {
		Context context = ModelManager.getInstance().getContext();
		String versionName = "";
		try {
			// 获取软件版本号，
			versionName = context.getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取包名
	 * 
	 * @return
	 */
	public static String getPackageName() {
		return ModelManager.getInstance().getContext().getPackageName();
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** 获取屏幕的高度 */
	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
