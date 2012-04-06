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

import org.blanco.solovolei.R;
import org.blanco.solovolei.misc.ExpandAnimation;
import org.blanco.solovolei.misc.VoleiAction;

import roboguice.inject.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * The Fragment in charge of Displaying to the user
 * the possible VoleiActions that can be picked 
 * and associated to the court.
 * This class is basically in charge of displaying the
 * values of the VoleiAction enum in a beautiful way
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class VoleiActionPickerFragment extends Fragment 
	implements View.OnClickListener{

	/**
	 * The button that activates the SPIKE VoleiAction
	 */
	private Button btnSpike;
	/**
	 * The button that activates the BLOCK VoleiAction
	 */
	private Button btnBlock;
	/**
	 * The button that activates the BAD_BLOCK VoleiAction
	 */
	private Button btnBadBlock;
	/**
	 * The button that activated the BAD_SPIKE VoleiAction
	 */
	private Button btnBadSpike;
	
	/**
	 * The button at the border of the fragment to hide and
	 * show the fragment
	 */
	private ImageButton btnShowHide;
	
	/**
	 * The VoleiActionListener to communicate the actions
	 * Occurred within the fragment
	 */
	private VoleiActionListener listener = null;
	
	/**
	 * The initial height of the Fragment's view in order to 
	 * handle the show hide functionality
	 */
	private int initialHeight = -1;
	/**
	 * The RelativeLayout that contains the action buttons 
	 * This view will be animated in order to display or
	 * hide the action buttons.
	 */
	RelativeLayout lytButtonsContainer = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.volei_action_picker_layout, null);
		lytButtonsContainer = (RelativeLayout) v.
				findViewById(R.id.volei_action_picker_lyt_buttons_container);
		btnSpike = (Button) v.findViewById(R.id.volei_action_picker_btn_spike);
		btnSpike.setOnClickListener(this);
		btnBlock = (Button) v.findViewById(R.id.volei_action_picker_btn_block);
		btnBlock.setOnClickListener(this);
		btnBadBlock = (Button) v.findViewById(R.id.volei_action_picker_btn_bad_block);
		btnBadBlock.setOnClickListener(this);
		btnShowHide = (ImageButton) v.findViewById(R.id.volei_action_picker_btn_show_hide);
		btnShowHide.setOnClickListener(this);
		btnBadSpike = (Button) v.findViewById(R.id.volei_action_picker_layout_btn_bad_spike);
		btnBadSpike.setOnClickListener(this);
		return v;
	}
	



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (listener == null && 
				!(activity instanceof VoleiActionListener)){
			throw new IllegalArgumentException("Attached activity does not implement"
					+" VoleiActionListener and not listener has been set up previously."
					+" Plase make the activity implement the interface or set up the "
					+" Correct listener in order to handle the events of the fragment");
		}else if (listener == null){
			listener = (VoleiActionListener) activity;
		}
	}

	@Override
	public void onClick(View v) {
		if (listener != null){
			switch(v.getId()){
				case  R.id.volei_action_picker_btn_spike:
					listener.onActionPicked(VoleiAction.SPIKE);
					break;
				case R.id.volei_action_picker_btn_block:
					listener.onActionPicked(VoleiAction.BLOCK);
					break;
				case R.id.volei_action_picker_btn_bad_block:
					listener.onActionPicked(VoleiAction.BAD_BLOCK);
					break;
				case R.id.volei_action_picker_layout_btn_bad_spike:
					listener.onActionPicked(VoleiAction.BAD_SPIKE);
				case R.id.volei_action_picker_btn_show_hide:
					//execute show and hide logic
					showHide();
					break;
			}
		}else{
			Log.w(TAG, "VoleiAction selected but not listener established to handle it");
		}
	}
	
	/**
	 * Method to decrease and increase the height of the fragments view
	 * in order to hide and show the actions.
	 */
	private void showHide(){
		//the initial height has not been initialized yet. Save it
		if (initialHeight == -1){
			initialHeight = getView().getHeight();
			Log.d(TAG, "Initial height of the view: "+initialHeight);
		}
		ExpandAnimation anim = new ExpandAnimation(lytButtonsContainer, 
				lytButtonsContainer.getHeight(), 
				(initialHeight != lytButtonsContainer.getHeight())? initialHeight : 2);
		lytButtonsContainer.setAnimation(anim);
		lytButtonsContainer.startAnimation(anim);
		//Change the icon of the button
		btnShowHide.setImageDrawable(getResources().getDrawable(
				(initialHeight != lytButtonsContainer.getHeight())?
				android.R.drawable.arrow_up_float:
				android.R.drawable.arrow_down_float));
		
	}
	
	
	/**
	 * The interface to communicate the events that occur within
	 * this fragment with the outside world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	public interface VoleiActionListener{
		/**
		 * Method to be executed when one VoleiAction is selected
		 * @param action The VoleiAction that has been selected.
		 */
		public void onActionPicked(VoleiAction action);
	}
}
