package com.example.cmput301.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cmput301.R;
import com.example.cmput301.model.Task;

public class TextAdapter extends BaseAdapter {

		private Context context;
		private Task t;

		public TextAdapter(Context context, Task t) {
			this.context = context;
			this.t = t;
		}

		
		private View getRow(View row) {

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(R.layout.text_response_entry, null);
			}
			return row;
		}
		
		@Override
		public int getCount() {
			
			return t.getResponses().size();
		}

		@Override
		public Object getItem(int position) {
			return t.getResponses().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = getRow(convertView);
			
			TextView timeView = (TextView) row
					.findViewById(R.id.p_entry_responseTime);
			timeView.setText(t.getResponses().get(position).getTimestamp()
					.toString());
			
			TextView annoView = (TextView) row
					.findViewById(R.id.p_entry_responseAno);
			annoView.setText(t.getResponses().get(position).getContent().toString());
			
			return row;
		}
		
	}