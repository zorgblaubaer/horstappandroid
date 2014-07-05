package de.horstfestival.android;

import de.horstfestival.android.model.Band;

public class EntryItem implements Item {

	public final String mName;
	public final String mDate;
	public final Band mBand;

	public EntryItem(String name, String date, Band band) {
		mName = name;
		mDate = date;
		mBand = band;
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
