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
package org.blanco.solovolei.fragments.game;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.Stack;

import org.blanco.solovolei.PreferenceActivity;
import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Set;
import org.blanco.solovolei.fragments.game.CourtView.CourtActionsListener;
import org.blanco.solovolei.misc.CourtActionsHandler;
import org.blanco.solovolei.misc.VoleiAction;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
/**
 * The fragment in charge of displaying the court and handle the options 
 * that are executed using gestures and icons present above the court.
 * 
 * @author Alexandro Blanco Santana
 *
 */
public class CourtFragment extends Fragment implements CourtActionsListener{
	/**
	 * The key name to store the number of sets per game for this fragment
	 */
	private static final String NUMBER_OF_SETS_PER_GAME_BUNDLE_KEY = "sett_sets_per_game_key";
	public static final String MSG_ID_ACTION_COURT_SCORE_CHANGED_EVT_KEY = "msg_id_action_court_score_changed_event_key";
	
	public static final int MSG_ID_ACTION_COURT_SCORE_CHANGED_VAL = 0;
	public static final int MSG_ID_ACTION_COURT_SET_ENDED_VAL = 1;
	public static final int MSG_ID_ACTION_COURT_MATCH_ENDED_VAL = 2;
	
	public static final String MSG_ID_ACTION_COURT_SCORE_CHANGED_ACTION = "msg_id_action_cout_score_changed_bndl_value_action";
	public static final String MSG_ID_ACTION_COURT_SCORE_CHANGED_TSCORE = "msg_id_action_cout_score_changed_bndl_value_team_score";
	public static final String MSG_ID_ACTION_COURT_SCORE_CHANGED_FSCORE = "msg_id_action_cout_score_changed_bndl_value_foe_score";
	public static final String MSG_ID_ACTION_COURT_SET_ENDED_TSCORE_VAL = "msg_id_action_cout_set_ended_bndl_value_team_score";
	public static final String MSG_ID_ACTION_COURT_SET_ENDED_FSCORE_VAL = "msg_id_action_cout_set_ended_bndl_value_foe_score";
	public static final String MSG_ID_ACTION_COURT_MATCH_ENDED_TSETS_VAL = "msg_id_action_cout_match_ended_bndl_value_team_sets";
	public static final String MSG_ID_ACTION_COURT_MATCH_ENDED_FSETS_VAL = "msg_id_action_cout_match_ended_bndl_value_foe_sets";
	/**
	 * The CourtView that will be inflated within the fragment in order
	 * to let the user register the actions that occurred in the game
	 */
	CourtView view = null;
	/**
	 * The dao to save the sets of the game
	 */
	Dao<Set, Long> dao = null;
	/**
	 * The number of sets that the home team has win
	 */
	private int sets = 0;
	/**
	 * The number of sets that the foe team has win
	 */
	private int foeSets = 0;
	/**
	 * The listener that will handle the score changes out of this
	 * fragment. If this listener is not set the score will
	 * be displayed using Toast 
	 */
	private OnScoreChangedListener listener = null;
	
	private Stack<CourtView.ActionTaken> actionsStack = null; 
	
	private int numberofSetsPerGame = -1;//Default value
	
	CourtActionsHandler courtActionsHandler = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = (Dao<Set, Long>) DaoFactory.getDao(getActivity(), Set.class);
		//Try to load the numbers of sets per fame from the bundle
		if (savedInstanceState != null &&
				savedInstanceState.containsKey(NUMBER_OF_SETS_PER_GAME_BUNDLE_KEY)){
			numberofSetsPerGame = savedInstanceState.getInt(NUMBER_OF_SETS_PER_GAME_BUNDLE_KEY);
		}
		//Retain this fragment on manager activity
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.court, null);
		view = (CourtView) (layout); //layout.findViewById(R.id.court_court_view);
		view.setCourtActionsListener(this);
		return view;
	}	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//Try to set the OnScoreChangedListener listener with the attached interface
		//if no listener has been set yet.
		if (listener == null && activity instanceof OnScoreChangedListener){
			//There is no listener set yet and the activity implements it. Set it
			listener = (OnScoreChangedListener) activity;
			Log.d(TAG, "CourtFragment - Activity implements score change interface. Attacched");
		}else if (listener == null && !(activity instanceof OnScoreChangedListener)){
			Log.d(TAG, "CourtFragment - Activity does implements score change interface."
					+" Toast will be used");
		}
		//Retrieve the number of sets per game if not set onCreate from the bundle
		if(numberofSetsPerGame == -1){
			//load from preference manager
			numberofSetsPerGame = PreferenceManager.getDefaultSharedPreferences(activity)
					.getInt(PreferenceActivity.PREF_SETS_BY_MATCH, 2);
		}
		courtActionsHandler = new CourtActionsHandler(listener);
	}

	/**
	 * Sets the VoleiAction that has been taken place in the
	 * CourtView of this fragment; 
	 * 
	 * @param action The VoleiAction that will be associated 
	 * with the court
	 */
	public void setCourtAction(VoleiAction action){
		view.setAction(action);
	}
	/**
	 * This method will trigger the invalidation of 
	 * the current action in the CourtView in order
	 * to start over.
	 */
	public void resetAction(){
		view.cancelCurrentAction();
	}
	/**
	 * This method will trigger the undo action in the
	 * current court view. It will invalidate the current
	 * action first.
	 */
	public void undoPreviousAction(){
		resetAction();
		view.revertLastAction();
	}
	/**
	 * This method will redraw all the actions that have
	 * happened in the court in order to review them.
	 * @param actions The Stack of ActionTaken object to be redrawn in the court view
	 */
	public void reviewActions(){
		view.setActivated(false);
		view.reviewActions(actionsStack);
	}
	
	//methods that are needed to implement from the CourtActionsListener
	//interface
	
	@Override
	public void onSetEnded(int teamScore, int foeScore, Stack<CourtView.ActionTaken> actions) {
		handleSets(teamScore, foeScore);
		
		try {
			saveSet(teamScore, foeScore, actions);
		} catch (SQLException e) {
			Log.e(TAG, "CourtFragment - Error onSetEnded - saveSet()",e);
		}
		
		//Check if end of the match.
		checkIfEndOfMatch();
	}
	
	/**
	 * Check if the match has ended and tells the listener 
	 */
	private void checkIfEndOfMatch(){
		if (sets == numberofSetsPerGame || foeSets == numberofSetsPerGame){
			//Game Ended Deactivate the view.
			view.setActivated(false);
			view.invalidate();
			if (listener != null){
				Log.d(TAG, "Communucating the end of the game to the rest of the world");
				//listener.onGameEnded(sets,foeSets);
				Message matchEndedMsg = new Message();
				Bundle msgData = new Bundle();
				msgData.putInt(MSG_ID_ACTION_COURT_SCORE_CHANGED_EVT_KEY, 
						MSG_ID_ACTION_COURT_MATCH_ENDED_VAL);
				msgData.putInt(MSG_ID_ACTION_COURT_MATCH_ENDED_TSETS_VAL, sets);
				msgData.putInt(MSG_ID_ACTION_COURT_MATCH_ENDED_FSETS_VAL, foeSets);
				matchEndedMsg.setData(msgData);
				courtActionsHandler.sendMessage(matchEndedMsg);
			}
		}
	}
	
	@Override
	public void onScoreChanged(VoleiAction action, int teamScore, int foeScore) {
		if (listener != null){
			Message msg = new Message();
			Bundle msgData = new Bundle();
			msgData.putInt(MSG_ID_ACTION_COURT_SCORE_CHANGED_EVT_KEY, 
					MSG_ID_ACTION_COURT_SCORE_CHANGED_VAL);
			msgData.putString(MSG_ID_ACTION_COURT_SCORE_CHANGED_ACTION,action.toString());
			msgData.putInt(MSG_ID_ACTION_COURT_SCORE_CHANGED_TSCORE,teamScore);
			msgData.putInt(MSG_ID_ACTION_COURT_SCORE_CHANGED_FSCORE,foeScore);
			//Communicate the action to the rest of the world
			//listener.onScoreChanged(action, teamScore, foeScore);
			msg.setData(msgData);
			courtActionsHandler.sendMessage(msg);
		}else{
			Toast.makeText(getActivity(), teamScore+" - "+foeScore, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onActionsReviewEnded() {
		if (listener != null)
			listener.setExecutingTask(false);
	}
	
	//end of methods that are needed to implement from the CourtActionsListener
	//interface
	/**
	 * Method that handle the sets win by each team according to the
	 * final score passed on the parameters.
	 * @param teamScore The final int value of the score for the home team
	 * @param foeScore The final int value of the score for the foe team
	 */
	private void handleSets(int teamScore, int foeScore) {
		//Communicate the end of the set to the rest of the world
		if (listener != null){
			Message setEndMessage = new Message();
			Bundle msgData = new Bundle();
			msgData.putInt(MSG_ID_ACTION_COURT_SCORE_CHANGED_EVT_KEY, 
					MSG_ID_ACTION_COURT_SET_ENDED_VAL);
			msgData.putInt(MSG_ID_ACTION_COURT_SET_ENDED_TSCORE_VAL, teamScore);
			msgData.putInt(MSG_ID_ACTION_COURT_SET_ENDED_FSCORE_VAL, foeScore);
			setEndMessage.setData(msgData);
			courtActionsHandler.sendMessage(setEndMessage);
			//listener.onSetEnded(teamScore, foeScore);
		}
		//Handle the sets count internally.
		if (teamScore > foeScore){
			sets++;
		} else {
			foeSets ++;
		}
	}

	/**
	 * Save the results of a set in the database using the established dao of the fragment
	 * 
	 * @param teamScore the int Score of the team
	 * @param foeScore The int Score of the foe team
	 * @param actions The CourtView.ActionTaken stack of operations that ocurred during the
	 * set
	 * 
	 * @throws SQLException If something wrong happens during the Set.
	 */
	private void saveSet(int teamScore, int foeScore, Stack<CourtView.ActionTaken> actions)
		throws SQLException{
		//save the set and actions
		Set s = new Set();
		s.setDate(System.currentTimeMillis());
		s.setEnemyScore(foeScore);
		s.setScore(teamScore);
		dao.createOrUpdate(s);
		
		//maintain reference to the actions happened in the set just for review purposes;
		actionsStack = actions;
	}
	
	/**
	 * The interface that will handle the changes on the score of the game. This   
	 * handler will help to communicate the score changes to the rest of the world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface OnScoreChangedListener{
		/**
		 * The method that will handle the event triggered when the score of the game
		 * changed.
		 * @param action The VoleiAction object that triggered the score change
		 * @param teamScore The int value of the score for the home team.
		 * @param foeScore The int value of the score for the visit team.
		 */
		public void onScoreChanged(VoleiAction action, int teamScore, int foeScore);
		/**
		 * The method that will communicate when one set has ended
		 * @param teamScore
		 * @param foeScore
		 */
		public void onSetEnded(int teamScore, int foeScore);
		/***
		 * The method that will communicate when the game is over 
		 */
		public void onGameEnded(int teamSets, int foeSets);
		/**
		 * Return true if some other task is being executed in this listener
		 * so two actions don't overlap each other. For example onSetEnded and
		 * onGameEnded can occur together at the end of the last set. This 
		 * method should be used to check this kind of conditions on the
		 * implementing class
		 * 
		 * @return True if some other call back is being executed at this time
		 * false otherwise.
		 */
		public boolean isExecutingTaks();
		/**
		 * Sets the value to view if the listener is executing tasks, this is used
		 * to communicate task ends between the listener and the
		 * events dispatcher.
		 * @param executing boolean value to communicate is the listener
		 * is executing tasks.
		 */
		public void setExecutingTask(boolean executing);
	}

}
