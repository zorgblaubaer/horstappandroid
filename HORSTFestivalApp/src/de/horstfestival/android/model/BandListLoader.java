package de.horstfestival.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import de.horstfestival.android.MainActivity;
import de.horstfestival.android.R;

public class BandListLoader extends AsyncTask {

	public interface OnTaskCompleted {
		void onTaskCompleted(BandList bandList);
	}

	private static final String TAG = "BandListLoader";
	private OnTaskCompleted mListener;
	private Activity mActivity;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage(mActivity
				.getString(R.string.progress_dialog_bandlist));
		mProgressDialog.show();
		((MainActivity) mActivity).setDialog(mProgressDialog);
	}

	public BandListLoader(Activity activity, OnTaskCompleted listener) {
		mActivity = activity;
		mListener = listener;
	}

	public void setListener(OnTaskCompleted listener) {
		mListener = listener;
	}

	@Override
	protected BandList doInBackground(Object... arg0) {
		Log.v(TAG, "Checking for BandList version");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(
				"http://ios.horstfestival.de/publish/version/type/bands");
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response == null) {
			Log.v(TAG, "connection is broken. returning null");
			return null;
		}
		HttpEntity ht = response.getEntity();

		BufferedHttpEntity buf = null;
		try {
			buf = new BufferedHttpEntity(ht);
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream is = null;
		try {
			is = buf.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader r = new BufferedReader(new InputStreamReader(is));

		StringBuilder total = new StringBuilder();
		String line;
		try {
			while ((line = r.readLine()) != null) {
				total.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		float version = Float.valueOf(total.toString());
		BandList bandList = BandList.fromFile(mActivity);
		if ((bandList != null) && (bandList.mVersion >= version)) {
			Log.v(TAG, "Found up-to-date BandList (version "
					+ bandList.mVersion + ") locally. Fin.");
			return bandList;
		} else if (bandList != null) {
			Log.v(TAG, "Found outdated BandList locally. Updating to version "
					+ version + " from " + bandList.mVersion);
		} else {
			Log.v(TAG, "No BandList found locally. Fetching version " + version
					+ " from remote.");
		}

		Log.v(TAG, "Loading BandList from json");
		httppost = new HttpGet("http://ios.horstfestival.de/json/Bands.json");
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ht = response.getEntity();

		buf = null;
		try {
			buf = new BufferedHttpEntity(ht);
		} catch (IOException e) {
			e.printStackTrace();
		}

		is = null;
		try {
			is = buf.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}

		r = new BufferedReader(new InputStreamReader(is));

		total = new StringBuilder();
		try {
			while ((line = r.readLine()) != null) {
				total.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Log.v(TAG, total.toString());
		Log.v(TAG, "Converting to JSONArray");

		JSONArray jObj = null;

		try {
			jObj = new JSONArray(total.toString());
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// Log.v(TAG, jObj.toString());

		bandList = new BandList();
		bandList.mVersion = version;

		for (int i = 0; i < jObj.length(); i++) {
			try {
				JSONObject j = jObj.getJSONObject(i);
				// Log.v(TAG, j.toString());
				// Parse JSONObject to Band
				Band band = new Band(j, mActivity);
				// Log.v(TAG, band.mName);
				bandList.mList.add(band);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return bandList;
	}

	@Override
	protected void onPostExecute(Object bandList) {
		// your stuff
		mListener.onTaskCompleted((BandList) bandList);
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}