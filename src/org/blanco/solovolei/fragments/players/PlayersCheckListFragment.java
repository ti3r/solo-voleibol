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
package org.blanco.solovolei.fragments.players;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.entities.PlayerTeam;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.gui.adapters.PlayersCheckListAdapter;
import org.blanco.solovolei.loaders.PlayersAsyncLoader;
import org.blanco.solovolei.loaders.PlayersAsyncLoader.AsyncLoaderListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
/**
 * Class that will display the list of players as a checkable list
 * in order to select one or more objects from the available list.
 * You can perform extra tasks with the selected items.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class PlayersCheckListFragment extends ListFragment 
	implements AsyncLoaderListener {

	Dao<Player,Long> dao =  null;
	PlayersCheckListListener listener = null;
	PlayersAsyncLoader loader = null;
	/**
	 * The boolean flag that marks if the process of loading
	 * the players that will be displayed in the fragment should
	 * be launched when the onStart method executes.
	 */
	private boolean launchLoadOnStart = false;
	/**
	 * The team that will be related to the players list in this
	 * fragment. This object will rule the modifications selected
	 * in the boolean properties: markRelated.
	 */
	Team relatedTeam = null;
	/**
	 * The flag that indicates if the items related to the relatedTeam
	 * Team object should be queried and marked in the adapter.
	 */
	boolean markRelated = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//init the dao
		dao = ((Dao<Player, Long>) DaoFactory.getDao(getActivity(), Player.class));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (listener == null && 
				!(activity instanceof PlayersCheckListListener)){
			throw new IllegalArgumentException("Attached activity does not implement " +
					"PlayersCheckListListener in order to handle the results. " +
					"Please implement this interface in passed activity or set the " +
					"appropiate listener before starting the Fragment");
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (launchLoadOnStart)
			//execute the loading of the players
			loadPlayers();
	}
	
	/**
	 * Load the players in order to display them in the fragment.
	 * It created the corresponding adapter that will be set in the
	 * list of the fragment 
	 */
	public void loadPlayers(){
		if (loader != null && loader.getStatus().equals(AsyncTask.Status.RUNNING)){
			loader.cancel(true);
		}
		loader = new PlayersAsyncLoader(dao);
		loader.setAsyncLoaderListener(this);
		loader.execute();
	}

	//Methods of the AsyncLoaderListener interface
	@Override
	public void onLoadComplete(List<Player> results) {
		//Create the Adapter
		PlayersCheckListAdapter adapter = 
				new PlayersCheckListAdapter(getActivity(), results);
		//After the load complete check if we need to mark the 
		//items that are already related to the passed team
		if (markRelated && relatedTeam != null){ 
			Log.d(TAG, "markRelated is active. Marking related Objects");
			Dao<PlayerTeam, Long> d2 = 
					(Dao<PlayerTeam, Long>) DaoFactory.getDao(getActivity(), PlayerTeam.class);
			try {
				//TODO Sanitize this
				QueryBuilder<PlayerTeam, Long> builder = d2.queryBuilder();
				builder.where().eq("team_fk", relatedTeam.getId());
				List<PlayerTeam> objects = d2.query(builder.prepare());
				List<Long> selected = new ArrayList<Long>();
				for(PlayerTeam object : objects){
					selected.add(object.getPlayer().getId());
				}
				Log.d(TAG, "Ids to mark found"+objects);
				//Set the ids to be added to the checked objects
				adapter.checkOnBatch(selected);
			} catch (SQLException e) {
				Log.e(TAG, "Error retrieving ids of the items to mark.",e);
			}
		}
		
		this.setListAdapter(adapter);
	}
	
	
	//End of Methods of the AsyncLoaderListener interface
	
	public List<Player> getSelectedPlayers(){
		if (getListAdapter() instanceof PlayersCheckListAdapter)
			return ((PlayersCheckListAdapter) getListAdapter())
				.getSelected();
		else{
			throw new RuntimeException("Unable to retrieve Selected players. " +
					"Set adapter is not instanceof PlayersCheckListAdapter");
		}
	}
	
	/**
	 * Gets the relatedTeam property of this fragment, this means that the items
	 * present in the check list should be related to this object in one way. 
	 * They might be filtered or marked according to the specified flags.
	 * 
	 * @return The Team object related to this fragment
	 */
	public Team getRelatedTeam() {
		return relatedTeam;
	}

	/**
	 * Sets the relatedTeam property of this fragment, this means that the items
	 * present in the check list should be related to this object in one way. 
	 * They might be filtered or marked according to the specified flags.
	 * 
	 * @return The Team object related to this fragment
	 */
	public void setRelatedTeam(Team relatedTeam) {
		this.relatedTeam = relatedTeam;
	}

	/**
	 * Returns the value of the flag markRelated property of the fragment.
	 * This flag shows if the Items related to relatedTeam object should
	 * be marked in the fragment.
	 * 
	 * @return The boolean value of the markRelated property of the fragment
	 */
	public boolean isMarkRelated() {
		return markRelated;
	}

	/**
	 * Sets the value of the flag markRelated property of the fragment.
	 * This flag shows if the Items related to relatedTeam object should
	 * be marked in the fragment.
	 * @param markRelated
	 */
	public void setMarkRelated(boolean markRelated) {
		this.markRelated = markRelated;
	}



	/**
	 * Interface to communicate the events occurred within the fragment
	 * to the rest of the world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface PlayersCheckListListener{
		
	}
}
