package com.zagapps.eventblank.models;


public class Texts
{
    private int mIDText;
    private String mTitle;
    private String mContent;

    public Texts(int IDText, String title, String content)
    {
        mIDText = IDText;
        mTitle = title;
        mContent = content;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getContent()
    {
        return mContent;
    }

    public void setContent(String content)
    {
        mContent = content;
    }
}
