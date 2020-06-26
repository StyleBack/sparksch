package com.doschool.ahu.configfile;

/**
 * Created by X on 2018/9/10
 */
public class ApiConfig {


    public static final String INS_AGREEMENT="https://www.iefu.org/privacy.html";//隐私协议
    public static final String USER_SERVER_CLAUSE="https://www.iefu.org/privacy.html";//用户服务条款

    public static final String HEADLINE_API="https://api.dobell.me";//头条
    public static final String API_APPCONFIG="general/getAppConfig";//获取app配置
    public static final String API_LOGIN="user/login";//登录
    public static final String API_PHONE_LOGIN="user/loginByPhone";//手机号登录、注册
    public static final String API_PHONE_YZM="general/getPhoneCaptcha";//获取短信验证码
    public static final String API_ACDMIC="user/loginByFuncId";//老用户工号密码登录
    public static final String API_BINDPHONE="user/updateUserBindPhone";//绑定手机号
    public static final String API_ADD_USERVER="user/addUserVerify";//添加审核信息
    public static final String API_MINE="user/getUser";//用户信息
    public static final String API_WAITVER="user/getUserVerify";//获取审核的情况界面
    public static final String API_CVI="user/addUrgeNum";//催一下的增加数量
    public static final String API_LOGIN_RECORD="user/getLoginRecord";//获取是不是今天的首次登录，同时进行登录记录
    public static final String API_SCHOOL_DEPART="general/getSchoolDepart";//获取学校院系列表
    public static final String API_TOOL_SERVICE="service/getToolsConfigList";//服务工具
    public static final String API_BLOG_LIST="ugc/getMircoblogList";//微博列表
    public static final String API_BLOG_DETAIL="ugc/getMicroblogDetailByBlogId";//微博详情
    public static final String API_BLOG_COMT_LIST="ugc/getMicroblogCommentList";//评论列表
    public static final String API_COMFIR="general/getSchoolSystemList";//获取学校验证系统
    public static final String API_SYSCAP="academic/getSystemCaptcha";//获取教务系统的验证码图片
    public static final String API_NEXT_SYS="academic/getIdentity";//验证用户身份
    public static final String API_GOTO="user/addUser";//添加用户
    public static final String API_FOLLWERS="user/getFollowList";//获取关注(粉丝列表)
    public static final String API_ADDBLOG="ugc/addMicroblog";//发微博
    public static final String API_ISLIKE="ugc/updateMicroblogLike";//点赞(or取消)
    public static final String API_ISCOLL="ugc/updateMicroblogCollection";//收藏   取消
    public static final String API_DELETEBLOG="ugc/deleteMicroblog";//删除微博
    public static final String API_DELETE_COMM="ugc/deleteMicroblogComment";//删除微博评论
    public static final String API_COMMENTLIKE="ugc/updateMicroblogCommentLike";//评论点赞
    public static final String API_INFOS_MSG="ugc/getMicroblogMessageList";//获取微博@,评论相关消息列表 评论我
    public static final String API_INFOS_LIKE="ugc/getMicroblogLikeList";//获取给自己微博点赞列表
    public static final String API_TOPIC_CARD="ugc/getTopicBoard";//获取热门话题版块
    public static final String API_JOIN_TOPIC="ugc/getTopicJoinedList";//参与的话题
    public static final String API_ADDCOMMENT="ugc/addMircoblogComment";//参与评论
    public static final String API_ARTICLE="activity/getArticleList";//获取头条列表
    public static final String API_SEARCH_RESULT="general/getSearchResult";//获取搜索结果
    public static final String API_UPDATE_USER="user/updateUser";//更新用户信息
    public static final String API_BANNER="ugc/getBannerList";//获取banner列表
    public static final String API_RES_PWD="user/resetPassword";//修改密码
    public static final String API_BANGBANGTANG="user/sendLollipopDaily";//日常送棒棒糖
    public static final String API_ADDFOLLOW="user/updateFollow";//添加(取消)关注
    public static final String API_CLICK_BANNER="activity/addBannerOpenRecord";//banner点击记录
    public static final String API_CLICK_SERVICE="activity/addToolsOpenRecord";//添加服务点击记录
    public static final String API_START="general/getStartPic";//获取闪屏
    public static final String API_REPORT="general/addReport";//添加举报
    public static final String API_LANDMARK="ugc/getMicroblogPlaceList";//获取地标列表
    public static final String API_COMM_TIC="ugc/getHotTopic";//获取常用话题列表
    public static final String API_CHECK_ACCOUNT="user/checkUpdateUserPhone";//检查用户的账号
    public static final String API_CHANGE_PHONE="user/updateUserPhone";//更新验证通过的用户的手机号
    public static final String API_PUSH_RECORD="user/getPushRecordList";//推送历史
    public static final String API_DOT_RED="ugc/getMicroblogMessageCount";//获取评论我的、给我点赞的数量信息
    public static final String API_APP_UPDATE="service/checkAppVersion";//app更新
    public static final String API_CHECK_UPDATE="service/checkAppIsNeedUpdate";//版本检查更新
    public static final String API_REMARK_NAME="user/updateUserRemark";//添加备注/修改备注
    public static final String API_SKIPSCREEN="general/getJumpScreenRecord";//跳屏-获取
    public static final String API_CLICK_SKIP="general/addSplashOpenNumber";//跳屏--添加点击记录
    public static final String API_PUSHOPEN="user/addPushOpenNumber";//推送点击计数
    public static final String API_BROWSE_RECODE="ugc/addBlogBrowseRecord";//微博浏览记录添加
    public static final String API_RECODE_LIST="ugc/getBlogBrowseRecordList";//获取足迹列表
    public static final String API_SERVICE_PRO="service/getMiniPrograms";//获取服务
    public static final String API_VOTE_CLICK="ugc/vote";//投票
    public static final String API_RECOMMEND_LIST="ugc/getRecommendBlogList";//推荐列表
    public static final String API_KACA="ugc/kaCa";//投票-咔擦
    public static final String API_RECOM_MU="ugc/addRecommendBrowseRecord";//添加推荐日记的浏览记录
}
