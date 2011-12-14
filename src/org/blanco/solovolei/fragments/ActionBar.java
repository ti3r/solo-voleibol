package org.blanco.solovolei.fragments;

import org.blanco.solovolei.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import static org.blanco.solovolei.MainActivity.TAG;

public class ActionBar extends Fragment {

	ImageButton btnAdd = null;
	ImageButton btnDelete = null;
	AddCommandHandler addHandler = null;
	DeleteCommandHandler deleteHandler = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.action_bar, null);
		btnAdd = (ImageButton) v.findViewById(R.id.action_bar_btn_add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPressed();
			}
		});
		btnDelete = (ImageButton) v.findViewById(R.id.action_bar_btn_delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deletePressed();
			}
		});
		return v;
	}

	/**
	 * The method that will be executed when the add button is pressed.
	 */
	private void addPressed(){
		Log.d(TAG, "Button Add clicked. Ignoring call");
		if (addHandler != null)
			addHandler.addTriggered();
	}
	/**
	 * The method that will be executed when the delete button is pressed.
	 */
	private void deletePressed(){
		if (deleteHandler != null)
			deleteHandler.deleteTriggered();
		Log.d(TAG, "Button Delete clicked. Ignoring call");
	}
	
	public void setAddCommandHandler(AddCommandHandler handler){
		this.addHandler = handler;
	}
	
	public void setDeleteCommandHandler(DeleteCommandHandler handler){
		this.deleteHandler = handler;
	}
	
	/**
	 * Interface that must be implemented in order to handle 
	 * the add command of the bar.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 */
	public interface AddCommandHandler{
		/**
		 * Method that will be executed when the add command is triggered
		 */
		public void addTriggered();
	}
	/**
	 * Interface that must be implemented in order to handle
	 * the delete command of the bar.
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
	 *
	 */
	public interface DeleteCommandHandler{
		/**
		 * Method that will be executed when the delete command is triggered
		 */
		public void deleteTriggered();
	}
}
