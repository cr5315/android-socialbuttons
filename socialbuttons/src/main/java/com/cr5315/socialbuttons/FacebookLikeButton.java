package com.cr5315.socialbuttons;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FacebookLikeButton extends ShareButton {
    private static final String TAG = FacebookLikeButton.class.getSimpleName();

    private String mPageId = null;
    private String mPageUrl = null;

    public FacebookLikeButton(Context context) {
        super(context);
        initView(null);
    }

    public FacebookLikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public FacebookLikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    protected void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.button_facebook_like, this);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShareButton);
            TypedArray b = getContext().obtainStyledAttributes(attrs, R.styleable.LikeButton);

            mPageId = b.getString(R.styleable.LikeButton_pageId);
            mPageUrl = b.getString(R.styleable.LikeButton_pageUrl);

            String buttonText = a.getString(R.styleable.ShareButton_buttonText);
            if (buttonText != null) setButtonText(buttonText);

            Drawable buttonDrawable = a.getDrawable(R.styleable.ShareButton_buttonDrawable);
            if (buttonDrawable != null)
                setButtonDrawable(buttonDrawable);

            a.recycle();
            b.recycle();
        }

        // We don't care about these fields
        setAccessToken(null);
        setAnnotation(ANNOTATION_NONE);
        setProgressType(PROGRESS_TYPE_NONE);
        setShareUrl(null);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageId != null && mPageUrl != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + mPageId));
                    shareIntent.setPackage("com.facebook.katana");

                    List<ResolveInfo> resolveInfoList = getContext().getPackageManager().queryIntentActivities(shareIntent, 0);
                    if (resolveInfoList == null || resolveInfoList.isEmpty()) {
                        shareIntent = new Intent();
                        shareIntent.setData(Uri.parse(mPageUrl));
                        shareIntent.setAction(Intent.ACTION_VIEW);
                    }

                    getContext().startActivity(shareIntent);
                } else {
                    Log.e(TAG, "Both pageId and pageUrl must be set to use FacebookLikeButton.");
                }
            }
        });

        super.initView(attrs);
    }

    @Override
    protected void getShares() {}

    public void setButtonText(String text) {
        ((TextView) findViewById(R.id.buttonText)).setText(text);
    }

    public void setButtonDrawable(Drawable drawable) {
        ((ImageView) findViewById(R.id.buttonImage)).setImageDrawable(drawable);
    }
}