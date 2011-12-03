package org.blanco.solovolei.gui.adapters;

import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeamsListAdapter extends ArrayAdapter<Team> {

	public TeamsListAdapter(Context context, List<Team> objects) {
		//-1 because get view will be overridden in the adapter later
		//we don't need android to map the layout for us
		super(context, -1, -1, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext())
				.inflate(R.layout.teams_list_item, null);
		//map the data to the view 
		((TextView)convertView.findViewById(R.id.teams_list_item_name))
			.setText(getItem(position).getName());
		
		return convertView;
	}	
	
}
