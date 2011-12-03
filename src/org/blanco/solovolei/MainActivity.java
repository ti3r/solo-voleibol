package org.blanco.solovolei;

import org.blanco.solovolei.entities.Team;
import org.blanco.solovolei.fragments.TeamsAddFragment;
import org.blanco.solovolei.fragments.TeamsListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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

	@Override
	protected void onStart() {
		FragmentManager m = getSupportFragmentManager();
		final TeamsListFragment f = (TeamsListFragment) m.findFragmentById(R.id.main_teams_list_fragment);
		TeamsAddFragment fa = (TeamsAddFragment) m.findFragmentById(R.id.main_teams_add_fragment);
		fa.setListener(new TeamsAddFragment.TeamsAddListener() {
			
			@Override
			public void onTeamAddingError(Team t, Exception e) {
				
			}
			
			@Override
			public void onTeamAddingCancel() {
								
			}
			
			@Override
			public void onTeamAdded(long id, Team t) {
				f.refreshList();
			}
		});
		super.onStart();
	}
    
    
}