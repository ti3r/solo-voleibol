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
import org.blanco.solovolei.fragments.teams.TeamsListFragment.TeamsListCommandsListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.j256.ormlite.dao.Dao;
/**
 * The fragment in charge of editing one team and save it
 * to the database, this fragment must be started with a
 * specific team id or Team object in order to present the
 * information of the team to the user.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com> 
 *
 */
public class TeamsEditFragment extends Fragment 
	implements View.OnClickListener{

	/** the Team object to be edited */
	Team team = null;
	/** The dao that will be used by the fragment */ 
	Dao<Team, Long> teamsDao = null;
	TeamsEditListener listener = null;
	
	/* GUI controls*/
	private EditText txtName = null;
	private Button accept = null;
	private Button cancel = null;
	private ImageButton btnLogo = null;
	/* end of GUI controls */
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		teamsDao = (Dao<Team, Long>) DaoFactory.getDao(getActivity(), Team.class);
		super.onCreate(savedInstanceState);
	}

	/**
	 * Set the Team object to be edited and persisted to the database.
	 * 
	 * @param team The Team object to be edited by the fragment
	 */
	public void setTeam(Team team){
		this.team = team;
	}
	/**
	 * Sets the listener that will handle the results of the edit
	 * process
	 * @param listener The TeamsEditListener listener that will handle
	 * the results of the edit process
	 */
	public void setTeamsEditListener(TeamsEditListener listener){
		this.listener = listener;
	}

	/**
	 * On create view of the fragment, it build the view needed to present
	 * the Team object associated with the fragment and the controls
	 * launch the edit process.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.teams_edit_layout, null);
		//establish the links of the controls
		txtName = (EditText) v.findViewById(R.id.teams_edit_edt_name);
		txtName.setText(team.getName());
		accept = (Button) v.findViewById(R.id.teams_edit_btn_accept);
		accept.setOnClickListener(this);
		cancel = (Button) v.findViewById(R.id.teams_edit_btn_cancel);
		cancel.setOnClickListener(this);
		btnLogo = (ImageButton) 
				v.findViewById(R.id.teams_edit_layout_btn_logo);
		btnLogo.setOnClickListener(this);
		//Load the values from the bundle if exist
		if (savedInstanceState != null){
			txtName.setText(savedInstanceState.getString("name"));
		}
		return v;
	}
	
	
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
					"TeamsEditListener in order to handle the results. " +
					"Please implement this interface in passed activity or set the appropiate listener");
		}
		if (this.listener == null)
			setTeamsEditListener((TeamsEditListener) activity);
		super.onAttach(activity);
	}
	/**
	 * The method that will be executed when the Edit process is 
	 * cancelled. This passes the result to the TeamsEditListener
	 * Appropriate method if set.
	 */
	private void cancelEdit(){
		if (listener != null)
			listener.onTeamItemEditCancelled();
	}
	/**
	 * The method that will be executed when the Edit process is 
	 * accepted. It executes the onTeamItemPreEdit method of the
	 * set TeamsEditListener listener if set, then if the result
	 * is positive it persists the new team on the database
	 * and passes the results to the appropriate method of the 
	 * listener.
	 */
	private void acceptEdit(){
		team.setName(txtName.getText().toString());
		try {
			if (listener == null || listener.onTeamItemPreEdit(team))
				teamsDao.update(team);
			if (listener != null)
				listener.onTeamItemPostEdit(team);
		} catch (SQLException e) {
			Log.e(TAG, "Error updating team",e);
			if (listener != null)
				listener.onTeamItemEditError(team, e);
		}
	}
	
	/**
	 * Launch the intent to pick a photo from the gallery 
	 * or the camera. The result must be treated in the 
	 * onActivityResult method of the fragment.
	 */
	private void pickPhoto(){
		//TODO finish implementing this.
		Intent i = new Intent(Intent.ACTION_PICK);
		i.setType("image/*");
		startActivityForResult(i, 0);
	}
	
	/**
	 * Parses the result of the Intent ACTION_PICK in order
	 * to retrieve the path of the image that has been selected
	 * 
	 * @param intent The intent that resulted from the ACTION_PICK
	 * action
	 * @return String containing the path of the selected image
	 */
	private String parseImage(Intent intent){
		Uri data = intent.getData();
		
		Cursor cursor = getActivity().managedQuery(data, 
				new String[]{MediaStore.Images.Media.DATA}, null, null, null);
		
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String image = cursor.getString(column_index);
        cursor.close();
        return image;
	}
	
	/**
	 * On activity result and request code was pick photo, and result
	 * is ok. Create the team logo.
	 */
	@Override
	public void onActivityResult(int requestCode, 
			int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK){
			String path = parseImage(data);
			btnLogo.setImageBitmap(Bitmap.createBitmap(
					BitmapFactory.decodeFile(path), 0, 0, btnLogo.getWidth(), 
					btnLogo.getHeight(), null, false));
			team.setLogo(path);
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}



	/**
	 * Interface to handle the edit events that happen within the fragment 
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>	
	 */
	public interface TeamsEditListener{
		public boolean onTeamItemPreEdit(Team team);
		
		public void onTeamItemPostEdit(Team team);
		
		public void onTeamItemEditError(Team team, Exception e);
		
		public void onTeamItemEditCancelled();
	}


	//Method of the OnClickListener interface in order to keep 
	//trach of the touch events within the fragment (ex. buttons)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.teams_edit_btn_accept:
			acceptEdit();
			break;
		case R.id.teams_edit_btn_cancel:
			cancelEdit();
			break;
		default:
			Log.i(TAG, "Touch event registered and no id of buttons " +
					"match. Event Ignored");
			break;
		}
	}
}
