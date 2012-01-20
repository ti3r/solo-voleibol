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
package org.blanco.solovolei.misc;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Class that helps with different animations on views
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public final class Animate {

	/**
	 * Create a new fade-out animation and applies the animation to the
	 * passed view
	 * @param view The View object to animate
	 */
	public static void fadeOut(View view){
		Animation anim = AnimationUtils.loadAnimation(view.getContext(), 
				android.R.anim.fade_out);
		view.startAnimation(anim);
	}
	/**
	 * Animate a group a views with the fade-out effect using
	 * Animate.fadeOut(View view) on each non null view  
	 * 
	 * @param views The Array of View objects to animate
	 */
	public static void fadeOut(View[] views){
		for(View v: views){
			if (v != null)
				fadeOut(v);
		}
	}
}
