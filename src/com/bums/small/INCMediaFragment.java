package com.bums.small;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class INCMediaFragment extends Fragment {
	private WebView webView;
	private View v;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//View v = inflater.inflate(R.layout.webview, container, false);
		v = LayoutInflater.from(getActivity()).inflate(R.layout.webview, null);
		if (getArguments() != null) {
			//
			try {
				mDialog = ProgressDialog.show(getActivity(), "", "Loading...",
						true);
				mDialog.setCancelable(false);

				webView = (WebView) v.findViewById(R.id.webPage);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new WebViewClientOverride());
				webView.loadUrl("http://www.incmedia.org");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			webView.loadUrl("file:///android_asset/nonexistent.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class WebViewClientOverride extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (mDialog != null && mDialog.isShowing()) {
				new java.util.Timer().schedule( 
					new java.util.TimerTask() {
						@Override
						public void run() {
							mDialog.dismiss();
						}
					}, 
					3000 
					);
			}
		}
	}
}