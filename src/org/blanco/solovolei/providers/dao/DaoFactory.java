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
package org.blanco.solovolei.providers.dao;

import java.sql.SQLException;

import org.blanco.solovolei.MainActivity;
import org.blanco.solovolei.providers.SoloVoleiSQLiteOpenHelper;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

/**
 * The class in charge of building, register and managing the daos 
 * for the application.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class DaoFactory {
	
	/**
	 * The single SQLiteOpenHelper class used in the application 
	 */
	static SoloVoleiSQLiteOpenHelper helper = null;
	
	
	private static void initFactory(Context context){
		//helper = //new SoloVoleiSQLiteOpenHelper(context, null, 1);
		helper = new SoloVoleiSQLiteOpenHelper(context, null, 2);
	}
	
	public static <T> Dao<?, ?> getDao(Context context, Class<T> clas){
		if (helper == null){
			initFactory(context);
		}
		try {
			Dao<?,?> result = 
			//DaoManager.createDao(helper.getConnectionSource(), clas);
			DaoManager.lookupDao(helper.getConnectionSource(), clas);
			if (result == null){
				//then create the dao and register it
				result = DaoManager.createDao(helper.getConnectionSource(), clas);
				DaoManager.registerDao(helper.getConnectionSource(), result);
			}
			return result;
		} catch (SQLException e) {
			Log.e(MainActivity.TAG, "Error looking up for dao to manage: "
					+clas.getCanonicalName(),e);
		}
		return null;
	}
	
}
