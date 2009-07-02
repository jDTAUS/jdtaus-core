/*
 *  jDTAUS Core API
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <schulte2005@users.sourceforge.net> (+49 2331 3543887)
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
package org.jdtaus.core.container.test;

import java.io.ObjectInputStream;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.container.Message;

/**
 * jUnit tests for {@code Message} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class MessageTest extends TestCase
{
    //--MessageTest-------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Message m1 = new Message();
        final Message m2 = new Message();

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        m1.setName( "TEST" );
        m2.setName( "TEST" );

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Message clone1 = (Message) m1.clone();
        final Message clone2 = (Message) m2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_5() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Message.ser" ) );

        final Message current = new Message();
        final Message serialized = (Message) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //-------------------------------------------------------------MessageTest--
}
