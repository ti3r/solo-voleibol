package org.blanco.solovolei.misc;

import org.blanco.solovolei.fragments.game.CourtFragment;
import org.blanco.solovolei.fragments.game.CourtFragment.OnScoreChangedListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class CourtActionsHandler extends Handler {

	OnScoreChangedListener listener = null;
	
	public CourtActionsHandler(OnScoreChangedListener listener) {
		super();
		this.listener = listener;
	}

	
	
	@Override
	public void handleMessage(Message msg) {
		Bundle b = msg.getData();
		int msgKey = b.getInt(CourtFragment.MSG_ID_ACTION_COURT_SCORE_CHANGED_EVT_KEY,-1);
		switch(msgKey){
			case CourtFragment.MSG_ID_ACTION_COURT_SCORE_CHANGED_VAL:
				//score change message has been sent
				handleScoreChange(msg);
			break;
			case CourtFragment.MSG_ID_ACTION_COURT_SET_ENDED_VAL:
				//Handle the end of the set
				handleSetEnd(msg);
			break;
			case CourtFragment.MSG_ID_ACTION_COURT_MATCH_ENDED_VAL:
				handleMatchEnd(msg);
				break;
		}
	}
	
	private void handleScoreChange(Message message){
		Bundle bundle = message.getData();
		//Retrieve the data
		String action = bundle.getString(CourtFragment.MSG_ID_ACTION_COURT_SCORE_CHANGED_ACTION);
		int teamScore = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_SCORE_CHANGED_TSCORE);
		int foeScore = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_SCORE_CHANGED_FSCORE);
		VoleiAction a = VoleiAction.valueOf(action);
		if (listener.isExecutingTaks()){
			//TODO improve this instead of a delay a listener should be implemented for the listener when it is ready
			Message delayedMessage = new Message();
			delayedMessage.copyFrom(message);
			sendMessageDelayed(delayedMessage, 1000);
		}else{
			listener.onScoreChanged(a, teamScore, foeScore);
		}
	}
	
	private void handleSetEnd(Message message){
		Bundle bundle = message.getData();
		int teamScore = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_SET_ENDED_TSCORE_VAL);
		int foeScore = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_SET_ENDED_FSCORE_VAL);
		if (listener.isExecutingTaks()){
			//TODO improve this instead of a delay a listener should be implemented for the listener when it is ready
			Message delayedMessage = new Message();
			delayedMessage.copyFrom(message);
			sendMessageDelayed(delayedMessage, 1000);
		}else{
			listener.onSetEnded(teamScore, foeScore);
		}
	}
	
	
	private void handleMatchEnd(Message message){
		Bundle bundle = message.getData();
		int teamSets = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_MATCH_ENDED_TSETS_VAL);
		int foeSets = bundle.getInt(CourtFragment.MSG_ID_ACTION_COURT_MATCH_ENDED_FSETS_VAL);
		
		if (listener.isExecutingTaks()){
			//TODO improve this instead of a delay a listener should be implemented for the listener when it is ready
			Message delayedMessage = new Message();
			delayedMessage.copyFrom(message);
			sendMessageDelayed(delayedMessage, 1000);
		}else{
			listener.onGameEnded(teamSets, foeSets);
		}
	}
	
}
