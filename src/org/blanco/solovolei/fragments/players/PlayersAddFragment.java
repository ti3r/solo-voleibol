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
package org.blanco.solovolei.fragments.players;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;
import org.blanco.solovolei.misc.Animate;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
/**
 * The fragment that will handle all the processes to 
 * add a new Player to the database. This fragment can
 * be attached to any part of the application any time.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class PlayersAddFragment extends Fragment {

	/** The dao to be used in the fragment */
	private Dao<Player, Long> dao = null;
	
	/** The listener to be used in the fragments operation*/
	private PlayersAddListener listener = null;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//retrieve the dao
		dao = (Dao<Player, Long>) DaoFactory.getDao(getActivity(), Player.class);
		super.onCreate(savedInstanceState);
	}
	/**
	 * Creates the view to be displayed to the user in order to create a new
	 * Player in the database.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.players_add_layout, null);
		//set the appropriate listeners for this fragment
		Button btnAccept = (Button) result.findViewById(R.id.players_add_btn_accpet);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				add();
			}
		});
		
		//Button btnClear = (Button) result.findViewById(R.id.teams_add_btn_clear);
		//btnClear.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
		//		clearFields();
		//	}
		//});
		
		Button btnCancel = (Button) result.findViewById(R.id.players_add_btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				communicateCancel();
			}
		});
		ImageButton btnLogo = (ImageButton) result.findViewById(R.id.players_add_btn_photo);
		btnLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickLogo();
			}
		});
		return result;
	}
	
	/**
	 * Method that will pick the logo from the galler or camera in order to attach
	 * it to the new created team. 
	 */
	private void pickLogo() {
		//TODO Create the method to pick the logo from the gallery or cammera
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("image/png");
		//i.addCategory(Intent.CATEGORY_OPENABLE);
		//i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 0);
	}
	
	/**
	 * Method that will be executed when the add process is launched.
	 * This method will communicate the results to the exterior through
	 * the specified listener
	 */
	private void add(){
		Log.d(TAG, "A Add Team command has been received in the TeamsAddFragment");
		//Parse the Player information
		Player t = parsePlayerInfo();
		if (t != null && dao != null){
			try {
				dao.createOrUpdate(t);
				clearFields();
				if (listener != null) //communicate the action
					listener.onPlayerAdded(t);
			} catch (SQLException e) {
				Log.e(TAG, "Error createOrUpdate Team.",e);
				if (listener != null)
					listener.onPlayerAddingError(t, e);
			}
		}else{
			Log.e(TAG, "Error adding Team. Dao is null");
			if (listener != null) //communicate the error
				listener.onPlayerAddingError(t, new Exception("TeamsAdd dao is null"));
		}
	}

	/**
	 * Clears the input fields that are available for the user in order to
	 * leave a clear form ready for reuse
	 */
	private void clearFields(){
		Log.d(TAG, "Clearing fields in the TeamsAddFragment");
		((TextView)getView().findViewById(R.id.players_add_txt_name)).setText("");
		((TextView)getView().findViewById(R.id.players_add_txt_number)).setText("");
	}
	/**
	 * It communicates the cancel action to the established listener.
	 */
	private void communicateCancel() {
		Log.d(TAG, "A cancel method has been received in the PlayersAddFragment");
		if (listener != null)
			listener.onPlayerAddingCanceled();
	}
	
	/**
	 * Parses the information that the user inserts in the form to 
	 * create a new player and creates a new Player object ready to be inserted
	 * @return a <code>Player</code> object with the information entered by the
	 * user
	 */
	private Player parsePlayerInfo() {
		Player p = new Player();
		View failed[] = new View[2];
		EditText txtName = ((EditText)getView().findViewById(R.id.players_add_edt_name)); 
		EditText txtNumber = ((EditText)getView().findViewById(R.id.players_add_edt_number));
		
		if (txtName.getText() != null && txtName.getText().length() > 0){
			p.setName(txtName.getText().toString());
		}else{
			failed[0] = txtName;
		}
		
		if (txtNumber.getText() != null && txtNumber.getText().length() > 0){
			p.setNumber(Integer.parseInt(txtNumber.getText().toString()));
		}else{
			failed[1] = txtNumber;
		}
		
		if (failed[0] != null || failed[1] != null){
			Animate.fadeOut(failed);
			return null;
		}
		
		return p;
	}
	
	/* Getters and setter */
	
	/**
	 * Returns the listener for this Fragment
	 * @return The <code>TeamsAddListener</code> object associated with this fragment
	 */
	public PlayersAddListener getListener() {
		return listener;
	}
	
	/**
	 * Sets the listener for this Fragment
	 * @param listener The <code>TeamsAddListener</code> that will be  associated with 
	 * this fragment
	 */
	public void setListener(PlayersAddListener listener) {
		this.listener = listener;
	}

	/* End of Getters and setter */
	
	/**
	 * When the method is attached to the activity this should implement
	 * TeamsEditListener Interface in order to be set as the listener of
	 * the commands.
	 */
	@Override
	public void onAttach(Activity activity) {
		if(this.listener == null 
				&& (!(activity instanceof PlayersAddListener ))){
			throw new IllegalArgumentException("Attached activity does not implement " +
					"AddActionListener in order to handle the results. " +
					"Please implement this interface in passed activity or set the appropiate listener");
		}
		if (this.listener == null)
			setListener((PlayersAddListener) activity);
		super.onAttach(activity);
	}
	
	/**
	 * Interface to be used when the adding commands are executed
	 * this interface will execute the calls specified to handle
	 * communication with the outside classes in the commands of
	 * this fragment
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	public interface PlayersAddListener{
		/**
		 * The call that will be executed when one team has been successfully added
		 * to the database
		 * @param p The <code>Player</code>  object that has been added to the database.
		 */
		public void onPlayerAdded(Player p);
		/**
		 * The call that will be executed when the add process fails and the player can't
		 * be added to the database.
		 * @param p The incomplete <code>Player</code> object that we tried to add to the 
		 * database
		 * @param e The <code>Exception</code> object that caused the error.
		 */
		public void onPlayerAddingError(Player p, Exception e);
		/**
		 * The call that will be executed then the user cancels the adding process.
		 */
		public void onPlayerAddingCanceled();
		
	}
}
