package com.bums.small;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class INCMediaFragment extends Fragment {
	private WebView webView;
	private View v;
	private ProgressDialog mDialog;
	private Bundle webViewBundle;
	private ProgressBar bar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public WebView getWebView() {
		return webView;
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
				//bar = (ProgressBar) v.findViewById(R.id.progressBar);
				mDialog = ProgressDialog.show(getActivity(), "", "Loading...",
						true);
				mDialog.setCancelable(true);
				
//				webView.setWebChromeClient(new WebChromeClient() {
//	                public void onProgressChanged(WebView view, int progress) {
//	                    if (progress == 100) {
//	                        // stop ProgressDialog after loading
//	                        mDialog.dismiss();
//	                    }
//	                }
//	            });

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
	   super.onActivityCreated(savedInstanceState);
	   if (webViewBundle != null) {
           webView.restoreState(webViewBundle);
       }
	}


	@Override
	public void onPause() {
		super.onPause();
		
		webViewBundle = new Bundle();
        webView.saveState(webViewBundle);
		try {
			webView.loadUrl("file:///android_asset/nonexistent.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        this.myMenu = menu;
//        MenuItem item = menu.add(0, 1, 0, "Home");
//        item.setIcon(R.drawable.home);
//        MenuItem item2 = menu.add(0, 2, 0, "Back");
//        item2.setIcon(R.drawable.arrowleft);
//        MenuItem item3 = menu.add(0, 3, 0, "Reload");
//        item3.setIcon(R.drawable.s);
//        MenuItem item4 = menu.add(0, 4, 0, "Share");
//        item4.setIcon(R.drawable.share);
//        MenuItem item5 = menu.add(0, 5, 0, "Rate");
//        item5.setIcon(R.drawable.vote);
//        MenuItem item6 = menu.add(0, 6, 0, "Exit");
//        item6.setIcon(R.drawable.close);
//        return true;
//    }
	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
//            wv.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

	private class WebViewClientOverride extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
//			if (mDialog != null && mDialog.isShowing()) {
//				new java.util.Timer().schedule( 
//					new java.util.TimerTask() {
//						@Override
//						public void run() {
//							mDialog.dismiss();
//						}
//					}, 
//					3000 
//					);
//			}
			
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
	}
}