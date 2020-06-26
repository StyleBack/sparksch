package com.doschool.ahu.configfile;

/**
 * Created by X on 2018/7/19
 *
 * code编码配置
 */
public class CodeConfig {


    public static final String IM_BBT_INFO = "这是一只棒棒糖。";
    public static final String IM_BBT_MSG="送出一只棒棒糖";
    public static final String IM_BBT_URL="https://alicdn.dobell.me/icon/lollipop.png";
    public static final String IM_SEND_MSG="我关注了你哟。";
    public static final String QQ_LINE="2984776507";//官方qq
    public static final String IM_RELOGIN="重新登录";

    public static final String MSSP_ID="6014637";//百度广告位ID，代码位错误会导致无法请求到广告


    //微博type
    public static final int BLOG_NEW=1;//最新微博,主界面加载使用
    public static final int BLOG_HOT=2;//热门微博,热门榜使用
    public static final int BLOG_FOLLOW=3;//关注对象的微博,关注页面使用
    public static final int BLOG_TOPIC=4;//带话题的微博,话题页使用
    public static final int BLOG_SOMEONE=5;//目标用户的微博
    public static final int BLOG_COLLECT=6;//收藏的微博,收藏页使用

    public static final String BLOG_LIKE="1";//点赞
    public static final String BLOG_UNLIKE="0";//取消点赞

    public static final String BLOG_COLL="1";//收藏
    public static final String BLOG_UNCOLL="0";//取消收藏

    public static final int FOLLOWSTATE_NULL=0;//无关系
    public static final int FOLLOWSTATE_FOLLOWED=1;//粉丝
    public static final int FOLLOWSTATE_FOLLOW=2;//关注
    public static final int FOLLOWSTATE_ALL=3;//互相关注

    //微博信息type
    public static final int TYPE_MICROBLOG_AT=1;//(1, "host是微博id", "在动态中@你了")
    public static final int TYPE_MICROBLOG_COMMENT=2;//(2, "host是评论的id", "评论了你的动态")
    public static final int TYPE_MICROBLOG_TRANSMIT=3;//(3, "host是转发的新微博的id", "转发了你的动态")
    public static final int TYPE_MICROBLOG_DELETE=4;//(4, "host是被删除微博的id", "你的动态已被删除")
    public static final int TYPE_COMMENT_AT=5;//(5, "host是评论id", "在评论中@你了")
    public static final int TYPE_COMMENT_COMMENT=6;//(6, "host是新评论+的id", "回复了你的评论")
    public static final int TYPE_COMMENT_DELETE=7;//(7, "host是被删除评论的id", "你的评论已被删除")
    public static final int TYPE_MICROBLOG_ZAN=8;//(8, "host是被赞的微博的id", "赞了你的动态!")
    public static final int TYPE_MICROBLOG_ROOTTRANSMIT=9;//(9, "host是转发的新微博的id", "转发了你的动态")
    public static final int TYPE_COMMENT_ZAN=10;//(10, "host是被赞的评论的id", "赞了你的评论")

    //复用界面 toact 赞  评论
    public static final String MSG_TYPE_PRAISE="praise";
    public static final String MSG_TYPE_COMMENT="comment";

    //回滚
    public static final int SCROLL_CODE=-99;
    public static final int SCROLL_CODE_NEW=-910;
    public static final int SCROLL_CODE_RECOMM=-911;
    public static final int SCROLL_CODE_FOLLOW=-912;

    //服务工具type
    public static final int SERVICE_DIS=1;//发现界面
    public static final int SERVICE_MINE=2;//我的界面

    public static final int ME_BGIV=91;
    public static final int ME_HDIV=92;

    //关注  粉丝
    public static final int FOLLOW_FS=1;//粉丝
    public static final int FOLLOW_GZ=2;//关注

    //举报
    //类型
    public static final int REPORT_BLOG=1;//举报微博
    public static final int REPORT_COMMENT=2;//举报评论
    public static final int REPORT_USER=3;//举报用户
    //理由类型
    public static final int REPORT_SQDS=1;//色情低俗
    public static final int REPORT_GGSR=2;//广告骚扰
    public static final int REPORT_ZZMG=3;//政治敏感
    public static final int REPORT_YYCB=4;//谣言传播
    public static final int REPORT_QZPQ=5;//欺诈骗钱
    public static final int REPORT_WF=6;//违法（暴力恐怖、违禁品等）

    //后台解密出错返回状态  客户端再次处理
    public static final int ERROE_AES_CODE=-1024;

    //会话未读消息 code
    public static final int CVS_UNRED_CODE=18830;

    //emoj的 行 列
    public static final int EMOJ_ROW_COUNT=4;
    public static final int EMOJ_COLUMN_COUNT=7;


    //blog添加图片
    public static final int ADDBLOG_IV=502;
    //blog添加视频
    public static final int ADDBLOG_VD=503;
    //常用话题
    public static final int COMMTIC_CODE=501;
    //地标
    public static final int LAND_CODE=500;
    //at
    public static final int AT_CODE=10001;
    //图片
    public static final int REQUEST_CODE_CHOOSE=9001;
    //拍摄视频
    public static final int REQUEST_CODE_SHOOT=9002;
    //视频
    public static final int REQUEST_CODE_PH=9003;

    //bos上传的id和key  buckname
    public static final String BOS_AK="0F11289af218bc754f54772fe71b8c32";
    public static final String BOS_SK="DD0c2957539f06c823122282bb998755";
    public static final String BOS_HTTP="bj.bcebos.com";
    public static final String BOS_BUCKET="doschool-database";
    //上传bos位置
    public static final String BOS_BLOG="photos/";
    public static final String BOS_HEAD="HDhead/";
    public static final String BOS_BG="background/";
    public static final String BOS_VIDEO="video/";
    public static final String BOS_VERIFY="verify/";
    public static final int AIR_CARD_CODE=1;
}
