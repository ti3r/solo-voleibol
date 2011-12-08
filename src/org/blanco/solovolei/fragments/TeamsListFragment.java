package org.blanco.solovolei.fragments;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.gui.adapters.TeamsListAdapter;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.j256.ormlite.dao.Dao;

public class TeamsListFragment extends ListFragment {

	/** The dao that will be used by the fragment */ 
	Dao<Team, Long> teamsDao = null;
	/** The item selected listener associated with the fragment */
	TeamsListItemSelectedListener commandsListener = null;
	/** The item deleted listener associated with the fragment */
	TeamsListDeleteItemListener deleteListener = null;
	
	/** variable to enable/disable the delete process in the fragment */
	boolean deleteEnabled = true;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		teamsDao = (Dao<Team, Long>) DaoFactory.getDao(getActivity(), Team.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		getListView().setEmptyView(new ProgressBar(getActivity()));//set a progress bar as empty view
		findTeamsAndPopulateList();
		//set the selection listener for the list contained in this fragment
		getListView().setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (commandsListener != null)
					commandsListener.onTeamItemSelected((getListAdapter() != null)?
							(Team) getListAdapter().getItem(arg2): null);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				if (commandsListener != null)
					commandsListener.onTeamItemSelected(null);
			}
		});
		registerForContextMenu(getListView());
		super.onStart();
	}	

	/**
	 * Sets the listener class that will execute calls when different select
	 * events arise. 
	 * @param listener The TeamItemSelectionListener listener that will be 
	 * associated with this fragment
	 */
	public void setSelectListener(TeamsListItemSelectedListener listener){
		commandsListener = listener;
	}
	/**
	 * Sets the listener class that will execute calls when different delete
	 * events arise within the fragment.
	 * 
	 * @param listener The TeamsListDeleteItemsListener listener that will be
	 * associated with this fragment.
	 */
	public void setDeleteListener(TeamsListDeleteItemListener listener){
		deleteListener = listener;
	}
	
	/**
	 * Gets the boolean value of the deleteEnabled property of the fragment in 
	 * order to know if the delete option is enabled.
	 * Default value is true (enabled)
	 * @return boolean value with the value of the deleteEnabled property
	 */
	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}
	/**
	 * Sets the boolean value of the deleteEnabled property of the fragment in
	 * order to enable/disable the delete option.
	 * If this property is set the fragment must be restarted in order to reflect
	 * the change.
	 * @param deleteEnabled The boolean value to be set in the deleteEnabled 
	 * property of the fragment
	 */
	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	/**
	 * Re-populates the list of items in this fragment. 
	 */
	public void refreshList(){
		findTeamsAndPopulateList();
	}
	
	/**
	 * Executed when the fragment is shown, it finds the teams from the database
	 * and build the requested adapter in order to display the teams in the 
	 * application.
	 */
	private void findTeamsAndPopulateList(){
		setListAdapter(null);
		TeamsLoader loader = new TeamsLoader(teamsDao);
		loader.execute();
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (deleteEnabled){
			MenuInflater inflater = new MenuInflater(getActivity());
			inflater.inflate(R.menu.deelete_ctx_menu, menu);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.delete_update_ctx_menu_delete_item:
			Log.d(TAG, "delete option was selected from the context menu");
			deleteTeamItem(item);
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Deletes the team element that correspond to the selected
	 * menu item from the database.
	 *  
	 * @param item The <code>MenuItem</code> selected that represents
	 * the team to delete
	 */
	private void deleteTeamItem(MenuItem item) {
		if (getListAdapter() != null){
			final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			if (info != null){
				Team t = (Team)getListAdapter().getItem(info.position);
				if (this.deleteListener == null || this.deleteListener.onTeamItemPreDelete(t)){
					//Call the onTeamItemPreDelete if set in order to know if continue or not with the delete
					try {
						teamsDao.delete(t);
						refreshList();
						if (this.deleteListener != null) //send the signal to the exterior (item deleted)
							this.deleteListener.onTeamItemPostDelete(t);
					} catch (SQLException e) {
						Log.e(TAG, "Dao Delete operation failed.",e);
						if (this.deleteListener != null) //Call the exterior to communicate the error
							this.deleteListener.onTeamItemDeleteError(t, e);
					}
				}else{
					Log.d(TAG, "Delete cancelled by onTeamItemPreDelete");
				}
			}else{
				Log.d(TAG, "Unable to determine the position of the item to delete. AdapterContextMenuInfo null");
			}
		}else{
			Log.d(TAG, "deleteTeamItem - The Adapter is set to null delete command will be ignored");
		}
	}

	/**
	 * Interface that will handle the select events occurred with the teams
	 * contained in this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>	
	 */
	interface TeamsListItemSelectedListener{
		/**
		 * The method that will be executed when one item has been selected from
		 * the list.
		 * @param item The Team item selected from the list
		 */
		public void onTeamItemSelected(Team item);
		/**
		 * The method that will be executed when the edit command has been launched
		 * for one item of the list.
		 * @param item The Team item selected from the list.
		 */
		public void onTeamItemEditionStarted(Team item);
	}
	
	/**
	 * Interface that will handle the delete events ocurred within the fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	interface TeamsListDeleteItemListener{
		/**
		 * The method that will be executed before one Team item is deleted
		 * from the list.
		 * @param team The Team item that will be deleted
		 * @return True if everything is ok and the delete command should 
		 * continue, False otherwise.
		 */
		public boolean onTeamItemPreDelete(Team team);
		/**
		 * The method that will be executed after one Team item has been 
		 * deleted from the list.
		 * @param team The Team item that was deleted
		 */
		public void onTeamItemPostDelete(Team team);
		/**
		 * The method that will be executed if the delete command fails
		 * on one item of the list.
		 * @param team The Team item that attempted to be deleted
		 * @param e The Exception that show up during the delete command.
		 */
		public void onTeamItemDeleteError(Team team, Exception e);
	}
	
	
	/**
	 * The async task that will load the teams build the adapter and populate
	 * the teams list.
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	private class TeamsLoader extends AsyncTask<Void, Void, ListAdapter>{

		Dao<Team, Long> dao = null;
		
		public TeamsLoader(Dao<Team, Long> teamsDao){
			if (teamsDao == null)
				throw new IllegalArgumentException("Passed Dao can't be null");
			dao = teamsDao;
		}
		
		@Override
		protected ListAdapter doInBackground(Void... params) {
			try {
				List<Team> teams = dao.queryForAll();
				TeamsListAdapter adapter = new TeamsListAdapter(getActivity(),teams);
				return adapter;
			} catch (SQLException e) {
				Log.e(TAG, "Error quering the dao for all the teams. Unable to populate the list",e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(ListAdapter result) {
			if (result != null)
				setListAdapter(result);
		}
		
	}
	
}
