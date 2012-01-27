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

import java.util.List;

import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.gui.adapters.PlayersCheckListAdapter;
import org.blanco.solovolei.loaders.PlayersAsyncLoader;
import org.blanco.solovolei.loaders.PlayersAsyncLoader.AsyncLoaderListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.j256.ormlite.dao.Dao;

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
		//execute the loading of the players
		loadPlayers();
	}
	
	/**
	 * Load the players in order to display them in the fragment.
	 * It created the corresponding adapter that will be set in the
	 * list of the fragment 
	 */
	private void loadPlayers(){
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
	 * Interface to communicate the events occurred within the fragment
	 * to the rest of the world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface PlayersCheckListListener{
		
	}
}
