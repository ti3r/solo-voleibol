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

import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * The Adapter object used to populate the Players List present in 
 * the PlayersListFragment class. 
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class PlayersListAdapter extends ArrayAdapter<Player> {

	public PlayersListAdapter(Context context, List<Player> objects) {
		//-1 because get view will be overridden in the adapter later
		//we don't need android to map the layout for us
		super(context, -1, -1, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext())
				.inflate(R.layout.players_list_item, null);
		//map the data to the view 
		final TextView name = 
		((TextView)convertView.findViewById(R.id.players_list_item_name));
			name.setText(getItem(position).getName());
		
		final TextView num = 
				((TextView)convertView.findViewById(R.id.players_list_item_number));
		num.setText(String.valueOf(getItem(position).getNumber()));
			
		
		return convertView;
	}	
	
}
