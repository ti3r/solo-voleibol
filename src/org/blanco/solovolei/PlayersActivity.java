package org.blanco.solovolei;

import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.fragments.ActionBar;
import org.blanco.solovolei.fragments.PlayersAddFragment;
import org.blanco.solovolei.fragments.PlayersListFragment;
import org.blanco.solovolei.fragments.PlayersAddFragment.PlayersAddListener;
import org.blanco.solovolei.fragments.PlayersListFragment.PlayersListCommandsListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import static org.blanco.solovolei.MainActivity.TAG;

public class PlayersActivity extends FragmentActivity 
	implements ActionBar.AddCommandHandler
	, PlayersAddListener, PlayersListCommandsListener
	{
	
	ActionBar actionBar = null;
	/** The fragment that will hold the list of players */
	PlayersListFragment listFragment = null;
	/** The fragment in charge of adding players to db */
	PlayersAddFragment addFragment = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.players_layout);
		init();
	}

	/**
	 * Initializes the GUI components and their listeners
	 */
	private void init(){
		//action bar
		actionBar = (ActionBar) getSupportFragmentManager().
				findFragmentById(R.id.action_bar);
		if (actionBar != null)
			actionBar.setAddCommandHandler(this);
		//retrieve the fragment from the intial view
		listFragment = (PlayersListFragment) getSupportFragmentManager().
			findFragmentById(R.id.teams_layout_fragment_container);
		
		
		//Create a new Add Fragment and add it to the stack
		addFragment = new PlayersAddFragment();
		//editFragment = new TeamsEditFragment();
		//listFragment = new TeamsListFragment();
		
	}
	
	/**
	 * It start the adding process to create a new Team in 
	 * the database
	 */
	private void executeAdd(){
		setFragmentOnFragmentContainer(addFragment);
	}
	
	/**
	 * Sets the appropriate fragment in the fragment container area.
	 * @param fragment The fragment to be placed in the container
	 */
	public void setFragmentOnFragmentContainer(Fragment fragment){
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.remove(getSupportFragmentManager()
				.findFragmentById(R.id.teams_layout_fragment_container));
		
		trans.replace(R.id.teams_layout_fragment_container,fragment);
		trans.commit();
	}

	/**
	 * The implementation of the ActionBar methods
	 */
	@Override
	public void addTriggered() {
		executeAdd();
	}

	@Override
	public void onPlayerAdded(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerAddingError(Player p, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerAddingCanceled() {
		Log.d(TAG, "Players add was cancelled");
		setFragmentOnFragmentContainer(listFragment);
	}
	
	//methods of the PlayersListCommandsListener interface

	@Override
	public boolean onPlayerItemPreDelete(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onPlayerItemPostDelete(Player player) {
		Toast.makeText(getBaseContext(), getString(R.string.player_deleted),
				Toast.LENGTH_SHORT).show();
		listFragment.findPlayersAndPopulateList();
	}

	@Override
	public void onPlayerItemDeleteError(Player player, Exception e) {
		// TODO Auto-generated method stub
		
	}

	//end of methods of the playerslistcommandslistener interface
	
	/*
	@Override
	public void onTeamItemSelected(Team item) {
		//Do nothing
	}

	@Override
	public void onTeamItemEditionStarted(Team item) {
		this.editFragment.setTeam(item);
		setFragmentOnFragmentContainer(editFragment);
	}

	@Override
	public boolean onTeamItemPreDelete(Team team) {
		//Do nothing and return true to continue with the deletion
		return true;
	}

	@Override
	public void onTeamItemPostDelete(Team team) {
		Toast.makeText(getBaseContext(), "Team Deleted", Toast.LENGTH_LONG).show();
		//remove the team that has been deleted from the list
		listFragment.refreshList();
	}
	
	@Override
	public void onTeamItemDeleteError(Team team, Exception e) {
		Log.e(TAG, "TeamDeleteError, fragment returned error while deleting team "+team,e);
		Toast.makeText(getBaseContext(), "Error deleting error", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onTeamItemPreEdit(Team team) {
		//do nothing and continue with the edit action
		return true;
	}

	@Override
	public void onTeamItemPostEdit(Team team) {
		setFragmentOnFragmentContainer(listFragment);
		listFragment.refreshList();
	}

	@Override
	public void onTeamItemEditError(Team team, Exception e) {
		Log.e(TAG, "TeamDeleteError, fragment returned error while deleting team "+team,e);
		Toast.makeText(getBaseContext(), "Error deleting error", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTeamItemEditCancelled() {
		setFragmentOnFragmentContainer(listFragment);
	}
	
	@Override
	public void onTeamAdded(long id, Team t) {
		Toast.makeText(PlayersActivity.this.getApplicationContext(),
				"TeamAdded", Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);
	}

	@Override
	public void onTeamAddingError(Team t, Exception e) {
		Toast.makeText(PlayersActivity.this.getApplicationContext(),
				"Error ading the team."+e.getMessage(), Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);
	}

	@Override
	public void onTeamAddingCancel() {
		Toast.makeText(PlayersActivity.this.getApplicationContext(),
				"Action Cancelled", Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);	
	}
	*/
}
