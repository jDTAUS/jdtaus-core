/*
 *  jDTAUS Core API
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;

/**
 * jUnit tests for {@code Specifications} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class SpecificationsTest extends TestCase
{
    //--SpecificationsTest------------------------------------------------------

    public void testObject() throws Exception
    {
        final Specifications s1 = new Specifications();
        final Specifications s2 = new Specifications();

        Assert.assertEquals( s1, s2 );
        Assert.assertEquals( s1.hashCode(), s2.hashCode() );

        System.out.println( s1.toString() );
        System.out.println( s2.toString() );

        final Specification[] spec1 =
        {
            new Specification(), new Specification(), new Specification()
        };
        final Specification[] spec2 =
        {
            new Specification(), new Specification(), new Specification()
        };
        final Specification[] spec3 =
        {
            new Specification(), new Specification(), new Specification()
        };
        final Specification[] spec4 =
        {
            new Specification(), new Specification()
        };

        spec1[0].setIdentifier( "TEST 1" );
        spec1[1].setIdentifier( "TEST 2" );
        spec1[2].setIdentifier( "TEST 3" );
        spec2[0].setIdentifier( "TEST 3" );
        spec2[1].setIdentifier( "TEST 2" );
        spec2[2].setIdentifier( "TEST 1" );
        spec3[0].setIdentifier( "TEST 2" );
        spec3[1].setIdentifier( "TEST 1" );
        spec3[2].setIdentifier( "TEST 3" );
        spec4[0].setIdentifier( "TEST 1" );
        spec4[1].setIdentifier( "TEST 3" );

        s1.setSpecifications( spec1 );
        s2.setSpecifications( spec2 );

        Assert.assertEquals( s1, s2 );
        Assert.assertEquals( s1.hashCode(), s2.hashCode() );

        System.out.println( s1.toString() );
        System.out.println( s2.toString() );

        final Specifications clone1 = (Specifications) s1.clone();
        final Specifications clone2 = (Specifications) s2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );

        s1.setSpecifications( spec3 );

        Assert.assertEquals( s1, s2 );
        Assert.assertEquals( s1.hashCode(), s2.hashCode() );

        s1.setSpecifications( spec4 );

        Assert.assertFalse( s1.equals( s2 ) );
        Assert.assertFalse( s1.hashCode() == s2.hashCode() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Specifications.ser" ) );

        final Specifications current = new Specifications();
        final Specifications serialized = (Specifications) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //------------------------------------------------------SpecificationsTest--
}
