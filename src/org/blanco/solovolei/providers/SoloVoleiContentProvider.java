package org.blanco.solovolei.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * The content provider for the entire application, all the requests of data
 * belonging to the database maintained by the application should pass through 
 * this way.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class SoloVoleiContentProvider extends ContentProvider {
	
	/** The application's database */
	SQLiteDatabase db = null;
	
	/**
	 * The base uri to direct petitions for this content provider.
	 * CONTENT_URIS that this provider solves should be built using
	 * this base uri string.
	 */
	static final String SOLO_VOLEI_CONTENT_URI_BASE =
			"content://org.blanco.solovolei";
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		if (Teams.CONTENT_URI.equals(arg0)){
			return Teams.class.getCanonicalName();
		}else if (Players.CONTENT_URI.equals(arg0)){
			return Players.class.getCanonicalName();
		}else return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		db = new SoloVoleiSQLiteOpenHelper(getContext(), null, 1).getWritableDatabase();
		return (db != null)? true : false;
	}

	/**
	 * The query part of the content provider. A query request has arrived from the 
	 * exterior and should be treated. We need to know what type of request has arrived
	 * and send it to the appropriate fetcher.
	 * 
	 * @param uri The 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		/*The client is requesting content from the teams part of the database*/
		if (uri.equals(Teams.CONTENT_URI)){
			cursor = Teams.query(db, projection, selection, selectionArgs, sortOrder);
		/*The client is requesting content from the players part of the database*/
		}else if (uri.equals(Players.CONTENT_URI)){
			cursor = Players.query(db, projection, selection, selectionArgs, sortOrder);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
