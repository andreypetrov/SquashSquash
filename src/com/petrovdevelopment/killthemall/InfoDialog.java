package com.petrovdevelopment.killthemall;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class InfoDialog extends DialogFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, getTheme());
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup infoLayout = (ViewGroup) inflater.inflate(R.layout.dialog_info, container, false);
        Typeface customFont = ((MainApplication) getActivity().getApplication()).getCustomFont();
		Utils.setCustomFont(infoLayout, customFont, MainApplication.FONT_SIZE);
        
        View backButton = infoLayout.findViewById(R.id.okInfoButton);
        backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InfoDialog.this.dismiss();
			}
		});
        
        getDialog().setCanceledOnTouchOutside(true);
        return infoLayout;
    }
}
