package com.zagapps.eventblank.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.Toast;

import com.zagapps.eventblank.fragments.WebViewFragment;
import com.zagapps.eventblank.R;

public class CustomLinkMovementMethod extends LinkMovementMethod
{

    private static Context movementContext;
    private static FragmentManager sFragmentManager;
    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event)
    {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if(link.length != 0)
            {
                System.out.println(link[0].getURL());
                String url = link[0].getURL();
                if(url.startsWith("https") || url.startsWith("http"))
                {
                    Fragment fragment = new WebViewFragment();
                    Bundle urlBundle = new Bundle();
                    urlBundle.putString(WebViewFragment.URL, url);
                    urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.WEBSITE_URL);
                    fragment.setArguments(urlBundle);

                    sFragmentManager.beginTransaction().addToBackStack(fragment.getClass().getSimpleName())
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_right)
                            .replace(R.id.moreContainer, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
                }
                if(url.startsWith("mailto:"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    try {
                        movementContext.startActivity(Intent.createChooser(intent, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(movementContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static android.text.method.MovementMethod getInstance(Context c,FragmentManager fragmentManager)
    {
        movementContext = c;
        sFragmentManager=fragmentManager;
        return linkMovementMethod;
    }
}
