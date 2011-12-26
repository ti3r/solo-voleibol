package org.blanco.solovolei.fragments;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.gui.adapters.PlayersListAdapter;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class PlayersListFragment extends ListFragment {
	
	Dao<Player, Long> dao = null;
	PlayersLoader loader = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = (Dao<Player, Long>) DaoFactory.getDao(getActivity(), Player.class);
	}

	@Override
	public void onStart() {
		findPlayersAndPopulateList();
		registerForContextMenu(getListView());
		super.onStart();
	}
	
	/**
	 * Creates a new Players loader in order to populate the list of
	 * players with the information in the database. 
	 */
	public void findPlayersAndPopulateList(){
		if (loader != null && loader.getStatus().equals(AsyncTask.Status.RUNNING))
			loader.cancel(true);
		loader = new PlayersLoader(dao);
		loader.execute();
	}
	
	/**
	 * Creates the context menu that will be available for the 
	 * items in the players list
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater =  new MenuInflater(getActivity());
		inflater.inflate(R.menu.deelete_ctx_menu, menu);
		inflater.inflate(R.menu.edit_ctx_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_ctx_menu_edit_item:
			Toast.makeText(getActivity(), "Edit", Toast.LENGTH_LONG).show();
			break;
		case R.id.delete_update_ctx_menu_delete_item:
			Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
			break;
		default:
			return super.onContextItemSelected(item);
		} 	
		return true;
	}




	/**
	 * The async task that will load the teams build the adapter and populate
	 * the teams list.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 * 
	 */
	private class PlayersLoader extends AsyncTask<Void, Void, ListAdapter> {

		Dao<Player, Long> dao = null;

		public PlayersLoader(Dao<Player, Long> teamsDao) {
			if (teamsDao == null)
				throw new IllegalArgumentException("Passed Dao can't be null");
			dao = teamsDao;
		}

		@Override
		protected ListAdapter doInBackground(Void... params) {
			try {
				List<Player> teams = dao.queryForAll();
				PlayersListAdapter adapter = new PlayersListAdapter(getActivity(),
						teams);
				return adapter;
			} catch (SQLException e) {
				Log.e(TAG,
						"Error quering the dao for all the teams. Unable to populate the list",
						e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(ListAdapter result) {
			if (result != null){
				setListAdapter(result);
			}
		}

	}

	
}
