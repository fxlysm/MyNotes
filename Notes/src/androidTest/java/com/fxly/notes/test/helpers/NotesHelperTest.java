/*
 * Copyright (C) 2016 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundatibehaon, either version 3 of the License, or
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

package com.fxly.notes.test.helpers;

import android.test.InstrumentationTestCase;
import com.fxly.notes.helpers.NotesHelper;
import com.fxly.notes.models.Note;
import com.fxly.notes.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class NotesHelperTest extends InstrumentationTestCase {

	@Test
	public void mergeNotes() {

		int notesNumber = 3;
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < notesNumber; i++) {
			Note note = new Note();
			note.setTitle("Merged note " + i + " title");
			note.setContent("Merged note " + i + " content");
			notes.add(note);
		}
		Note mergeNote = NotesHelper.mergeNotes(notes, false);

		assertNotNull(mergeNote);
		assertTrue(mergeNote.getTitle().equals("Merged note 0 title"));
		assertTrue(mergeNote.getContent().contains("Merged note 0 content"));
		assertTrue(mergeNote.getContent().contains("Merged note 1 content"));
		assertTrue(mergeNote.getContent().contains("Merged note 2 content"));
		assertEquals(StringUtils.countMatches(mergeNote.getContent(), Constants.MERGED_NOTES_SEPARATOR), 2);
	}
}
