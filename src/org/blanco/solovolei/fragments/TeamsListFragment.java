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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.j256.ormlite.dao.Dao;

public class TeamsListFragment extends ListFragment {

	Dao<Team, Long> teamsDao = null;
	TeamItemSelectionListener selectListener = null;
	
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
				if (selectListener != null)
					selectListener.onTeamItemSelected((getListAdapter() != null)?
							(Team) getListAdapter().getItem(arg2): null);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				if (selectListener != null)
					selectListener.onTeamItemSelected(null);
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
	public void setSelectListener(TeamItemSelectionListener listener){
		selectListener = listener;
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
		MenuInflater inflater = new MenuInflater(getActivity());
		inflater.inflate(R.menu.delete_update_ctx_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.delete_update_ctx_menu_delete_item:
			Log.d(TAG, "delete option was selected from the context menu");
			deleteTeamItem(item);
			return true;
		case R.id.delete_update_ctx_menu_update_item:
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
			//TODO replace this item number with the actual number of the selected item.
			Team t = (Team)getListAdapter().getItem(0);
			try {
				teamsDao.delete(t);
				refreshList();
			} catch (SQLException e) {
				Log.e(TAG, "Dao Delete operation failed.",e);
			}
		}else{
			Log.d(TAG, "The Adapter is set to null delete command will be ignored");
		}
	}




	/**
	 * Interface that will handle the select events occurred with the teams
	 * contained in this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>	
	 */
	interface TeamItemSelectionListener{
		public void onTeamItemSelected(Team item);
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
