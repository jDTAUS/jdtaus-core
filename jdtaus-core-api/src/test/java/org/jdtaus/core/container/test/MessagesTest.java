/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container.test;

import java.io.ObjectInputStream;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.container.Message;
import org.jdtaus.core.container.Messages;

/**
 * jUnit tests for {@code Messages} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class MessagesTest extends TestCase
{
    //--MessagesTest------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Messages m1 = new Messages();
        final Messages m2 = new Messages();

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Message msg1 = new Message();
        final Message msg2 = new Message();
        final Message msg3 = new Message();

        msg1.setName( "TEST1" );
        msg2.setName( "TEST2" );
        msg3.setName( "TEST3" );

        m1.setMessages( new Message[]
            {
                msg1, msg2, msg3
            } );

        m2.setMessages( new Message[]
            {
                msg1, msg2, msg3
            } );

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Messages clone1 = (Messages) m1.clone();
        final Messages clone2 = (Messages) m2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_5() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Messages.ser" ) );

        final Messages current = new Messages();
        final Messages serialized = (Messages) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //------------------------------------------------------------MessagesTest--
}
