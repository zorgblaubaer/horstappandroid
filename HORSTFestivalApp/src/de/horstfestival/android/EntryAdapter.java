package de.horstfestival.android;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> implements Serializable {

	private static final long serialVersionUID = -7440812620999829348L;
	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.listview_header, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v
						.findViewById(R.id.list_header_title);
				sectionView.setText(si.getTitle());
			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.listview_bands_layout, null);
				final TextView name = (TextView) v.findViewById(R.id.name);
				final TextView date = (TextView) v.findViewById(R.id.date);
				final ImageView picture = (ImageView) v
						.findViewById(R.id.picture);

				if (name != null)
					name.setText(ei.mName);
				if (date != null)
					date.setText(ei.mDate);
				picture.setImageDrawable(context.getResources().getDrawable(
						R.drawable.icon));
			}
		}
		return v;
	}

}
