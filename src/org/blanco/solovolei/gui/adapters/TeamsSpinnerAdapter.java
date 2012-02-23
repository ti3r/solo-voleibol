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
		ViewHolder holder = null;
		if (convertView == null){
			convertView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.teams_spinner_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.
					findViewById(R.id.teams_list_item_name);
			//Add the holder to the tag of the object
			convertView.setTag(holder);
		}else{
			//retrieve the holder view for the object
			holder = (ViewHolder) convertView.getTag();
		}
		//Set the properties of the view
		holder.name.setText(((Team)getItem(position)).getName());
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

	/**
	 * The ViewHolder class to improve the performance of the 
	 * adapter when building and displaying the views of this
	 * adapter. This avoid unnecessary call to findViewById
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	static class ViewHolder{
		TextView name = null;
	}
}
