package org.blanco.solovolei.providers;

import static org.blanco.solovolei.MainActivity.TAG;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class SoloVoleiSQLiteOpenHelper extends OrmLiteSqliteOpenHelper {

	/** The name of the database that will be used in the app */
	private static final String DB_NAME = "solo_volei.db";
	
	
	public SoloVoleiSQLiteOpenHelper(Context context,
			CursorFactory factory, int version) {
		super(context, DB_NAME, factory, version);
	}

	/**
	 * Copies the base data base stored in the assets to a file
	 * stored in the passed path.
	 * @param path The path of the file where to copy the database.
	 */
//	private void copyBasicDataBase(String path) throws IOException{
//		//copy the database from the assets
//				InputStream is = null;
//				OutputStream os = null;
//				try {
//					is = ctx.getAssets().open(DB_NAME);
//					os = new FileOutputStream(path);
//					byte buffer[] = new byte[1024]; 
//					while ( is.read(buffer) > 0 ){
//						os.write(buffer);
//				}
//				}finally{
//					if (is != null)
//						is.close();
//					if (os != null){
//						os.flush();
//						os.close();
//					}
//				}
//	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
		String path = db.getPath();
		Log.i(TAG,"Database will be created on path "+path);
		try{
			db.execSQL("CREATE TABLE TEAMS (_id  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
			db.execSQL("CREATE TABLE PLAYERS (_id  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,number INTEGER NOT NULL);");
			db.execSQL("CREATE TABLE PLATER_OF_TEAM(_id INTEGER PRIMARY KEY AUTOINCREMENT, player_fk INTEGER, team_fk INTEGER);");
			//copyBasicDataBase(path);
		}catch(SQLiteException e){
			Log.e(TAG, "Error creating the database from the base",e);
			throw e;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase ds, ConnectionSource cs, int from,
			int to) {
		throw new RuntimeException("No upgrade process is supported yet");
	}
	
}
