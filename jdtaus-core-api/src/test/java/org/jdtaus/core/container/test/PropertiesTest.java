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
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;

/**
 * jUnit tests for {@code Properties} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class PropertiesTest extends TestCase
{
    //--PropertiesTest----------------------------------------------------------

    public void testObject() throws Exception
    {
        final Properties p1 = new Properties();
        final Properties p2 = new Properties();

        Assert.assertEquals( p1, p2 );
        Assert.assertEquals( p1.hashCode(), p2.hashCode() );

        System.out.println( p1.toString() );
        System.out.println( p2.toString() );

        final Property[] prop1 =
        {
            new Property(), new Property(), new Property()
        };
        final Property[] prop2 =
        {
            new Property(), new Property(), new Property()
        };
        final Property[] prop3 =
        {
            new Property(), new Property(), new Property()
        };
        final Property[] prop4 =
        {
            new Property(), new Property()
        };

        prop1[0].setName( "TEST 1" );
        prop1[1].setName( "TEST 2" );
        prop1[2].setName( "TEST 3" );
        prop2[0].setName( "TEST 3" );
        prop2[1].setName( "TEST 2" );
        prop2[2].setName( "TEST 1" );
        prop3[0].setName( "TEST 2" );
        prop3[1].setName( "TEST 1" );
        prop3[2].setName( "TEST 3" );
        prop4[0].setName( "TEST 1" );
        prop4[1].setName( "TEST 3" );

        p1.setProperties( prop1 );
        p2.setProperties( prop2 );

        Assert.assertEquals( p1, p2 );
        Assert.assertEquals( p1.hashCode(), p2.hashCode() );

        System.out.println( p1.toString() );
        System.out.println( p2.toString() );

        final Properties clone1 = (Properties) p1.clone();
        final Properties clone2 = (Properties) p2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );

        p1.setProperties( prop3 );

        Assert.assertEquals( p1, p2 );
        Assert.assertEquals( p1.hashCode(), p2.hashCode() );

        p1.setProperties( prop4 );

        Assert.assertFalse( p1.equals( p2 ) );
        Assert.assertFalse( p1.hashCode() == p2.hashCode() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Properties.ser" ) );

        final Properties current = new Properties();
        final Properties serialized = (Properties) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //----------------------------------------------------------PropertiesTest--
}
