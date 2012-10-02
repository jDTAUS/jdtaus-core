/*
 *  jDTAUS Core Utilities
 *  Copyright (C) 2005 Christian Schulte
 *  <cs@schulte.it>
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
package org.jdtaus.core.nio.util.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.nio.util.Charsets;

/**
 * Testcase for {@code Charsets} utility.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class CharsetsTest extends TestCase
{
    //--Tests-------------------------------------------------------------------

    /** String used for testing. */
    private static final String TEST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Tests the {@link Charsets#decode(byte[],String)} and
     * {@link Charsets#encode(String,String)} methods to support the US-ASCII,
     * UTF-8 and UTF-16 charsets.
     */
    public void testPlatformCharsets() throws Exception
    {
        final byte[] ascii = Charsets.encode( TEST, "US-ASCII" );
        final byte[] utf8 = Charsets.encode( TEST, "UTF-8" );
        final byte[] utf16 = Charsets.encode( TEST, "UTF-16" );

        final String asciiDecoded = Charsets.decode( ascii, "US-ASCII" );
        final String utf8Decoded = Charsets.decode( utf8, "UTF-8" );
        final String utf16Decoded = Charsets.decode( utf16, "UTF-16" );

        Assert.assertEquals( TEST.length(), ascii.length );
        Assert.assertEquals( TEST.length(), utf8.length );

        Assert.assertEquals( TEST, asciiDecoded );
        Assert.assertEquals( TEST, utf8Decoded );
        Assert.assertEquals( TEST, utf16Decoded );
    }

    //-------------------------------------------------------------------Tests--
}
