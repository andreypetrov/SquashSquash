package com.petrovdevelopment.squashsquash.game;

import android.os.Parcel;
import android.os.Parcelable;

import com.petrovdevelopment.squashsquash.R;


public enum NpcType implements Parcelable {
	BAD1 (R.drawable.bad1, true),
	BAD2 (R.drawable.bad2, true),
	BAD3 (R.drawable.bad3, true),
	BAD4 (R.drawable.bad4, true),
	BAD5 (R.drawable.bad5, true),
	BAD6 (R.drawable.bad6, true),
	GOOD1 (R.drawable.good1, false),
	GOOD2 (R.drawable.good2, false),
	GOOD3 (R.drawable.good3, false),
	GOOD4 (R.drawable.good4, false),
	GOOD5 (R.drawable.good5, false),
	GOOD6 (R.drawable.good6, false);
		
	
	private final int mResourceId;
	private final boolean mIsAlien;
	
	private NpcType(int resourceId, boolean isAlien) {
		mResourceId = resourceId;
		mIsAlien = isAlien;
	}
	
	public int resourceId() {
		return mResourceId;
	}
	public boolean isAlien() {
		return mIsAlien;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.toString());
	}
	
	public static final Creator<NpcType> CREATOR = new Creator<NpcType>() {
		@Override 
		//valueOf() returns the enum constant with the specified name
		public NpcType createFromParcel(final Parcel source) {
			return NpcType.valueOf(source.readString());	
		}

		@Override
		public NpcType[] newArray(int size) {
			return new NpcType[size];
		}
			
	};
}
