package com.zagapps.eventblank.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.zagapps.eventblank.R;
import com.zagapps.eventblank.fragments.WebViewFragment;


public class Utils {

    private Utils() {
        throw new AssertionError("This class shouldn't be instantiated. It has only static methods");
    }

    public static void openWebsiteInsideApp(String url, FragmentManager fragmentManager) {
        Fragment fragment = new WebViewFragment();
        Bundle urlBundle = new Bundle();
        urlBundle.putString(WebViewFragment.URL, url);
        urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.WEBSITE_URL);
        fragment.setArguments(urlBundle);

        fragmentManager.beginTransaction().addToBackStack(fragment.getClass().getSimpleName())
                .setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right)
                .add(R.id.moreContainer, fragment, fragment.getClass().getSimpleName()).commit();
    }

    public static <A extends CharacterStyle, B extends CharacterStyle> Spannable replaceAllSpans(Spanned original,
                                                                                            Class<A> sourceType,
                                                                                                 SpanConverter<A, B> converter) {
        SpannableString result = new SpannableString(original);
        A[] spans = result.getSpans(0, result.length(), sourceType);

        for (A span : spans) {
            int start = result.getSpanStart(span);
            int end = result.getSpanEnd(span);
            int flags = result.getSpanFlags(span);

            result.removeSpan(span);
            result.setSpan(converter.convert(span), start, end, flags);
        }

        return (result);
    }

    public interface SpanConverter<A extends CharacterStyle, B extends CharacterStyle> {
        B convert(A span);
    }

    public static void goToMailClient(Activity activity,String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        //startActivity(Intent.createChooser(intent, ""));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

}
