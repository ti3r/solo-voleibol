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
package org.blanco.solovolei;

import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.fragments.ActionBar;
import org.blanco.solovolei.fragments.PlayersAddFragment;
import org.blanco.solovolei.fragments.PlayersEditFragment;
import org.blanco.solovolei.fragments.PlayersListFragment;
import org.blanco.solovolei.fragments.PlayersAddFragment.PlayersAddListener;
import org.blanco.solovolei.fragments.PlayersEditFragment.PlayersEditListener;
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
	, PlayersAddListener, PlayersListCommandsListener,
	PlayersEditListener
	{
	
	ActionBar actionBar = null;
	/** The fragment that will hold the list of players */
	PlayersListFragment listFragment = null;
	/** The fragment in charge of adding players to db */
	PlayersAddFragment addFragment = null;
	/** The fragment to edit players */
	PlayersEditFragment editFragment = null;
	
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
		editFragment = new PlayersEditFragment();
		
		
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
		setFragmentOnFragmentContainer(listFragment);
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
		Log.d(TAG, "Error deleting Player",e);
		Toast.makeText(getBaseContext(), 
				getString(R.string.player_delete_error)
				, Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onPlayerItemEditionStarted(Player item) {
		this.editFragment.setPlayer(item);
		setFragmentOnFragmentContainer(editFragment);
	}

	@Override
	public boolean onPlayerItemPreEdit(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onPlayerItemPostEdit(Player player) {
		//reload the list of players and set the list fragment in the container
		listFragment.findPlayersAndPopulateList();
		setFragmentOnFragmentContainer(listFragment);
	}

	@Override
	public void onPlayerItemEditError(Player team, Exception e) {
		Toast.makeText(getBaseContext(), getString(R.string.players_edit_error)
				, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPlayerItemEditCancelled() {
		setFragmentOnFragmentContainer(listFragment);
	}
}
