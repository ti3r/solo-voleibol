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
package org.blanco.solovolei.fragments.teams;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.AcceptCancelHandler;
import org.blanco.solovolei.fragments.teams.TeamsListFragment.TeamsListCommandsListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
/**
 * The fragment that will handle all the processes to 
 * add a new Team to the database. This fragment can
 * be attached to any part of the application any time.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public class TeamsAddFragment extends Fragment
	implements AcceptCancelHandler{

	/** The dao to be used in the fragment */
	private Dao<Team, Long> dao = null;
	
	/** The listener to be used in the fragments operation*/
	private TeamsAddListener listener = null;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//retrieve the dao
		dao = (Dao<Team, Long>) DaoFactory.getDao(getActivity(), Team.class);
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * Creates the view to be displayed ot the user in order to create a new
	 * team in the database.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.teams_add_layout, null);
		//set the appropriate listeners for this fragment
		Button btnAccept = (Button) result.findViewById(R.id.teams_add_btn_accept);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTeam();
			}
		});
		
		Button btnClear = (Button) result.findViewById(R.id.teams_add_btn_clear);
		btnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearFields();
			}
		});
		
		Button btnCancel = (Button) result.findViewById(R.id.teams_add_btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				communicateCancel();
			}
		});
		ImageButton btnLogo = (ImageButton) result.findViewById(R.id.teams_add_layout_btn_logo);
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
	private void addTeam(){
		Log.d(TAG, "A Add Team command has been received in the TeamsAddFragment");
		//Parse the Team information
		Team t = parseTeamInfo();
		if (t != null && dao != null){
			try {
				dao.createOrUpdate(t);
				clearFields();
				if (listener != null) //communicate the action
					listener.onTeamAdded(t.getId(), t);
			} catch (SQLException e) {
				Log.e(TAG, "Error createOrUpdate Team.",e);
				if (listener != null)
					listener.onTeamAddingError(t, e);
			}
		}else{
			Log.e(TAG, "Error adding Team. Dao is null");
			if (listener != null) //communicate the error
				listener.onTeamAddingError(t, new Exception("TeamsAdd dao is null"));
		}
	}

	/**
	 * Clears the input fields that are available for the user in order to
	 * leave a clear form ready for reuse
	 */
	private void clearFields(){
		Log.d(TAG, "Clearing fields in the TeamsAddFragment");
		((TextView)getView().findViewById(R.id.teams_add_txt_name)).setText("");
	}
	/**
	 * It communicates the cancel action to the established listener.
	 */
	private void communicateCancel() {
		Log.d(TAG, "A cancel method has been received in the TeamsAddFragment");
		if (listener != null)
			listener.onTeamAddingCancel();
	}
	
	/**
	 * Parses the information that the user inserts in the form to 
	 * create a new team and creates a new Team object ready to be inserted
	 * @return a <code>Team</code> object with the information entered by the
	 * user
	 */
	private Team parseTeamInfo() {
		Team t = new Team();
		TextView txtName = ((TextView)getView().findViewById(R.id.teams_add_txt_name)); 
		if (txtName.getText() != null && txtName.getText().length() > 0){
			t.setName(txtName.getText().toString());
			return t;
		}else{
			Animation anim = AnimationUtils.loadAnimation(getActivity(), 
					android.R.anim.fade_out);
			txtName.startAnimation(anim);
			return null;
		}
		
	}
	
	/* Getters and setter */
	
	/**
	 * Returns the listener for this Fragment
	 * @return The <code>TeamsAddListener</code> object associated with this fragment
	 */
	public TeamsAddListener getListener() {
		return listener;
	}
	
	/**
	 * Sets the listener for this Fragment
	 * @param listener The <code>TeamsAddListener</code> that will be  associated with 
	 * this fragment
	 */
	public void setListener(TeamsAddListener listener) {
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
				&& (!(activity instanceof TeamsListCommandsListener))){
			throw new IllegalArgumentException("Attached activity does not implement " +
					"TeamsAddListener in order to handle the results. " +
					"Please implement this interface in passed activity or set the appropiate listener");
		}
		if (this.listener == null)
			setListener((TeamsAddListener) activity);
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
	public interface TeamsAddListener{
		/**
		 * The call that will be executed when one team has been successfully added
		 * to the database
		 * @param id The <code>long</code> id of the new created Team
		 * @param t The <code>Team</code> Team object that has been added to the database.
		 */
		public void onTeamAdded(long id, Team t);
		/**
		 * The call that will be executed when the add process fails and the team can't
		 * be added to the database.
		 * @param t The incomplete <code>Team</code> object that we tried to add to the 
		 * database
		 * @param e The <code>Exception</code> object that caused the error.
		 */
		public void onTeamAddingError(Team t, Exception e);
		/**
		 * The call that will be executed then the user cancels the adding process.
		 */
		public void onTeamAddingCancel();
	}


	/*
	 *Methods of the AcceptCancelHandler interface to handle signals from
	 *the outside world 
	 */
	
	/**
	 * Method to handle the Accept signal from the outside and save
	 * the team.
	 */
	@Override
	public void triggerAccept() {
		//when the accept signal is received trigger the add team method
		addTeam();
	}
	/**
	 * Method to handle the Cancel signal from the outside and save
	 * the team.
	 */
	@Override
	public void triggerCancel() {
		communicateCancel();
	}

	
}
