package com.doschool.ahu.widget.component;

import android.graphics.Color;
import android.graphics.Typeface;

import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseApplication;

import java.util.regex.Pattern;

/**
 * Created by X on 2018/9/14
 */
public class Link {
    public static final int DEFAULT_COLOR = BaseApplication.getContext().getResources().getColor(R.color.now_txt_color);
    private static final float DEFAULT_ALPHA = .20f;

    private String text;
    private String prependedText;
    private String appendedText;
    private Pattern pattern;
    private int textColor = 0;
    private float highlightAlpha = DEFAULT_ALPHA;
    private boolean underlined = false;
    private boolean bold = false;
    private Typeface typeface;
    private DisplayTextCallback showTextCallback;
    private TextColorCallback textColorCallback;

    private OnClickListener clickListener;
    private OnLongClickListener longClickListener;

    /**
     * Copy Constructor.
     *
     * @param link what you want to base the new link off of.
     */
    public Link(Link link) {
        this.text = link.getText();
        this.prependedText = link.getPrependedText();
        this.appendedText = link.getAppendedText();
        this.pattern = link.getPattern();
        this.clickListener = link.getClickListener();
        this.longClickListener = link.getLongClickListener();
        this.textColor = link.getTextColor();
        this.highlightAlpha = link.getHighlightAlpha();
        this.underlined = link.isUnderlined();
        this.bold = link.isBold();
        this.typeface = link.getTypeface();
        this.showTextCallback = link.getShowTextCallback();
        this.textColorCallback=link.getTextColorCallback();
    }

    /**
     * Construct a new Link rule to match the text.
     *
     * @param text Text you want to highlight.
     */
    public Link(String text) {
        this.text = text;
        this.pattern = null;
    }

    /**
     * Construct a new Link rule to match the pattern.
     *
     * @param pattern pattern of the different texts you want to highlight.
     */
    public Link(Pattern pattern) {
        this.pattern = pattern;
        this.text = null;
    }

    /**
     * Specify the text you want to match.
     *
     * @param text to match.
     * @return the current link object.
     */
    public Link setText(String text) {
        this.text = text;
        this.pattern = null;
        return this;
    }

    /**
     * This text will be added *icon_lib_star_empty* any matches.
     *
     * @param text to place icon_lib_star_empty the link's text.
     * @return the current link object.
     */
    public Link setPrependedText(String text) {
        this.prependedText = text;
        return this;
    }

    /**
     * This text will be added *icon_lib_star* any matches.
     *
     * @param text to place icon_lib_star the link's text.
     * @return the current link object.
     */
    public Link setAppendedText(String text) {
        this.appendedText = text;
        return this;
    }

    /**
     * Specify the pattern you want to match.
     *
     * @param pattern to match.
     * @return the current link object.
     */
    public Link setPattern(Pattern pattern) {
        this.pattern = pattern;
        this.text = null;
        return this;
    }

    /**
     * Specify what happens with a short click.
     *
     * @param clickListener action for the short click.
     * @return the current link object.
     */
    public Link setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * Specify what happens with a long click.
     *
     * @param longClickListener action for the long click.
     * @return the current link object.
     */
    public Link setOnLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    /**
     * Specify the text color for the linked text.
     *
     * @param color as an integer (not resource).
     * @return the current link object.
     */
    public Link setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    /**
     * Specify whether you want it underlined or not.
     *
     * @param underlined
     * @return the current link object.
     */
    public Link setUnderlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Specify whether you want it bold or not.
     *
     * @param bold
     * @return the current link object.
     */
    public Link setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * Specify the alpha of the links background when the user clicks it.
     *
     * @param alpha
     * @return the current link object.
     */
    public Link setHighlightAlpha(float alpha) {
        this.highlightAlpha = alpha;
        return this;
    }

    public Link setTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public String getText() {
        return text;
    }

    public String getPrependedText() {
        return prependedText;
    }

    public String getAppendedText() {
        return appendedText;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getHighlightAlpha() {
        return highlightAlpha;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public boolean isBold() {
        return bold;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public OnLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public DisplayTextCallback getShowTextCallback() {
        return showTextCallback;
    }

    public Link setShowTextCallback(DisplayTextCallback showTextCallback) {
        this.showTextCallback = showTextCallback;
        return this;
    }

    public Link setTextColorCallback(TextColorCallback textColorCallback) {
        this.textColorCallback = textColorCallback;
        return this;
    }
    public TextColorCallback getTextColorCallback() {
        return textColorCallback;
    }


    public String getShowTextSmart() {
        if (showTextCallback == null) {
            return text;
        } else {
            return getShowTextCallback().getDisplayText(getText());
        }
    }

    public int getTextColorSmart() {
        if (textColorCallback == null || textColorCallback.getTextColor(getText())==0) {
            return textColor;
        } else {
            return getTextColorCallback().getTextColor(getText());
        }
    }


    /**
     * Interface to manage the single clicks.
     */
    public interface OnClickListener {
        void onClick(String clickedText);
    }

    /**
     * Interface to manage the long clicks.
     */
    public interface OnLongClickListener {
        void onLongClick(String clickedText);
    }

    public interface TextColorCallback {
        int getTextColor(String clickedText);
    }
    public interface DisplayTextCallback {
        String getDisplayText(String clickedText);
    }
}
