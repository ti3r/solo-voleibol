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
 * The class that relates One Player to One Team this
 * entity is stored in the XXXXX table of the database
 * in order to manage the Many-to-Many relationship between
 * players and teams
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
@DatabaseTable(tableName="PLAYER_OF_TEAM")
public class PlayerTeam {

	@DatabaseField(dataType=DataType.LONG,generatedId=true, columnName="_id")
	long id;
	
	@DatabaseField(foreign=true, columnName="player_fk")
	Player player = null;
	
	@DatabaseField(foreign=true,columnName="team_fk")
	Team team = null;

	public PlayerTeam() {
		//for ORM Lite
	}
	
	public PlayerTeam(Player player, Team team) {
		super();
		this.player = player;
		this.team = team;
	}

	/**
	 * Returns the player associated with this object
	 * 
	 * @return The Player player object associated with
	 * the object
	 */
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public String toString() {
		return "Relation Player-Team: P["+player.getId()+", T"+team.getId();
	}

	
}
