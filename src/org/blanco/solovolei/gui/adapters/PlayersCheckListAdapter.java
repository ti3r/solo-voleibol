package org.blanco.solovolei.gui.adapters;

import java.util.ArrayList;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * The class that will create an adapter for the CheckList
 * of the Player objects. 
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class PlayersCheckListAdapter extends ArrayAdapter<Player> {

	private List<Player> checks = null;
	
	public PlayersCheckListAdapter(Context context, List<Player> objects) {
		//Do not set the text view and resources ids because createView will
		//be overwritten
		super(context, -1, -1, objects);
		//start the list of the checked items
		checks = new ArrayList<Player>(5);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.players_ckeck_list_item, null);
		
		CheckBox chk = (CheckBox) convertView
				.findViewById(R.id.players_check_list_item_check_box);
		//set the name of the player to be checked
		chk.setText(getItem(position).getName());
		//set if the box must be checked according to the previous state
		chk.setChecked(checks.contains(getItem(position).getId()));
		//set the listener of the checkbox in order to keep track of the
		//selected items
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) //Append the id to the checked list
					checks.add(getItem(position));
				else
					checks.remove(getItem(position));
			}
		});
		return convertView;
	}

	/**
	 * Returns the List of Player objects that have been selected
	 * in the list that contains the adapter.
	 * 
	 * @return List of Player objects that were selected in the 
	 * object associated with the adapter.
	 */
	public List<Player> getSelected(){
		return checks;
	}
	
}