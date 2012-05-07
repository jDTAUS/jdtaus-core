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
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Specifications;

/**
 * jUnit tests for {@code Module} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ModuleTest extends TestCase
{
    //--ModuleTest--------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Module m1 = new Module();
        final Module m2 = new Module();

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        m1.setImplementations( new Implementations() );
        m1.setProperties( new Properties() );
        m1.setSpecifications( new Specifications() );

        m2.setImplementations( new Implementations() );
        m2.setProperties( new Properties() );
        m2.setSpecifications( new Specifications() );

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        System.out.println( m1.toString() );
        System.out.println( m2.toString() );

        final Module clone1 = (Module) m1.clone();
        final Module clone2 = (Module) m2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Module.ser" ) );

        final Module current = new Module();
        final Module serialized = (Module) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //--------------------------------------------------------------ModuleTest--
}
