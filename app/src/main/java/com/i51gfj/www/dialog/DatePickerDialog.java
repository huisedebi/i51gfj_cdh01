/*
package com.i51gfj.www.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import com.i51gfj.www.R;
import com.i51gfj.www.impl.MyCallBack;

import java.util.Calendar;

*/
/**
 *
 *//*

public class DatePickerDialog extends DialogFragment implements OnClickListener {

    private MyCallBack myCallBack;
    private boolean gregorian;
    private int year;
    private int month;
    private int day;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    private GregorianLunarCalendarView datePicker;
    private String TAG = "加载中···";
    private boolean lunar;
    private Button btn1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        datePicker = (GregorianLunarCalendarView)view.findViewById(R.id.my_view);
        datePicker.initConfigs(Calendar.getInstance(), true);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn1 = (Button) view.findViewById(R.id.btn1);
        if (year != 0){
            datePicker.setTime(year, month, day);
            datePicker.setGregorian(gregorian);
            lunar = !gregorian;
            btn1.setText(lunar ? "切换到公历" : "切换到农历");
        }
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.setTitle("选择生日");
        //设置点击dialog外部，dialog不会消失
        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        return dialog;
    }

    public void setBirthday(int year, int month, int day, boolean gregorian){
        setTime(year, month, day);
        setGregorian(gregorian);
    }

    private void setTime(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    */
/**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     *//*

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                datePicker.setGregorian(lunar);
                lunar = !lunar;
//                btn1.setText("切换公/农历");
                break;
            case R.id.btn2:
                CalendarData calendarData = datePicker.getCalendarData();
                Log.d(TAG, "calendarData G --> y:" + calendarData.getCalendar().get(Calendar.YEAR));
                Log.d(TAG, "calendarData G --> m:" + (calendarData.getCalendar().get(Calendar.MONTH) + 1));
                Log.d(TAG, "calendarData G --> d:" + calendarData.getCalendar().get(Calendar.DAY_OF_MONTH));

                Log.d(TAG, "calendarData L --> y:" + calendarData.getCalendar().get(ChineseCalendar.CHINESE_YEAR));
                Log.d(TAG, "calendarData L --> m:" + (calendarData.getCalendar().get(ChineseCalendar.CHINESE_MONTH)));
                Log.d(TAG, "calendarData L --> d:" + calendarData.getCalendar().get(ChineseCalendar.CHINESE_DATE));
                myCallBack.onCallBack(lunar ? "L" : "G", calendarData);
                dismiss();
                break;
        }
    }

    public void setGregorian(boolean gregorian) {
        this.gregorian = gregorian;
    }
}
*/
