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

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public class FacebookShareButton extends ShareButton {
    private static final String TAG = FacebookShareButton.class.getSimpleName();

    private FacebookService mFacebookService;

    private interface FacebookService {
        @GET("/v2.4/")
        void getUrlInfo(@Query("id") String query, @Query("access_token") String accessToken, Callback<JsonObject> callback);
    }

    public FacebookShareButton(Context context) {
        super(context);
        initView(null);
    }

    public FacebookShareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public FacebookShareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    protected void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.button_facebook_share, this);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShareButton);
            setAccessToken(a.getString(R.styleable.ShareButton_accessToken));
            setAnnotation(a.getInt(R.styleable.ShareButton_annotation, ANNOTATION_NONE));
            setProgressType(a.getInt(R.styleable.ShareButton_progressType, PROGRESS_TYPE_SPINNER));
            setShareUrl(a.getString(R.styleable.ShareButton_shareUrl));

            String buttonText = a.getString(R.styleable.ShareButton_buttonText);
            if (buttonText != null) setButtonText(buttonText);

            Drawable buttonDrawable = a.getDrawable(R.styleable.ShareButton_buttonDrawable);
            if (buttonDrawable != null)
                setButtonDrawable(buttonDrawable);

            a.recycle();
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = getShareUrl();
                if (shareUrl != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                    shareIntent.setPackage("com.facebook.katana");

                    List<ResolveInfo> resolveInfoList = getContext().getPackageManager().queryIntentActivities(shareIntent, 0);
                    if (resolveInfoList == null || resolveInfoList.isEmpty()) {
                        shareIntent = new Intent();
                        shareIntent.setData(Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + shareUrl));
                        shareIntent.setAction(Intent.ACTION_VIEW);
                    }

                    getContext().startActivity(shareIntent);
                }
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://graph.facebook.com")
                .build();

        mFacebookService = restAdapter.create(FacebookService.class);

        super.initView(attrs);
    }

    @Override
    protected void getShares() {
        if (getAccessToken() == null) {
            Log.e(TAG, "You must set an access token to use FacebookShareButton");
            onSharesDownloaded(null);
        } else if (getShareUrl() == null) {
            Log.e(TAG, "You must set a share URL to use FacebookShareButton");
            onSharesDownloaded(null);
        } else if (mFacebookService != null) {
            mFacebookService.getUrlInfo(getShareUrl(), getAccessToken(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    JsonObject share = jsonObject.getAsJsonObject("share");
                    onSharesDownloaded(share.get("share_count").getAsString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.toString());
                    onSharesDownloaded(null);
                }
            });
        } else {
            Log.e(TAG, "FacebookShareButton not properly initialized");
            onSharesDownloaded(null);
        }
    }

    public void setButtonText(String text) {
        ((TextView) findViewById(R.id.buttonText)).setText(text);
    }

    public void setButtonDrawable(Drawable drawable) {
        ((ImageView) findViewById(R.id.buttonImage)).setImageDrawable(drawable);
    }
}