package org.blanco.solovolei.providers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Interface that will hold the base definitions to retrieve teams
 * from the content provider of the application.
 *  
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class Teams {
	
	/**
	 * The Uri used to retrieve teams from the application's content provider
	 */
	public static final Uri CONTENT_URI = Uri.parse( 
			SoloVoleiContentProvider.SOLO_VOLEI_CONTENT_URI_BASE+"/teams");
	
	/**
	 * The name of the table to consult by the provider to return information for this
	 * type
	 */
	private static final String TABLE_NAME = "TEAMS";
	
	/* Column names */
	
	/** The name of the ID column */
	public static final String ID = "_id";
	
	/** The name of the ID column */
	public static final String NAME = "name";
	
	/* End of Column names */
	
	/**
	 * Gets a cursor of teams based on the passed query criteria
	 * @param projection The String Array of the column names to be fetch
	 * @param selection The String criteria for the selection ex, where _id=?
	 * @param selectionArgs The String Array with the values of the criteria parameters
	 * @param sortOrder The String value for the order by clause;
	 * 
	 * @return a Cursor with the results that match the passed criteria.
	 */
	static Cursor query(SQLiteDatabase db, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder){
		/*** Create mock results*/
		Cursor c = null;
		c =	db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}
	
}