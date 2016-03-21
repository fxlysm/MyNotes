/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fxly.notes.async.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.fxly.notes.OmniNotes;
import com.fxly.notes.db.DbHelper;
import com.fxly.notes.models.Attachment;
import com.fxly.notes.models.Note;
import com.fxly.notes.models.listeners.OnNoteSaved;
import com.fxly.notes.utils.Constants;
import com.fxly.notes.utils.ReminderHelper;
import com.fxly.notes.utils.StorageHelper;
import com.fxly.notes.utils.date.DateUtils;

import java.util.List;


public class SaveNoteTask extends AsyncTask<Note, Void, Note> {

    private Context context;
    private boolean updateLastModification = true;
    private OnNoteSaved mOnNoteSaved;


    public SaveNoteTask(boolean updateLastModification) {
        this(null, updateLastModification);
    }


    public SaveNoteTask(OnNoteSaved mOnNoteSaved, boolean updateLastModification) {
        super();
        this.context = OmniNotes.getAppContext();
        this.mOnNoteSaved = mOnNoteSaved;
        this.updateLastModification = updateLastModification;
    }


    @Override
    protected Note doInBackground(Note... params) {
        Note note = params[0];
        purgeRemovedAttachments(note);
		boolean reminderMustBeSet = DateUtils.isFuture(note.getAlarm());
        if (reminderMustBeSet) {
            note.setReminderFired(false);
        }
        note = DbHelper.getInstance().updateNote(note, updateLastModification);
		if (reminderMustBeSet) {
			ReminderHelper.addReminder(context, note);
		}
        return note;
    }


    private void purgeRemovedAttachments(Note note) {
        List<Attachment> deletedAttachments = note.getAttachmentsListOld();
        for (Attachment attachment : note.getAttachmentsList()) {
            if (attachment.getId() != null) {
                // Workaround to prevent deleting attachments if instance is changed (app restart)
                if (deletedAttachments.indexOf(attachment) == -1) {
                    attachment = getFixedAttachmentInstance(deletedAttachments, attachment);
                }
                deletedAttachments.remove(attachment);
            }
        }
        // Remove from database deleted attachments
        for (Attachment deletedAttachment : deletedAttachments) {
            StorageHelper.delete(context, deletedAttachment.getUri().getPath());
            Log.d(Constants.TAG, "Removed attachment " + deletedAttachment.getUri());
        }
    }


    private Attachment getFixedAttachmentInstance(List<Attachment> deletedAttachments, Attachment attachment) {
        for (Attachment deletedAttachment : deletedAttachments) {
            if (deletedAttachment.getId() == attachment.getId()) return deletedAttachment;
        }
        return attachment;
    }


    @Override
    protected void onPostExecute(Note note) {
        super.onPostExecute(note);
        if (this.mOnNoteSaved != null) {
            mOnNoteSaved.onNoteSaved(note);
        }
    }
}
