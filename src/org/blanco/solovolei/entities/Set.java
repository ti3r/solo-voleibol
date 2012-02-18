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
package org.blanco.solovolei.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entity class that will represent one SET of a match in the database
 * This class uses ORMLite annotations, the Content provider will
 * be in charge of managing these procedures. 
 *  
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
@DatabaseTable(tableName="SETS")
public class Set {

	@DatabaseField(dataType=DataType.INTEGER, generatedId=true,columnName="_id")
	private long _id;
	@DatabaseField(dataType=DataType.INTEGER, columnName="date")
	private long date;
	@DatabaseField(dataType=DataType.INTEGER, columnName="score")
	private int score;
	@DatabaseField(dataType=DataType.INTEGER, columnName="enemy_score")
	private int enemyScore;
	
	/**
	 * Returns the long id of the object 
	 * @return the long value of the id property
	 */
	public long get_id() {
		return _id;
	}
	/**
	 * Sets the long id property of the object
	 * @param _id the long value to be set
	 */
	public void set_id(long _id) {
		this._id = _id;
	}
	/**
	 * Returns the long representation of the date
	 * associated with this object.
	 * @return the long value of the date property
	 */
	public long getDate() {
		return date;
	}
	/**
	 * Sets the long value of the date of this object
	 * @param date the long value to be set on the date property
	 */
	public void setDate(long date) {
		this.date = date;
	}
	/**
	 * Returns the final score of the team in this set 
	 * @return the int score value
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Sets the int value of the final score for this set 
	 * @param score the int value of the score property
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * Returns the final score of the enemy team in this set 
	 * @return the int score value
	 */
	public int getEnemyScore() {
		return enemyScore;
	}
	/**
	 * Sets the int value of the final score of the enemy team
	 * for this set 
	 * @param score the int value of the score property
	 */
	public void setEnemyScore(int enemyScore) {
		this.enemyScore = enemyScore;
	}
	
	/**
	 * Returns a string representation of the object and
	 * its property at the current time.
	 */
	@Override
	public String toString() {
		return "Set: "+_id+", Score: "+score+" vs "+enemyScore;
	}

	
}
