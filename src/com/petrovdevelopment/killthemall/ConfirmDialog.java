package com.petrovdevelopment.killthemall;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ConfirmDialog extends DialogFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, getTheme());
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		ViewGroup confirmLayout = (ViewGroup) inflater.inflate(R.layout.dialog_confirm, container, false);
        Typeface customFont = ((MainApplication) getActivity().getApplication()).getCustomFont();
		Utils.setCustomFont(confirmLayout, customFont, MainApplication.FONT_SIZE);
	
        View cancelButton = confirmLayout.findViewById(R.id.cancelConfirmButton);
        cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfirmDialog.this.dismiss();
			}
		});
        
        //TODO: change this to be implemented by the activity if we need configurable action, not just finish() call
        //and maybe add an interface that all activities with this dialog should implement
        View okButton = confirmLayout.findViewById(R.id.okConfirmButton);
        okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
        
        getDialog().setCanceledOnTouchOutside(true);
        return confirmLayout;
    }

}
