package de.horstfestival.android.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.content.Context;

public class BandList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8578859711057915341L;
	private static final String FILENAME = "bandlist";
	public ArrayList<Band> mList;
	public float mVersion;

	public class TimeComperator implements Comparator<Band> {
		@Override
		public int compare(Band o1, Band o2) {
			return (int) (o1.mDate.getTime() - o2.mDate.getTime());
		}
	}

	public class AlphabeticalComperator implements Comparator<Band> {
		@Override
		public int compare(Band o1, Band o2) {
			return o1.mName.compareToIgnoreCase(o2.mName);
		}
	}

	public class StageComperator implements Comparator<Band> {
		@Override
		public int compare(Band o1, Band o2) {
			return o1.mStage - o2.mStage;
		}
	}

	public BandList() {
		super();
		mList = new ArrayList<Band>();
		mVersion = 0;
	}

	public void toFile(Context context) {

		ObjectOutputStream objectOut = null;
		try {
			FileOutputStream fileOut = context.openFileOutput(FILENAME,
					Activity.MODE_PRIVATE);
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(this);
			fileOut.getFD().sync();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) {
					// do nowt
				}
			}
		}
	}

	public static BandList fromFile(Context context) {
		ObjectInputStream objectIn = null;
		Object object = null;
		try {

			FileInputStream fileIn = context.getApplicationContext()
					.openFileInput(FILENAME);
			objectIn = new ObjectInputStream(fileIn);
			object = objectIn.readObject();

		} catch (FileNotFoundException e) {
			// Do nothing
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					// do nowt
				}
			}
		}

		return (BandList) object;
	}

	public void populateFakeList() {
		mVersion = 1.0f;
		mList = new ArrayList<Band>();
		Band band = new Band("Metallica", "nix.jpg", "Sind kacke", new Date(),
				1);
		mList.add(band);
		band = new Band("Iron Maiden", "nix.jpg", "Sind auch kacke",
				new Date(), 1);
		mList.add(band);
		band = new Band("Taylor Swift", "nix.jpg", "Ist besonders kacke",
				new Date(), 1);
		mList.add(band);
		band = new Band("Doktor Alban", "nix.jpg", "Der ist gut.", new Date(),
				1);
		mList.add(band);
	}
}