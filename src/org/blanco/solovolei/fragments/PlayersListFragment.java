package org.blanco.solovolei.fragments;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.MainActivity;
import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.gui.adapters.PlayersListAdapter;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class PlayersListFragment extends ListFragment {
	
	Dao<Player, Long> dao = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		dao = (Dao<Player, Long>) DaoFactory.getDao(getActivity(), Player.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		findPlayersAndPopulateList();
		super.onStart();
	}

	
	
	public void findPlayersAndPopulateList(){
		
		try {
			List<Player> players = (List<Player>) dao.queryForAll();
			PlayersListAdapter adapter = new PlayersListAdapter(getActivity(), players);
			setListAdapter(adapter);
			Log.d("alog", "Players count: "+players.size());
		} catch (SQLException e) {
			Log.e(MainActivity.TAG, "Error retrieving the players");
		}	
	}
	
	
}
