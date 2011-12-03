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
		helper = new SoloVoleiSQLiteOpenHelper(context, null, 1);
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
