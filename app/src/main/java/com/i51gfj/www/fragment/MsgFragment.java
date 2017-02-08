package com.i51gfj.www.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.activity.RongyunLoginActivity;
import com.i51gfj.www.activity.RongyunMainActivity;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MsgFragment extends Fragment {
//    @Override
//    public int getLayout() {
//        return R.layout.fragment_meg;
//    }
//
//    @Override
//    public void initView(View view) {
//
//    }
//
//    @Override
//    public void initData() {
//        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
//        connect(loginData.getToken());
//    }
//
//    /**
//     * 建立与融云服务器的连接
//     *
//     * @param token
//     */
//
//    private void connect(String token) {
//
//        if (getActivity().getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getActivity().getApplicationContext()))) {
//
//
///**
// * IMKit SDK调用第二步,建立与服务器的连接
// */
//
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//
//
//                /**
//                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
//                 */
//
//                @Override
//                public void onTokenIncorrect() {
//
//                    Log.d("LoginActivity", "--onTokenIncorrect");
//                }
//
//
//                /**
//                 * 连接融云成功
//                 * @param userid 当前 token
//                 */
//
//                @Override
//                public void onSuccess(String userid) {
//
//                    Log.d("LoginActivity", "--onSuccess" + userid);
//                    if (RongIM.getInstance() != null)
//                        RongIM.getInstance().startConversationList(getActivity());
//                }
//
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
//                 */
//
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//
//                    Log.d("LoginActivity", "--onError" + errorCode);
//                }
//            });
//        }
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversationlist,container,false);

        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
        return  view;
    }
}
