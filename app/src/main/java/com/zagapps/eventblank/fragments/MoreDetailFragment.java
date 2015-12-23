package com.zagapps.eventblank.fragments;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zagapps.eventblank.R;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.utils.ClickableLinkSpan;
import com.zagapps.eventblank.utils.Utils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class MoreDetailFragment extends Fragment {

    private TextView mTitle;
    private HtmlTextView mContent;
    private ImageView mBackNavigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_detail_fragment, container, false);
        Bundle bundle = getArguments();

        mTitle = (TextView) view.findViewById(R.id.more_detail_header_text);

        mTitle.setTextColor(ModelManager.get(getActivity()).getEvent().getTernaryColor());


        mBackNavigation = (ImageView) view.findViewById(R.id.more_detail_back_navigation);
        Drawable backNavigator = ContextCompat.getDrawable(getActivity(), R.mipmap.arrow_back);
        backNavigator.mutate().setColorFilter(ModelManager.get(getActivity()).getEvent().getTernaryColor(), PorterDuff.Mode.SRC_ATOP);
        mBackNavigation.setImageDrawable(backNavigator);
        mBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        view.findViewById(R.id.more_detail_header).setBackgroundColor(ModelManager.get(getActivity()).getEvent().getMainColor());

        mContent = (HtmlTextView) view.findViewById(R.id.more_details_text);

        mTitle.setText((ModelManager.get(getActivity()).getTexts().get(bundle.getInt(MoreFragment.MORE_ITEM)).getTitle()));
        mContent.setHtmlFromString((ModelManager.get(getActivity()).getTexts().
                get(bundle.getInt(MoreFragment.MORE_ITEM)).getContent()), new HtmlTextView.LocalImageGetter());

        mContent.setText(Utils.replaceAllSpans((Spanned) mContent.getText(),
                URLSpan.class,
                new Utils.SpanConverter<URLSpan, CharacterStyle>() {
                    @Override
                    public CharacterStyle convert(final URLSpan span) {
                        return (new ClickableLinkSpan(ModelManager.get(getActivity()).getEvent().getMainColor(), false) {
                            @Override
                            public void onClick(View widget) {
                                String url = span.getURL();
                                if (url.contains("mailto:")) {
                                    //hape si email, 7 i bjen pozita ku ia nis emaila, pasi "mailto:" osht ne positions 0-6
                                    Utils.goToMailClient(getActivity(),url.substring(7));
                                } else {
                                    Utils.openWebsiteInsideApp(url, getFragmentManager());
                                }
                            }
                        });
                    }
                }));


        return view;
    }


}
