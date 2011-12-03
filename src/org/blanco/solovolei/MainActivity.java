package org.blanco.solovolei;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    
	/**
	 * The tag used with Log cat methods. 
	 * The simple name of the application.
	 */
	public static final String TAG = "solo_volei";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
}