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
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Specification;

/**
 * jUnit tests for {@code Dependency} implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DependencyTest extends TestCase
{
    //--DependencyTest----------------------------------------------------------

    public void testObject() throws Exception
    {
        final Dependency d1 = new Dependency();
        final Dependency d2 = new Dependency();

        Assert.assertEquals( d1, d2 );
        Assert.assertEquals( d1.hashCode(), d2.hashCode() );

        System.out.println( d1.toString() );
        System.out.println( d2.toString() );

        d1.setImplementation( new Implementation() );
        d1.setSpecification( new Specification() );
        d2.setImplementation( new Implementation() );
        d2.setSpecification( new Specification() );

        Assert.assertEquals( d1, d2 );
        Assert.assertEquals( d1.hashCode(), d2.hashCode() );

        System.out.println( d1.toString() );
        System.out.println( d2.toString() );

        final Dependency clone1 = (Dependency) d1.clone();
        final Dependency clone2 = (Dependency) d2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Dependency.ser" ) );

        final Dependency current = new Dependency();
        final Dependency serialized = (Dependency) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //----------------------------------------------------------DependencyTest--
}
