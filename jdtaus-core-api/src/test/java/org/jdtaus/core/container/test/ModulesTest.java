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
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Modules;

/**
 * jUnit tests for {@code Modules} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ModulesTest extends TestCase
{
    //--ModulesTest-------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Modules m1 = new Modules();
        final Modules m2 = new Modules();

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Module[] mod1 =
        {
            new Module(), new Module(), new Module()
        };
        final Module[] mod2 =
        {
            new Module(), new Module(), new Module()
        };
        final Module[] mod3 =
        {
            new Module(), new Module(), new Module()
        };
        final Module[] mod4 =
        {
            new Module(), new Module()
        };

        mod1[0].setName( "TEST 1" );
        mod1[1].setName( "TEST 2" );
        mod1[2].setName( "TEST 3" );
        mod2[0].setName( "TEST 3" );
        mod2[1].setName( "TEST 2" );
        mod2[2].setName( "TEST 1" );
        mod3[0].setName( "TEST 2" );
        mod3[1].setName( "TEST 1" );
        mod3[2].setName( "TEST 3" );
        mod4[0].setName( "TEST 1" );
        mod4[1].setName( "TEST 3" );

        m1.setModules( mod1 );
        m2.setModules( mod2 );

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Modules clone1 = (Modules) m1.clone();
        final Modules clone2 = (Modules) m2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );

        m1.setModules( mod3 );

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        m1.setModules( mod4 );

        Assert.assertFalse( m1.equals( m2 ) );
        Assert.assertFalse( m1.hashCode() == m2.hashCode() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Modules.ser" ) );

        final Modules current = new Modules();
        final Modules serialized = (Modules) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //-------------------------------------------------------------ModulesTest--
}
