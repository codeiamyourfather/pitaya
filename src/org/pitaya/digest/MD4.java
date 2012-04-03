/*----------------------------------------------------------------------------*
 * This file is part of Pitaya.                                               *
 * Copyright (C) 2012 Osman KOCAK <kocakosm@gmail.com>                        *
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

package org.pitaya.digest;

/**
 * The MD4 digest algorithm. Instances of this class are not thread safe.
 *
 * @author Osman KOCAK
 */
final class MD4 implements Digest
{
	private static final int DIGEST_LENGTH = 16;
	private static final int BLOCK_LENGTH = 64;

	/** Number of bytes processed so far. */
	private long counter;

	/** Current digest value (4 32-bit words). */
	private int[] value;

	/** Input buffer (16 32-bit words). */
	private byte[] buffer;

	/** Number of bytes in the input buffer. */
	private int bufferLen;

	/** Creates a new ready to use {@code MD4}. */
	MD4()
	{
		this.value = new int[4];
		this.buffer = new byte[BLOCK_LENGTH];
		reset();
	}

	@Override
	public int length()
	{
		return DIGEST_LENGTH * 8;
	}

	@Override
	public void reset()
	{
		counter = 0L;
		value[0] = 0x67452301;
		value[1] = 0xEFCDAB89;
		value[2] = 0x98BADCFE;
		value[3] = 0x10325476;
		bufferLen = 0;
	}

	@Override
	public void update(byte input)
	{
		buffer[bufferLen] = input;
		if (++bufferLen == BLOCK_LENGTH) {
			processBuffer();
		}
	}

	@Override
	public void update(byte... input)
	{
		update(input, 0, input.length);
	}

	@Override
	public void update(byte[] input, int off, int len)
	{
		while (len > 0) {
			int cpLen = Math.min(BLOCK_LENGTH - bufferLen, len);
			System.arraycopy(input, off, buffer, bufferLen, cpLen);
			bufferLen += cpLen;
			off += cpLen;
			len -= cpLen;
			if (bufferLen == BLOCK_LENGTH) {
				processBuffer();
			}
		}
	}

	@Override
	public byte[] digest(byte... input)
	{
		update(input, 0, input.length);
		return digest();
	}

	@Override
	public byte[] digest()
	{
		addPadding();
		byte[] res = new byte[DIGEST_LENGTH];
		encodeLittleEndian(value[0], res, 0);
		encodeLittleEndian(value[1], res, 4);
		encodeLittleEndian(value[2], res, 8);
		encodeLittleEndian(value[3], res, 12);
		reset();
		return res;
	}

	/** Adds the padding bits and the message length to the input data. */
	private void addPadding()
	{
		int len = BLOCK_LENGTH - bufferLen;
		if (len < 9) {
			len += BLOCK_LENGTH;
		}
		byte[] buf = new byte[len];
		buf[0] = (byte) 0x80;
		for (int i = 1; i < len - 8; i++) {
			buf[i] = (byte) 0x00;
		}
		counter = (counter + (long) bufferLen) * 8L;
		encodeLittleEndian(counter, buf, len - 8);
		update(buf);
	}

	/**
	 * Encodes in little-endian the two 32-bit words value {@code val} into
	 * the array {@code buf} starting at offset {@code off}.
	 *
	 * @param val the value to encode.
	 * @param buf the destination buffer.
	 * @param off the offset.
	 */
	private void encodeLittleEndian(long val, byte[] buf, int off)
	{
		encodeLittleEndian((int) val, buf, off);
		encodeLittleEndian((int) (val >>> 32), buf, off + 4);
	}

	/**
	 * Encodes in little-endian the 32-bit word {@code val} into the array
	 * {@code buf} starting at offset {@code off}.
	 *
	 * @param val the value to encode.
	 * @param buf the destination buffer.
	 * @param off the offset.
	 */
	private void encodeLittleEndian(int val, byte[] buf, int off)
	{
		buf[off] = (byte) val;
		buf[off + 1] = (byte) (val >>> 8);
		buf[off + 2] = (byte) (val >>> 16);
		buf[off + 3] = (byte) (val >>> 24);
	}

	/**
	 * Decodes a 32-bit little-endian word from the given array starting at
	 * the given offset.
	 *
	 * @param buf the buffer to read from.
	 * @param off the starting offset.
	 *
	 * @return the decoded {@code int} value.
	 */
	private int decodeLittleEndian(byte[] buf, int off)
	{
		return (buf[off] & 0xFF)
			| ((buf[off + 1] & 0xFF) << 8)
			| ((buf[off + 2] & 0xFF) << 16)
			| ((buf[off + 3] & 0xFF) << 24);
	}

	private void processBuffer()
	{
		int A = value[0];
		int B = value[1];
		int C = value[2];
		int D = value[3];

		int[] X = new int[16];
		for (int i = 0; i < 16; i++) {
			X[i] = decodeLittleEndian(buffer, 4 * i);
		}

		/* Round 1 */
		A = FF(A, B, C, D, X[ 0], 3);
		D = FF(D, A, B, C, X[ 1], 7);
		C = FF(C, D, A, B, X[ 2], 11);
		B = FF(B, C, D, A, X[ 3], 19);
		A = FF(A, B, C, D, X[ 4], 3);
		D = FF(D, A, B, C, X[ 5], 7);
		C = FF(C, D, A, B, X[ 6], 11);
		B = FF(B, C, D, A, X[ 7], 19);
		A = FF(A, B, C, D, X[ 8], 3);
		D = FF(D, A, B, C, X[ 9], 7);
		C = FF(C, D, A, B, X[10], 11);
		B = FF(B, C, D, A, X[11], 19);
		A = FF(A, B, C, D, X[12], 3);
		D = FF(D, A, B, C, X[13], 7);
		C = FF(C, D, A, B, X[14], 11);
		B = FF(B, C, D, A, X[15], 19);

		/* Round 2 */
		A = GG(A, B, C, D, X[ 0], 3);
		D = GG(D, A, B, C, X[ 4], 5);
		C = GG(C, D, A, B, X[ 8], 9);
		B = GG(B, C, D, A, X[12], 13);
		A = GG(A, B, C, D, X[ 1], 3);
		D = GG(D, A, B, C, X[ 5], 5);
		C = GG(C, D, A, B, X[ 9], 9);
		B = GG(B, C, D, A, X[13], 13);
		A = GG(A, B, C, D, X[ 2], 3);
		D = GG(D, A, B, C, X[ 6], 5);
		C = GG(C, D, A, B, X[10], 9);
		B = GG(B, C, D, A, X[14], 13);
		A = GG(A, B, C, D, X[ 3], 3);
		D = GG(D, A, B, C, X[ 7], 5);
		C = GG(C, D, A, B, X[11], 9);
		B = GG(B, C, D, A, X[15], 13);

		/* Round 3 */
		A = HH(A, B, C, D, X[ 0], 3);
		D = HH(D, A, B, C, X[ 8], 9);
		C = HH(C, D, A, B, X[ 4], 11);
		B = HH(B, C, D, A, X[12], 15);
		A = HH(A, B, C, D, X[ 2], 3);
		D = HH(D, A, B, C, X[10], 9);
		C = HH(C, D, A, B, X[ 6], 11);
		B = HH(B, C, D, A, X[14], 15);
		A = HH(A, B, C, D, X[ 1], 3);
		D = HH(D, A, B, C, X[ 9], 9);
		C = HH(C, D, A, B, X[ 5], 11);
		B = HH(B, C, D, A, X[13], 15);
		A = HH(A, B, C, D, X[ 3], 3);
		D = HH(D, A, B, C, X[11], 9);
		C = HH(C, D, A, B, X[ 7], 11);
		B = HH(B, C, D, A, X[15], 15);

		value[0] += A;
		value[1] += B;
		value[2] += C;
		value[3] += D;

		counter += 64L;
		bufferLen = 0;
	}

	private int FF(int a, int b, int c, int d, int x, int s)
	{
		return rotateLeft(a + F(b, c, d) + x, s);
	}

	private int GG(int a, int b, int c, int d, int x, int s)
	{
		return rotateLeft(a + G(b, c, d) + x + 0x5A827999, s);
	}

	private int HH(int a, int b, int c, int d, int x, int s)
	{
		return rotateLeft(a + H(b, c, d) + x + 0x6ED9EBA1, s);
	}

	private int F(int x, int y, int z)
	{
		return (y & x) | (z & ~x);
	}

	private int G(int x, int y, int z)
	{
		return (x & y) | (x & z) | (y & z);
	}

	private int H(int x, int y, int z)
	{
		return x ^ y ^ z;
	}

	private int rotateLeft(int x, int n)
	{
		return (x << n) | (x >>> (32 - n));
	}

	@Override
	public String toString()
	{
		return "MD4";
	}
}