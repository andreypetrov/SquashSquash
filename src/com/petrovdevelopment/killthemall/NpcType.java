package com.petrovdevelopment.killthemall;

import android.os.Parcel;
import android.os.Parcelable;

public enum NpcType implements Parcelable {
	BAD1 (R.drawable.bad1),
	BAD2 (R.drawable.bad2),
	BAD3 (R.drawable.bad3),
	BAD4 (R.drawable.bad4),
	BAD5 (R.drawable.bad5),
	BAD6 (R.drawable.bad6),
	GOOD1 (R.drawable.good1),
	GOOD2 (R.drawable.good2),
	GOOD3 (R.drawable.good3),
	GOOD4 (R.drawable.good4),
	GOOD5 (R.drawable.good5),
	GOOD6 (R.drawable.good6);
		
	private final int mResourceId;
	private NpcType(int resourceId) {
		mResourceId = resourceId;
	}
	
	/**
	 * Return the NpcType based on its string representation
	 * @param string
	 * @return
	 */
	public NpcType fromString(String string) {
		
		return null;
	}
	
	public int resourceId() {
		return mResourceId;
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
		public NpcType createFromParcel(final Parcel source) {
			return NpcType.valueOf(source.readString());	
		}

		@Override
		public NpcType[] newArray(int size) {
			return new NpcType[size];
		}
			
	};
}
