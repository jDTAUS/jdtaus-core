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
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;

/**
 * jUnit tests for {@code Implementations} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ImplementationsTest extends TestCase
{
    //--ImplementationsTest-----------------------------------------------------

    public void testObject() throws Exception
    {
        final Implementations i1 = new Implementations();
        final Implementations i2 = new Implementations();

        Assert.assertEquals( i1, i2 );
        Assert.assertEquals( i1.hashCode(), i2.hashCode() );

        System.out.println( i1.toString() );
        System.out.println( i2.toString() );

        final Implementation[] impl1 =
        {
            new Implementation(), new Implementation(), new Implementation()
        };
        final Implementation[] impl2 =
        {
            new Implementation(), new Implementation(), new Implementation()
        };
        final Implementation[] impl3 =
        {
            new Implementation(), new Implementation(), new Implementation()
        };
        final Implementation[] impl4 =
        {
            new Implementation(), new Implementation()
        };

        impl1[0].setIdentifier( "TEST 1" );
        impl1[1].setIdentifier( "TEST 2" );
        impl1[2].setIdentifier( "TEST 3" );
        impl2[0].setIdentifier( "TEST 3" );
        impl2[1].setIdentifier( "TEST 2" );
        impl2[2].setIdentifier( "TEST 1" );
        impl3[0].setIdentifier( "TEST 2" );
        impl3[1].setIdentifier( "TEST 1" );
        impl3[2].setIdentifier( "TEST 3" );
        impl4[0].setIdentifier( "TEST 1" );
        impl4[1].setIdentifier( "TEST 3" );

        i1.setImplementations( impl1 );
        i2.setImplementations( impl2 );

        Assert.assertEquals( i1, i2 );
        Assert.assertEquals( i1.hashCode(), i2.hashCode() );

        System.out.println( i1.toString() );
        System.out.println( i2.toString() );

        final Implementations clone1 = (Implementations) i1.clone();
        final Implementations clone2 = (Implementations) i2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );

        i1.setImplementations( impl3 );

        Assert.assertEquals( i1, i2 );
        Assert.assertEquals( i1.hashCode(), i2.hashCode() );

        i1.setImplementations( impl4 );

        Assert.assertFalse( i1.equals( i2 ) );
        Assert.assertFalse( i1.hashCode() == i2.hashCode() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Implementations.ser" ) );

        final Implementations current = new Implementations();
        final Implementations serialized = (Implementations) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //-----------------------------------------------------ImplementationsTest--
}
