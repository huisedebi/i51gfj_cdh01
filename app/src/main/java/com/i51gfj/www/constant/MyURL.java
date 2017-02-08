package com.i51gfj.www.constant;

/**
 * 网址接口
 */
public class MyURL {
    /*实际地址http://www.51gfj.com/apidata/index.php?r_type=1&ctl=index&act=index*/
    /*public final static String URL ="http://www.51gfj.com/apidata/";*/
    public final static String URL ="https://www.guangfujin.cn/apidata/";



    //主页
    public static  String URL_INDEX = URL+"index.php?r_type=1&ctl=index&act=index";
    //极光
    public static  String  URL_JPUSH = URL+"index.php?r_type=1&ctl=index&act=jpush";
    //商家详情
    public static  String  URL_SHOP_DETAIL = URL+"index.php?r_type=1&ctl=store&act=index";
    //收藏商家
    public static  String  URL_SHOP_COLLECT = URL+"index.php?r_type=1&ctl=store&act=collect";
    //会员商家信息保存
    public static  String  URL_STORE_SAVE = URL+"index.php?r_type=1&ctl=user&act=storeSave";



    //商家列表
    public static String URL_STORE_LIST= URL+"index.php?r_type=1&ctl=stores";
    //商家评论列表
    public static String URL_SHOP_COMMENT_LIST= URL+"index.php?r_type=1&ctl=dp&act=index";
    //登录
    public static String URL_LOGIN= URL+"index.php?r_type=1&ctl=user&act=login";
    //注册
    public static String URL_REGISTER= URL+"index.php?r_type=1&ctl=user&act=reg";
    //注册验证码
    public static String URL_REG= URL+"index.php?r_type=1&ctl=user&act=reg_sms";
    //忘记密码
    public static String URL_FORGET= URL+"index.php?r_type=1&ctl=user&act=forget";
    //忘记密码验证码
    public static String URL_FORGET_REG= URL+"index.php?r_type=1&ctl=user&act=forget_sms";
    //会员资料
    public static String URL_INFORMATION= URL+"index.php?r_type=1&ctl=user&act=profile";
    //会员修改密码
    public static String URL_CHANGE_PSW= URL+"index.php?r_type=1&ctl=user&act=pwdSave";
    //会员资料保存
    public static String URL_INFORMATION_SAVE= URL+"index.php?r_type=1&ctl=user&act=profileSave";
    //红包列表
    public static String URL_RED_LIST= URL+"index.php?r_type=1&ctl=bonus";
    //会员关注
    public static String URL_MY_COLLECT= URL+"index.php?r_type=1&ctl=user&act=collect";
    //领取红包记录&发放红包记录
    public static String URL_RED_RECORD_LIST= URL+"index.php?r_type=1&ctl=user&act=bonus_log";
    //我的账户
    public static String URL_MY_ACCOUNT= URL+"index.php?r_type=1&ctl=user&act=account_log";
    //我的点评
    public static String URL_MY_COMMENT= URL+"index.php?r_type=1&ctl=user&act=review";
    //反馈意见请求
    public static String URL_FEEDBACK= URL+"index.php?r_type=1&ctl=user&act=feedback";
    //反馈意见保存
    public static String URL_FEEDSAVE= URL+"index.php?r_type=1&ctl=user&act=feedbackSave";
    //个人中心主页
    public static String URL_MINE_INDEX= URL+"index.php?r_type=1&ctl=user&act=index";
    //开通年费会员请求
    public static String URL_MINE_OPEN_HY= URL+"index.php?r_type=1&ctl=user&act=yearVip";
    //开通年费会员提交
    public static String URL_MINE_OPEN_HY_UPLOAD= URL+"index.php?r_type=1&ctl=user&act=yearVip_add";
    //商户信息
    public static String URL_MINE_SHOP_INFO= URL+"index.php?r_type=1&ctl=user&act=store";
    //搜索店铺
    public static String URL_SEARCH_STORE= URL+"index.php?r_type=1&ctl=stores&act=keyword";
    //城市列表
    public static String URL_CITY_LIST= URL+"index.php?r_type=1&ctl=user&act=cityList";
    //零钱充值请求
    public static String URL_CHONGZHI_REQUST= URL+"index.php?r_type=1&ctl=user&act=recharge";
    //红包详情
    public static String URL_DETAIL_RED= URL+"index.php?r_type=1&ctl=bonus&act=info";
    //领取红包
    public static String URL_GET_RED= URL+"index.php?r_type=1&ctl=bonus&act=getBonus";
    //提现申请
    public static String URL_GET_TIXIAN= URL+"index.php?r_type=1&ctl=user&act=withdraw";
    //收支明细
    public static String URL_AMOUNT_LOG= URL+"index.php?r_type=1&ctl=user&act=amount_log";
    //评价提交
    public static String URL_UPLOAD_COMMENT= URL+"index.php?r_type=1&ctl=dp&act=add_dp";
    //会员充值确认支付
    public static String URL_RECHARGE_ADD= URL+"index.php?r_type=1&ctl=user&act=recharge_add";
    //会员头像修改
    public static String URL_HEADIMG_UPLOAD= URL+"index.php?r_type=1&ctl=user&act=headImg";
    //提交红包
    public static String URL_UPLOAD_RED= URL+"index.php?r_type=1&ctl=bonus&act=newBonus";
    //请求发红包
    public static String URL_WANG_RED= URL+"index.php?r_type=1&ctl=bonus&act=before";
    //会员提现验证码
    public static String URL_USER_SMS= URL+"index.php?r_type=1&ctl=user&act=user_sms";
    //提现提交
    public static String URL_DRWA_SUBMIT= URL+"index.php?r_type=1&ctl=user&act=withdrawSubmit";
    //会员营业状态修改
    public static String URL_STORE_STATUS= URL+"index.php?r_type=1&ctl=user&act=storeStatus";
    //昵称和头像
    public static String URL_USER_INFO= URL+"index.php?r_type=1&ctl=user&act=user_info";
    //会员取消关注
    public static String URL_CANCEL_COLLECT= URL+"index.php?r_type=1&ctl=user&act=cancelCollect";
    //更新版本
    public static String URL_UPDATA_VERSION = URL+"index.php?r_type=1&ctl=version&act=index";

}
