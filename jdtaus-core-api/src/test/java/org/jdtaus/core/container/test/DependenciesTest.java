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
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;

/**
 * jUnit tests for {@code Dependencies} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DependenciesTest extends TestCase
{
    //--DependenciesTest--------------------------------------------------------

    public void testObject() throws Exception
    {
        final Dependencies d1 = new Dependencies();
        final Dependencies d2 = new Dependencies();

        Assert.assertEquals( d1, d2 );
        Assert.assertEquals( d1.hashCode(), d2.hashCode() );

        System.out.println( d1.toString() );
        System.out.println( d2.toString() );

        final Dependency[] deps1 =
        {
            new Dependency(), new Dependency(), new Dependency()
        };
        final Dependency[] deps2 =
        {
            new Dependency(), new Dependency(), new Dependency()
        };
        final Dependency[] deps3 =
        {
            new Dependency(), new Dependency(), new Dependency()
        };
        final Dependency[] deps4 =
        {
            new Dependency(), new Dependency()
        };

        deps1[0].setName( "TEST 1" );
        deps1[1].setName( "TEST 2" );
        deps1[2].setName( "TEST 3" );
        deps2[0].setName( "TEST 3" );
        deps2[1].setName( "TEST 2" );
        deps2[2].setName( "TEST 1" );
        deps3[0].setName( "TEST 2" );
        deps3[1].setName( "TEST 1" );
        deps3[2].setName( "TEST 3" );
        deps4[0].setName( "TEST 1" );
        deps4[1].setName( "TEST 3" );

        d1.setDependencies( deps1 );
        d2.setDependencies( deps2 );

        Assert.assertEquals( d1, d2 );
        Assert.assertEquals( d1.hashCode(), d2.hashCode() );

        System.out.println( d1.toString() );
        System.out.println( d2.toString() );

        final Dependencies clone1 = (Dependencies) d1.clone();
        final Dependencies clone2 = (Dependencies) d2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );

        d1.setDependencies( deps3 );

        Assert.assertEquals( d1, d2 );
        Assert.assertEquals( d1.hashCode(), d2.hashCode() );

        d1.setDependencies( deps4 );

        Assert.assertFalse( d1.equals( d2 ) );
        Assert.assertFalse( d1.hashCode() == d2.hashCode() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Dependencies.ser" ) );

        final Dependencies current = new Dependencies();
        final Dependencies serialized = (Dependencies) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //--------------------------------------------------------DependenciesTest--
}
