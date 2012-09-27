/*
 *  jDTAUS Core Client Container
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
package org.jdtaus.core.container.ri.client.versioning.test;

import java.io.StringReader;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.container.ri.client.versioning.ParseException;
import org.jdtaus.core.container.ri.client.versioning.Token;
import org.jdtaus.core.container.ri.client.versioning.VersionParser;

/**
 * Tests the {@code VersionParser} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class VersionParserTest extends TestCase
{

    public void testParseable() throws Exception
    {
        this.assertValidVersion( "1" );
        this.assertValidVersion( "1.1" );
        this.assertValidVersion( "SNAPSHOT" );
        this.assertValidVersion( "RELEASE" );
        this.assertValidVersion( "LATEST" );
        this.assertValidVersion( "4aug2007r7-dev" );
        this.assertValidVersion( "1.0-alpha-10-stable-1" );
    }

    public void testUnparseable() throws Exception
    {
        this.assertInvalidVersion( "." );
    }

    protected void assertValidVersion( final String version ) throws Exception
    {
        final VersionParser parser =
            new VersionParser( new StringReader( version ) );

        final Token[] tokens = parser.parse();
        final StringBuffer buf = new StringBuffer().append( "Parses '" +
                                                            version + "' to" );

        for ( int i = 0; i < tokens.length; i++ )
        {
            buf.append( "\n\t" ).append( "token[" ).append( i ).append( "]=" ).
                append( tokens[i].image );

        }

        System.out.println( buf.toString() );
    }

    protected void assertInvalidVersion( final String version )
    {
        try
        {
            final VersionParser parser =
                new VersionParser( new StringReader( version ) );

            parser.parse();
            fail( "Could parse '" + version + "'." );
        }
        catch ( ParseException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }
    }

}
