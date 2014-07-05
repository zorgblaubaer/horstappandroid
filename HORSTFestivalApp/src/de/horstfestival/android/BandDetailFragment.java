package de.horstfestival.android;

import java.io.File;
import java.io.FileOutputStream;

import com.loopj.android.image.SmartImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import de.horstfestival.android.model.Band;
import de.horstfestival.android.model.FavList;

public class BandDetailFragment extends Fragment implements OnClickListener {

	private static final String TAG = "BandDetailFragment";
	private static final String ARG_BAND = "band";
	Band mBand;

	static BandDetailFragment createWithBand(Band band) {
		BandDetailFragment frag = new BandDetailFragment();
		frag.mBand = band;
		return frag;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mBand = (Band) savedInstanceState.get(ARG_BAND);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_band_detail, null, false);

		SmartImageView picture = (SmartImageView) view.findViewById(R.id.picture);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView date = (TextView) view.findViewById(R.id.date);
		TextView description = (TextView) view.findViewById(R.id.description);

		CheckBox box = (CheckBox) view.findViewById(R.id.fav_check);
		FavList list = FavList.fromFile(getActivity());
		if (list.contains(mBand))
			box.setChecked(true);
		box.setOnClickListener(this);

		/*String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/horst_cache");
		String fname = mBand.mPicture;
		File file = new File(myDir, fname);
		if (file.exists()) {
			Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
			picture.setImageBitmap(b);
		} else {
			picture.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.ic_launcher));
			picture.setImageUrl(mBand.getPictureUrl());
		}*/
		
		picture.setImageUrl(mBand.getPictureUrl());

		name.setText(mBand.mName);
		date.setText(mBand.mHumanDate);
		description.setText(mBand.mDescription);

		return view;
	}

	public Band getBand() {
		return mBand;
	}

	@Override
	public void onClick(View view) {
		FavList list = FavList.fromFile(getActivity());
		boolean checked = ((CheckBox) view).isChecked();

		if (checked) {
			list.add(mBand);
			list.toFile(getActivity());
		} else {
			list.remove(mBand);
			list.toFile(getActivity());
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		outState.putSerializable(ARG_BAND, mBand);
		super.onSaveInstanceState(outState);
	}
}