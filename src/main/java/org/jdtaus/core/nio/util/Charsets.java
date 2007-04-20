/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdtaus.core.nio.spi;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Charset coder and decoder utilities.
 * <p>This class extends the former charset provider implementations which
 * cannot be used in every environment (e.g. WebStart, Maven) without
 * installation in the JRE extensions directory where they are available to the
 * system classloader.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class Charsets
{
    //--Charsets----------------------------------------------------------------

    /** Constant for the DIN-66003 Charset. */
    public static final Integer DIN66003 = new Integer(1);
    private static final int DIN66003_INT = 1;

    /** Constant for the IBM-273 Charset. */
    public static final Integer IBM273 = new Integer(2);
    private static final int IBM273_INT = 2;

    /** All supported charsets. */
    private static final Integer[] ALL = {
        DIN66003, IBM273
    };

    /** Charset instances. */
    private static final HashMap charsets = new HashMap(100);

    /**
     * Gets a charset for the given constant.
     *
     * @param charset the constant value of the charset to return.
     *
     * @throws NullPointerException if {@code charset} is {@code null}.
     * @throws IllegalArgumentException if {@code charset} is not a valid
     * charset constant.
     */
    private static Charset getCharset(final Integer charset)
    {
        if(charset == null)
        {
            throw new NullPointerException("charset");
        }

        boolean valid = false;

        for(int i = ALL.length - 1; i >= 0; i--)
        {
            if(ALL[i].equals(charset))
            {
                valid = true;
                break;
            }
        }

        if(!valid)
        {
            throw new IllegalArgumentException(charset.toString());
        }

        Charset ret = (Charset) charsets.get(charset);
        if(ret == null)
        {
            switch(charset.intValue())
            {
                case Charsets.DIN66003_INT:
                    ret = new DIN66003CharsetProvider.DIN66003Charset();
                    break;

                case Charsets.IBM273_INT:
                    ret = new IBM273CharsetProvider.IBM273Charset();
                    break;

                default:
                    throw new IllegalArgumentException(charset.toString());

            }

            charsets.put(charset, ret);
        }

        return ret;
    }

    /**
     * Encodes a given string to an array of bytes representing the characters
     * of the string in a given charset.
     *
     * @param str the string to encode.
     * @param charset the constant value of the charset to use.
     *
     * @throws NullPointerException if {@code str} or {@code charset} is
     * {@code null}.
     * @throws IllegalArgumentException if {@code charset} is not a valid
     * charset constant.
     */
    public static byte[] encode(final String str, final Integer charset)
    {
        if(str == null)
        {
            throw new NullPointerException("str");
        }

        final byte[] ret;
        final ByteBuffer buf = Charsets.getCharset(charset).encode(str);

        if(buf.hasArray())
        {
            ret = buf.array();
        }
        else
        {
            ret = new byte[buf.limit()];
            buf.get(ret);
        }

        return ret;
    }

    /**
     * Decodes the bytes of a given array to a string.
     *
     * @param bytes the bytes to decode.
     * @param charset the constant value of the charset to use.
     *
     * @throws NullPointerException if {@code bytes} or {@code charset} is
     * {@code null}.
     * @throws IllegalArgumentException if {@code charset} is not a valid
     * charset constant.
     */
    public static String decode(final byte[] bytes, final Integer charset)
    {
        if(bytes == null)
        {
            throw new NullPointerException("bytes");
        }

        final String ret;
        final CharBuffer buf = Charsets.getCharset(charset).
            decode(ByteBuffer.wrap(bytes));

        if(buf.hasArray())
        {
            ret = new String(buf.array());
        }
        else
        {
            final char[] c = new char[buf.limit()];
            buf.get(c);
            ret = new String(c);
        }

        return ret;
    }

    /**
     * Decodes the bytes of a given array to a string.
     *
     * @param bytes the bytes to decode.
     * @param off the offset from where to start decoding.
     * @param count the number of bytes to decode starting at {@code offset}.
     * @param charset the constant value of the charset to use.
     *
     * @throws NullPointerException if {@code bytes} or {@code charset} is
     * {@code null}.
     * @throws IllegalArgumentException if {@code charset} is not a valid
     * charset constant.
     * @throws IndexOutOfBoundsException if {@code off} is negative or greater
     * than the length of {@code bytes} or {@code off + count} is negative or
     * greater than the length of {@code bytes}.
     */
    public static String decode(final byte[] bytes, final int off,
        final int count, final Integer charset)
    {
        if(bytes == null)
        {
            throw new NullPointerException("bytes");
        }
        if(off < 0 || off >= bytes.length)
        {
            throw new ArrayIndexOutOfBoundsException(off);
        }
        if(count < 0 || off + count >= bytes.length)
        {
            throw new ArrayIndexOutOfBoundsException(count + off);
        }

        final String ret;
        final CharBuffer buf = Charsets.getCharset(charset).
            decode(ByteBuffer.wrap(bytes, off, count));

        if(buf.hasArray())
        {
            ret = new String(buf.array());
        }
        else
        {
            final char[] c = new char[buf.limit()];
            buf.get(c);
            ret = new String(c);
        }

        return ret;
    }

    //----------------------------------------------------------------Charsets--

}
