package de.horstfestival.android.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import de.horstfestival.android.NavigationDrawerFragment;
import de.horstfestival.android.R;

public class Uploader extends AsyncTask {

	private static final String TAG = "PicUploader";

	private Context mContext;
	private NavigationDrawerFragment mFrag;
	private ProgressDialog mProgressDialog;

	public Uploader(Context context, NavigationDrawerFragment f) {
		super();
		mContext = context;
		mFrag = f;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getResources().getString(
				R.string.uploading));
		mProgressDialog.show();
		mFrag.setDialog(mProgressDialog);
	}

	@Override
	protected Object doInBackground(Object... params) {
		Log.v(TAG, "upload started");
		File imagesFolder = new File(Environment.getExternalStorageDirectory(),
				"horst_cache");
		imagesFolder.mkdirs();
		File image = new File(imagesFolder, "cam_comp.jpg");

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("image", image.getPath()));

		nameValuePair.add(new BasicNameValuePair("name", "userfile"));
		nameValuePair.add(new BasicNameValuePair("filename", "cam_comp.jpg"));
		nameValuePair.add(new BasicNameValuePair("comment", ""));

		Log.v(TAG, "path: " + image.getPath());

		post("http://ios.horstfestival.de/media/fetch", nameValuePair);
		return null;
	}

	public void post(String url, List<NameValuePair> nameValuePairs) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(url);

		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < nameValuePairs.size(); index++) {
				Log.v(TAG, "adding " + nameValuePairs.get(index).getName());
				if (nameValuePairs.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart("userfile", new FileBody(new File(
							nameValuePairs.get(index).getValue()),
							"multipart/form-data"));
				} else {
					// Normal string data
					entity.addPart(
							nameValuePairs.get(index).getName(),
							new StringBody(nameValuePairs.get(index).getValue()));
				}
			}

			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost, localContext);
			Log.v(TAG,
					"response: " + response.toString() + "\n"
							+ response.getStatusLine() + "\n" + "HttpPost: "
							+ httpPost.getRequestLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}
}