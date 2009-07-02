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
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Specifications;

/**
 * jUnit tests for {@code Implementation} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class ImplementationTest extends TestCase
{
    //--ImplementationTest------------------------------------------------------

    public void testObject() throws Exception
    {
        final Implementation i1 = new Implementation();
        final Implementation i2 = new Implementation();

        Assert.assertEquals( i1, i2 );
        Assert.assertEquals( i1.hashCode(), i2.hashCode() );

        System.out.println( i1.toString() );
        System.out.println( i2.toString() );

        i1.setDependencies( new Dependencies() );
        i1.setImplementedSpecifications( new Specifications() );
        i1.setParent( new Implementation() );
        i1.setProperties( new Properties() );

        i2.setDependencies( new Dependencies() );
        i2.setImplementedSpecifications( new Specifications() );
        i2.setParent( new Implementation() );
        i2.setProperties( new Properties() );

        Assert.assertEquals( i1, i2 );
        Assert.assertEquals( i1.hashCode(), i2.hashCode() );

        System.out.println( i1.toString() );
        System.out.println( i2.toString() );

        final Implementation clone1 = (Implementation) i1.clone();
        final Implementation clone2 = (Implementation) i2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Implementation.ser" ) );

        final Implementation current = new Implementation();
        final Implementation serialized = (Implementation) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //------------------------------------------------------ImplementationTest--
}
