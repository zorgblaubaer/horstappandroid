package de.horstfestival.android.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import de.horstfestival.android.R;

public class Band implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6724395791403514634L;

	public static final String IMAGE_URL = "http://ios.horstfestival.de/media/bands/small/";

	private static final String TAG = "Band";

	public String mName;
	public String mPicture;
	public String mDescription;
	public String mHumanDate;
	public Date mDate;
	public int mStage;

	public Band(String name, String picture, String description, Date date,
			int stage) {
		mName = name;
		mPicture = picture;
		mDescription = description;
		mDate = date;
		mStage = stage;
	}

	private String parseDate(Date date, Context context) {
		String dateString;

		SimpleDateFormat weekdayFinder = new SimpleDateFormat("EEEE");
		dateString = weekdayFinder.format(date);

		dateString += ", ";
		dateString += (date.getHours() + 2) + ":" + date.getMinutes();
		if (date.getMinutes() < 10) {
			dateString += "0";
		}
		dateString += ", " + context.getString(R.string.stage) + ": ";

		if (mStage == 0) {
			dateString += context.getString(R.string.bigstage);
		} else if (mStage == 1) {
			dateString += context.getString(R.string.smallstage);
		} else {
			dateString += context.getString(R.string.tentstage);
		}

		return dateString;
	}

	public Band(final JSONObject j, Context context) {
		try {
			mName = j.getString("name");
			mPicture = j.getString("picture");
			mDescription = j.getString("description");
			SimpleDateFormat parser = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			try {
				mDate = parser.parse(j.getString("date"));
			} catch (ParseException e) {
				mDate = new Date();
				e.printStackTrace();
			}
			mStage = j.getInt("stage");
			mHumanDate = parseDate(mDate, context);
			// Log.v(TAG, "parsed " + j.getString("date") + "\nas " + mHumanDate);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getPictureUrl() {
		return IMAGE_URL + mPicture;
	}
}