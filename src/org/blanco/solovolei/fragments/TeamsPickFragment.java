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
package org.blanco.solovolei.fragments;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.j256.ormlite.dao.Dao;

public class TeamsPickFragment extends Fragment
	implements OnItemSelectedListener{

	/**
	 * The spinner where the teams are stored to be picked
	 */
	private Spinner spnTeams = null;
	private ProgressBar progress = null;
	Dao<Team, Long> dao = null;
	TeamsLoader loader = null;
	TeamsPickListener listener = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = (Dao<Team, Long>) DaoFactory.getDao(getActivity(), Team.class);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//Set the listener
		if (listener == null && !(activity instanceof TeamsPickListener)){
			throw new IllegalArgumentException("No TeamsPickListener has been set " +
					"and the attached activity does not implement this interface. " +
					"First establish the appropiate listener or make the Acitivy " +
					"implement the interface to handle the occurred events");
		}else if (listener == null){
			//set the activity as the listener
			listener = (TeamsPickListener) activity;
		}
	}

	@Override
	public void onStart(){
		super.onStart();
		if (loader != null 
				&& loader.getStatus().equals(AsyncTask.Status.RUNNING)){
			//stop the previous loader
			loader.cancel(true);
		}
		loader = new TeamsLoader();
		loader.execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.teams_pick_layout, null);
		init(v);
		return v;
	}
	
	/**
	 * Initializes the properties of the object based on the passed view.
	 * It tries to find all the components for the fragment within the view
	 * and assign them to its corresponding field. It also sets the 
	 * needed event listeners.
	 * 
	 * @param v The View where to initialize from
	 */
	private void init(View v) {
		spnTeams = (Spinner)v.findViewById(R.id.teams_pick_layout_spn_teams);
		spnTeams.setOnItemSelectedListener(this);
		progress = (ProgressBar) v.findViewById(R.id.teams_pick_layout_progress);
	}
	
	//Methods of the OnItemSelectedListener established in the spinner
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		//retrieve the team and let the listener handle the event.
		Object team = arg0.getItemAtPosition(arg2);
		if (listener != null)
			listener.onTeamPicked((Team) team);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//TODO Figure out what to do with this
		//Nothing to do yet
	}
	//Methods of the OnItemSelectedListener established in the spinner
	
	/**
	 * Class that will load the existing teams from the database and will
	 * create one adapter with the results ready to be set.
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	private class TeamsLoader extends AsyncTask<Void, Void, List<Team>>{
		
		@Override
		protected void onPreExecute() {
			//Show the Progress
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<Team> doInBackground(Void... params) {
			List<Team> results = null;
			try {
				results = dao.queryForAll();
			} catch (SQLException e) {
				Log.d(TAG, "Error retrieving teams for TeamsPickFragment",e);
				e.printStackTrace();
			}
			return results;
		}

		@Override
		protected void onPostExecute(List<Team> result) {
			//Create the Adapter for the results; Simple ArrayAdapter is Ok
			ArrayAdapter<Team> adapter = 
					new ArrayAdapter<Team>(getActivity(), 
							android.R.layout.simple_list_item_1,
							android.R.id.text1,
							result
							);
			spnTeams.setAdapter(adapter);			
			if (progress != null)
				progress.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Interface that will handle the events generated within this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface TeamsPickListener{
		/**
		 * The method that will be executed when one Team is picked
		 * @param team The Team object that has been selected
		 */
		public void onTeamPicked(Team team);
	}
	
}
