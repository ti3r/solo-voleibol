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
