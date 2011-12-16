package org.blanco.solovolei;

import static org.blanco.solovolei.MainActivity.TAG;

import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.ActionBar;
import org.blanco.solovolei.fragments.teams.TeamsAddFragment;
import org.blanco.solovolei.fragments.teams.TeamsEditFragment;
import org.blanco.solovolei.fragments.teams.TeamsListFragment;
import org.blanco.solovolei.fragments.teams.TeamsAddFragment.TeamsAddListener;
import org.blanco.solovolei.fragments.teams.TeamsEditFragment.TeamsEditListener;
import org.blanco.solovolei.fragments.teams.TeamsListFragment.TeamsListCommandsListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

public class TeamsActivity extends FragmentActivity 
	implements ActionBar.AddCommandHandler,
	TeamsAddListener,
	TeamsListCommandsListener,
	TeamsEditListener{
	
	ImageButton btnAdd = null;
	TeamsListFragment listFragment = null;
	TeamsAddFragment addFragment = null;
	TeamsEditFragment editFragment = null;
	ActionBar actionBar = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.teams_layout);
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
		listFragment = (TeamsListFragment) getSupportFragmentManager().
				findFragmentById(R.id.teams_layout_fragment_container);
		
		
		//Create a new Add Fragment and add it to the stack
		addFragment = new TeamsAddFragment();
		editFragment = new TeamsEditFragment();
	}
	
	/**
	 * It start the adding process to create a new Team in 
	 * the database
	 */
	private void executeAdd(){
		setFragmentOnFragmentContainer(addFragment);
		actionBar.setCommandsType(ActionBar.ACC_TYPE_ACCEPT_CANCEL,addFragment);
	}
	
	/**
	 * Sets the appropriate fragment in the fragment container area.
	 * @param fragment The fragment to be placed in the container
	 */
	private void setFragmentOnFragmentContainer(Fragment fragment){
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		//trans.remove(getSupportFragmentManager()
		//		.findFragmentById(R.id.teams_layout_fragment_container));
		
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
		Toast.makeText(TeamsActivity.this.getApplicationContext(),
				"TeamAdded", Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);
	}

	@Override
	public void onTeamAddingError(Team t, Exception e) {
		Toast.makeText(TeamsActivity.this.getApplicationContext(),
				"Error ading the team."+e.getMessage(), Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);
	}

	@Override
	public void onTeamAddingCancel() {
		Toast.makeText(TeamsActivity.this.getApplicationContext(),
				"Action Cancelled", Toast.LENGTH_LONG).show();
		setFragmentOnFragmentContainer(listFragment);	
	}
}
