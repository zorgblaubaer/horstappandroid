/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.horstfestival.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("ValidFragment")
public class WebViewFragment extends Fragment {

	private WebView mWebView;
	private boolean mIsWebViewAvailable;
	private String mUrl = null;

	/**
	 * Creates a new fragment which loads the supplied url as soon as it can
	 * 
	 * @param url
	 *            the url to load once initialised
	 */
	public WebViewFragment(String url) {
		super();
		mUrl = url;
	}

	/**
	 * Called to instantiate the view. Creates and returns the WebView.
	 */
	@SuppressLint("SetJavaScriptEnabled") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mWebView != null) {
			mWebView.destroy();
		}
		mWebView = new WebView(getActivity());
		mWebView.loadUrl(mUrl);
		mIsWebViewAvailable = true;
		if (mUrl == HorstUtils.URL_SITE_MAP)
			mWebView.getSettings().setBuiltInZoomControls(true);
		else if (mUrl == HorstUtils.URL_GALLERY) {
			mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
    			@Override
    			public boolean shouldOverrideUrlLoading(WebView view, String url) {
    				return false;
    			}
    		});
		}
		return mWebView;
	}

	/**
	 * Convenience method for loading a url. Will fail if {@link View} is not
	 * initialised (but won't throw an {@link Exception})
	 * 
	 * @param url
	 */
	public void loadUrl(String url) {
		if (mIsWebViewAvailable)
			getWebView().loadUrl(mUrl = url);
		else
			Log.w("ImprovedWebViewFragment",
					"WebView cannot be found. Check the view and fragment have been loaded.");
	}

	/**
	 * Called when the fragment is visible to the user and actively running.
	 * Resumes the WebView.
	 */
	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	/**
	 * Called when the WebView has been detached from the fragment. The WebView
	 * is no longer available after this time.
	 */
	@Override
	public void onDestroyView() {
		mIsWebViewAvailable = false;
		super.onDestroyView();
	}

	/**
	 * Called when the fragment is no longer in use. Destroys the internal state
	 * of the WebView.
	 */
	@Override
	public void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	/**
	 * Gets the WebView.
	 */
	public WebView getWebView() {
		return mIsWebViewAvailable ? mWebView : null;
	}
}