package mysp.bpt.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import mysp.bpt.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

	private final Typeface typeface;

	public SpinnerAdapter(Context context, int resource, String[] items, Typeface typeface) {
		super(context, resource, items);
		this.typeface = typeface;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView textView = setSpinnerParams(view);
		textView.setPaddingRelative(25, 15, 0, 0);
		return view;
	}
	@Override
	public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
		View view = super.getDropDownView(position, convertView, parent);
		TextView textView = setSpinnerParams(view);
		textView.setPaddingRelative(25, 25, 0, 25);
		return view;
	}

	public TextView setSpinnerParams(View view) {
		TextView textView = view.findViewById(android.R.id.text1);
		textView.setTypeface(typeface);
		textView.setTextSize(18);
		textView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
		return textView;
	}
}