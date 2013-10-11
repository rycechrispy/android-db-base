package com.bums.small;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
	private String lastURL = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public WebView getWebView() {
		return webView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View v = inflater.inflate(R.layout.webview, container, false);
		v = LayoutInflater.from(getActivity()).inflate(R.layout.webview, null);
		if (getArguments() != null) {
			//
			try {
				//mDialog = ProgressDialog.show(getActivity(), "", "Loading...",
				//		true);
				//mDialog.setCancelable(true);
				
//				webView.setWebChromeClient(new WebChromeClient() {
//	                public void onProgressChanged(WebView view, int progress) {
//	                    if (progress == 100) {
//	                        // stop ProgressDialog after loading
//	                        mDialog.dismiss();
//	                    }
//	                }
//	            });
				bar = (ProgressBar) v.findViewById(R.id.progressBar);
				bar.setVisibility(View.VISIBLE);

				webView = (WebView) v.findViewById(R.id.webPage);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setSupportZoom(false);
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.web, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.home:
	    	webView.loadUrl("http://www.incmedia.org");
	        return true;
	    case R.id.action_back:
	    	if (webView.canGoBack()) {
	    		webView.goBack();
            }
	        return true;
	    case R.id.action_refresh:
	    	webView.loadUrl(lastURL);
	        return true;
	    default:
	        break;
	    }

	    return false;
	}
	
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
		public void onPageStarted(WebView view, String url,
                Bitmap favicon) {
//			if (!mDialog.isShowing()) {
//				mDialog = ProgressDialog.show(getActivity(), "", "Loading...",
//						true);
//				mDialog.setCancelable(true);
//			}
			
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.VISIBLE);

            lastURL = url;
            view.getSettings().setLoadsImagesAutomatically(true);
            view.getSettings().setBuiltInZoomControls(true);
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
			
//			if (mDialog != null && mDialog.isShowing()) {
//				mDialog.dismiss();
//			}
			
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.INVISIBLE);
		}
	}
}