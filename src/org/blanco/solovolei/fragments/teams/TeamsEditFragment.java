package org.blanco.solovolei.fragments.teams;

import static org.blanco.solovolei.MainActivity.TAG;

import java.sql.SQLException;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.teams.TeamsListFragment.TeamsListCommandsListener;
import org.blanco.solovolei.providers.dao.DaoFactory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
public class TeamsEditFragment extends Fragment {

	/** the Team object to be edited */
	Team team = null;
	/** The dao that will be used by the fragment */ 
	Dao<Team, Long> teamsDao = null;
	TeamsEditListener listener = null;
	
	/* GUI controls*/
	EditText txtName = null;
	Button accept = null;
	Button cancel = null;
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
		accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptEdit();
			}
		});
		cancel = (Button) v.findViewById(R.id.teams_edit_btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelEdit();
			}
		});
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
}
