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
import org.jdtaus.core.container.Argument;

/**
 * jUnit tests for {@code Argument} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ArgumentTest extends TestCase
{
    //--ArgumentTest------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Argument a1 = new Argument();
        final Argument a2 = new Argument();

        Assert.assertEquals( a1, a2 );
        Assert.assertEquals( a1.hashCode(), a2.hashCode() );

        System.out.println( a1.toString() );
        System.out.println( a2.toString() );

        a1.setIndex( 1 );
        a1.setName( "TEST" );
        a1.setType( Argument.TYPE_DATE );

        a2.setIndex( 1 );
        a2.setName( "TEST" );
        a2.setType( Argument.TYPE_DATE );

        Assert.assertEquals( a1, a2 );
        Assert.assertEquals( a1.hashCode(), a2.hashCode() );

        System.out.println( a1.toString() );
        System.out.println( a2.toString() );

        final Argument clone1 = (Argument) a1.clone();
        final Argument clone2 = (Argument) a2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( a1.toString() );
        System.out.println( a2.toString() );
    }

    public void testBackwardCompatibility_1_5() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Argument.ser" ) );

        final Argument current = new Argument();
        final Argument serialized = (Argument) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //------------------------------------------------------------ArgumentTest--
}
