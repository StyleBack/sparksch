package com.doschool.ahu.widget.component;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by X on 2018/9/14
 */
public class MagicText {

    public static final Pattern PHONE_PATTERN
            = Pattern.compile(                                  // sdd = space, dot, or dash
            "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                    + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
                    + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>
    public static final Pattern WEB_URL_PATTERN
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
    // input.  This is to stop foo.sure from
    // matching as foo.su

    public void magicShow(final View textView) {

        List<Link> linkList = new ArrayList<>();

        Link atlink = new Link(Pattern.compile("\\[a=.*?\\].*?\\[\\/a\\]", Pattern.CASE_INSENSITIVE))
                .setShowTextCallback(new Link.DisplayTextCallback() {

                    @Override
                    public String getDisplayText(String clickedText) {

                        Pattern patternNick = Pattern.compile("].*?\\[/a\\]", Pattern.CASE_INSENSITIVE);
                        Matcher matcherNick = patternNick.matcher(clickedText);
                        String keyNick = null;
                        while (matcherNick.find()) {
                            keyNick = matcherNick.group();
                            keyNick = keyNick.substring(1, keyNick.length() - 4);
                        }
                        return keyNick;
                    }
                })
                .setTextColorCallback(new Link.TextColorCallback() {
                    @Override
                    public int getTextColor(String clickedText) {
                        return textView.getContext().getResources().getColor(R.color.now_txt_color);
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Pattern patternUid = Pattern.compile("\\[a=.*?\\]", Pattern.CASE_INSENSITIVE);
                        Matcher matcherUid = patternUid.matcher(clickedText);
                        Long uid = 0L;
                        while (matcherUid.find()) {
                            String keyUid = matcherUid.group();
                            String temp = keyUid.substring(3, keyUid.length() - 1);
                            uid = Long.valueOf(temp);
                        }
//                        ListenerFactory_Person.gotoHomePage(textView.getContext(), uid.longValue());
                    }
                });


        Link magicLink = new Link(Pattern.compile("<magic=.*?>.*?<\\/magic>", Pattern.CASE_INSENSITIVE))
                .setShowTextCallback(new Link.DisplayTextCallback() {

                    @Override
                    public String getDisplayText(String clickedText) {

                        Pattern pattern = Pattern.compile(">.*?<\\/magic>", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(clickedText);
                        while (matcher.find()) {
                            String key = matcher.group();
                            key = key.substring(1, key.length() - 8);
                            return key;
                        }
                        return "";
                    }
                })
                .setTextColorCallback(new Link.TextColorCallback() {
                    @Override
                    public int getTextColor(String clickedText) {

//                        Pattern pattern = Pattern.compile("<magic=.*?>", Pattern.CASE_INSENSITIVE);
//                        Matcher matcher = pattern.matcher(clickedText);
//                        MagicLink magicLink=null;
//                        while (matcher.find()) {
//                            String key = matcher.group();
//                            key = key.substring(7, key.length() - 1);
//                        }
//                        try {
//                            return Color.parseColor(magicLink.getColor());
//                        }catch (Exception e){
                            return 0;
//                        }

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                        Pattern pattern = Pattern.compile("<magic=.*?>", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(clickedText);
                        while (matcher.find()) {
                            String key = matcher.group();
                            key = key.substring(7, key.length() - 1);
//                            MagicLink magicLink = JsonUtil.Json2T(key, MagicLink.class, new MagicLink());
//                            ListenerFactory.smartMthod(textView.getContext(), magicLink.getDoUrl());
                        }
                    }
                });
//        Link phoneLink = new Link(PHONE_PATTERN)
//                .setOnClickListener(new Link.OnClickListener() {
//                    @Override
//                    public void onClick(String clickedText) {
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clickedText));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        textView.getContext().startActivity(intent);
//                    }
//                });
        Link webLink = new Link(WEB_URL_PATTERN)
                .setShowTextCallback(new Link.DisplayTextCallback() {
                    @Override
                    public String getDisplayText(String clickedText) {
                        return "[网页链接]";
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(final String clickedText) {

                        new AlertDialog.Builder(textView.getContext())
                                .setTitle("打开连接")
                                .setMessage(clickedText)
                                .setNegativeButton("取消",null)
                                .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url=clickedText;
                                        if (!clickedText.startsWith("http://") && !clickedText.startsWith("https://")){
                                            url="https://"+clickedText;
                                        }

                                        Bundle bundle=new Bundle();
                                        bundle.putString("URL",url);
                                        bundle.putInt("code",WebActivity.WEBGO);
                                        IntentUtil.toActivity(textView.getContext(),bundle, WebActivity.class);
                                    }
                                }).create().show();

                    }
                });
        linkList.add(atlink);
        linkList.add(magicLink);
//        linkList.add(phoneLink);
        linkList.add(webLink);

        LinkBuilder.on((TextView) textView)
                .addLinks(linkList)
                .build();

    }
}
