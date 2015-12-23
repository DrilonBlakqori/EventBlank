package com.zagapps.eventblank.fragments;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebViewFragment extends Fragment
{

    private WebView mWebView;
    private RelativeLayout mToolbar;
    private ImageView mBackNavigation;
    private TextView mTitle;
    private ProgressBar mProgressBar;

    public static final String TAG="web_view";
    public static final String URL ="web_view_tag";
    public static final String URL_TYPE ="web_view_url_or_twitter";
    public static final int WEBSITE_URL =0;
    public static final int TWITTER_URL =1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.webview_fragment, container, false);

        int ternaryColor=ModelManager.get(getActivity()).getEvent().getTernaryColor();

        mProgressBar=(ProgressBar)view.findViewById(R.id.web_view_progressBar);
        mProgressBar.setIndeterminate(false);
        mBackNavigation=(ImageView)view.findViewById(R.id.web_view_back_navigation);

        Drawable backNavigator= ContextCompat.getDrawable(getActivity(), R.mipmap.arrow_back);
        backNavigator.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);

        mBackNavigation.setImageDrawable(backNavigator);
        mBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStackImmediate();

            }
        });

        mToolbar=(RelativeLayout)view.findViewById(R.id.web_view_header);
        mToolbar.setBackgroundColor(ModelManager.get(getActivity()).getEvent().getMainColor());

        mTitle=(TextView)view.findViewById(R.id.web_view_title);
        mTitle.setTextColor(ternaryColor);

        mWebView=(WebView)view.findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d(TAG, "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(webSettings, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(webSettings, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(webSettings, getActivity().getFilesDir() + getActivity().getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(webSettings, 1024*1024*8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(webSettings, getActivity().getFilesDir() + getActivity().getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                m6.invoke(webSettings, Boolean.TRUE);

                Log.d(TAG, "Enabled HTML5-Features");
            }
            catch (NoSuchMethodException e) {
                Log.e(TAG, "Reflection fail", e);
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }

        }

        Bundle bundle=getArguments();
        String link=bundle.getString(URL);
        int urlType=bundle.getInt(URL_TYPE, 0);

        mWebView.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onLoadResource(WebView view, String url)
            {
                super.onLoadResource(view, url);
                mTitle.setText(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        mProgressBar.getProgressDrawable().setColorFilter(ModelManager.get(getActivity()).
                getEvent().getSecondaryColor(), PorterDuff.Mode.SRC_IN);



        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE)
                {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
                    mProgressBar.setProgress(progress);
                if(progress == 100)
                {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        switch (urlType)
        {
            case TWITTER_URL:
                mWebView.loadUrl("https://twitter.com/"+link);
                break;
            case WEBSITE_URL:
                mWebView.loadUrl(link);
                break;
        }



        return view;
    }


    public WebView getWebView()
    {
        return mWebView;
    }



}
