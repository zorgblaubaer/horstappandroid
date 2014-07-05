package de.horstfestival.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.horstfestival.android.model.BandList;
import de.horstfestival.android.model.FavList;

public class BandListFragment extends ListFragment {

	private static final int RESULT_FILTER = 0;
	private static final String TAG = "BandListFragment";
	BandList mBands;
	EntryAdapter mAdapter;
	private int mSorting;
	private boolean mFavsOnly = false;
	private FavList mFavList;
	public static final int SORTING_ALPHA = 0;
	public static final int SORTING_TIME = 1;

	public BandListFragment(int sorting, boolean favs) {
		super();
		mSorting = sorting;
		mFavsOnly = favs;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	public void setFavsOnly(boolean b) {
		mFavsOnly = b;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mBands == null) {
			mBands = BandList.fromFile(getActivity());
		}

		// mSorting = SORTING_ALPHA;
		refreshListView();

		View view = inflater.inflate(R.layout.fragment_bands, container, false);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Item item = mAdapter.getItem(position);

		if (item.isSection()) {
			return;
		}

		EntryItem eItem = (EntryItem) item;

		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
	
		ft.replace(R.id.container,
				BandDetailFragment.createWithBand(eItem.mBand));
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_bandlist, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.order_alphabtically:
			mSorting = SORTING_ALPHA;
			refreshListView();
			return true;
		case R.id.order_time:
			mSorting = SORTING_TIME;
			refreshListView();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshListView() {
		mFavList = FavList.fromFile(getActivity());

		if (mSorting == SORTING_ALPHA) {
			Collections.sort(mBands.mList, mBands.new AlphabeticalComperator());
		} else if (mSorting == SORTING_TIME) {
			Collections.sort(mBands.mList, mBands.new TimeComperator());
		} else {
			Collections.sort(mBands.mList, mBands.new TimeComperator());
		}

		ArrayList<Item> list = new ArrayList<Item>();

		if (mSorting == SORTING_ALPHA) {
			// String letter = mBands.mList.get(0).mName.substring(0, 1)
			// .toUpperCase();
			String letter = "0";
			// list.add(new SectionItem(letter));
			for (int i = 0; i < mBands.mList.size(); i++) {
				if (!mFavsOnly || mFavList.contains(mBands.mList.get(i))) {
					if (!mBands.mList.get(i).mName.substring(0, 1)
							.toUpperCase().equals(letter)) {
						letter = mBands.mList.get(i).mName.substring(0, 1)
								.toUpperCase();
						list.add(new SectionItem(letter));
					}
					list.add(new EntryItem(mBands.mList.get(i).mName,
							mBands.mList.get(i).mHumanDate, mBands.mList.get(i)));
				}
			}
		} else {
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
			String day = null;// =
								// simpleDateformat.format(mBands.mList.get(0).mDate);

			// list.add(new SectionItem(day));
			for (int i = 0; i < mBands.mList.size(); i++) {
				if (!mFavsOnly || mFavList.contains(mBands.mList.get(i))) {
					if (!simpleDateformat.format(mBands.mList.get(i).mDate)
							.equals(day)) {
						day = simpleDateformat
								.format(mBands.mList.get(i).mDate);
						list.add(new SectionItem(day));
					}
					list.add(new EntryItem(mBands.mList.get(i).mName,
							mBands.mList.get(i).mHumanDate, mBands.mList.get(i)));
				}
			}
		}

		mAdapter = new EntryAdapter(getActivity(), list);
		setListAdapter(mAdapter);
	}

	public void setSorting(int sorting) {
		mSorting = sorting;
		// refreshListView();
	}

}