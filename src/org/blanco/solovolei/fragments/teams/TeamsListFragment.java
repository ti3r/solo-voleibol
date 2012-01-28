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
package org.blanco.solovolei.fragments.teams;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.gui.adapters.TeamsListAdapter;
import org.blanco.solovolei.loaders.TeamsAsyncLoader;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.j256.ormlite.dao.Dao;

public class TeamsListFragment extends Fragment implements
	TeamsAsyncLoader.AsyncLoaderListener{

	/** The dao that will be used by the fragment */
	Dao<Team, Long> teamsDao = null;
	/** The item selected listener associated with the fragment */
	TeamsListCommandsListener commandsListener = null;
	/** The list view that contains the items */
	ListView list = null;
	/** variable to enable/disable the delete process in the fragment */
	boolean deleteEnabled = true;
	boolean upateEnabled = true;
	/** the loader that will be used to refresh the list */
	TeamsAsyncLoader loader = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		teamsDao = (Dao<Team, Long>) DaoFactory.getDao(getActivity(),
				Team.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.teams_list_layout, null);
		// associate the GUI controls
		list = (ListView) v.findViewById(R.id.teams_list_layout_list);
		return v;
	}

	@Override
	public void onStart() {
		list.setEmptyView(new ProgressBar(getActivity()));// set a progress bar
															// as empty view
		findTeamsAndPopulateList();
		// set the selection listener for the list contained in this fragment
		list.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (commandsListener != null)
					commandsListener
							.onTeamItemSelected((list.getAdapter() != null) ? (Team) list
									.getAdapter().getItem(arg2) : null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				if (commandsListener != null)
					commandsListener.onTeamItemSelected(null);
			}
		});
		registerForContextMenu(list);
		super.onStart();
	}

	/**
	 * Sets the listener class that will execute calls when different select
	 * events arise.
	 * 
	 * @param listener
	 *            The TeamItemSelectionListener listener that will be associated
	 *            with this fragment
	 */
	public void setSelectListener(TeamsListCommandsListener listener) {
		commandsListener = listener;
	}

	/**
	 * Gets the boolean value of the deleteEnabled property of the fragment in
	 * order to know if the delete option is enabled. Default value is true
	 * (enabled)
	 * 
	 * @return boolean value with the value of the deleteEnabled property
	 */
	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}

	/**
	 * Sets the boolean value of the deleteEnabled property of the fragment in
	 * order to enable/disable the delete option. If this property is set the
	 * fragment must be restarted in order to reflect the change.
	 * 
	 * @param deleteEnabled
	 *            The boolean value to be set in the deleteEnabled property of
	 *            the fragment
	 */
	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	/**
	 * Re-populates the list of items in this fragment.
	 */
	public void refreshList() {
		findTeamsAndPopulateList();
	}
	
	/**
	 * Executed when the fragment is shown, it finds the teams from the database
	 * and build the requested adapter in order to display the teams in the
	 * application.
	 */
	private void findTeamsAndPopulateList() {
		list.setAdapter(null);
		if (loader != null && loader.getStatus().equals(AsyncTask.Status.RUNNING))
			loader.cancel(true);
		loader = new TeamsAsyncLoader(teamsDao,this);
		loader.execute();
	}
	
	/**
	 * Method to implement the TeamsAsyncLoader.AsyncLoaderListener onLoadComplete
	 * method. This method will create the adapter for the list and set it to the 
	 * fragments list.
	 * 
	 * @param teams The List of Teams that were found by the loader
	 */
	@Override
	public void onLoadComplete(List<Team> teams){
		TeamsListAdapter adapter = new TeamsListAdapter(getActivity(), teams);
		list.setAdapter(adapter);
	}
	
	/**
	 * Create the context menu with the appropiate options based on the 
	 * established permissions of the fragment
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = new MenuInflater(getActivity());
		if (deleteEnabled) {
			inflater.inflate(R.menu.deelete_ctx_menu, menu);
		}
		if (upateEnabled) {
			inflater.inflate(R.menu.edit_ctx_menu, menu);
		}
	}
	/**
	 * When one item of the menu is selected trigger the appropiate action
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_update_ctx_menu_delete_item:
			Log.d(TAG, "delete option was selected from the context menu");
			deleteTeamItem(item);
			return true;
		case R.id.edit_ctx_menu_edit_item:
			Log.d(TAG, "edit option was selected from context menu");
			editTeamItem(item);
		default:
			return false;
		}
	}
	/**
	 * When this action is triggered it retrieves the selected item from
	 * the list and activates the appropriate listener method with the item.
	 * @param item The MenuItem item selected from the context menu
	 */
	private void editTeamItem(MenuItem item) {
		Team t = retrieveTeamItemFromList(item);
		if (t != null && this.commandsListener != null){
			this.commandsListener.onTeamItemEditionStarted(t);
		}else {
			Log.d(TAG, "Team retrieved from the list is null");
		}
	}

	/**
	 * It retrieved the appropiate Team item from the fragment's list based
	 * on the MenuItem retrieved from the context menu.
	 * @param item The <code>MenuItem</code> item selected from the context
	 * menu
	 * @return A Team object from the fragment's list that was selected with
	 * the context menu.
	 */
	private Team retrieveTeamItemFromList(MenuItem item) {
		Team t = null;
		if (list.getAdapter() != null) {
			final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			if (info != null) {
				t = (Team) list.getAdapter().getItem(info.position);
			} else {
				Log.d(TAG,
						"Unable to determine the position of the item to delete."
								+ " AdapterContextMenuInfo null");
			}
		} else {
			Log.d(TAG,
					"deleteTeamItem - The Adapter is set to null delete command will be ignored");
		}
		return t;
	}

	/**
	 * Deletes the team element that correspond to the selected menu item from
	 * the database.
	 * 
	 * @param item The <code>MenuItem</code> selected that represents the team to
	 *            delete
	 */
	private void deleteTeamItem(MenuItem item) {
		Team t = retrieveTeamItemFromList(item);
		if (t != null) {
			if (this.commandsListener == null
					|| this.commandsListener.onTeamItemPreDelete(t)) {
				// Call the onTeamItemPreDelete if set in order to know if
				// continue or not with the delete
				try {
					teamsDao.delete(t);
					refreshList();
					if (this.commandsListener != null) // send the signal to the
														// exterior (item
														// deleted)
						this.commandsListener.onTeamItemPostDelete(t);
				} catch (SQLException e) {
					Log.e(TAG, "Dao Delete operation failed.", e);
					if (this.commandsListener!= null) // Call the exterior to
														// communicate the error
						this.commandsListener.onTeamItemDeleteError(t, e);
				}
			} else {
				Log.d(TAG, "Delete cancelled by onTeamItemPreDelete");
			}
		} else {
			Log.d(TAG, "Team retrieved from the list is null");
		}
	}

	
	/**
	 * On attach set the activity as listener in order to handle the command results
	 * if the appropiate listener is not set.
	 */
	@Override
	public void onAttach(Activity activity) {
		//Only if the command listener is not set when the activity is attached
		if(this.commandsListener == null 
				&& (!(activity instanceof TeamsListCommandsListener))){
			throw new IllegalArgumentException("Attached Activity does not implement" +
					" The appropiate listeners for this fragment. " +
					"Implement TeamsListCommandsListener or set the appropiate listener");
		}
		if (this.commandsListener ==  null)
			setSelectListener((TeamsListCommandsListener) activity);
		super.onAttach(activity);
	}





	/**
	 * Interface that will handle the select events occurred with the teams
	 * contained in this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface TeamsListCommandsListener {
		/**
		 * The method that will be executed when one item has been selected from
		 * the list.
		 * 
		 * @param item
		 *            The Team item selected from the list
		 */
		public void onTeamItemSelected(Team item);

		/**
		 * The method that will be executed when the edit command has been
		 * launched for one item of the list.
		 * 
		 * @param item
		 *            The Team item selected from the list.
		 */
		public void onTeamItemEditionStarted(Team item);
		
		/**
		 * The method that will be executed before one Team item is deleted from
		 * the list.
		 * 
		 * @param team
		 *            The Team item that will be deleted
		 * @return True if everything is ok and the delete command should
		 *         continue, False otherwise.
		 */
		public boolean onTeamItemPreDelete(Team team);

		/**
		 * The method that will be executed after one Team item has been deleted
		 * from the list.
		 * 
		 * @param team
		 *            The Team item that was deleted
		 */
		public void onTeamItemPostDelete(Team team);

		/**
		 * The method that will be executed if the delete command fails on one
		 * item of the list.
		 * 
		 * @param team
		 *            The Team item that attempted to be deleted
		 * @param e
		 *            The Exception that show up during the delete command.
		 */
		public void onTeamItemDeleteError(Team team, Exception e);
	}

	
}
