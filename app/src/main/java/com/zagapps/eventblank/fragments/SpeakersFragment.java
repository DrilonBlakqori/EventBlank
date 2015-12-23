package com.zagapps.eventblank.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zagapps.eventblank.adapters.SpeakersCardViewAdapter;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;


public class SpeakersFragment extends Fragment
{
    private ImageView mSearchView;
    private TextView mTitle;
    private ImageView mFavouriteFilter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SpeakersCardViewAdapter mAdapter;
    private EditText mSearchText;
    private ImageView mCloseSearch;
    private Drawable likeFull;
    private Drawable likeEmpty;
    private boolean isFavourite;
    private boolean isSearched;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        final View view=inflater.inflate(R.layout.speakers_fragment, parent, false);

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                init(view);
            }
        });

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(getActivity().getCurrentFocus()!=null)
        {
            InputMethodManager inputManager =
                    (InputMethodManager) getActivity().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void init(final View view)
    {
        mSearchView=(ImageView)view.findViewById(R.id.speakers_search_view);
        mFavouriteFilter =(ImageView)view.findViewById(R.id.speaker_favourite);
        mTitle =(TextView)view.findViewById(R.id.speakers_title);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.speakers_recycler);
        mSearchText= (EditText)view.findViewById(R.id.speakers_search_edit_text);
        mCloseSearch= (ImageView)view.findViewById(R.id.speakers_search_view_close);

        int ternaryColor= ModelManager.get(getActivity()).getEvent().getTernaryColor();
        int mainColor= ModelManager.get(getActivity()).getEvent().getMainColor();

        mTitle.setTextColor(ternaryColor);

        view.findViewById(R.id.speakers_header).setBackgroundColor(mainColor);

        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new SpeakersCardViewAdapter(getActivity(),getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        Drawable favouriteFull= ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_full);
        favouriteFull.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        likeFull=favouriteFull;

        Drawable favouriteEmpty= ContextCompat.getDrawable(getActivity(),R.mipmap.favourite );
        favouriteEmpty.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        likeEmpty=favouriteEmpty;

        mAdapter.switchList(isFavourite);
        if(!isFavourite)
        {
            mFavouriteFilter.setImageDrawable(likeEmpty);
            mFavouriteFilter.setActivated(false);
        }
        else
        {
            mFavouriteFilter.setImageDrawable(likeFull);
            mFavouriteFilter.setActivated(true);
        }

        final Animation shakeUp= AnimationUtils.loadAnimation(getActivity(),R.anim.image_shake_up);
        final Animation shakeDown= AnimationUtils.loadAnimation(getActivity(),R.anim.image_shake_down);
        shakeUp.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(!mFavouriteFilter.isActivated())
                    mFavouriteFilter.setImageDrawable(likeEmpty);
                else
                    mFavouriteFilter.setImageDrawable(likeFull);

                mFavouriteFilter.startAnimation(shakeDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        mFavouriteFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mFavouriteFilter.isActivated())
                {
                    mFavouriteFilter.setActivated(true);
                    mFavouriteFilter.startAnimation(shakeUp);
                    isFavourite = true;
                    mAdapter.switchList(true);
                } else
                {
                    mFavouriteFilter.setActivated(false);
                    mFavouriteFilter.startAnimation(shakeUp);
                    isFavourite = false;
                    mAdapter.switchList(false);
                }
            }

        });


        mSearchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTitle.setVisibility(View.GONE);
                mSearchView.setVisibility(View.GONE);
                mFavouriteFilter.setVisibility(View.GONE);
                mSearchText.setVisibility(View.VISIBLE);
                mCloseSearch.setVisibility(View.VISIBLE);
                mSearchView.setClickable(false);
                mSearchText.requestFocus();
                mSearchText.setActivated(true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSearchText, InputMethodManager.SHOW_IMPLICIT);
                isSearched=true;
            }
        });

        if(isSearched)
        {
            mSearchView.performClick();
        }

        mCloseSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTitle.setVisibility(View.VISIBLE);
                mSearchView.setVisibility(View.VISIBLE);
                mFavouriteFilter.setVisibility(View.VISIBLE);
                mSearchText.setVisibility(View.GONE);
                mCloseSearch.setVisibility(View.GONE);
                mSearchView.setClickable(true);
                mSearchText.setText(null);
                mSearchText.setActivated(false);
                if(getActivity().getCurrentFocus() != null)
                {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                isSearched=false;
            }
        });


        Drawable searchIcon= ContextCompat.getDrawable(getActivity(), R.mipmap.search);
        searchIcon.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        mSearchView.setImageDrawable(searchIcon);

        mSearchText.setTextColor(ternaryColor);
        mSearchText.setGravity(Gravity.CENTER_VERTICAL);
        mSearchText.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_GO)
                {
                    if(getActivity().getCurrentFocus() != null)
                    {
                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        getActivity().getCurrentFocus().clearFocus();
                        view.findViewById(R.id.speakers_header).requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });

        mSearchText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mAdapter.searchSpeakers(mSearchText.getText().toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        Drawable closeIcon= ContextCompat.getDrawable(getActivity(), R.mipmap.close);
        closeIcon.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        mCloseSearch.setImageDrawable(closeIcon);
    }
}
