package com.applovin.mediation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;

import java.util.ArrayList;

class AppLovinNativeAdMapper extends NativeAppInstallAdMapper {

    private AppLovinNativeAd mNativeAd;

    AppLovinNativeAdMapper(AppLovinNativeAd nativeAd, Context context) {
        mNativeAd = nativeAd;
        setHeadline(nativeAd.getTitle());
        setBody(nativeAd.getDescriptionText());
        setCallToAction(nativeAd.getCtaText());

        ImageView mediaView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mediaView.setLayoutParams(layoutParams);

        ArrayList<NativeAd.Image> images = new ArrayList<>(1);
        Drawable imageDrawable = Drawable.createFromPath(Uri.parse(nativeAd.getImageUrl()).getPath());
        Drawable iconDrawable = Drawable.createFromPath(Uri.parse(nativeAd.getIconUrl()).getPath());
        AppLovinNativeAdImage image = new AppLovinNativeAdImage(Uri.parse(nativeAd.getImageUrl()), imageDrawable);
        AppLovinNativeAdImage icon = new AppLovinNativeAdImage(Uri.parse(nativeAd.getIconUrl()), iconDrawable);
        images.add(image);
        setImages(images);
        setIcon(icon);

        mediaView.setImageDrawable(imageDrawable);
        setMediaView(mediaView);
        setStarRating(nativeAd.getStarRating());

        Bundle extraAssets = new Bundle();
        extraAssets.putLong(AppLovinNativeAdapter.KEY_EXTRA_AD_ID, nativeAd.getAdId());
        extraAssets.putString(
                AppLovinNativeAdapter.KEY_EXTRA_CAPTION_TEXT, nativeAd.getCaptionText());

        setOverrideClickHandling(false);
        setOverrideImpressionRecording(false);
    }

    @Override
    public void recordImpression() {
        super.recordImpression();
        mNativeAd.trackImpression();
    }

    @Override
    public void handleClick(View view) {
        super.handleClick(view);
        mNativeAd.launchClickTarget(view.getContext());
    }

    /**
     * A {@link NativeAd.Image} class used to map AppLovin native image to AdMob native image.
     */
    private static class AppLovinNativeAdImage extends NativeAd.Image {

        private final Drawable mDrawable;
        private final Uri mUri;

        AppLovinNativeAdImage(Uri uri, Drawable drawable) {
            mDrawable = drawable;
            mUri = uri;
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public Uri getUri() {
            return mUri;
        }

        @Override
        public double getScale() {
            // AppLovin SDK does not provide scale, return 1 by default.
            return 1;
        }
    }
}
