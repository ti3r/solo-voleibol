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

import java.sql.SQLException;

import org.blanco.solovolei.R;
import org.blanco.solovolei.entities.Player;
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
public class PlayersEditFragment extends Fragment {

	/** the Player object to be edited */
	private Player player = null;
	/** The dao that will be used by the fragment */ 
	private Dao<Player, Long> playersDao = null;
	private PlayersEditListener listener = null;
	
	/* GUI controls*/
	private EditText edtName = null;
	private Button accept = null;
	private Button cancel = null;
	private ImageButton btnLogo = null;
	private EditText edtNumber;
	/* end of GUI controls */
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		playersDao = (Dao<Player, Long>) DaoFactory.
				getDao(getActivity(), Player.class);
		super.onCreate(savedInstanceState);
	}
	/**
	 * Set the Player object to be edited and persisted to the database.
	 * 
	 * @param player The Player object to be edited by the fragment
	 */
	public void setPlayer(Player player){
		this.player = player;
	}
	/**
	 * Sets the listener that will handle the results of the edit
	 * process
	 * @param listener The TeamsEditListener listener that will handle
	 * the results of the edit process
	 */
	public void setPlayerEditListener(PlayersEditListener listener){
		this.listener = listener;
	}

	/**
	 * On create view of the fragment, it build the view needed to present
	 * the Player object associated with the fragment and the controls
	 * to launch the edit process.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.players_add_layout, null);
		//establish the links of the controls
		edtName = (EditText) v.findViewById(R.id.players_add_edt_name);
		edtName.setText(player.getName());
		edtNumber = (EditText) v.findViewById(R.id.players_add_edt_number);
		edtNumber.setText(String.valueOf(player.getNumber()));
		accept = (Button) v.findViewById(R.id.players_add_btn_accpet);
		accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptEdit();
			}
		});
		cancel = (Button) v.findViewById(R.id.players_add_btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelEdit();
			}
		});
		btnLogo = (ImageButton) 
				v.findViewById(R.id.players_add_btn_photo);
		btnLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//pickPhoto();
			}
		});
		return v;
	}
	
	
	/**
	 * When the method is attached to the activity it should implement
	 * TeamsEditListener Interface in order to be set as the listener of
	 * the commands or set the listener previous to the onAttach Method
	 */
	@Override
	public void onAttach(Activity activity) {
		if(this.listener == null 
				&& (!(activity instanceof PlayersEditListener))){
			throw new IllegalArgumentException("Attached activity does not implement " +
					"TeamsEditListener in order to handle the results. " +
					"Please implement this interface in passed activity or set the appropiate listener");
		}
		if (this.listener == null)
			setPlayerEditListener((PlayersEditListener) activity);
		super.onAttach(activity);
	}
	/**
	 * The method that will be executed when the Edit process is 
	 * cancelled. This passes the result to the PlayerEditListener
	 * Appropriate method if set.
	 */
	private void cancelEdit(){
		if (listener != null)
			listener.onPlayerItemEditCancelled();
	}
	/**
	 * The method that will be executed when the Edit process is 
	 * accepted. It executes the onPlayersItemPreEdit method of the
	 * set PlayersEditListener listener if set, then if the result
	 * is positive it persists the new team on the database
	 * and passes the results to the appropriate method of the 
	 * listener.
	 */
	private void acceptEdit(){
		player.setName(edtName.getText().toString());
		player.setNumber(Integer.parseInt(edtNumber.getText().toString()));
		try {
			if (listener == null || listener.onPlayerItemPreEdit(player))
				playersDao.update(player);
			if (listener != null)
				listener.onPlayerItemPostEdit(player);
		} catch (SQLException e) {
			Log.e(TAG, "Error updating team",e);
			if (listener != null)
				listener.onPlayerItemEditError(player, e);
		}
	}
	
	/**
	 * Launch the intent to pick a photo from the gallery 
	 * or the camera. The result must be treated in the 
	 * onActivityResult method of the fragment.
	 */
//	private void pickPhoto(){
//		Intent i = new Intent(Intent.ACTION_PICK);
//		i.setType("image/*");
//		startActivityForResult(i, 0);
//	}
	
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
	
	@Override
	public void onActivityResult(int requestCode, 
			int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK){
			String path = parseImage(data);
			btnLogo.setImageBitmap(Bitmap.createBitmap(
					BitmapFactory.decodeFile(path), 0, 0, btnLogo.getWidth(), 
					btnLogo.getHeight(), null, false));
			//player.setLogo(path);
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}



	/**
	 * Interface to communicate the edit events that happen within the fragment 
	 * 
	 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>	
	 */
	public interface PlayersEditListener{
		public boolean onPlayerItemPreEdit(Player player);
		
		public void onPlayerItemPostEdit(Player player);
		
		public void onPlayerItemEditError(Player team, Exception e);
		
		public void onPlayerItemEditCancelled();
	}
}
