package org.blanco.solovolei;

import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.TeamsAddFragment;
import org.blanco.solovolei.fragments.TeamsAddFragment.TeamsAddListener;
import org.blanco.solovolei.fragments.TeamsListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class TeamsActivity extends FragmentActivity {
	
	ImageButton btnAdd = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.teams_layout);
		init();
	}

	/**
	 * Initializes the GUI components and their listeners
	 */
	private void init(){
		btnAdd = (ImageButton) findViewById(R.id.teams_layout_btn_add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				executeAdd();
			}
		});
	}
	/**
	 * It start the adding process to create a new Team in 
	 * the database
	 */
	private void executeAdd(){
		//Create a new Add Fragment and add it to the stack
		TeamsAddFragment fragment = new TeamsAddFragment();
		//set the call backs
		fragment.setListener(new TeamsAddListener() {
			
			@Override
			public void onTeamAddingError(Team t, Exception e) {
				Toast.makeText(TeamsActivity.this.getApplicationContext(),
						"Error ading the team."+e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onTeamAddingCancel() {
				Toast.makeText(TeamsActivity.this.getApplicationContext(),
						"Action Cancelled", Toast.LENGTH_LONG).show();
				setFragmentOnFragmentContainer(new TeamsListFragment());				
			}
			
			@Override
			public void onTeamAdded(long id, Team t) {
				Toast.makeText(TeamsActivity.this.getApplicationContext(),
						"TeamAdded", Toast.LENGTH_LONG).show();
				setFragmentOnFragmentContainer(new TeamsListFragment());
			}
		});
		setFragmentOnFragmentContainer(fragment);
	}
	
	private void setFragmentOnFragmentContainer(Fragment fragment){
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.replace(R.id.teams_layout_fragment_container,fragment);
		trans.commit();
	}
}
