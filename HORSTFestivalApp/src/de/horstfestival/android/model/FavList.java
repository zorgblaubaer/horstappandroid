package de.horstfestival.android.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

public class FavList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4167012118265670666L;
	private static final String FILENAME = "favlist";
	private List<String> mList;

	public FavList() {
		mList = new ArrayList<String>();
	}

	public void add(Band band) {
		mList.add(band.mName);
	}

	public void remove(Band band) {
		mList.remove(band.mName);
	}

	public boolean contains(Band band) {
		return mList.contains(band.mName);
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

	public static FavList fromFile(Context context) {
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

		return (FavList) object;
	}
}