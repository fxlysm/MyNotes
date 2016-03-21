package com.fxly.notes.async.bus;

import android.util.Log;
import com.fxly.notes.models.Note;
import com.fxly.notes.utils.Constants;

import java.util.ArrayList;


public class NotesLoadedEvent {

	public ArrayList<Note> notes;


	public NotesLoadedEvent(ArrayList<Note> notes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.notes = notes;
	}
}
