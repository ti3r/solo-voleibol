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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Class that will be in charge of loading photos from the gallery
 * or the camera in order to be used in different parts of the 
 * application and fragments.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class PhotoPicker {

	public Context ctx = null;
	
	public PhotoPicker(Context context){
		if (context == null)
			throw new IllegalArgumentException("Context Can't be null for the PhotoPicker");
		this.ctx = context;
	}
	
	/**
	 * The method that will be in charge of picking the photo and return it
	 * to the user
	 * 
	 * @return The Bitmap of the photo that has been picked or taken 
	 */
	public Bitmap pickPhoto(){
		//launch the intent to pick the photo
		launchIntent();
		return null;
	}
	
	public void launchIntent(){
		Intent i = new Intent(Intent.ACTION_PICK);
		i.setType("image/*");
		ctx.startActivity(i);
	}
	
	
}
