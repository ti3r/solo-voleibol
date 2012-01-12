package org.blanco.solovolei.fragments;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.gui.adapters.PlayersListAdapter;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.j256.ormlite.dao.Dao;

public class PlayersListFragment extends ListFragment {
	
	Dao<Player, Long> dao = null;
	PlayersLoader loader = null;
	private PlayersListCommandsListener listener = null;
	
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
	
	
	
	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof PlayersListCommandsListener){
			if (listener == null){
				listener = (PlayersListCommandsListener) activity;
			}else{
				Log.d(TAG, "PlayersListFragment. Attached activity implements " +
						"listener but listener is already set.");
			}
		}else if(listener == null){
			//If the attached activity does not implement commandslistener
			//and no listener is established then throw and exception
			throw new IllegalArgumentException("Attached activity does not " +
					"implement PlayersListCommandsListener. Please implement " +
					"this interface or establish the correct listener before starting the fragment");
		}
		super.onAttach(activity);
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
			delete(item);
			break;
		default:
			return super.onContextItemSelected(item);
		} 	
		return true;
	}


	private void delete(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Player player = (Player) getListAdapter().getItem(info.position);
		if (player == null && listener != null){
			listener.onPlayerItemDeleteError(null, 
					new Exception("Unable to locate Player on List Adapter"));
		}else if (listener == null || listener.onPlayerItemPreDelete(null)){
			try {
				dao.delete(player);
				if (listener != null)
					listener.onPlayerItemPostDelete(player);
			} catch (SQLException e) {
				Log.d(TAG, "Delete Error for Player: "+player,e);
				if (listener != null)
					listener.onPlayerItemDeleteError(player, e);
			}
		}
		
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

	/**
	 * Interface that will handle the selected events occurred with the players
	 * contained in this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface PlayersListCommandsListener {
		/**
		 * The method that will be executed when one item has been selected from
		 * the list.
		 * 
		 * @param item
		 *            The Team item selected from the list
		 */
		//public void onTeamItemSelected(Team item);

		/**
		 * The method that will be executed when the edit command has been
		 * launched for one item of the list.
		 * 
		 * @param item
		 *            The Team item selected from the list.
		 */
		//public void onTeamItemEditionStarted(Team item);
		
		/**
		 * The method that will be executed before one Team item is deleted from
		 * the list.
		 * 
		 * @param team
		 *            The Team item that will be deleted
		 * @return True if everything is ok and the delete command should
		 *         continue, False otherwise.
		 */
		public boolean onPlayerItemPreDelete(Player player);

		/**
		 * The method that will be executed after one Team item has been deleted
		 * from the list.
		 * 
		 * @param team
		 *            The Team item that was deleted
		 */
		public void onPlayerItemPostDelete(Player player);

		/**
		 * The method that will be executed if the delete command fails on one
		 * item of the list.
		 * 
		 * @param team
		 *            The Team item that attempted to be deleted
		 * @param e
		 *            The Exception that show up during the delete command.
		 */
		public void onPlayerItemDeleteError(Player player, Exception e);
	}
}
