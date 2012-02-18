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
			db.execSQL("CREATE TABLE TEAMS (_id  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, logo BLOB );");
			db.execSQL("CREATE TABLE PLAYERS (_id  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,number INTEGER NOT NULL);");
			db.execSQL("CREATE TABLE PLAYER_OF_TEAM(_id INTEGER PRIMARY KEY AUTOINCREMENT, player_fk INTEGER, team_fk INTEGER);");
			db.execSQL("CREATE TABLE SETS(_id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER, score INTEGER, enemy_score INTEGER);");
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
