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
