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

import static org.blanco.solovolei.MainActivity.TAG;

import org.blanco.solovolei.fragments.game.CourtFragment;
import org.blanco.solovolei.fragments.game.ScoreFragment;
import org.blanco.solovolei.fragments.game.VoleiActionPickerFragment;
import org.blanco.solovolei.fragments.game.VoleiActionPickerFragment.VoleiActionListener;
import org.blanco.solovolei.misc.VoleiAction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class GameActivity extends FragmentActivity 
	implements VoleiActionListener, CourtFragment.OnScoreChangedListener{

	/**
	 * The String action name used to launch the activity
	 */
	public static final String INTENT_ACTION = "org.blanco.solovolei.GAME";
	/**
	 * The fragment in charge of displaying and handling the court & actions
	 */
	CourtFragment courtFragment = null;
	/**
	 * The fragment in charge of displaying and handling the selection of
	 * VoleiActions
	 */
	VoleiActionPickerFragment actionPickFragment = null;
	/**
	 * The fragment in charge of displaying the score
	 */
	private ScoreFragment scoreFragment = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.game_layout);
		
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		switch (fragment.getId()) {
		
		case R.id.court_layout_court_fragment:
			courtFragment = (CourtFragment) fragment;
			break;
		case R.id.court_layout_action_pick_fragment:
			actionPickFragment = (VoleiActionPickerFragment) fragment;
			break;
		case R.id.court_layout_score_fragment:
			scoreFragment = (ScoreFragment)fragment;
		default:
			Log.w(TAG, "Attached fragment has not recognized id: "+fragment);
			break;
		}
	}

	@Override
	public void onActionPicked(VoleiAction action) {
		if (courtFragment != null){
			courtFragment.resetAction();
			courtFragment.setCourtAction(action);
		}else{
			Log.w(TAG, "VoleiActionPicked but court fragment is not set yet");
		}
	}

	@Override
	public void onUndoPreviousAction(){
		if (courtFragment != null){
			courtFragment.undoPreviousAction();
		}else{
			Log.w(TAG, "Undo action was selected but no court fragment has " +
					"been set in the game to handle the event");
		}
	}
	
	private void askForReview(){
//		AlertDialog dialog = new AlertDialog.Builder(this).create();
//		dialog.setTitle(R.string.solo_volei);
//		dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.accept),
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//		dialog.setMessage(getString(R.string.set_review_actions_ques));
//		dialog.show();
		courtFragment.reviewActions();
	}
	
	//Implementation of the CourtFragment OnScoreChangedListener
	@Override
	public void onScoreChanged(VoleiAction action, int teamScore, int foeScore) {
		if (action.isPointToFavor()){
			scoreFragment.setHome(teamScore);
		}else{
			scoreFragment.setVisit(foeScore);
		}
	}

	@Override
	public void onSetEnded(final int teamScore, final int foeScore) {
		//Set the final score
		scoreFragment.setHome(teamScore);
		scoreFragment.setVisit(foeScore);
		scoreFragment.refreshScoreView();
		
		//Broadcast the message to Reset the score and increment the sets
		scoreFragment.getView().postDelayed(new Runnable(){
			@Override
			public void run() {
				scoreFragment.resetScore();
				if (teamScore > foeScore)
					scoreFragment.setHomeSets(scoreFragment.getHomeSets()+1);
				else
					scoreFragment.setVisitSets(scoreFragment.getVisitSets()+1);
			}
		}, 5000);
		//Toast.makeText(this, "Set Ended", Toast.LENGTH_LONG).show();
		//ask for review of the set.
		askForReview();
	}

	@Override
	public void onGameEnded() {
		//TODO implement this correctly
		this.finish();
	}
	
	//End of Implementation of the CourtFragment OnScoreChangedListener
	
}

