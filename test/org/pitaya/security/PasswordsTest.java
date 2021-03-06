/*----------------------------------------------------------------------------*
 * This file is part of Pitaya.                                               *
 * Copyright (C) 2012-2013 Osman KOCAK <kocakosm@gmail.com>                   *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation, either version 3 of the License, or (at your *
 * option) any later version.                                                 *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public     *
 * License for more details.                                                  *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *----------------------------------------------------------------------------*/

package org.pitaya.security;

import static org.junit.Assert.*;

import org.pitaya.charset.ASCII;

import org.junit.Test;

/**
 * {@link Passwords}' unit tests.
 *
 * @author Osman KOCAK
 */
public final class PasswordsTest
{
	@Test
	public void testGenerate()
	{
		assertTrue(ASCII.isAlphaNumeric(Passwords.generate()));
		assertEquals(10, Passwords.generate().length());
	}

	@Test
	public void testValidPassword()
	{
		String password = Passwords.generate();
		byte[] hash = Passwords.hash(password);
		assertTrue(Passwords.verify(password, hash));
	}

	@Test
	public void testInvalidPassword()
	{
		byte[] hash = Passwords.hash("password");
		assertFalse(Passwords.verify("Password", hash));
	}
}
