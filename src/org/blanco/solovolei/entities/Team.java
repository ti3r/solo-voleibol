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
