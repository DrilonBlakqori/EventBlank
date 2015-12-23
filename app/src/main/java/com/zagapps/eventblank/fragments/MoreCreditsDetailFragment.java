package com.zagapps.eventblank.fragments;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoreCreditsDetailFragment extends Fragment {

    @Bind(R.id.more_detail_header_text)
    TextView mTitle;

    @Bind({R.id.more_details_text1, R.id.more_details_text2, R.id.more_details_text3, R.id.more_details_text4})
    List<HtmlTextView> mContents;

    @Bind(R.id.more_detail_back_navigation)
    ImageView mBackNavigation;
    int mainColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_credits_fragment, container, false);
        ButterKnife.bind(this, view);
        setupToolbar();

        mainColor = ModelManager.get(getActivity()).getEvent().getMainColor();
        view.findViewById(R.id.more_detail_header).setBackgroundColor(mainColor);


        Utils.SpanConverter<URLSpan, CharacterStyle> spanConverter = new Utils.SpanConverter<URLSpan, CharacterStyle>() {
            @Override
            public CharacterStyle convert(final URLSpan span) {
                return (new ClickableLinkSpan(ModelManager.get(getActivity()).getEvent().getMainColor(), false) {
                    @Override
                    public void onClick(View widget) {
                        String url = span.getURL();
                        if (url.contains("mailto:")) {
                            //hape si email, 7 i bjen pozita ku ia nis emaila, pasi "mailto:" osht ne positions 0-6
                            Utils.goToMailClient(getActivity(), url.substring(7));
                        } else {
                            Utils.openWebsiteInsideApp(url, getFragmentManager());
                        }
                    }
                });
            }
        };

        for (int i = 0; i < mContents.size(); i++) {
            HtmlTextView textView = mContents.get(i);
            textView.setHtmlFromString(generateCredits(i), new HtmlTextView.LocalImageGetter());
            textView.setText(Utils.replaceAllSpans((Spanned) textView.getText(),
                    URLSpan.class, spanConverter
            ));
        }

        return view;
    }


    private void setupToolbar() {
        mTitle.setText("Credits");
        mTitle.setTextColor(ModelManager.get(getActivity()).getEvent().getTernaryColor());
        setupBackButton();
    }

    private void setupBackButton() {
        Drawable backNavigator = ContextCompat.getDrawable(getActivity(), R.mipmap.arrow_back);
        backNavigator.mutate().setColorFilter(ModelManager.get(getActivity()).getEvent().getTernaryColor(), PorterDuff.Mode.SRC_ATOP);
        mBackNavigation.setImageDrawable(backNavigator);
        mBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    private String generateCredits(int position) {
        switch (position) {
            case 0:
                return "<h1><font color=\"" + mainColor + "\"><b>EventBlank for Android</b></font></h1>" +
                        "<font color=\"" + mainColor + "\"><b>by ZAGAPPS,</b> <a href=\"https://twitter.com/zagapps\"><b>@zagapps</b></a></font>.";

            case 1:
                return "<p><font color=\"" + mainColor + "\"><b>EventBlank </b></font>" +
                        "is a project aiming to help all kinds of events small or big to quickly build their own event app.</p>" +
                        "<p>EventBlank is an open source project, which you can obtain for free and use for commercial or non-commercial purposes</p>" +
                        "<p>The license the source code is distributed under is MIT and it is a requirement that you keep this attribution in its entirety in any app you build using EventBlank</p>" +
                        "You can find all info and code here <font color=\"" + mainColor + "\"><a href=\"http://eventblankapp.com/\">www.eventblankapp.com</a></font>.";

            case 2:
                return "<p>Project author: <font color=\"" + mainColor + "\"><b>Marin Todorov</b> <a href=\"mailto:marin@underplot.com\">marin@underplot.com</a></font>. </p>" +
                        "<p>More info, contacts, and speaking schedule at:<font color=\"" + mainColor + "\"><a href=\"http://underplot.com/\">www.underplot.com</a>.</font></p>" +
                        "Icons by: <font color=\"" + mainColor + "\"><a href=\"https://dribbble.com/shots/1791904-54-Free-e-commerce-icons\">Virgil Pana</a></font>" +
                        " and <font color=\"" + mainColor + "\"><a href=\"https://www.google.com/design/icons/\">Google</a></font>.";

            case 3:
                return "<p><font color=\"" + mainColor + "\"><b>MIT License</b></font></p>" +
                        "<p>The MIT License (MIT)</p>" +
                        "<p>Copyright (c) 2015 Marin Todorov, <font color=\"" + mainColor + "\"><a href=\"http://underplot.com/\">www.underplot.com</a></font>.</p>" +
                        "<p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without " +
                        "restriction, including withoujt limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software," +
                        "and to permit persons to whom the Software is furnished to do so, subject to the following conditions:</p>" +
                        "<p>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.</p>" +
                        "<p>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM," +
                        " DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p>";
            default:
                return "";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
