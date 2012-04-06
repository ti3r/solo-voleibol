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

import java.util.Stack;

import org.blanco.solovolei.PreferenceActivity;
import org.blanco.solovolei.misc.VoleiAction;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
/**
 * The class that will be drawn in the game part of the application
 * to interact with the user and record all the events that are
 * happening in the match. It communicates the results to the 
 * containing parent with the set CourtActionsListener listener;
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class CourtView extends RelativeLayout 
	implements OnTouchListener{

	private Point points[] = new Point[2];
	private Paint paint = null;
	private VoleiAction action = null;
	private int scoreboard = 0;
	private int enemyScoreboard = 0;
	private CourtActionsListener listener = null;
	Stack<ActionTaken> actionsStack = new Stack<CourtView.ActionTaken>();
	private boolean activated = true;
	
	private int goodPointColor = Color.GREEN;
	private int badPointColor = Color.RED;
	
	public CourtView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(R.color.white);
		setOnTouchListener(this);
		//The default action is spike. Volei standards says you
		//should start receiving the ball and make a spike.
		action = VoleiAction.SPIKE;
		//Parse the colors of the actions from the properties
		String bColor = PreferenceManager.getDefaultSharedPreferences(context)
			.getString(PreferenceActivity.PREF_COURT_BAD_ACTION_COLOR_KEY,"#FF0000");
		String gColor = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PreferenceActivity.PREF_COURT_GOOD_ACTION_COLOR_KEY,"#00FF00");
		goodPointColor = Color.parseColor(gColor);
		badPointColor = Color.parseColor(bColor);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor((action.isPointToFavor())? goodPointColor : badPointColor);
		//Set the Stroke Width
		paint.setStrokeWidth(3.5f);
		//set the background of the court
		this.setBackgroundResource(org.blanco.solovolei.R.drawable.court);
	}
	
	/**
	 * Sets the current action that has happened on the court
	 * @param action The VoleiAction to relate to the court
	 */
	public void setAction(VoleiAction action){
		this.action = action;
		//update paint color
		paint.setColor((action.isPointToFavor())? goodPointColor : badPointColor);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (activated){
			Point p = new Point();
			if (event.getAction() == MotionEvent.ACTION_UP){
				p.set((int)event.getX(), (int)event.getY());
				points[(points[0] == null)?0:1] = p;
				v.invalidate();
			}
			return true;
		}else
			return false;
	}

	/**
	 * The on draw method that will handle the necessary
	 * actions to draw the actions that have taken place
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!activated){
			setBackgroundColor(Color.BLACK);
		}else{
		//Draw the points 
		if (points[0] != null && points[1] != null){
			//Draw a line
			canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, paint);
			
			//Add the action taken to the stack
			ActionTaken action = new ActionTaken(this.action, points[0],points[1]);
			actionsStack.push(action);
			
			handleScore(actionsStack.peek());
			
			//both points taken	clear the sequence
			points[0] = null; points[1] = null;
		}else if (points[0] != null){
			//Draw the first point of the sequence
			canvas.drawPoint(points[0].x, points[0].y, paint);
		}
		}
	}
	
	/**
	 * It handles the score of the current match based on the passed
	 * ActionTaken.
	 * @param action The ActionTaken object that represents the last
	 * action taken in the match
	 */
	private void handleScore(ActionTaken action){
		//Two points have been draw, check the action taken and increment the board
		if(action != null && action.voleiAction.isPointToFavor()){
			scoreboard++;
		}else if (this.action != null){
			enemyScoreboard++;
		}
		
		//finish the game, if the scoreboard is complete
		if ((Math.abs(scoreboard - enemyScoreboard) >= 2) &&
			(scoreboard >= 25 ) || (enemyScoreboard >= 25)){
			if (listener != null)
				listener.onSetEnded(scoreboard, enemyScoreboard, actionsStack);
			else
				Log.w(TAG, "Set ended but not listener is defined");
			//clear the action stack
			actionsStack = new Stack<CourtView.ActionTaken>();
			//reset the board
			scoreboard = 0; enemyScoreboard = 0;
		}else{
			//Communicate the change of score to the rest of the world
			if (listener != null){
				listener.onScoreChanged(action.voleiAction, scoreboard, enemyScoreboard);
			}
		}
	}
	
	/**
	 * Returns the CourtActionsListener object associated with this 
	 * view
	 * @return The CourtActionsListener object associated.
	 */
	public CourtActionsListener getCourtActionsListener() {
		return listener;
	}
	
	/**
	 * Sets the CourtActionsListener associated with the view. This
	 * object will be used to communicate the actions that occur in the
	 * view to the exterior world 
	 * @param listener The CourtActionsListener object to be associated
	 */
	public void setCourtActionsListener(CourtActionsListener listener) {
		this.listener = listener;
	}

	/**
	 * it cancels the current action in the court, it invalidates the points
	 * that have been drawn so far
	 */
	public void cancelCurrentAction(){
		if (points[0] != null){
			//TODO sanitize this
			invalidate(new Rect((int)(points[0].x-paint.getStrokeWidth()),
					(int)(points[0].y-paint.getStrokeWidth()),
					(int)(points[0].x+paint.getStrokeWidth()),
					(int)(points[0].y+paint.getStrokeWidth())));
			points[0] = null; 
		}
		if (points[1] != null){
			//This should never happen but just in case
			invalidate(new Rect((int)(points[1].x-paint.getStrokeWidth()),
					(int)(points[1].y-paint.getStrokeWidth()),
					(int)(points[1].x+paint.getStrokeWidth()),
					(int)(points[1].y+paint.getStrokeWidth())));
			points[1] = null;
		}
	}
	
	/**
	 * reverts the last action that has been registered in the court. It
	 * pops the last action from the stack of actions.
	 */
	public void revertLastAction(){
		ActionTaken action = actionsStack.pop();
		if (action.voleiAction.isPointToFavor()){
			scoreboard --;
		}else {
			enemyScoreboard --;
		}
		//invalidate the part of the action if it's still present
		invalidate(new Rect((int)(action.point.x-paint.getStrokeWidth()),
				(int)(action.point.y-paint.getStrokeWidth()),
				(int)(action.point.x+paint.getStrokeWidth()),
				(int)(action.point.y+paint.getStrokeWidth())));
	}
	
	/**
	 * Sets the activated property of the view in order to know if
	 * user can iteract with the view.
	 */
	public void setActivated(boolean activated){
		this.activated = activated;
	}
	
	/***
	 * Class that represents one action in the volleyball court, this
	 * related One VoleiAction object with the points (coordinates)
	 * where it occurred.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public class ActionTaken{
		
		public ActionTaken(VoleiAction action, Point p1, Point p2){
			this.voleiAction = action;
			this.point = p1;
			this.point2 = p2;
		}
		
		public VoleiAction voleiAction = null;
		public Point point = null;
		public Point point2 = null;
	}
	
	/**
	 * The interface that will communicate the events that occur within the court such
	 * as game ended.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	 interface CourtActionsListener{
		/**
		 * Event that will be triggered when the set is Over
		 * @param teamScore The int value of the team score at the end of the set.
		 * @param foeScore The int value of the enemy team score at the end of the set.
		 */
		public void onSetEnded(int teamScore, int foeScore, Stack<ActionTaken> actions);
		/**
		 * The event that will communicate the changes on the score based on the action
		 * that has taken place within the court.
		 * @param action The VoleiAction object that took place in the court
		 * @param teamScore The int score of the home team
		 * @param foeScore The int score of the foe team
		 */
		public void onScoreChanged(VoleiAction action, int teamScore, int foeScore);
	}
}
