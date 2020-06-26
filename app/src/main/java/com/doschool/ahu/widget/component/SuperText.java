package com.doschool.ahu.widget.component;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by X on 2018/11/29.
 */
public class SuperText {

    //网页链接
    public static final Pattern WEB_URL
            = Pattern.compile(
            "((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+"   // named host
                    + "(?:"   // plus top level domain
                    + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                    + "|(?:biz|b[abdefghijmnorstvwyz])"
                    + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
                    + "|d[ejkmoz]"
                    + "|(?:edu|e[cegrstu])"
                    + "|f[ijkmor]"
                    + "|(?:gov|g[abdefghilmnpqrstuwy])"
                    + "|h[kmnrtu]"
                    + "|(?:info|int|i[delmnoqrst])"
                    + "|(?:jobs|j[emop])"
                    + "|k[eghimnrwyz]"
                    + "|l[abcikrstuvy]"
                    + "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                    + "|(?:name|net|n[acefgilopruz])"
                    + "|(?:org|om)"
                    + "|(?:pro|p[aefghklmnrstwy])"
                    + "|qa"
                    + "|r[eouw]"
                    + "|s[abcdeghijklmnortuvyz]"
                    + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
                    + "|u[agkmsyz]"
                    + "|v[aceginu]"
                    + "|w[fs]"
                    + "|y[etu]"
                    + "|z[amw]))"
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9])))"
//                    + "|\\.\\.\\."
                    + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$)"); // and finally, a word boundary or end of

    public static void txtlink(Context context,TextView textView){
        List<Link> linkList = new ArrayList<>();
        //at用户
        Link atLink=new Link(Pattern.compile("\\[a=.*?\\].*?\\[\\/a\\]", Pattern.CASE_INSENSITIVE))
                .setShowTextCallback(clickedText -> {
                    Pattern patternNick = Pattern.compile("].*?\\[/a\\]", Pattern.CASE_INSENSITIVE);
                    Matcher matcherNick = patternNick.matcher(clickedText);
                    String keyNick = null;
                    while (matcherNick.find()) {
                        keyNick = matcherNick.group();
                        keyNick = keyNick.substring(1, keyNick.length() - 4);
                    }
                    return keyNick;
                })
                .setOnClickListener(clickedText -> {
                    Pattern patternUid = Pattern.compile("\\[a=.*?\\]", Pattern.CASE_INSENSITIVE);
                    Matcher matcherUid = patternUid.matcher(clickedText);
                    int uid = 0;
                    while (matcherUid.find()) {
                        String keyUid = matcherUid.group();
                        String temp = keyUid.substring(3, keyUid.length() - 1);
                        uid = Integer.parseInt(temp);
                    }
                    if (new LoginDao(context).getObject().getUserDO().getId()!=uid){
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid",uid);
                        IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
                    }
                });

        //跳转网页链接
        Link webLink=new Link(WEB_URL)
                .setShowTextCallback(clickedText -> "[网页链接]")
                .setOnClickListener(clickedText -> {
                    AlertDialog alertDialog=null;
                    AlertDialog.Builder builder=new AlertDialog.Builder(context)
                            .setTitle("打开连接")
                            .setMessage(clickedText)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("打开", (dialog, which) -> {
                                String url=clickedText;
                                if (!clickedText.startsWith("http://") && !clickedText.startsWith("https://")){
                                    url="https://"+clickedText;
                                }
                                Bundle bundle=new Bundle();
                                bundle.putString("URL",url);
                                bundle.putInt("code",WebActivity.WEBGO);
                                IntentUtil.toActivity(context,bundle, WebActivity.class);
                            });
                    alertDialog=builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.un_title_color));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.now_txt_color));
                });
        linkList.add(atLink);
        linkList.add(webLink);
        LinkBuilder.on(textView)
                .addLinks(linkList)
                .build();
    }
}
