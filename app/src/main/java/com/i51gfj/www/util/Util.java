package com.i51gfj.www.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.constant.ChineseCalendar;
import com.i51gfj.www.model.UserInfo;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class Util {

    public static int totalUnreadCount = 0;//未读信息
    public static Handler handler;//触发未读消息
    //图片转为bitmap
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            if (url.startsWith("http")) {
                URL iconUrl = new URL(url);
                URLConnection conn = iconUrl.openConnection();
                HttpURLConnection http = (HttpURLConnection) conn;
                int length = http.getContentLength();
                conn.connect();
                // 获得图像的字符流
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, length);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();// 关闭流
            } else {
                File file = new File(url);
                if (file.exists()) {
                    bm = BitmapFactory.decodeFile(url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    //bitmap转为base64
    public static String bitmapToBase64(
            Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    //支付宝
    //MIICWwIBAAKBgQCq9/1IpWh9JrQ+cOBG6+slc8BZtDUoR6psBbv4711dbtvEapsrJZdgPdhbM6QFCtac/hNAmCtJE90loDl54MGlMXT6ZDDj7mdbpfojMyLWLHRULjKXcDtaMmmAqta764KTKiSpzaLsJOdWYrEqJdvwGmB+9YVuB1cVkBaRZZPgNwIDAQABAoGARHAEqaHc08w8dopEnN8vP8y4ALCMXtVhOOCKwDlBDHel7VgnSPxBPjtDci6Xx5TxF+L03fSLzzLuYXNjzo/Ts58FNKZdt6F8boXOBuqH5f7Q8qAqF/3fmpLm2nX0pIf9I2A9L9FkaaCv4eblIvnnJhxVqZ2YEVqW7mY3lKlGshECQQDcINxsQ4Kv8lT/B3hUI81KiCoArkHkve8JOEyU365UWGl7mM8wCuCBUQ7PkWDCZhNu/vrIyxUFUmO4tinwfc9LAkEAxtRTQMyDiuI9udCukuSQoicDTy0X2mFUa0f8UZ3gcTpAMMuESauIW/c2jHB4RGN/JpKA7DV9EXkGGmChN+NjRQJAW9/XP9R5gif5c6vRfASpVTc5mKkiVW80ijuWMvic0RVJiU2BBAM2KqMDNHzvzpM9/zCqyQ2By4PUnS5imL5ZuwJATO6u3+3CICT2GUXDppbF19gckbAzAI6UaYZK/RGHXIhbegXlQ9QZSOxYsPJ7toqBEl2xgLQZln8re11URBm5uQJAO0WUlN3okIOoqlrTRP6TZTb9y6a5MgC3whjhEfq3++SXnsvPfoZM0rCWGLmJTObysMSNv2DOzR8be1WFswT3qg==
    public static String RSA_PRIVATE = "MIICWwIBAAKBgQCq9/1IpWh9JrQ+cOBG6+slc8BZtDUoR6psBbv4711dbtvEapsrJZdgPdhbM6QFCtac/hNAmCtJE90loDl54MGlMXT6ZDDj7mdbpfojMyLWLHRULjKXcDtaMmmAqta764KTKiSpzaLsJOdWYrEqJdvwGmB+9YVuB1cVkBaRZZPgNwIDAQABAoGARHAEqaHc08w8dopEnN8vP8y4ALCMXtVhOOCKwDlBDHel7VgnSPxBPjtDci6Xx5TxF+L03fSLzzLuYXNjzo/Ts58FNKZdt6F8boXOBuqH5f7Q8qAqF/3fmpLm2nX0pIf9I2A9L9FkaaCv4eblIvnnJhxVqZ2YEVqW7mY3lKlGshECQQDcINxsQ4Kv8lT/B3hUI81KiCoArkHkve8JOEyU365UWGl7mM8wCuCBUQ7PkWDCZhNu/vrIyxUFUmO4tinwfc9LAkEAxtRTQMyDiuI9udCukuSQoicDTy0X2mFUa0f8UZ3gcTpAMMuESauIW/c2jHB4RGN/JpKA7DV9EXkGGmChN+NjRQJAW9/XP9R5gif5c6vRfASpVTc5mKkiVW80ijuWMvic0RVJiU2BBAM2KqMDNHzvzpM9/zCqyQ2By4PUnS5imL5ZuwJATO6u3+3CICT2GUXDppbF19gckbAzAI6UaYZK/RGHXIhbegXlQ9QZSOxYsPJ7toqBEl2xgLQZln8re11URBm5uQJAO0WUlN3okIOoqlrTRP6TZTb9y6a5MgC3whjhEfq3++SXnsvPfoZM0rCWGLmJTObysMSNv2DOzR8be1WFswT3qg==";
    public static String SELLER = "3182628273@qq.com";
    public static String PARTNER = "2088121402853640";
    private static final int SDK_PAY_FLAG = 1;

    //加载中
    public static com.i51gfj.www.view.LoadingDialog loadingDialog;

    public static void showLoading(FragmentActivity mActivity) {
        if (loadingDialog == null) {
            loadingDialog = new com.i51gfj.www.view.LoadingDialog(mActivity, "加载中...");
            loadingDialog.show();
        } else {
            loadingDialog.show();
        }
    }
    public static void showLoadingUpload(FragmentActivity mActivity) {
        if (loadingDialog == null) {
            loadingDialog = new com.i51gfj.www.view.LoadingDialog(mActivity, "上传中，请稍等...");
            loadingDialog.show();
        } else {
            loadingDialog.show();
        }
    }

    public static void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.close();
            loadingDialog = null;
        }
    }

    public static boolean need_to_login(FragmentActivity mActivity) {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            AppUtil.is_ry_connet = true;
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            return true;
        } else {
            if (AppUtil.is_logining) {
                return false;
            } else {
                if (loginData.is_remenrber()) {
                    AppUtil.is_logining = true;
                    return false;
                } else {
                    AppUtil.is_ry_connet = true;
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                    return true;
                }
            }
        }
    }


    public static final String[] lunarNumbers = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    public static final String[] lunarMonths = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    public static final String[] lunarDays = {"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "廿十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};

    public static HashMap<Integer, String[]> twelveMonthWithLeapCache = new HashMap<>();


    public static int getMonthLeapByYear(int year) {
        return ChineseCalendar.getMonthLeapByYear(year);
    }

    /**
     * 通过月份的索引获取当月对应的天数
     *
     * @param year        年份
     * @param monthSway   索引从1开始
     * @param isGregorian 是否是公历
     * @return 月份包含的天数
     */
    public static int getSumOfDayInMonth(int year, int monthSway, boolean isGregorian) {
        if (isGregorian) {
            return getSumOfDayInMonthForGregorianByMonth(year, monthSway);
        } else {
            return getSumOfDayInMonthForLunarByMonthSway(year, monthSway);
        }
    }

    /**
     * 获取公历year年month月的天数
     *
     * @param year  年
     * @param month 月，从1开始计数
     * @return 月份包含的天数
     */
    public static int getSumOfDayInMonthForGregorianByMonth(int year, int month) {
        return new GregorianCalendar(year, month, 0).get(Calendar.DATE);
    }

    /**
     * 获取农历year年monthSway月的天数
     *
     * @param year      年
     * @param monthSway 月，包含闰月，如闰五月，monthSway为1代表1月，5代表五月，6代表闰五月
     * @return
     */
    public static int getSumOfDayInMonthForLunarByMonthSway(int year, int monthSway) {
        int monthLeap = ChineseCalendar.getMonthLeapByYear(year);
        int monthLunar = convertMonthSwayToMonthLunar(monthSway, monthLeap);
        return ChineseCalendar.daysInChineseMonth(year, monthLunar);
    }

    public static int getSumOfDayInMonthForLunarByMonthLunar(int year, int monthLunar) {
        return ChineseCalendar.daysInChineseMonth(year, monthLunar);
    }

    /**
     * 根据已知的闰月月份获取monthSway指向的月份天数
     *
     * @param year      年
     * @param monthSway 月，包含闰月，如闰五月，monthSway为1代表1月，5代表五月，6代表闰五月
     * @param monthLeap 闰月，如闰五月则为5
     * @return monthSway指向的月份天数
     */
    public static int getSumOfDayInMonthForLunarLeapYear(int year, int monthSway, int monthLeap) {
        int month = convertMonthSwayToMonthLunar(monthSway, monthLeap);
        return ChineseCalendar.daysInChineseMonth(year, month);
    }

    /**
     * 根据year的阿拉伯数字生成汉字
     *
     * @param year year in number format, e.g. 1970
     * @return year in Hanzi format, e.g. 一九七零年
     */
    public static String getLunarNameOfYear(int year) {

        StringBuilder sb = new StringBuilder();
        int divider = 10;
        int digital = 0;

        while (year > 0) {
            digital = year % divider;
            sb.insert(0, lunarNumbers[digital]);
            year = year / 10;
        }
        sb.append("年");
        return sb.toString();
    }

    /**
     * 获取月份的农历中文
     *
     * @param month month in number format, e.g. 1
     *              month should be in range of [1, 12]
     * @return month in Hanzi format, e.g. 一月
     */
    public static String getLunarNameOfMonth(int month) {
        if (month > 0 && month < 13) {
            return lunarMonths[month - 1];
        } else {
            throw new IllegalArgumentException("month should be in range of [1, 12] month is " + month);
        }
    }

    /**
     * 获取农历的日的中文
     *
     * @param day day in number format, e.g. 1
     *            day should be in range of [1, 30]
     * @return month in Hanzi format, e.g. 初一
     */
    public static String getLunarNameOfDay(int day) {
        if (day > 0 && day < 31) {
            return lunarDays[day - 1];
        } else {
            throw new IllegalArgumentException("day should be in range of [1, 30] day is " + day);
        }
    }

    /**
     * 获取农历的月份的中文，可包含闰月
     *
     * @param monthLeap the leap month
     *                  month should be in range of [0, 12], 0 if not leap
     * @return
     */
    public static String[] getLunarMonthsNamesWithLeap(int monthLeap) {

        if (monthLeap == 0) {
            return lunarMonths;
        }

        if (monthLeap < -12 || monthLeap > 0) {
            throw new IllegalArgumentException("month should be in range of [-12, 0]");
        }

        int monthLeapAbs = Math.abs(monthLeap);

        String[] monthsOut = twelveMonthWithLeapCache.get(monthLeapAbs);
        if (monthsOut != null && monthsOut.length == 13) {
            return monthsOut;
        }

        monthsOut = new String[13];

        System.arraycopy(lunarMonths, 0, monthsOut, 0, monthLeapAbs);
        monthsOut[monthLeapAbs] = "闰" + getLunarNameOfMonth(monthLeapAbs);
        System.arraycopy(lunarMonths, monthLeapAbs, monthsOut, monthLeapAbs + 1, lunarMonths.length - monthLeapAbs);

        twelveMonthWithLeapCache.put(monthLeapAbs, monthsOut);
        return monthsOut;
    }

    /**
     * 农历中，根据闰月、月份，获取月份view应该选择显示的游标值
     *
     * @param monthLunar 小于0为闰月。取值范围是[-12,-1] + [1,12]
     * @param monthLeap  已知的闰月。取值范围是[-12,-1] + 0
     *                   0代表无闰月
     * @return
     */
    public static int convertMonthLunarToMonthSway(int monthLunar, int monthLeap) {

        if (monthLeap > 0) {
            throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
        }

        if (monthLeap == 0) {
            return monthLunar;
        }

        if (monthLunar == monthLeap) {//闰月
            return -monthLunar + 1;
        } else if (monthLunar < -monthLeap + 1) {
            return monthLunar;
        } else {
            return monthLunar + 1;
        }
    }

    /**
     * 农历根据月份的游标值和闰月值，获取月份的值，负值为闰月
     *
     * @param monthSway 在NumberPicker中的value，取值范围[1,12] + 13
     * @param monthLeap 已知的闰月。取值范围是[-12,-1] + 0
     *                  0代表无闰月
     * @return 返回ChineseCalendar中需要的month，如果是闰月，传入负值
     * 返回值的范围是[-12,-1] + [1,12]
     */
    public static int convertMonthSwayToMonthLunar(int monthSway, int monthLeap) {

        if (monthLeap > 0) {
            throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
        }

        if (monthLeap == 0) {
            return monthSway;
        }
        //有闰月
        if (monthSway == -monthLeap + 1) {//闰月
            return monthLeap;
        } else if (monthSway < -monthLeap + 1) {
            return monthSway;
        } else {
            return monthSway - 1;
        }
    }

    /**
     * 农历根据年份和月份游标值，获取月份的值。负值为闰月
     *
     * @param monthSway 农历月份view的游标值
     * @param year      农历年份
     * @return
     */
    public static int convertMonthSwayToMonthLunarByYear(int monthSway, int year) {
        int monthLeap = getMonthLeapByYear(year);
        return convertMonthSwayToMonthLunar(monthSway, monthLeap);
    }

    //超出字数显示头尾
    public static String handleString(String name) {
        if (name != null) {
            if (name.length() > 6) {
                return name.substring(0, 3) + "..." + name.substring(name.length() - 3, name.length());
            } else {
                return name;
            }
        } else {
            return null;
        }
    }


    /**
     * 创建 XML
     */
   /* public static void createdXML(DanmuXmlWrapper danmuXmlWrapper) {
        XmlSerializer serializer = Xml.newSerializer();
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/comments.xml");
            if(!file.exists()){
                file.createNewFile();
            }else {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            // 指定序列化对象输出的位置和编码
            serializer.setOutput(fos, "utf-8");

            // 写开始 <!--?xml version='1.0' encoding='utf-8' standalone='yes' ?-->
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "i");           // <i>

            serializer.startTag(null, "chatserver");
            serializer.text(danmuXmlWrapper.getChatserver());
            serializer.endTag(null, "chatserver");

            serializer.startTag(null, "chatid");
            serializer.text(danmuXmlWrapper.getChatid());
            serializer.endTag(null, "chatid");

            serializer.startTag(null, "mission");
            serializer.text(danmuXmlWrapper.getMission());
            serializer.endTag(null, "mission");

            serializer.startTag(null, "maxlimit");
            serializer.text(danmuXmlWrapper.getMaxlimit());
            serializer.endTag(null, "maxlimit");

            serializer.startTag(null, "source");
            serializer.text(danmuXmlWrapper.getSource());
            serializer.endTag(null, "source");

            if (danmuXmlWrapper.getData() != null && danmuXmlWrapper.getData().size() > 0) {
                for (int i = 0; i < danmuXmlWrapper.getData().size(); i++) {
                    serializer.startTag(null, "d");
                    StringBuffer pstr = new StringBuffer();
                    pstr.append(danmuXmlWrapper.getData().get(i).getTime()+",");
                    pstr.append("1,25,");//这里修改了样式
                  *//*  if("4".equals(danmuXmlWrapper.getData().get(i).getType())){
                        pstr.append("65280,");//green
                    }else if("6".equals(danmuXmlWrapper.getData().get(i).getType())){
                        pstr.append("65535,");//yellow
                    }else if("5".equals(danmuXmlWrapper.getData().get(i).getType())){
                        pstr.append("32896,");//blue
                    }else{
                        pstr.append("16777215,");//white
                    }*//*
                    pstr.append("16777215,");//white
                    pstr.append("1422201001,0,D6673695,757075520");
                    serializer.attribute(null, "p", pstr.toString());
                    serializer.text(danmuXmlWrapper.getData().get(i).getText());
                    serializer.endTag(null, "d");
                }
            }
            serializer.endTag(null, "i");             // </i>
            serializer.endDocument();                       // 结束
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static List<String> cityList = new ArrayList<String>(Arrays.asList
            ("台北市", "高雄市", "基隆市", "台中市", "台南市", "新竹市", "嘉义市", "重庆市", "杭州市", "湖州市", "嘉兴市", "金华市", "丽水市", "宁波市", "衢州市", "绍兴市", "台州市", "温州市", "舟山市", "保山市", "楚雄彝族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "迪庆藏族自治州", "红河哈尼族彝族自治州", "昆明市", "丽江市", "临沧市", "怒江僳僳族自治州", "普洱市", "曲靖市", "文山壮族苗族自治州", "西双版纳傣族自治州", "玉溪市", "昭通市", "阿克苏地区", "阿勒泰地区", "巴音郭楞蒙古自治州", "博尔塔拉蒙古自治州", "昌吉回族自治州", "哈密地区", "和田地区", "喀什地区", "克拉玛依市", "克孜勒苏柯尔克孜自治州", "塔城地区", "吐鲁番地区", "乌鲁木齐市", "伊犁哈萨克自治州", "自治区直辖县级行政区划", "阿里地区", "昌都地区", "拉萨市", "林芝地区", "那曲地区", "日喀则地区", "山南地区", "阿坝藏族羌族自治州", "巴中市", "成都市", "达州市", "德阳市", "甘孜藏族自治州", "广安市", "广元市", "乐山市", "凉山彝族自治州", "泸州市", "眉山市", "绵阳市", "内江市", "南充市", "攀枝花市", "遂宁市", "雅安市", "宜宾市", "资阳市", "自贡市", "安康市", "宝鸡市", "汉中市", "商洛市", "铜川市", "渭南市", "西安市", "咸阳市", "延安市", "榆林市", "长治市", "大同市", "晋城市", "晋中市", "临汾市", "吕梁市", "朔州市", "太原市", "忻州市", "阳泉市", "运城市", "滨州市", "德州市", "东营市", "菏泽市", "济南市", "济宁市", "莱芜市", "聊城市", "临沂市", "青岛市", "日照市", "泰安市", "威海市", "潍坊市", "烟台市", "枣庄市", "淄博市", "果洛藏族自治州", "海北藏族自治州", "海东地区", "海南藏族自治州", "海西蒙古族藏族自治州", "黄南藏族自治州", "西宁市", "玉树藏族自治州", "固原市", "石嘴山市", "吴忠市", "银川市", "中卫市", "阿拉善盟", "巴彦淖尔市", "包头市", "赤峰市", "鄂尔多斯市", "呼和浩特市", "呼伦贝尔市", "通辽市", "乌海市", "乌兰察布市", "锡林郭勒盟", "兴安盟", "鞍山市", "本溪市", "朝阳市", "大连市", "丹东市", "抚顺市", "阜新市", "葫芦岛市", "锦州市", "辽阳市", "盘锦市", "沈阳市", "铁岭市", "营口市", "抚州市", "赣州市", "吉安市", "景德镇市", "九江市", "南昌市", "萍乡市", "上饶市", "新余市", "宜春市", "鹰潭市", "常州市", "淮安市", "连云港市", "南京市", "南通市", "苏州市", "宿迁市", "泰州市", "无锡市", "徐州市", "盐城市", "扬州市", "镇江市", "白城市", "白山市", "长春市", "吉林市", "辽源市", "四平市", "松原市", "通化市", "延边朝鲜族自治州", "长沙市", "常德市", "郴州市", "衡阳市", "怀化市", "娄底市", "邵阳市", "湘潭市", "湘西土家族苗族自治州", "益阳市", "永州市", "岳阳市", "张家界市", "株洲市", "鄂州市", "恩施土家族苗族自治州", "黄冈市", "黄石市", "荆门市", "荆州市", "十堰市", "随州市", "武汉市", "咸宁市", "襄樊市", "孝感市", "宜昌市", "大庆市", "大兴安岭地区", "哈尔滨市", "鹤岗市", "黑河市", "鸡西市", "佳木斯市", "牡丹江市", "七台河市", "齐齐哈尔市", "双鸭山市", "绥化市", "伊春市", "安阳市", "鹤壁市", "焦作市", "开封市", "洛阳市", "漯河市", "南阳市", "平顶山市", "濮阳市", "三门峡市", "商丘市", "新乡市", "信阳市", "许昌市", "郑州市", "周口市", "驻马店市", "保定市", "沧州市", "承德市", "邯郸市", "衡水市", "廊坊市", "秦皇岛市", "石家庄市", "唐山市", "邢台市", "张家口市", "海口市", "三亚市", "省直辖县级行政区划", "安顺市", "毕节地区", "贵阳市", "六盘水市", "黔东南苗族侗族自治州", "黔南布依族苗族自治州", "黔西南布依族苗族自治州", "铜仁地区", "遵义市", "百色市", "北海市", "崇左市", "防城港市", "贵港市", "桂林市", "河池市", "贺州市", "来宾市", "柳州市", "南宁市", "钦州市", "梧州市", "玉林市", "潮州市", "东莞市", "佛山市", "广州市", "河源市", "惠州市", "江门市", "揭阳市", "茂名市", "梅州市", "清远市", "汕头市", "汕尾市", "韶关市", "深圳市", "阳江市", "云浮市", "湛江市", "肇庆市", "中山市", "珠海市", "白银市", "定西市", "甘南藏族自治州", "嘉峪关市", "金昌市", "酒泉市", "兰州市", "临夏回族自治州", "陇南市", "平凉市", "庆阳市", "天水市", "武威市", "张掖市", "福州市", "龙岩市", "南平市", "宁德市", "莆田市", "泉州市", "厦门市", "漳州市", "香港", "澳门", "北京市", "天津市", "安庆市", "蚌埠市", "亳州市", "巢湖市", "池州市", "滁州市", "阜阳市", "合肥市", "淮北市", "淮南市", "黄山市", "六安市", "马鞍山市", "宿州市", "铜陵市", "芜湖市", "宣城市"));

    //{"安庆市","蚌埠市","亳州市","巢湖市","池州市","滁州市","阜阳市","合肥市","淮北市","淮南市","黄山市","六安市","马鞍山市","宿州市","铜陵市","芜湖市","宣城市"},
  /*  {"澳门"},
    {"香港"},*/
    // {"福州市","龙岩市","南平市","宁德市","莆田市","泉州市","厦门市","漳州市"}
    // {"白银市","定西市","甘南藏族自治州","嘉峪关市","金昌市","酒泉市","兰州市","临夏回族自治州","陇南市","平凉市","庆阳市","天水市","武威市","张掖市"},
    // {"潮州市","东莞市","佛山市","广州市","河源市","惠州市","江门市","揭阳市","茂名市","梅州市","清远市","汕头市","汕尾市","韶关市","深圳市","阳江市","云浮市","湛江市","肇庆市","中山市","珠海市"},
    // {"百色市","北海市","崇左市","防城港市","贵港市","桂林市","河池市","贺州市","来宾市","柳州市","南宁市","钦州市","梧州市","玉林市"},
    // {"安顺市","毕节地区","贵阳市","六盘水市","黔东南苗族侗族自治州","黔南布依族苗族自治州","黔西南布依族苗族自治州","铜仁地区","遵义市"},
    // {"海口市","三亚市","省直辖县级行政区划"},
    //{"保定市","沧州市","承德市","邯郸市","衡水市","廊坊市","秦皇岛市","石家庄市","唐山市","邢台市","张家口市"},
    //  {"安阳市","鹤壁市","焦作市","开封市","洛阳市","漯河市","南阳市","平顶山市","濮阳市","三门峡市","商丘市","新乡市","信阳市","许昌市","郑州市","周口市","驻马店市"},
    //{"大庆市","大兴安岭地区","哈尔滨市","鹤岗市","黑河市","鸡西市","佳木斯市","牡丹江市","七台河市","齐齐哈尔市","双鸭山市","绥化市","伊春市"},
    //{"鄂州市","恩施土家族苗族自治州","黄冈市","黄石市","荆门市","荆州市","十堰市","随州市","武汉市","咸宁市","襄樊市","孝感市","宜昌市"},
    // {"长沙市","常德市","郴州市","衡阳市","怀化市","娄底市","邵阳市","湘潭市","湘西土家族苗族自治州","益阳市","永州市","岳阳市","张家界市","株洲市"},
    // {"白城市","白山市","长春市","吉林市","辽源市","四平市","松原市","通化市","延边朝鲜族自治州"},
    // {"常州市","淮安市","连云港市","南京市","南通市","苏州市","宿迁市","泰州市","无锡市","徐州市","盐城市","扬州市","镇江市"},
    //{"抚州市","赣州市","吉安市","景德镇市","九江市","南昌市","萍乡市","上饶市","新余市","宜春市","鹰潭市"},
    //{"鞍山市","本溪市","朝阳市","大连市","丹东市","抚顺市","阜新市","葫芦岛市","锦州市","辽阳市","盘锦市","沈阳市","铁岭市","营口市"},
    //{"阿拉善盟","巴彦淖尔市","包头市","赤峰市","鄂尔多斯市","呼和浩特市","呼伦贝尔市","通辽市","乌海市","乌兰察布市","锡林郭勒盟","兴安盟"},
    // {"固原市","石嘴山市","吴忠市","银川市","中卫市"},
    // {"果洛藏族自治州","海北藏族自治州","海东地区","海南藏族自治州","海西蒙古族藏族自治州","黄南藏族自治州","西宁市","玉树藏族自治州"},
    //{"滨州市","德州市","东营市","菏泽市","济南市","济宁市","莱芜市","聊城市","临沂市","青岛市","日照市","泰安市","威海市","潍坊市","烟台市","枣庄市","淄博市"},
    // {"长治市","大同市","晋城市","晋中市","临汾市","吕梁市","朔州市","太原市","忻州市","阳泉市","运城市"},
    //{"安康市","宝鸡市","汉中市","商洛市","铜川市","渭南市","西安市","咸阳市","延安市","榆林市"},
    // {"阿坝藏族羌族自治州","巴中市","成都市","达州市","德阳市","甘孜藏族自治州","广安市","广元市","乐山市","凉山彝族自治州","泸州市","眉山市","绵阳市","内江市","南充市","攀枝花市","遂宁市","雅安市","宜宾市","资阳市","自贡市"},
    // {"阿里地区","昌都地区","拉萨市","林芝地区","那曲地区","日喀则地区","山南地区"},
    // {"阿克苏地区","阿勒泰地区","巴音郭楞蒙古自治州","博尔塔拉蒙古自治州","昌吉回族自治州","哈密地区","和田地区","喀什地区","克拉玛依市","克孜勒苏柯尔克孜自治州","塔城地区","吐鲁番地区","乌鲁木齐市","伊犁哈萨克自治州","自治区直辖县级行政区划"},
    // {"保山市","楚雄彝族自治州","大理白族自治州","德宏傣族景颇族自治州","迪庆藏族自治州","红河哈尼族彝族自治州","昆明市","丽江市","临沧市","怒江僳僳族自治州","普洱市","曲靖市","文山壮族苗族自治州","西双版纳傣族自治州","玉溪市","昭通市"},
 /*   {"杭州市","湖州市","嘉兴市","金华市","丽水市","宁波市","衢州市","绍兴市","台州市","温州市","舟山市"},
    {"重庆市"},
    {"台北市","高雄市","基隆市","台中市","台南市","新竹市","嘉义市"},*/


    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */

    public static void connect(String token, Activity a) {

        if (a.getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(a.getApplicationContext()))) {


/**
 * IMKit SDK调用第二步,建立与服务器的连接
 */

            RongIM.connect(token, new RongIMClient.ConnectCallback() {


                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */

                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }


                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */

                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--onSuccess" + userid);
                    AppUtil.is_ry_connet = true;
                }


                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }

    public static int getStartResource(String star) {
        int ic;
        switch (star) {
            case "0":
                ic = R.drawable.star0;
                break;
            case "0.5":
                ic = R.drawable.star_0_5;
                break;
            case "1":
                ic = R.drawable.star1;
                break;
            case "1.5":
                ic = R.drawable.star_1_5;
                break;
            case "2":
                ic = R.drawable.star2;
                break;
            case "2.5":
                ic = R.drawable.star_2_5;
                break;
            case "3":
                ic = R.drawable.star3;
                break;
            case "3.5":
                ic = R.drawable.star_3_5;
                break;
            case "4":
                ic = R.drawable.star4;
                break;
            case "4.5":
                ic = R.drawable.star_4_5;
                break;
            case "5":
                ic = R.drawable.star5;
                break;
            default:
                ic = R.drawable.star5;
                break;
        }
        return ic;
    }
}