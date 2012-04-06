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



/**
 * The enumeration that represents the possible actions
 * that can occur on a volleyball court.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public enum VoleiAction {
	/**
	 * A player spikes the ball and it is a good point
	 */
	SPIKE (true),
	/**
	 * A player blocks the enemy's spike and it is a 
	 * good point
	 */
	BLOCK (true), 
	/**
	 * A player blocks the enemy's spike but it goes
	 * out, or the player touches the net so it is a
	 * bad point
	 */
	BAD_BLOCK (false), 
	/**
	 * A player tries to spike the ball but it goes 
	 * out or it does not crosses the net so it is a
	 * bad point.
	 */
	BAD_SPIKE(false);
	
	/**
	 * The property to know if this action produces
	 * a point in favor or con
	 */
	private boolean pointToFavor = true;
	
	/**
	 * Constructor to build a new VoleiAction 
	 * Enumeration Item
	 * @param favor The boolean argument to know if
	 * it is point on favor or not.
	 */
	VoleiAction(boolean favor){
		this.pointToFavor = favor;
	}
	/**
	 * Retrieves the pointToFavor property of the 
	 * Enum Item
	 * @return The boolean value of the pointToFavor
	 * property of the Enum Item.
	 */
	public boolean isPointToFavor(){
		return this.pointToFavor;
	}
}
