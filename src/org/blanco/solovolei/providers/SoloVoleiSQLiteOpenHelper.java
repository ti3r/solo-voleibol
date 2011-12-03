package org.blanco.solovolei.providers;

import static org.blanco.solovolei.MainActivity.TAG;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SoloVoleiSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "solo_volei.db";
	
	Context ctx = null;
	public SoloVoleiSQLiteOpenHelper(Context context,
			CursorFactory factory, int version) {
		super(context, "solo_volei.db", factory, version);
		ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String path = db.getPath();
		Log.i("solo_volei","Database will be created on path "+path);
		try{
			db.execSQL("CREATE TABLE TEAMS (_id INTEGER NOT NULL,name TEXT NOT NULL);");
			db.execSQL("CREATE TABLE PLAYERS (_id INTEGER NOT NULL,name TEXT NOT NULL,number INTEGER NOT NULL,team_fk INTEGER NOT NULL);");
			//db.execSQL("ALTER TABLE PLAYERS ADD CONSTRAINT players_id PRIMARY KEY (_id);");
			//db.execSQL("ALTER TABLE TEAMS ADD CONSTRAINT teams_id PRIMARY KEY (_id);");
			//copyBasicDataBase(path);
		}catch(Exception e){
			Log.e(TAG, "Error creating the database from the base",e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Copies the base data base stored in the assets to a file
	 * stored in the passed path.
	 * @param path The path of the file where to copy the database.
	 */
	private void copyBasicDataBase(String path) throws IOException{
		//copy the database from the assets
				InputStream is = null;
				OutputStream os = null;
				try {
					is = ctx.getAssets().open(DB_NAME);
					os = new FileOutputStream(path);
					byte buffer[] = new byte[1024]; 
					while ( is.read(buffer) > 0 ){
						os.write(buffer);
				}
				}finally{
					if (is != null)
						is.close();
					if (os != null){
						os.flush();
						os.close();
					}
				}
	}
	
}
