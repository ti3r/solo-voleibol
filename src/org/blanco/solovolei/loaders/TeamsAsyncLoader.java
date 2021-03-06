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
package org.blanco.solovolei.loaders; 

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;
import java.util.List;

import org.blanco.solovolei.entities.Team;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

/**
	 * The async task that will load the teams build the adapter and populate
	 * the teams list.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 * 
	 */
	public class TeamsAsyncLoader extends AsyncTask<Void, Void, List<Team>> {

		Dao<Team, Long> dao = null;
		AsyncLoaderListener listener = null;
		
		public TeamsAsyncLoader(Dao<Team, Long> teamsDao, AsyncLoaderListener listener) {
			if (teamsDao == null)
				throw new IllegalArgumentException("Passed Dao can't be null");
			dao = teamsDao;
			this.listener = listener;
		}

		@Override
		protected List<Team> doInBackground(Void... params) {
			try {
				List<Team> teams = dao.queryForAll();
				return teams;
			} catch (SQLException e) {
				Log.e(TAG,
						"Error quering the dao for all the teams. Unable to populate the list",
						e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Team> result) {
			if (listener != null)
				listener.onLoadComplete(result);
			else
				Log.d(TAG, "TeamsAsyncLoader. Listener is null, results will be ignored");
		}

		
		public interface AsyncLoaderListener{
			public void onLoadComplete(List<Team> teams);
		}
		
	}
