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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Class that will display the scores for two teams or
 * individuals using cool visual resources.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class ScoreFragment extends Fragment {

	private int home = 0;
	private int visit = 0;
	private TextView view = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = new TextView(inflater.getContext());
		view.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		view.setText(home + " - " + visit);
		return view;
	}
	
	/**
	 * Handles the necessary animations and methods to change the view
	 * displaying the score to the new score value 
	 */
	private void changeScore(){
		view.setText(home + " - " + visit);
		view.invalidate();
	}
	/**
	 * Increments the value of the home team by 1
	 */
	public void scoreHome(){
		home++;
		changeScore();
	}
	/**
	 * Increments the value of the visit team by 1
	 */
	public void scoreVisit(){
		visit++;
		changeScore();
	}
	/**
	 * Sets the values of the score to 0 - 0
	 */
	public void resetScore(){
		home = 0; visit = 0;
		changeScore();
	}
	/**
	 * Returns the int value of the score for the Home Team
	 * @return the int value of the score
	 */
	public int getHome() {
		return home;
	}
	/**
	 * Sets the value of the score for the Home Team
	 * @param home The int value for the score
	 */
	public void setHome(int home) {
		this.home = home;
	}
	/**
	 * Returns the int value of the score for the Foe Team
	 * @return the int value of the score
	 */
	public int getVisit() {
		return visit;
	}
	/**
	 * Sets the value of the score for the Foe Team
	 * @param home The int value for the score
	 */
	public void setVisit(int visit) {
		this.visit = visit;
	
	}
	
}
