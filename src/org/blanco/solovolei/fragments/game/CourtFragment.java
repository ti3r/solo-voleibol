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
import org.blanco.solovolei.misc.CourtView;
import org.blanco.solovolei.misc.VoleiAction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * The fragment in charge of displaying the court and handle the options 
 * that are executed using gestures and icons present above the court.
 * 
 * @author Alexandro Blanco Santana
 *
 */
public class CourtFragment extends Fragment{
	/**
	 * The CourtView that will be inflated within the fragment in order
	 * to let the user register the actions that occurred in the game
	 */
	CourtView view = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (CourtView) inflater.inflate(R.layout.court, null);
		return view;
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
	 * This method will invalidate the current action 
	 * in the CourtView in order to start over.
	 */
	public void resetAction(){
		view.reset();
	}
	
}
