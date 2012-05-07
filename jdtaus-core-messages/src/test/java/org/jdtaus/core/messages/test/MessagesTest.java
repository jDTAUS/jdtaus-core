/*
 *  jDTAUS Core Messages
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.messages.test;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.messages.BugReportMessage;
import org.jdtaus.core.messages.DeletesBlocksMessage;
import org.jdtaus.core.messages.ExceptionMessage;
import org.jdtaus.core.messages.IllegalNumberMessage;
import org.jdtaus.core.messages.IllegalPropertyMessage;
import org.jdtaus.core.messages.IllegalStringMessage;
import org.jdtaus.core.messages.InsertsBlocksMessage;
import org.jdtaus.core.messages.MandatoryPropertyMessage;
import org.jdtaus.core.messages.UndefinedApplicationStateMessage;
import org.jdtaus.core.text.Message;

/**
 * Unit tests for core application messages.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class MessagesTest extends TestCase
{

    /** Tests instantiation of each core application message and for non-null texts for the current default locale. */
    public void testMessages() throws Exception
    {
        this.assertNotNull( new ExceptionMessage( new IllegalArgumentException( "TEST" ) ) );
        this.assertNotNull( new DeletesBlocksMessage() );
        this.assertNotNull( new InsertsBlocksMessage() );
        this.assertNotNull( new MandatoryPropertyMessage() );
        this.assertNotNull( new IllegalPropertyMessage() );
        this.assertNotNull( new UndefinedApplicationStateMessage() );
        this.assertNotNull( new BugReportMessage( new File( System.getProperty( "user.home" ) ),
                                                  new URL( "http://jdtaus.sourceforge.net" ), "TEST" ) );

        this.assertNotNull( new IllegalStringMessage( "TEST", new char[]
            {
                'A', 'B', 'C'
            }, new Integer( 10 ), new Integer( 10 ) ) );

        this.assertNotNull( new IllegalNumberMessage( new Integer( 10 ), new Integer( 10 ), new Integer( 10 ) ) );
    }

    private void assertNotNull( final Message message )
    {
        Assert.assertNotNull( message.getText( Locale.getDefault() ) );
        System.out.println( message.getText( Locale.getDefault() ) );
    }

}
