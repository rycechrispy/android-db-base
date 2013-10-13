package com.bums.small;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bums.library.UserFunctions;
import com.xtremelabs.imageutils.ImageLoader;

public class FashionFragment extends ListFragment {
	/**
	 * Called when the activity is first created.
	 */
	private static String KEY_SUCCESS = "success";
	private static String KEY_ID = "id";

	private static String KEY_DATA = "data";
	private static String KEY_IMAGES = "images";
	private static String KEY_STANDARD = "standard_resolution";
	private static String KEY_CAPTION = "caption";
	private static String KEY_TEXT = "text";
	private static String KEY_USER = "user";
	private static String KEY_USERNAME = "username";
	private static String KEY_NAME = "full_name";
	private static String KEY_URL = "url";

	private ProgressBar bar;

	// An array of all of our comments
	private JSONArray rData = null;
	// manages all of our comments in a list.
	private ArrayList<RowData> rowDataList;
	private View v;

	private boolean flag = true;

	private RowData rd;
	private RowAdapter adapter;
	private String next_url;
	private static ImageLoader mImageLoader;
	private static View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.list, container, false);
		//list = (ListView) ((Activity) inflater.getContext()).findViewById(R.id.list);

		bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.setVisibility(View.VISIBLE);

		if (flag) {
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.INVISIBLE);
			flag = false;
			NetAsync(v);
		}

		mImageLoader = ImageLoader.buildImageLoaderForActivity(getActivity());
		setHasOptionsMenu(true);
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();

		bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.setVisibility(View.INVISIBLE);
	}

	/**
	 * Async Task to check whether internet connection is working.
	 **/
	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.VISIBLE);
		}
		/**
		 * Gets current device state and checks for working internet connection by trying Google.
		 **/
		@Override
		protected Boolean doInBackground(String... args){
			ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://www.google.com");
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean th){
			if(th == true){
				new LoadJSON().execute();
			}
			else{
				Toast.makeText(getActivity().getApplicationContext(),
						"Network Error Connection", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void updateJSONdata(JSONObject json) {
		//iDataList = new ArrayList<HashMap<String, String>>();
		rowDataList = new ArrayList<RowData>();
		try {
			rData = json.getJSONArray(KEY_DATA);
			next_url = json.getJSONObject("pagination").getString("next_url");
			for (int i = 0; i < rData.length(); i++) {
				JSONObject data = rData.getJSONObject(i);

				JSONObject images = data.getJSONObject(KEY_IMAGES);
				JSONObject standard = images.getJSONObject(KEY_STANDARD);
				String url = standard.getString(KEY_URL);

				String text = "";
				try {
					JSONObject caption = data.getJSONObject(KEY_CAPTION);
					text = caption.getString(KEY_TEXT);
				} catch(JSONException e) {}

				JSONObject user = data.getJSONObject(KEY_USER);
				String username = user.getString(KEY_USERNAME);

				// creating new HashMap
				rd = new RowData();
				rd.setCaption(text);
				rd.setLink(url);
				rd.setUsername(username);

				//when you first run, the whole thing is empty, there is no row data. 
				//once 

				rowDataList.add(rd);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class LoadJSON extends AsyncTask<String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getFashionJSON();

			updateJSONdata(json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				bar = (ProgressBar) v.findViewById(R.id.progressBar);
				bar.setVisibility(View.INVISIBLE);
				adapter = new RowAdapter(getActivity(), R.layout.list_itm, rowDataList);
				setListAdapter(adapter);
				//				for (int i = 0; i < rowDataList.size(); i++) {
				////					View v = LayoutInflater.from(context).inflate(
				////							R.layout.list_itm, getActivity(), false);
				//					image = (ImageView) view.findViewById(R.id.imageView);
				//					image.setTag(rowDataList.get(i).getLink());
				//					
				////					map.put(KEY_URL, url);
				////					map.put(KEY_USERNAME, username);
				////					map.put(KEY_TEXT, text);
				//					new DownloadImagesTask().execute(image);
				//					adapter.notifyDataSetChanged();
				//				}
				//set the bitmaps

			} catch (Exception e) {
				Toast.makeText(getActivity().getApplicationContext(),
						"JSON FAILED", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class LoadNextJSON extends AsyncTask<String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getFashionJSON(next_url);

			updateJSONdata(json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				bar = (ProgressBar) v.findViewById(R.id.progressBar);
				bar.setVisibility(View.INVISIBLE);
				adapter = new RowAdapter(getActivity(), R.layout.list_itm, rowDataList);
				setListAdapter(adapter);

			} catch (Exception e) {
				Toast.makeText(getActivity().getApplicationContext(),
						"JSON FAILED", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	private static class RowAdapter extends ArrayAdapter<RowData> {

		private ArrayList<RowData> data = new ArrayList<RowData>();

		public RowAdapter(Context context, int textViewResourceId,
				ArrayList<RowData> row_data) {
			super(context, textViewResourceId, row_data);
			this.data = row_data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.list_itm, parent, false);
				holder.header = (TextView) convertView
						.findViewById(R.id.header);
				holder.image = (ImageView) convertView.
						findViewById(R.id.imageView);
				convertView.setTag(holder);
				//view = convertView;
			}
			holder = (ViewHolder) convertView.getTag();
			RowData rd = data.get(position);
			holder.header.setText(rd.getCaption());
			//holder.image.setTag(rd.getLink());
			//new DownloadImagesTask().execute(holder.image);

			mImageLoader.loadImage(holder.image, rd.getLink());
			return convertView;
		}

		private class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
			ImageView imageView = null;

			@Override
			protected Bitmap doInBackground(ImageView... imageViews) {
				this.imageView = imageViews[0];
				return download_Image((String)imageView.getTag());
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				imageView.setImageBitmap(result);
			}

			private Bitmap download_Image(String url) {
				Bitmap bmp = null;
				try {
					URL ulrn = new URL(url);
					HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
					InputStream is = con.getInputStream();
					bmp = BitmapFactory.decodeStream(is);
					if (null != bmp)
						return bmp;
				} catch(Exception e){}
				return bmp;
			}
		}
	}

	private static class ViewHolder {
		TextView header;
		TextView username;
		ImageView image;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fashion, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			new LoadJSON().execute();
			return true;
		case R.id.action_forward:
			new LoadNextJSON().execute();
			return true;
		default:
			break;
		}
		return false;
	}

	public void NetAsync(View view){
		new NetCheck().execute();
	}
}