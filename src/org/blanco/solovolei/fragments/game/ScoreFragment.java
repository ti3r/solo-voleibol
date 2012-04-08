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

import org.blanco.solovolei.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
	private int homeSets = 0;
	private int visitSets = 0;
	
	private ImageView htens = null;
	private ImageView hunits = null;
	private ImageView vtens = null;
	private ImageView vunits = null;
	private ImageView hSets = null;
	private ImageView vSets = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RelativeLayout r = (RelativeLayout) inflater.inflate(R.layout.score_layout, null);
		htens = (ImageView) r.findViewById(R.id.score_layout_home_tens);
		hunits = (ImageView) r.findViewById(R.id.score_layout_home_units);
		vtens = (ImageView) r.findViewById(R.id.score_layout_foe_tens);
		vunits = (ImageView) r.findViewById(R.id.score_layout_foe_units);
		hSets = (ImageView) r.findViewById(R.id.score_layout_home_sets);
		vSets = (ImageView) r.findViewById(R.id.score_layout_foe_sets);
		
		return r;
	}
	
	/**
	 * Invalidate the View Portion of the Home Score in order
	 * to reflect a new value in the score.
	 */
	private void invalidateHomeScoreView(){
		
		if (home >= 10){
			setViewNumber(htens, (home / 10));
			htens.invalidate();
		}
		setViewNumber(hunits, (home % 10));
		hunits.invalidate();
	}
	/**
	 * Invalidate the View Portion of the Visit Score in order
	 * to reflect a new value in the score.
	 */
	private void invalidateVisitScoreView(){
		if (visit >= 10){
			setViewNumber(vtens, (visit / 10));
			vtens.invalidate();
		}
		setViewNumber(vunits, (visit % 10));
		vunits.invalidate();
	}
	
	private void setViewNumber(ImageView view, int number){
		switch(number){
		case 0:
			view.setImageResource(R.drawable.zero);
			break;
		case 1:
			view.setImageResource(R.drawable.uno);
			break;
		case 2:
			view.setImageResource(R.drawable.dos);
			break;
		case 3:
			view.setImageResource(R.drawable.tres);
			break;
		case 4:
			view.setImageResource(R.drawable.cuatro);
			break;
		case 5:
			view.setImageResource(R.drawable.cinco);
			break;
		case 6:
			view.setImageResource(R.drawable.seis);
			break;
		case 7:
			view.setImageResource(R.drawable.siete);
			break;
		case 8:
			view.setImageResource(R.drawable.ocho);
			break;
		case 9:
			view.setImageResource(R.drawable.nueve);
			break;
		}
	}
	
	/**
	 * This method will redraw the view displaying the score
	 * No increments will be done calling this method it will
	 * not increment any values. This is important when you
	 * set the score manually to call this method to refresh
	 * the view
	 */
	public void refreshScoreView(){
		invalidateHomeScoreView();
		invalidateVisitScoreView();
	}
	
	/**
	 * Increments the value of the home team by 1
	 */
	public void scoreHome(){
		home++;
		invalidateHomeScoreView();
	}
	/**
	 * Increments the value of the visit team by 1
	 */
	public void scoreVisit(){
		visit++;
		invalidateVisitScoreView();
	}
	/**
	 * Sets the values of the score to 0 - 0
	 */
	public void resetScore(){
		home = 0; visit = 0;
		htens.setImageResource(R.drawable.none);
		hunits.setImageResource(R.drawable.none);
		vtens.setImageResource(R.drawable.none_red);
		vunits.setImageResource(R.drawable.none_red);
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
		invalidateHomeScoreView();
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
		invalidateVisitScoreView();
	}

	/**
	 * Return the int sets that are displayed as win
	 * by the home team
	 * @return the int value of the sets for Home team
	 */
	public int getHomeSets() {
		return homeSets;
	}

	/**
	 * Sets the number of sets displayed as win by the 
	 * home team. This method will cause the view
	 * of the Home sets to be invalidated.
	 * @param homeSets The int value of the sets win
	 */
	public void setHomeSets(int homeSets) {
		this.homeSets = homeSets;
		setViewNumber(hSets, this.homeSets);
	}

	/**
	 * Return the int sets that are displayed as win
	 * by the Visit team
	 * @return the int value of the sets for Visitor team
	 */
	public int getVisitSets() {
		return visitSets;
	}

	/**
	 * Sets the number of sets displayed as win by the 
	 * foe team. This method will cause the view
	 * of the Foe sets to be invalidated.
	 * @param visitSets The int value of the sets win
	 */
	public void setVisitSets(int visitSets) {
		this.visitSets = visitSets;
		setViewNumber(vSets, this.visitSets);
	}
	
	
	
}
