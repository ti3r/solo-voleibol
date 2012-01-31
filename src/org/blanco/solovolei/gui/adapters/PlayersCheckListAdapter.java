/**
 * The MIT License
 * 
 * Copyright (c) 2012 Alexandro Blanco <ti3r.bubblenet@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
		chk.setChecked(checks.contains(getItem(position)));
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
	
	/**
	 * This method adds to the list of checked items the objects
	 * that match the passed ids in the parameters. This method
	 * seeks on all the objects present in the adapter in order
	 * to compare the ids and add the entire object to the list
	 * of checked objects. 
	 * Note: It might consume some time depending on the 
	 * Amount of ids passed
	 * 
	 * @param playerIds The ids of the players to be added to 
	 * the checked list in this batch.
	 */
	public void checkOnBatch(List<Long> playerIds){
		for (int i=0; i < getCount(); i++){
			if (playerIds.contains(getItem(i).getId()))
				checks.add(getItem(i));
		}
	}
	
}
