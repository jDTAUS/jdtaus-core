/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container.test;

import java.io.ObjectInputStream;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.container.Argument;
import org.jdtaus.core.container.Arguments;

/**
 * jUnit tests for {@code Arguments} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ArgumentsTest extends TestCase
{
    //--ArgumentsTest-----------------------------------------------------------

    public void testObject() throws Exception
    {
        final Arguments a1 = new Arguments();
        final Arguments a2 = new Arguments();

        Assert.assertEquals( a1, a2 );
        Assert.assertEquals( a1.hashCode(), a2.hashCode() );

        System.out.println( a1.toString() );
        System.out.println( a2.toString() );

        final Argument args1 = new Argument();
        final Argument args2 = new Argument();
        final Argument args3 = new Argument();

        args1.setName( "TEST1" );
        args2.setName( "TEST2" );
        args3.setName( "TEST3" );

        a1.setArguments( new Argument[]
            {
                args1, args2, args3
            } );

        a2.setArguments( new Argument[]
            {
                args1, args2, args3
            } );

        Assert.assertEquals( a1, a2 );
        Assert.assertEquals( a1.hashCode(), a2.hashCode() );

        System.out.println( a1.toString() );
        System.out.println( a2.toString() );

        final Arguments clone1 = (Arguments) a1.clone();
        final Arguments clone2 = (Arguments) a2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_5() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Arguments.ser" ) );

        final Arguments current = new Arguments();
        final Arguments serialized = (Arguments) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //-----------------------------------------------------------ArgumentsTest--
}
