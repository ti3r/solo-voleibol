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
package org.blanco.solovolei;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.entities.PlayerTeam;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.TeamsPickFragment.TeamsPickListener;
import org.blanco.solovolei.fragments.players.PlayersCheckListFragment;
import org.blanco.solovolei.fragments.players.PlayersCheckListFragment.PlayersCheckListListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class MainActivity extends FragmentActivity 
	implements TeamsPickListener,
	PlayersCheckListListener, OnClickListener{
    
	Fragment pickTeamsFragment = null;
	PlayersCheckListFragment playersCheckListFragment = null;
	Dao<PlayerTeam, Long> dao = null;
	
	Button btnSave = null;
	/**
	 * The related team that will be associated to the selected 
	 * players
	 */
	Team relatedTeam = null;
	/**
	 * The tag used with Log cat methods. 
	 * The simple name of the application.
	 */
	public static final String TAG = "solo_volei";
	
	/** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the DAO
        dao = (Dao<PlayerTeam, Long>) DaoFactory.getDao(this, PlayerTeam.class);
        
        setContentView(R.layout.main);        
        btnSave = (Button) findViewById(R.id.main_btn_save);
        btnSave.setOnClickListener(this);
    }

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		//Check the fragment that has been attached from the XML
		//and assign it to the corresponding member
		switch(fragment.getId()){
		case R.id.main_teams_list_fragment:
			pickTeamsFragment = fragment;
			break;
		case R.id.main_players_checklist_fragment:
			playersCheckListFragment = (PlayersCheckListFragment) fragment;
			//set the mark items flag to true for the fragment
			playersCheckListFragment.setMarkRelated(true);
		default:
			Log.d(TAG, "Attached fragment has not recognized id. Assigned to no members");
		}
	}

	@Override
	public void onTeamPicked(Team team) {
		//establish the selected team to a member in order to save the data.
		relatedTeam = team;		
		playersCheckListFragment.setRelatedTeam(team);
		playersCheckListFragment.loadPlayers();
	}

	@Override
	public void onClick(View arg0) {
		//Chech the view that has been clicked
		switch(arg0.getId()){
		case R.id.main_btn_save:
				saveRelation();
			break;
		default:
			Log.d(TAG, "onClickMethod did not recognize the id of the clicked view. " +
					"Event will be ignored. Id:"+arg0.getId());
		}
	}
    
	/**
	 * The method that will relate the 
	 */
	private void saveRelation(){
		//TODO sanitize thisz
		//delete all the previous relations
		DeleteBuilder<PlayerTeam, Long> db = dao.deleteBuilder();
		try {
			db.where().eq("team_fk", new Integer(relatedTeam.getId()));
			dao.delete(db.prepare());
		} catch (SQLException e1) {
			Log.d(TAG, "Error deleting previous relations for team "+relatedTeam);
		}
		
		//get The list of selected players
		List<Player> players = playersCheckListFragment.getSelectedPlayers();
		Log.d(TAG, players.toString());
		for(Player player: players){
			PlayerTeam pt = new PlayerTeam(player, relatedTeam);
			try {
				dao.create(pt);
			} catch (SQLException e) {
				Log.e(TAG, "Error saving: "+pt,e);
			}
		}
	}
    
}