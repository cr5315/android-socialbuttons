package com.cr5315.socialbuttons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class ShareButton extends LinearLayout {
    private static final String TAG = ShareButton.class.getSimpleName();

    public static final int ANNOTATION_NONE = 0;
    public static final int ANNOTATION_BUBBLE = 1;
    public static final int ANNOTATION_SHEET = 2;

    public static final int PROGRESS_TYPE_NONE = 0;
    public static final int PROGRESS_TYPE_SPINNER = 1;

    // Attributes
    private String mAccessToken = null;
    private int mAnnotation = ANNOTATION_NONE;
    private int mProgressType = PROGRESS_TYPE_SPINNER;
    private String mShareUrl = null;

    // Internal state
    private boolean mIsFetched = false;
    private String mResult;

    public ShareButton(Context context) {
        super(context);
    }

    public ShareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initView(AttributeSet attrs) {
        displayAnnotation();

        if (!isInEditMode() || getAnnotation() != ANNOTATION_NONE) {
            fetchShares();
        }
    }

    protected void displayAnnotation() {
        boolean hasToShow;

        switch (getAnnotation()) {
            case ANNOTATION_BUBBLE:
            case ANNOTATION_SHEET:
                hasToShow = true;
                break;
            default:
                hasToShow = false;
        }

        findViewById(R.id.annotationLayout).setVisibility(hasToShow ? View.VISIBLE : View.GONE);
    }

    public void fetchShares() {
        fetchShares(false);
    }

    public void fetchShares(boolean forceRefresh) {
        displayAnnotation();

        if (getShareUrl() != null && (forceRefresh || !mIsFetched)) {
            TextView annotationText = (TextView) findViewById(R.id.annotationText);
            ProgressBar annotationProgress = (ProgressBar) findViewById(R.id.annotationProgress);

            switch (getProgressType()) {
                case PROGRESS_TYPE_SPINNER:
                    annotationProgress.setVisibility(View.VISIBLE);
                    break;
                default:
                    annotationProgress.setVisibility(View.GONE);
            }

            annotationText.setVisibility(View.GONE);

            getShares();
        }
    }

    protected abstract void getShares();

    protected void onSharesDownloaded(String result) {
        setResult(result);

        TextView annotationText = (TextView) findViewById(R.id.annotationText);
        ProgressBar annotationProgress = (ProgressBar) findViewById(R.id.annotationProgress);
        RelativeLayout annotationLayout = (RelativeLayout) findViewById(R.id.annotationLayout);

        switch (getAnnotation()) {
            case ANNOTATION_SHEET:
                annotationLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.sheet_background));
                break;
            case ANNOTATION_BUBBLE:
                annotationLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_baloon_normal));
                break;
        }

        if (result != null) {
            setIsFetched(true);
            annotationText.setText(result);
        } else {
            setIsFetched(false);
            annotationText.setText("An error occurred.");
        }

        annotationText.setVisibility(View.VISIBLE);
        annotationProgress.setVisibility(View.GONE);
        displayAnnotation();
    }

    /**
     * Setter for the attribute <b>sharesUrl</b>
     * @param shareUrl the URL to be passed to the social network API
     */
    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    /**
     * @return the URL to be passed to the social network API
     */
    public String getShareUrl() {
        return mShareUrl;
    }

    /**
     * Setter for the attribute <b>accessToken</b>
     * @param accessToken the access token to be passed to the social network API
     */
    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    /**
     * @return the access token to be passed to the social network API
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * Setter for the attribute <b>annotation</b>.
     * @param annotation Must be {@link #ANNOTATION_NONE}, {@link #ANNOTATION_BUBBLE}, or {@link #ANNOTATION_SHEET}.
     */
    public void setAnnotation(int annotation) {
        if (annotation == ANNOTATION_NONE || annotation == ANNOTATION_BUBBLE || annotation == ANNOTATION_SHEET) {
            mAnnotation = annotation;
        }
    }

    /**
     * @return the annotation type
     */
    public int getAnnotation() {
        return mAnnotation;
    }

    /**
     * Setter for the attribute <b>progressType</b>
     * @param progressType Must be {@link #PROGRESS_TYPE_NONE} or {@link #PROGRESS_TYPE_SPINNER}.
     */
    public void setProgressType(int progressType) {
        if (progressType == PROGRESS_TYPE_NONE || progressType == PROGRESS_TYPE_SPINNER) {
            mProgressType = progressType;
        }
    }

    public int getProgressType() {
        return mProgressType;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getResult() {
        return mResult;
    }

    public void setIsFetched(boolean isFetched) {
        mIsFetched = isFetched;
    }

    public boolean isFetched() {
        return mIsFetched;
    }
}