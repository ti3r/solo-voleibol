package org.blanco.solovolei.misc;

import org.blanco.solovolei.fragments.game.CourtFragment.OnScoreChangedListener;

import android.util.Log;
import static org.blanco.solovolei.MainActivity.TAG;

public class ScoreChangedRunnable implements Runnable {

	OnScoreChangedListener scoreChangedListener = null;
	VoleiAction action = null;
	int teamScore = -1;
	int foeScore = -1;
	
	public ScoreChangedRunnable(OnScoreChangedListener scoreChangedListener) {
		if (scoreChangedListener == null || action == null)
			throw new IllegalArgumentException("OnScoreChangedListener can't " +
					"be null for ScoreChangedRunnable");
		this.scoreChangedListener = scoreChangedListener;
	}

	@Override
	public void run() {
		if (scoreChangedListener == null || action == null || teamScore == -1 || foeScore == -1){
			throw new IllegalStateException("Illegal Values for this state of scoreChangedListener. " +
					"Set the correct values");
		}
		synchronized (this) {
			while(scoreChangedListener.isExecutingTaks()){
				try {
					wait(500);
				} catch (InterruptedException e) {
					Log.e(TAG, "InterruptedException on run for scoreChangeListener. Task will be executed",e);
				}
			}
			scoreChangedListener.onScoreChanged(action, teamScore, foeScore);
		}
	}

	public OnScoreChangedListener getScoreChangedListener() {
		return scoreChangedListener;
	}

	public void setScoreChangedListener(OnScoreChangedListener scoreChangedListener) {
		this.scoreChangedListener = scoreChangedListener;
	}

	public VoleiAction getAction() {
		return action;
	}

	public void setAction(VoleiAction action) {
		this.action = action;
	}

	public int getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(int teamScore) {
		this.teamScore = teamScore;
	}

	public int getFoeScore() {
		return foeScore;
	}

	public void setFoeScore(int foeScore) {
		this.foeScore = foeScore;
	}

	public void setScore(int teamScore, int foeScore){
		this.teamScore = teamScore;
		this.foeScore = foeScore;
	}
	
}
