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

package org.blanco.solovolei;

import java.util.Locale;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import static org.blanco.solovolei.MainActivity.TAG;
/**
 * The activity in charge of handling the preferences of the application
 * It load the preferences from the settings.xml XML file and displays 
 * the settings to the user. The class also holds static constants to
 * access the preference keys programatically.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class PreferenceActivity 
	extends android.preference.PreferenceActivity
	implements SharedPreferences.OnSharedPreferenceChangeListener{

	/**
	 * The key used to store the preference of the color for
	 * the good actions happened in the court view
	 */
	public static final String PREF_COURT_GOOD_ACTION_COLOR_KEY = 
			"prefs_volei_action_favor_colors";
	
	/**
	 * The key used to store the preference of the color for
	 * the bad actions happened in the court view
	 */
	public static final String PREF_COURT_BAD_ACTION_COLOR_KEY = 
			"prefs_volei_action_con_colors";
	/**
	 * The key used to store the preference of the number of 
	 * sets per match used in the application
	 */
	public static final String PREF_SETS_BY_MATCH = 
			"pref_sets_by_match";
	/**
	 * The key used to store the preference for the application's 
	 * language.
	 */
	public static final String PREF_APP_LANGUAGE = 
			"pref_app_language";
	
	/**
	 * The shared preferences object used to store and retrieve 
	 * the preferences of the application.
	 */
	private SharedPreferences prefs = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
	}

	@Override
	protected void onStart() {
		Object o = getLastNonConfigurationInstance();
		if (o != null && o instanceof SharedPreferences){
			prefs = (SharedPreferences) o;
		}else{
			prefs = PreferenceManager.getDefaultSharedPreferences(this); 
		}
		prefs.registerOnSharedPreferenceChangeListener(this);
		super.onStart();
	}

	@Override
	public Object getLastNonConfigurationInstance() {
		//Save the shared preference object if created
		return prefs;
	}

	/* Listener to change if language has been change and react imediately */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (PREF_APP_LANGUAGE.equalsIgnoreCase(key)){
			//check the new language setting
			String sett = prefs.getString(key, "en_US");
			if (!Locale.getDefault().toString().contains(sett)){
				Log.d(TAG, "Chaning language to: "+sett);
				String lang = sett.split("_")[0];
				Locale.setDefault(new Locale(lang));
			}
		}
	}
	
	
	
}
