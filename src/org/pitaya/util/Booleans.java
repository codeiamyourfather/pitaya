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

package org.pitaya.util;

/**
 * Utility methods that operate on or return boolean values.
 *
 * @author Osman KOCAK
 */
public final class Booleans
{
	/**
	 * The {@link Boolean} object corresponding to the primitive value
	 * {@code true}.
	 */
	public static final Boolean TRUE = Boolean.TRUE;

	/**
	 * The {@link Boolean} object corresponding to the primitive value
	 * {@code false}.
	 */
	public static final Boolean FALSE = Boolean.FALSE;

	/**
	 * Performs a logical "and" on the given values, that is, this method
	 * returns {@code true} if, and only if, all the given {@link Boolean}
	 * values are {@code true}, and {@code false} in all other cases.
	 *
	 * @param bools the {@link Boolean} values.
	 *
	 * @return the result of the logical "and" on the given values.
	 *
	 * @throws NullPointerException if {@code bools} is {@code null}.
	 * @throws IllegalArgumentException if {@code bools} is empty.
	 */
	public static Boolean and(Boolean... bools)
	{
		Parameters.checkCondition(bools.length > 0);
		for (Boolean bool : bools) {
			if (!bool.booleanValue()) {
				return FALSE;
			}
		}
		return TRUE;
	}

	/**
	 * Performs a logical "or" on the given values, that is, this method
	 * returns {@code true} if, and only if, at least one of the given
	 * {@link Boolean} values is {@code true}, and {@code false} in all
	 * other cases.
	 *
	 * @param bools the {@link Boolean} values.
	 *
	 * @return the result of the logical "or" on the given values.
	 *
	 * @throws NullPointerException if {@code bools} is {@code null}.
	 * @throws IllegalArgumentException if {@code bools} is empty.
	 */
	public static Boolean or(Boolean... bools)
	{
		Parameters.checkCondition(bools.length > 0);
		for (Boolean bool : bools) {
			if (bool.booleanValue()) {
				return TRUE;
			}
		}
		return FALSE;
	}

	/**
	 * Returns the negation of the given {@link Boolean}.
	 *
	 * @param bool the {@link Boolean} to negate.
	 *
	 * @return the negation of the given {@link Boolean}.
	 *
	 * @throws NullPointerException if {@code bool} is {@code null}.
	 */
	public static Boolean not(Boolean bool)
	{
		return bool.booleanValue() ? FALSE : TRUE;
	}

	/**
	 * Performs an "exclusive or" on the given values, that is, this method
	 * returns {@code true} if, and only if, only one of the given
	 * {@link Boolean} values is {@code true}, and {@code false} in all
	 * other cases.
	 *
	 * @param bools the {@link Boolean} values.
	 *
	 * @return the result of the "exclusive or" on the given values.
	 *
	 * @throws NullPointerException if {@code bools} is {@code null}.
	 * @throws IllegalArgumentException if {@code bools} is empty.
	 */
	public static Boolean xor(Boolean... bools)
	{
		Parameters.checkCondition(bools.length > 0);
		boolean xor = false;
		for (Boolean bool : bools) {
			if (bool.booleanValue()) {
				if (xor) {
					return FALSE;
				} else {
					xor = true;
				}
			}
		}
		return xor ? TRUE : FALSE;
	}

	/**
	 * Returns {@code true} if and only if the given {@link Boolean} object
	 * is {@code false}.
	 *
	 * @param bool the {@link Boolean} to test, may be {@code null}.
	 *
	 * @return whether the given {@link Boolean} is {@code false}.
	 */
	public static boolean isFalse(Boolean bool)
	{
		if (bool != null) {
			return !bool.booleanValue();
		}
		return false;
	}

	/**
	 * Returns {@code true} if and only if the given {@link Boolean} object
	 * is not {@code false}.
	 *
	 * @param bool the {@link Boolean} to test, may be {@code null}.
	 *
	 * @return whether the given {@link Boolean} is not {@code false}.
	 */
	public static boolean isNotFalse(Boolean bool)
	{
		if (bool != null) {
			return bool.booleanValue();
		}
		return true;
	}

	/**
	 * Returns {@code true} if and only if the given {@link Boolean} object
	 * is {@code true}.
	 *
	 * @param bool the {@link Boolean} to test, may be {@code null}.
	 *
	 * @return whether the given {@link Boolean} is {@code true}.
	 */
	public static boolean isTrue(Boolean bool)
	{
		if (bool != null) {
			return bool.booleanValue();
		}
		return false;
	}

	/**
	 * Returns {@code true} if and only if the given {@link Boolean} object
	 * is not {@code true}.
	 *
	 * @param bool the {@link Boolean} to test, may be {@code null}.
	 *
	 * @return whether the given {@link Boolean} is not {@code true}.
	 */
	public static boolean isNotTrue(Boolean bool)
	{
		if (bool != null) {
			return !bool.booleanValue();
		}
		return true;
	}

	/**
	 * Parses the given {@code String} into a {@link Boolean}. This method
	 * returns {@code true} if the given {@link String} contains "true",
	 * "yes", "1" or "on".
	 *
	 * @param bool the {@code String} to parse.
	 *
	 * @return the parsed {@code Boolean}.
	 *
	 * @throws NullPointerException if {@code bool} is {@code null}.
	 */
	public static Boolean valueOf(String bool)
	{
		String s = bool.replaceAll("\\s", "");
		boolean b = s.equalsIgnoreCase("true")
			|| s.equalsIgnoreCase("yes")
			|| s.equalsIgnoreCase("on")
			|| s.equalsIgnoreCase("1");
		return b ? TRUE : FALSE;
	}

	/**
	 * Converts the given {@code int} into a {@link Boolean} using the
	 * convention that zero represents {@code false} and all other values
	 * represent {@code true}.
	 * 
	 * @param i the {@code int} to convert.
	 *
	 * @return {@code false} if {@code i == 0}, {@code true} otherswise.
	 */
	public static Boolean valueOf(int i)
	{
		return i == 0 ? FALSE : TRUE;
	}

	/**
	 * Converts the given {@code Integer} into a {@link Boolean} using the
	 * convention that zero represents {@code false} and all other values
	 * represent {@code true}.
	 *
	 * @param i the {@code int} to convert.
	 *
	 * @return {@code false} if {@code i == 0}, {@code true} otherswise.
	 *
	 * @throws NullPointerException if {@code i} is {@code null}.
	 */
	public static Boolean valueOf(Integer i)
	{
		return valueOf(i.intValue());
	}

	/**
	 * Returns the {@link Boolean} instance representing the given primitive
	 * {@code boolean} value.
	 *
	 * @param bool the {@code boolean} value to convert.
	 *
	 * @return the {@link Boolean} instance representing the given value.
	 */
	public static Boolean valueOf(boolean bool)
	{
		return bool ? TRUE : FALSE;
	}

	/**
	 * Returns 1 if {@code bool} is {@code true}, 0 otherwise.
	 * 
	 * @param bool the {@code boolean} value to convert.
	 *
	 * @return 1 if {@code bool} is {@code true}, 0 otherwise.
	 */
	public static Integer toInteger(boolean bool)
	{
		return bool ? Integer.valueOf(1) : Integer.valueOf(0);
	}

	/**
	 * Returns 1 if {@code bool} is {@code true}, 0 if {@code false}.
	 *
	 * @param bool the {@code Boolean} instance to convert.
	 *
	 * @return 1 if {@code bool} is {@code true}, 0 if {@code false}.
	 *
	 * @throws NullPointerException if {@code bool} is {@code null}.
	 */
	public static Integer toInteger(Boolean bool)
	{
		return toInteger(bool.booleanValue());
	}

	/**
	 * Returns the given {@code boolean}'s {@link String} representation.
	 *
	 * @param bool the {@code boolean} value to convert.
	 *
	 * @return {@code bool}'s {@link String} representation.
	 */
	public static String toString(boolean bool)
	{
		return bool ? "true" : "false";
	}

	private Booleans()
	{
		/* ... */
	}
}
