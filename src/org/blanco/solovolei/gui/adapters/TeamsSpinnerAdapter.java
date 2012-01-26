package org.blanco.solovolei.gui.adapters;

import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamsSpinnerAdapter extends BaseAdapter {

	List<Team> teams = null;
	
	public TeamsSpinnerAdapter(List<Team> teams) {
		super();
		this.teams = teams;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.teams_spinner_item, null);
		TextView view = (TextView) convertView.findViewById(R.id.teams_list_item_name);
		view.setText(((Team)getItem(position)).getName());
		return convertView;
	}

	@Override
	public int getCount() {
		return (teams != null)? teams.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (teams != null && position < teams.size())? 
				teams.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return (teams != null && position < teams.size())? 
				teams.get(position).getId():0;
	}

	
}
