package com.petrovdevelopment.killthemall;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoDialog extends DialogFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, getTheme());
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info, container, false);
        
		TextView infoText1 = (TextView) view.findViewById(R.id.infoText1);
		TextView infoText2 = (TextView) view.findViewById(R.id.infoText2);
		TextView infoText3 = (TextView) view.findViewById(R.id.infoText3);
		TextView infoText4 = (TextView) view.findViewById(R.id.infoText4);
		TextView infoText5 = (TextView) view.findViewById(R.id.infoText5);
		
		MainMenuActivity mainActivity = ((MainMenuActivity) getActivity());
				
		mainActivity.setTextViewFont(infoText1);
		mainActivity.setTextViewFont(infoText2);
		mainActivity.setTextViewFont(infoText3);
		mainActivity.setTextViewFont(infoText4);
		mainActivity.setTextViewFont(infoText5);
		
        View backButton = view.findViewById(R.id.backInfoButton);
        backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InfoDialog.this.dismiss();
			}
		});
        
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }
}
