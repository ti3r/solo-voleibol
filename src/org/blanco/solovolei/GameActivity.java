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
import org.blanco.solovolei.fragments.game.VoleiActionPickerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import org.blanco.solovolei.fragments.game.VoleiActionPickerFragment.VoleiActionListener;
import org.blanco.solovolei.misc.VoleiAction;

public class GameActivity extends FragmentActivity 
	implements VoleiActionListener{

	CourtFragment courtFragment = null;
	VoleiActionPickerFragment actionPickFragment = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.court_layout);
		
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		switch (fragment.getId()) {
		case R.id.court_layout_court_fragment:
			courtFragment = (CourtFragment) fragment;
			break;
		case R.id.court_layout_action_pick_fragment:
			actionPickFragment = (VoleiActionPickerFragment) fragment;
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
	
}
