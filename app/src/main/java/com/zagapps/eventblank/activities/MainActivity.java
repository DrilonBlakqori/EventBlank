package com.zagapps.eventblank.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zagapps.eventblank.R;
import com.zagapps.eventblank.adapters.ActivityViewPagerAdapter;
import com.zagapps.eventblank.fragments.ScheduleDetailFragment;
import com.zagapps.eventblank.fragments.SpeakerDetailFragment;
import com.zagapps.eventblank.fragments.WebViewFragment;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.utils.NonSlideViewPager;



public class MainActivity extends AppCompatActivity
{

    public static final String DOWNLOAD_FINISHED ="download_finished";
    public static final String LAST_DATABASE_VERSION="last_database_version";

    NonSlideViewPager mViewPager;
    private TabLayout mTabLayout;
    private static final int[] imageResId={R.drawable.star_empty,R.drawable.calendar_empty,
            R.drawable.feed_empty,R.drawable.speakers_empty,R.drawable.more};


    public  Drawable[] mUnselected =new Drawable[imageResId.length];
    public  Drawable[] mSelected=new Drawable[imageResId.length];

    private int lastTabSeleced;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ModelManager modelManager=ModelManager.get(getApplicationContext());

        Intent intent=getIntent();
        if(intent!=null && intent.getBooleanExtra(DOWNLOAD_FINISHED,false))
        {
            if(intent.getDoubleExtra(LAST_DATABASE_VERSION,0) < modelManager.getMeta().getVersion())
                Toast.makeText(this,"App updated!",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"App is up to date!",Toast.LENGTH_SHORT).show();
        }



        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21)
        {
            int color=modelManager.getEvent().getStatusBarColor();
            getWindow().setStatusBarColor(color);
        }

        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);

         mViewPager= (NonSlideViewPager) findViewById(R.id.view_pager);


        ActivityViewPagerAdapter adapter=new ActivityViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabTextColors(Color.GRAY, modelManager.getEvent().getMainColor());
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.tw__transparent));


        for(int i=0;i<mTabLayout.getTabCount();i++)
        {
            Drawable drawable= ContextCompat.getDrawable(this,imageResId[i]);
            drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            mUnselected[i]=drawable;

            Drawable drawable1= ContextCompat.getDrawable(this,imageResId[i]);
            drawable1.mutate().setColorFilter(modelManager.getEvent().getMainColor(), PorterDuff.Mode.SRC_ATOP);
            mSelected[i]=drawable1;
        }


        mTabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager)
                {
                    @Override
                    public void onTabReselected(TabLayout.Tab tab)
                    {
                        super.onTabReselected(tab);
                        SpeakerDetailFragment speakerDetailFragment=(SpeakerDetailFragment)getSupportFragmentManager().
                                findFragmentByTag(SpeakerDetailFragment.class.getSimpleName());
                        ScheduleDetailFragment scheduleDetailFragment=(ScheduleDetailFragment)getSupportFragmentManager().
                                findFragmentByTag(ScheduleDetailFragment.class.getSimpleName());
                        lastTabSeleced=tab.getPosition();

                        if((speakerDetailFragment!=null && tab.getPosition()==3)
                                || (scheduleDetailFragment!=null&& tab.getPosition()==1))
                        {
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        super.onTabSelected(tab);
                        lastTabSeleced=tab.getPosition();
                        if(mTabLayout!=null && mTabLayout.getTabAt(tab.getPosition())!=null)
                            mTabLayout.getTabAt(tab.getPosition()).setIcon(mSelected[tab.getPosition()]);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab)
                    {
                        super.onTabUnselected(tab);
                            mTabLayout.getTabAt(tab.getPosition()).setIcon(mUnselected[tab.getPosition()]);
                    }
                }
        );


        for(int i=0;i<mTabLayout.getTabCount();i++)
        {
            if(i == 0)
                mTabLayout.getTabAt(i).setIcon(mSelected[i]);
            else
                mTabLayout.getTabAt(i).setIcon(mUnselected[i]);
        }
        runFadeInAnimation();
    }

    private void runFadeInAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.reset();
        mTabLayout.clearAnimation();
        mTabLayout.startAnimation(a);
        mViewPager.clearAnimation();
        mViewPager.startAnimation(a);
    }



    @Override
    public void onBackPressed()
    {
        WebViewFragment webViewFragment = (WebViewFragment)getSupportFragmentManager().
                findFragmentByTag(WebViewFragment.class.getSimpleName());

        SpeakerDetailFragment speakerDetailFragment=(SpeakerDetailFragment)getSupportFragmentManager().
                findFragmentByTag(SpeakerDetailFragment.class.getSimpleName());

        ScheduleDetailFragment scheduleDetailFragment=(ScheduleDetailFragment)getSupportFragmentManager().
                findFragmentByTag(ScheduleDetailFragment.class.getSimpleName());

        if(webViewFragment != null)
        {
            WebView webView=webViewFragment.getWebView();

            if(webView.canGoBack())
            {
                webView.goBack();
                return;
            }
        }
        else
        if(mTabLayout.getSelectedTabPosition()==3 && speakerDetailFragment==null)
        {
            EditText editText= (EditText)findViewById(R.id.speakers_search_edit_text);

            if(editText.isActivated())
            {
                ImageView closeSearch=(ImageView)findViewById(R.id.speakers_search_view_close);
                closeSearch.performClick();
                return;
            }
        }
        else
        if(speakerDetailFragment!=null && scheduleDetailFragment!=null)
        {
            if(lastTabSeleced==1)
            {
                getSupportFragmentManager().popBackStackImmediate(scheduleDetailFragment.getClass().
                        getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            else
            if(lastTabSeleced==3)
            {
                getSupportFragmentManager().popBackStackImmediate(speakerDetailFragment.getClass().
                        getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            return;
        }

        super.onBackPressed();
    }


}
