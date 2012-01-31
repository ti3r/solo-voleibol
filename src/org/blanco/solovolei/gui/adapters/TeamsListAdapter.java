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
import org.blanco.solovolei.entities.Team;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * The Adapter object used to populate the Teams List present in 
 * the TeamsListFragment class. 
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
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
		//If the team has a specified logo replace the image view bitmap with
		//the bitmap of the object
		if ((getItem(position).getLogo() != null)){
			final ImageView img =
			((ImageView)convertView.findViewById(R.id.team_list_item_logo));
			
			img.setImageBitmap(Bitmap.createBitmap(BitmapFactory.decodeFile(
					getItem(position).getLogo()), 0, 0, 25, 25)
					);
		}
		
		return convertView;
	}	
	
}
