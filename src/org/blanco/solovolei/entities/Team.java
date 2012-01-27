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
 * Entity class that will represent one team in the database
 * This class uses ORMLite annotations, the Content provider will
 * be in charge of managing these procedures. 
 *  
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
@DatabaseTable(tableName="TEAMS")
public class Team {
	
	@DatabaseField(dataType=DataType.INTEGER, generatedId=true,columnName="_id")
	private int id;
	@DatabaseField(dataType=DataType.STRING)
	private String name;
	@DatabaseField(dataType=DataType.STRING)
	private String logo;
	
	
	public Team(){
		this(null);
	}
	
	public Team(String string) {
		this.name = string;
	}
	
	/* Getters and Setters */
	/**
	 * Retrieves the <code>int</code> id attribute of the object
	 * @return The <code>int</code> Id of the Object
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets the <code>int</code> id attribute of the object
	 * @param id The <code>int</code> vale to be set in the id attribute
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Retrieves the <code>String</code> name attribute of the object
	 * @return The <code>String</code> name attribute of the object
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the <code>String</code> name attribute of the object
	 * @param name The <code>String</code> vale to be set in the name attribute
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the <code>String</code> representation 
	 * of the logo attribute of the object
	 * @return The <code>String</code> representation of the
	 * logo attribute of the object
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * Sets the <code>Bitmap</code> logo attribute of the object
	 * @param name The <code>Bitmap</code> vale to be set in the logo attribute
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	/* End of Getters and Setters */
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Team){
			return ((Team)o).getId() == this.id &&
			((Team)o).getName().equals(this.name);
		}
		return false;
	}
	@Override
	public String toString() {
		return new StringBuilder("Team[").append(this.id).append(", ").append(this.name)
		.append("]").toString();
	}
	
}
