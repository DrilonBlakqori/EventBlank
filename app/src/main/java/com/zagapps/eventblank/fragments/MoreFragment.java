package com.zagapps.eventblank.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zagapps.eventblank.R;
import com.zagapps.eventblank.services.UpdateService;
import com.zagapps.eventblank.models.ModelManager;


public class MoreFragment extends Fragment {


    public static final String MORE_ITEM = "more_item";


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent, Bundle onSavedInstanceState) {
        final View view = inflater.inflate(R.layout.more_fragment, parent, false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                init(view);
            }
        });


        return view;
    }

    private void setIcon(TextView view, int color, int resource) {
        Drawable icon = ContextCompat.getDrawable(getActivity(), resource);
        icon.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        view.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private void init(View view) {
        int mainColor = ModelManager.get(getActivity()).getEvent().getMainColor();
        int secondaryColor = ModelManager.get(getActivity()).getEvent().getMainColor();
        int ternaryColor = ModelManager.get(getActivity()).getEvent().getTernaryColor();

        TextView mAbout = (TextView) view.findViewById(R.id.card_about);
        TextView mCode = (TextView) view.findViewById(R.id.card_code);
        TextView mSponsors = (TextView) view.findViewById(R.id.card_sponsors);
        TextView mCredits = (TextView) view.findViewById(R.id.card_credits);
        TextView mAcknowledgements = (TextView) view.findViewById(R.id.card_acknowledgements);
        TextView mPendingUpdates = (TextView) view.findViewById(R.id.card_pending_update);
        RelativeLayout mMoreHeader = (RelativeLayout) view.findViewById(R.id.more_header);

        setIcon(mAbout, secondaryColor, R.mipmap.about);
        setIcon(mCode, secondaryColor, R.mipmap.code);
        setIcon(mSponsors, secondaryColor, R.mipmap.sponsors);
        setIcon(mCredits, secondaryColor, R.mipmap.credits);
        setIcon(mAcknowledgements, secondaryColor, R.mipmap.acknowledgements);
        setIcon(mPendingUpdates, secondaryColor, R.mipmap.update);

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreDetailFragment fragment = new MoreDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MORE_ITEM, 0);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.moreContainer, fragment).commit();
            }
        });

        mCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreDetailFragment fragment = new MoreDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MORE_ITEM, 1);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.moreContainer, fragment).commit();
            }
        });

        mSponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreDetailFragment fragment = new MoreDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MORE_ITEM, 2);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.moreContainer, fragment).commit();
            }
        });

        mCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreCreditsDetailFragment fragment = new MoreCreditsDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MORE_ITEM, 3);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.moreContainer, fragment).commit();
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.more_header_text);
        textView.setTextColor(ternaryColor);

        TextView cardHeader1 = (TextView) view.findViewById(R.id.card_header_1);
        cardHeader1.setTextColor(secondaryColor);

        TextView cardHeader2 = (TextView) view.findViewById(R.id.card_header_2);
        cardHeader2.setTextColor(secondaryColor);

        mMoreHeader.setBackgroundColor(mainColor);

        mPendingUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(ModelManager.get(getActivity()).getEvent().getUpdateFileUrl());
            }
        });

    }

    public void updateDatabase(String downloadUrl) {
        Intent intent = new Intent(getContext(), UpdateService.class);
        intent.putExtra(UpdateService.DOWNLOAD_URI_ARG, downloadUrl);
        getActivity().startService(intent);
    }

}
