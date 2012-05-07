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
import org.jdtaus.core.container.Specification;

/**
 * jUnit tests for {@code Specification} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class SpecificationTest extends TestCase
{
    //--SpecificationTest-------------------------------------------------------

    public void testObject() throws Exception
    {
        final Specification s1 = new Specification();
        final Specification s2 = new Specification();

        Assert.assertEquals( s1, s2 );
        Assert.assertEquals( s1.hashCode(), s2.hashCode() );

        System.out.println( s1.toString() );
        System.out.println( s2.toString() );

        s1.setImplementations( new Implementations() );
        s2.setImplementations( new Implementations() );

        Assert.assertEquals( s1, s2 );
        Assert.assertEquals( s1.hashCode(), s2.hashCode() );

        System.out.println( s1.toString() );
        System.out.println( s2.toString() );

        final Specification clone1 = (Specification) s1.clone();
        final Specification clone2 = (Specification) s2.clone();

        Assert.assertEquals( clone1, clone2 );
        Assert.assertEquals( clone1.hashCode(), clone2.hashCode() );

        System.out.println( clone1.toString() );
        System.out.println( clone2.toString() );
    }

    public void testBackwardCompatibility_1_0() throws Exception
    {
        final ObjectInputStream in = new ObjectInputStream( this.getClass().
            getResourceAsStream( "Specification.ser" ) );

        final Specification current = new Specification();
        final Specification serialized = (Specification) in.readObject();

        Assert.assertEquals( current, serialized );
        Assert.assertEquals( current.hashCode(), serialized.hashCode() );
    }

    //-------------------------------------------------------SpecificationTest--
}
