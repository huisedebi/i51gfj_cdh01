package com.i51gfj.www.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;


public class Logger {


	public static final String MSG_FAIL = "处理失败";
	public static final boolean DEBUG = false;//开启debug模式m

	private static final String DEFAULT_TAG = "DINGZHIJINGLING";

	private static final boolean RECYCLE_DETAIL = true;

	/**v
	 * @param tag String或Object
	 * @param message
	 */
	public static void LOG(Object tag, String message) {

		if (DEBUG) {
			if (tag instanceof String) {
				Log.v((String) tag, getTAG() + "---" + message);
				return;
			}
			String t = tag == null ? DEFAULT_TAG : tag.getClass()
					.getSimpleName();
			Log.v(t, message);
		}

	}
	/**
	 * @param isDebug
	 * @param message
	 */
	public static void LOG(Boolean isDebug, String message) {
		
		if (isDebug) {
				Log.v(DEFAULT_TAG, getTAG() + "---" + message);
				return;
		}
		
	}

	/**
	 * 打印bean对象
	 * @param bean
	 * @return
	 */
	public static String toDataStr(Object bean) {
		if (bean != null) {
			StringBuilder sb = new StringBuilder();
			Class<?> clazz = bean.getClass();
			Field[] fields = clazz.getDeclaredFields();
			sb.append(clazz.getSimpleName());
			sb.append("[");
			for (Field f : fields) {
				sb.append(f.getName());
				sb.append(" = ");
				try {
					f.setAccessible(true);
					sb.append(String.valueOf(f.get(bean)));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				sb.append(",");
			}
			if (sb.lastIndexOf(",") == sb.length() - 1) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			return sb.toString();
		}
		return "";
	}

	/**回收图像
	 * @param bitmap
	 * @param tag
	 */
	public static void recycleDebugLoggger(Bitmap bitmap, String tag) {

		if (true) {
			Log.i("recycle",
					"=====" + (bitmap == null ? "null" : bitmap.toString())
							+ " tag : " + tag);
		}
		if (bitmap != null&&!bitmap.isRecycled()) {
			try {
				bitmap.recycle();
				bitmap = null;
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (RECYCLE_DETAIL) {
			try {
				throw new RuntimeException();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**v 单独页面控制 是否debug
	 * @param debug
	 * @param msg
	 */
	public static void d(boolean debug, String msg) {
		if (debug) {
			Log.v(DEFAULT_TAG, getTAG() + "---" + msg);
		}
	}
	/**v 
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, getTAG() + "---" + msg);
		}
	}
	/**d 单独页面控制 是否debug
	 * @param debug
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, getTAG() + "---" + msg);
		}
	}

	/**i
	 * 默认tag:XINGJIAN
	 */
	public static void logd(String str) {
		if (DEBUG) {
			Log.i(DEFAULT_TAG, getTAG() + "---" + str);
		}
	}

	/**i
	 * @param tag
	 * @param str
	 */
	public static void logd(String tag, String str) {
		if (DEBUG) {
			Log.i(tag, getTAG() + "---" + str);
		}
	}

	/**e
	 * 默认tag
	 */
	public static void errord(String str) {
		if (DEBUG) {
			Log.e(DEFAULT_TAG, getTAG() + "---" + str);
		}
	}

	/**e
	 * @param tag
	 * @param str
	 */
	public static void errord(String tag, String str) {
		if (DEBUG) {
			Log.e(tag, getTAG() + "---" + str);
		}
	}
	/**e
	 * @param isdebug
	 * @param str
	 */
	public static void errord(Boolean isdebug, String str) {
		if (isdebug) {
			Log.e(DEFAULT_TAG, getTAG() + "---" + str);
		}
	}

	/**w
	 * 默认tag
	 */
	public static void mark() {
		if (DEBUG) {
			Log.w(DEFAULT_TAG, getTAG());
		}
	}

	/**w
	 * @param tag
	 * @param str
	 */
	public static void mark(String tag, String str) {
		if (DEBUG) {
			Log.w(tag, getTAG() + "---" + str);
		}
	}

	/**w
	 * 打印详细某位子的详细调用
	 * @param tag
	 */
	public static void traces() {
		if (DEBUG) {
			// StackTraceElement stack[] = (new Throwable()).getStackTrace();
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getClassName() + "." + ste.getMethodName()
						+ "#line=" + ste.getLineNumber() + "的调用：\n");
				for (int i = 4; i < stacks.length && i < 15; i++) {
					ste = stacks[i];
					sb.append((i - 4) + "--" + ste.getClassName() + "."
							+ ste.getMethodName() + "(...)#line:"
							+ ste.getLineNumber() + "\n");
				}
			}
			Log.w(DEFAULT_TAG, getTAG() + "--" + sb.toString());
		}

	}

	/**w
	 * 打印详细某位子的详细调用
	 * @param tag
	 */
	public static void traces(String tag) {
		if (DEBUG) {
			// StackTraceElement stack[] = (new Throwable()).getStackTrace();
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getClassName() + "." + ste.getMethodName()
						+ "#line=" + ste.getLineNumber() + "的调用：\n");
				for (int i = 4; i < stacks.length && i < 15; i++) {
					ste = stacks[i];
					sb.append((i - 4) + "--" + ste.getClassName() + "."
							+ ste.getMethodName() + "(...)#line:"
							+ ste.getLineNumber() + "\n");
				}
			}
			Log.w(tag, getTAG() + "--" + sb.toString());
		}

	}

	/**获取所在父对象及行号
	 * @return
	 */
	public static String getTAG() {
		try {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[4];
				sb.append(ste.getFileName().subSequence(0,
						ste.getFileName().length() - 5)
						+ "." + ste.getMethodName() + "#" + ste.getLineNumber());
			}
			return sb.toString();
		} catch (NullPointerException e) {
			return "PROGUARDED";
		}
	}

	/**i
	 * @param str
	 * @param bytes
	 */
	public static void log(String str, byte[] bytes) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bytes != null) {
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toHexString(bytes[i]));
					if (i != bytes.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}
	/**i
	 * @param tag
	 * @param bytes
	 */
	public static void log(String tag, String str) {
		if (DEBUG) {
			Log.i(tag, getTAG() + "---" + str);
		}
	}
	
	/**i
	 * @param str
	 * @param bs
	 */
	public static void log(String str, boolean[] bs) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bs != null) {
				for (int i = 0; i < bs.length; i++) {
					sb.append(bs[i]);
					if (i != bs.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}
	
	/**e
	 * @param str
	 * @param bs
	 */
	public static void errord(String str, boolean[] bs) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bs != null) {
				for (int i = 0; i < bs.length; i++) {
					sb.append(bs[i]);
					if (i != bs.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.e(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}

	/**i
	 * @param str
	 * @param shorts
	 */
	public static void log(String str, short[] shorts) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (shorts != null) {
				for (int i = 0; i < shorts.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(shorts[i]);
					if (i != shorts.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	/**i
	 * 打印 int数组
	 * @param str
	 * @param ints
	 */
	public static void log(String str, int[] ints) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (ints != null) {
				for (int i = 0; i < ints.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(ints[i]);
					if (i != ints.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	/**i
	 * 打印数组内容
	 * @param str
	 * @param strary
	 */
	public static void log(String str, String[] strary) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (str != null) {
				for (int i = 0; i < strary.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(strary[i]);
					if (i != strary.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}
	
	/**e
	 * 打印错误数组内容
	 * @param str
	 * @param strary
	 */
	public static void errord(String str, String[] strary) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (str != null) {
				for (int i = 0; i < strary.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(strary[i]);
					if (i != strary.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.e(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	/**i 
	 * 打印List内容
	 * @param str
	 * @param list
	 */
	public static void log(String str, List list) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (list != null) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					sb.append(list.get(i).toString());
					if (i != size - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	@SuppressLint("NewApi")
	public static void logStrictModeThread() {
		if (DEBUG && Build.VERSION.SDK_INT >= 10) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyLog().build());
		}
	}

}
