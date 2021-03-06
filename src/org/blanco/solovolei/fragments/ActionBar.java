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
package org.blanco.solovolei.fragments;

import static org.blanco.solovolei.MainActivity.TAG;

import org.blanco.solovolei.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ActionBar extends Fragment {

	/**
	 * The type of commands that will be displayed in the action bar
	 * when the options accept and cancel need to be displayed in the
	 * bar.
	 */
	public static final short ACC_TYPE_ACCEPT_CANCEL = 0x01;
	public static final short ACC_TYPE_ACCEPT_CANCEL_RESET = 0x02;
	public static final short ACC_TYPE_ADD_DELETE = 0x04;
	
	ImageButton btnAdd = null;
	ImageButton btnDelete = null;
	ImageButton btnAccept = null;
	ImageButton btnCancel = null;
	
	AddCommandHandler addHandler = null;
	DeleteCommandHandler deleteHandler = null;

	private ActionBarCommandsHandler commandsHandler;
	
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
		//btnAdd.setVisibility(View.GONE);
		btnDelete = (ImageButton) v.findViewById(R.id.action_bar_btn_delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deletePressed();
			}
		});
		btnDelete.setVisibility(View.GONE);
		btnAccept = (ImageButton) v.findViewById(R.id.action_bar_btn_accept);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptPressed();
			}
		}); 
		btnAccept.setVisibility(View.GONE);
		btnCancel = (ImageButton) v.findViewById(R.id.action_bar_btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelPressed();
			}
		}); 
		btnCancel.setVisibility(View.GONE);
		
		
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
	
	/**
	 * The method that will be executed when the accept button is pressed.
	 */
	private void acceptPressed(){
		if (commandsHandler != null && commandsHandler instanceof AcceptCancelHandler)
			((AcceptCancelHandler)commandsHandler).triggerAccept();
		else{
			if (!(commandsHandler instanceof AcceptCancelHandler))
				throw new RuntimeException("established commands handler is not " +
						"of type AcceptCancelHandler");
			Log.d(TAG, "Button Accept clicked. Ignoring call handler is null");
		}
	}
	/**
	 * The method that will be executed when the cancel button is pressed.
	 */
	private void cancelPressed(){
		if (commandsHandler != null && commandsHandler instanceof AcceptCancelHandler)
			((AcceptCancelHandler)commandsHandler).triggerCancel();
		else{
			if (!(commandsHandler instanceof AcceptCancelHandler))
				throw new RuntimeException("established commands handler is not " +
						"of type AcceptCancelHandler");
			Log.d(TAG, "Button Cancel clicked. Ignoring call handler is null");
		}
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
	//TODO finish the Action Bar classes and methods to interact with fragments
	public void setCommandsType(short acctionsType,
			ActionBarCommandsHandler handler) {
		if (handler == null)
			throw new IllegalArgumentException("Commands Handler must not be NULL");
		this.commandsHandler = handler;
		//refresh the view of the action bar to reflect the correct
		//options based on the established handler
		switch (acctionsType) {
		case ActionBar.ACC_TYPE_ACCEPT_CANCEL:
			//check the handler and display only accept and cancel
			checkAndDisplayAcceptAndCancel(handler);
			break;

		default:
			break;
		}
	}
	
	private void checkAndDisplayAcceptAndCancel(ActionBarCommandsHandler handler){
		if (!(handler instanceof AcceptCancelHandler)){
			throw new IllegalArgumentException("Established ActionBarCommandsHandler " +
					"is not instance of AcceptCancelHandler and can not handle this type " +
					"of ActionBar mode");
		}
		btnAccept.setVisibility(View.VISIBLE);
		btnCancel.setVisibility(View.VISIBLE);
		btnAdd.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
	}
	
}
