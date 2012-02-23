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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * The principal activity of the application
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class MainActivity extends FragmentActivity
	implements OnClickListener{
    
	/**
	 * The tag used with Log cat methods. 
	 * The simple name of the application.
	 */
	public static final String TAG = "solo_volei";
	
	private Button btnStartGame = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    /**
     * Inits the properties of this class
     */
    private void init(){
    	btnStartGame = (Button) findViewById(R.id.main_btn_start_game);
    	btnStartGame.setOnClickListener(this);
    	((Button)findViewById(R.id.main_btn_preference)).setOnClickListener(this);
    }
	/**
	 * The on click implementation that will handle the clicks
	 * of the buttons for this activity 
	 * @param v The View that has been clicked
	 */
    @Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.main_btn_start_game:
			Intent i = new Intent(GameActivity.INTENT_ACTION);
			startActivity(i);
			break;
			case R.id.main_btn_preference:
				Intent i2 = new Intent(this,PreferenceActivity.class);
				startActivity(i2);
		}
		
	}
	
}