package com.zagapps.eventblank.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;
import com.zagapps.eventblank.utils.MultiSwipeRefreshLayout;


public class FeedNewsFragment extends Fragment
{

    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private UserTimeline userTimeline;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view=inflater.inflate(R.layout.feed_news_fragment, container, false);
        mSwipeRefreshLayout=(MultiSwipeRefreshLayout)view.findViewById(R.id.feed_news_swipe);
        mListView= (ListView)view.findViewById(R.id.feed_news_list);
        ScrollView emptyList=(ScrollView)view.findViewById(R.id.feed_news_empty_list);


        String name= ModelManager.get(getActivity()).getEvent().getTwitterAdmin();

        userTimeline = new UserTimeline.Builder().screenName(name).includeRetweets(true).
                includeReplies(true).build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        mListView.setAdapter(adapter);
        mListView.setEmptyView(emptyList);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.feed_news_list, R.id.feed_news_empty_list);

        mSwipeRefreshLayout.setOnRefreshListener(new MultiSwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
               refreshItems(adapter);
            }
        });

        return view;
    }

    void refreshItems(TweetTimelineListAdapter adapter)
    {
        adapter.refresh(new Callback<TimelineResult<Tweet>>()
        {
            @Override
            public void success(Result<TimelineResult<Tweet>> result)
            {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception)
            {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

}
