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

package com.fxly.notes.test.utils;

import android.test.InstrumentationTestCase;

import com.fxly.notes.utils.Security;

public class SecurityTest extends InstrumentationTestCase {

    private final String PASS = "12345uselessPasswords";
    private final String TEXT = "Today is a - good - day to test useless things!";
    private final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    public void testEncrypt(){
        String encryptedText = Security.encrypt(TEXT, PASS);
        assertFalse(TEXT.equals(encryptedText));
    }

    public void testDecrypt(){
        String encryptedText = Security.encrypt(TEXT, PASS);
        assertEquals(TEXT, Security.decrypt(encryptedText, PASS));
        assertFalse(TEXT.equals(Security.decrypt(encryptedText, "zaza" + PASS)));
    }

    public void testDecryptUnencrypted(){
        String result = Security.decrypt(LOREM, PASS);
        assertFalse(result.length() == 0);
    }
}
