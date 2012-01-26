package org.blanco.solovolei.loaders;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.entities.Player;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

/**
 * The async task that will load the Players build the adapter and populate
 * the teams list.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 * 
 */
public class PlayersAsyncLoader extends AsyncTask<Void, Void, List<Player>> {

	Dao<Player, Long> dao = null;
	AsyncLoaderListener listener = null;
	
	public PlayersAsyncLoader(Dao<Player, Long> teamsDao) {
		if (teamsDao == null)
			throw new IllegalArgumentException("Passed Dao can't be null");
		dao = teamsDao;
	}

	@Override
	protected List<Player> doInBackground(Void... params) {
		try {
			List<Player> players = dao.queryForAll();
			//PlayersListAdapter adapter = new PlayersListAdapter(getActivity(),
			//		teams);
			return players;
		} catch (SQLException e) {
			Log.e(TAG,
					"Error quering the dao for all the teams. Unable to populate the list",
					e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Player> result) {
		if (listener != null){
			listener.onLoadComplete(result);
		}
	}

	public void setAsyncLoaderListener(AsyncLoaderListener listener){
		this.listener = listener;
	}
	
	/**
	 * Interface to communicate the results of the AsyncLoaders
	 * to the rest of the world.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	public interface AsyncLoaderListener{
		public void onLoadComplete(List<Player> results);
	}
}