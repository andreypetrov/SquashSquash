package com.petrovdevelopment.squashsquash.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.petrovdevelopment.squashsquash.MainApplication;
import com.petrovdevelopment.squashsquash.R;
import com.petrovdevelopment.squashsquash.utils.TextManager;
import com.petrovdevelopment.squashsquash.utils.U;

public class ConfirmDialog extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View confirmLayout = inflateLayout(inflater, container, savedInstanceState);
		setCustomFont(confirmLayout);
		setOkHandler(confirmLayout);
		setCancelHandler(confirmLayout);
		customizeView(confirmLayout);
		setCanceledOnTouchOutside();
		return confirmLayout;
	}

	/**
	 * Set the dialog to be cancelled if clicked outside of its area
	 */
	protected void setCanceledOnTouchOutside() {
		getDialog().setCanceledOnTouchOutside(true);
	}

	/**
	 * Inflate a layout, to be used by the dialog view.
	 * 
	 */
	protected View inflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View confirmLayout = (ViewGroup) inflater.inflate(R.layout.dialog_confirm, container, false);
		return confirmLayout;
	}

	/**
	 * Set custom font to the View if it is a TextView or to its children if it a ViewGroup
	 * 
	 * @param view
	 */
	protected void setCustomFont(View view) {
		Typeface customFont = ((MainApplication) getActivity().getApplication()).getTextManager().getCustomFont();
		U.setCustomFont(view, customFont, TextManager.FONT_SIZE);
	}

	/**
	 * Close the parent activity
	 * 
	 * @param view
	 */
	protected void setOkHandler(View view) {
		// close the activity
		View okButton = view.findViewById(R.id.okButton);
		if (okButton != null) {
			okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Activity parentActivity = getActivity();
					//optional interface allowing for customized OK behavior
					if (parentActivity instanceof ConfirmDialogParent) {
						((ConfirmDialogParent) parentActivity).onDialogOkClick();
					} else { //default if the 
						getActivity().finish();
					}
				}
			});
		}

	}

	/**
	 * Dismiss the fragment
	 * 
	 * @param view
	 */
	protected void setCancelHandler(View view) {
		View cancelButton = view.findViewById(R.id.cancelButton);
		if (cancelButton != null) {
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ConfirmDialog.this.dismiss();
				}
			});
		}

	}

	/**
	 * A method to be overwritten to provide some custom behavior
	 * 
	 * @param view
	 */
	protected void customizeView(View view) {
		// do nothing
	}
}
