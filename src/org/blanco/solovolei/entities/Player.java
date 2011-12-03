package org.blanco.solovolei.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entity class that will represent one player in the database
 * This class uses ORMLite annotations, the Content provider will
 * be in charge of managing these procedures.
 *  
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
@DatabaseTable(tableName="PLAYERS")
public class Player {
	
	/* attributes */
	@DatabaseField(dataType=DataType.INTEGER, generatedId=true,columnName="_id")
	private long id;
	@DatabaseField(dataType=DataType.STRING)
	private String name;
	@DatabaseField(dataType=DataType.INTEGER)
	private int number;
	/* end of attributes */
	
	/* getters and setters */
	/**
	 * Retrieves the <code>long</code> id attribute of the object
	 * @return The <code>long</code> Id of the Object
	 */
	public long getId() {
		return id;
	}
	/**
	 * Sets the <code>long</code> id attribute of the object
	 * @param id The <code>long</code> vale to be set in the id attribute
	 */
	public void setId(long id) {
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
	 * Retrieves the <code>int</code> number attribute of the object
	 * @return The <code>int</code> name of the Object
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Sets the <code>int</code> name attribute of the object
	 * @param id The <code>int</code> vale to be set in the name attribute
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/* getters and setters */
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Player){
			return ((Player)o).getId() == this.id &&
			((Player)o).getName().equals(this.name) &&
			((Player)o).getNumber() == this.number;
		}
		return false;
	}
	@Override
	public String toString() {
		return new StringBuilder("Player[").append(this.id).append(", ").append(this.name)
		.append(", ").append(this.number).append("]").toString();
	}
	
}
