package org.blanco.solovolei.misc;

import org.blanco.solovolei.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {

	/**
	 * Creates an AlertDialog based on the passed parameters.
	 * This method will allow to create consistent dialog
	 * interfaces among the application.
	 * @param ctx The Context where the dialog will be created
	 * @param msg The CharSequence message that will be displayed
	 * @param onOk The OnClickListener interface that will be 
	 * executed when the dialog is accepted.
	 * @param onCancel The OnClickListener interface that will
	 * be executed when the dialog is cancelled or dismissed.
	 * 
	 * @return The built AlertDialog object ready to be used.
	 */
	public static Dialog createQueryDialog(Context ctx,
			CharSequence msg,
			OnClickListener onOk,
			OnClickListener onCancel){

		Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(ctx.getString(R.string.solo_volei));
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setMessage(msg);
		if (onCancel != null){
			dialog.setNegativeButton(ctx.getString(R.string.cancel), onCancel);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.cancel();
				}
			});
			
		}
		if (onOk != null)
			dialog.setPositiveButton(ctx.getString(R.string.accept), onOk);
		
		return dialog.create();
	}
	
	public static Dialog createAlertDialog(Context ctx, CharSequence msg,
			OnClickListener onOk){
		Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(ctx.getString(R.string.solo_volei));
		dialog.setMessage(msg);
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setPositiveButton(ctx.getText(R.string.accept), onOk);
		return dialog.create();
	}
	
}
