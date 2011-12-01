package org.blanco.solovolei.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class PlayersListFragment extends ListFragment {

	@Override
	public void onStart() {
		findPlayersAndPopulateList();
		super.onStart();
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		
//		return inflater.inflate(org.blanco.solovolei.R.layout.players_list_layout, null);
//	}

	
	
	public void findPlayersAndPopulateList(){
		Cursor c = getActivity().managedQuery(Uri.parse("content://org.blanco.solovolei/players"), 
				null, null, null, null);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		while(c.moveToNext()){
			String nombre = c.getString(c.getColumnIndex("nombre"));
			adapter.add(nombre);
		}
		setListAdapter(adapter);
	}
	
	
}
