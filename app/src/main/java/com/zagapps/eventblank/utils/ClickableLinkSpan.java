package com.zagapps.eventblank.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;


public abstract class ClickableLinkSpan extends ClickableSpan {
    //TextView textView;
    private final boolean underlineText;
    int color;

    public ClickableLinkSpan(int color, boolean underlineText) {
        super();
        //this.textView =textView;
        this.color = color;
        this.underlineText = underlineText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(underlineText);
        ds.setColor(color);
    }
}
