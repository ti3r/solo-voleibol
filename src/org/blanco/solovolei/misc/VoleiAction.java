package org.blanco.solovolei.misc;

import android.graphics.Color;


/**
 * The enumeration that represents the possible actions
 * that can occur on a volleyball court.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public enum VoleiAction {
	
	SPIKE (Color.BLUE),
	BLOCK (Color.RED);
	
	/**
	 * The default color for the action.
	 */
	private int color = Color.BLACK;

	VoleiAction(int col){
		this.color = col;
	}
	
	public int getColor(){
		return this.color;
	}
}
