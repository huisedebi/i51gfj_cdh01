
package com.i51gfj.www.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.i51gfj.www.R;


public class LoadingDialog extends DialogFragment
{

	private String mMsg = "加载中···";

	private Dialog dialog;
	public void setMsg(String msg)
	{
		this.mMsg = msg;
	}



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_loading, null);
		TextView title = (TextView) view.findViewById(R.id.id_dialog_loading_msg);
		title.setText(mMsg);
		dialog = new Dialog(getActivity(), R.style.dialog);
		dialog.setContentView(view);
		//设置点击dialog外部，dialog不会消失
		//dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		});
		return dialog;
	}
	public boolean isShowing(){
		return dialog.isShowing();
	}
}

