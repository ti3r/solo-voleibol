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
import org.blanco.solovolei.misc.VoleiAction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

	
	private Button btnSpike;
	private Button btnBlock;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.volei_action_picker_layout, null);
		btnSpike = (Button) v.findViewById(R.id.volei_action_picker_btn_spike);
		btnSpike.setOnClickListener(this);
		btnBlock = (Button) v.findViewById(R.id.volei_action_picker_btn_block);
		btnBlock.setOnClickListener(this);
		return v;
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case  R.id.volei_action_picker_btn_spike:
				//courtFragment.setCourtAction(VoleiAction.SPIKE);
				break;
			case R.id.volei_action_picker_btn_block:
				//courtFragment.setCourtAction(VoleiAction.BLOCK);
		}
	}
	/**
	 * The interface to communicate the events that occur within
	 * this fragment with the outside world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	interface VoleiActionListener{
		public void onActionPicked(VoleiAction action);
	}
}
