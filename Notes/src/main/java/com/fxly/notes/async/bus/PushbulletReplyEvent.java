package com.fxly.notes.async.bus;

import android.util.Log;
import com.fxly.notes.utils.Constants;


public class PushbulletReplyEvent {

	public String message;

	public PushbulletReplyEvent(String message) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.message = message;
	}
}
