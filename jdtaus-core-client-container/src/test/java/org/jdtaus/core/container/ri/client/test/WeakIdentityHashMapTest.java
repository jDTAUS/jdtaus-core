/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client.test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdtaus.core.container.ri.client.WeakIdentityHashMap;

/**
 * Tests the {@code WeakIdentityHashMap} implementation.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public class WeakIdentityHashMapTest extends TestCase
{
    //--Tests-------------------------------------------------------------------

    public void testObject() throws Exception
    {
        final Map m1 = new WeakIdentityHashMap();
        final Map m2 = new WeakIdentityHashMap();

        Assert.assertEquals( m1, m2 );
        Assert.assertEquals( m1.hashCode(), m2.hashCode() );

        Assert.assertEquals( m1.entrySet(), m2.entrySet() );
        Assert.assertEquals( m1.entrySet().hashCode(),
            m2.entrySet().hashCode() );

        Assert.assertEquals( m1.keySet(), m2.keySet() );
        Assert.assertEquals( m1.keySet().hashCode(), m2.keySet().hashCode() );

        System.out.println( m1 );
        System.out.println( m1.keySet() );
        System.out.println( m1.entrySet() );
        System.out.println( m1.values() );

        System.out.println( m2 );
        System.out.println( m2.keySet() );
        System.out.println( m2.entrySet() );
        System.out.println( m2.values() );

        final Object o = new Object(); // Must not get garbage collected.
        m1.put( o, "TEST" );

        Assert.assertFalse( m1.equals( m2 ) );
        Assert.assertFalse( m1.hashCode() == m2.hashCode() );
    }

    public void testPutGetRemove() throws Exception
    {
        final Object o1 = new Object(); // Must not get garbage collected.
        final Object o2 = new Object(); // Must not get garbage collected.
        final Map m1 = new WeakIdentityHashMap();

        Assert.assertTrue( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 0 );
        Assert.assertFalse( m1.containsKey( o1 ) );
        Assert.assertFalse( m1.containsKey( o2 ) );
        Assert.assertFalse( m1.containsValue( "TEST1" ) );
        Assert.assertFalse( m1.containsValue( "TEST2" ) );

        System.out.println( m1 );

        m1.put( o1, "TEST1" );
        m1.put( o2, "TEST2" );

        Assert.assertFalse( m1.isEmpty() );
        Assert.assertEquals( 2, m1.size() );
        Assert.assertTrue( m1.containsKey( o1 ) );
        Assert.assertTrue( m1.containsKey( o2 ) );
        Assert.assertTrue( m1.containsValue( "TEST1" ) );
        Assert.assertTrue( m1.containsValue( "TEST2" ) );

        System.out.println( m1 );

        m1.clear();

        Assert.assertTrue( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 0 );
        Assert.assertFalse( m1.containsKey( o1 ) );
        Assert.assertFalse( m1.containsKey( o2 ) );
        Assert.assertFalse( m1.containsValue( "TEST1" ) );
        Assert.assertFalse( m1.containsValue( "TEST2" ) );

        System.out.println( m1 );
    }

    public void testConcurrentModificationException() throws Exception
    {
        final Object o1 = new Object(); // Must not get garbage collected.
        final Object o2 = new Object(); // Must not get garbage collected.
        final Map m1 = new WeakIdentityHashMap();

        m1.put( o1, "TEST1" );
        m1.put( o2, "TEST2" );

        final Iterator keyIterator = m1.keySet().iterator();
        final Iterator valueIterator = m1.values().iterator();
        final Iterator entryIterator = m1.entrySet().iterator();

        m1.clear();

        try
        {
            keyIterator.hasNext();
            throw new AssertionError();
        }
        catch ( ConcurrentModificationException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            valueIterator.hasNext();
            throw new AssertionError();
        }
        catch ( ConcurrentModificationException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

        try
        {
            entryIterator.hasNext();
            throw new AssertionError();
        }
        catch ( ConcurrentModificationException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }
    }

    public void testRemoveThroughIterator() throws Exception
    {
        final Object o1 = new Object(); // Must not get garbage collected.
        final Object o2 = new Object(); // Must not get garbage collected.
        final Map m1 = new WeakIdentityHashMap();

        m1.put( o1, "TEST1" );
        m1.put( o2, "TEST2" );

        Assert.assertFalse( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 2 );
        Assert.assertTrue( m1.containsKey( o1 ) );
        Assert.assertTrue( m1.containsKey( o2 ) );
        Assert.assertTrue( m1.containsValue( "TEST1" ) );
        Assert.assertTrue( m1.containsValue( "TEST2" ) );

        final Iterator keyIterator = m1.keySet().iterator();
        keyIterator.next();
        keyIterator.remove();
        keyIterator.next();
        keyIterator.remove();

        Assert.assertFalse( keyIterator.hasNext() );
        Assert.assertTrue( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 0 );
        Assert.assertFalse( m1.containsKey( o1 ) );
        Assert.assertFalse( m1.containsKey( o2 ) );
        Assert.assertFalse( m1.containsValue( "TEST1" ) );
        Assert.assertFalse( m1.containsValue( "TEST2" ) );

        m1.put( o1, "TEST1" );
        m1.put( o2, "TEST2" );

        final Iterator valueIterator = m1.keySet().iterator();
        valueIterator.next();
        valueIterator.remove();
        valueIterator.next();
        valueIterator.remove();

        Assert.assertFalse( valueIterator.hasNext() );
        Assert.assertTrue( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 0 );
        Assert.assertFalse( m1.containsKey( o1 ) );
        Assert.assertFalse( m1.containsKey( o2 ) );
        Assert.assertFalse( m1.containsValue( "TEST1" ) );
        Assert.assertFalse( m1.containsValue( "TEST2" ) );

        m1.put( o1, "TEST1" );
        m1.put( o2, "TEST2" );

        final Iterator entryIterator = m1.keySet().iterator();
        entryIterator.next();
        entryIterator.remove();
        entryIterator.next();
        entryIterator.remove();

        Assert.assertFalse( entryIterator.hasNext() );
        Assert.assertTrue( m1.isEmpty() );
        Assert.assertEquals( m1.size(), 0 );
        Assert.assertFalse( m1.containsKey( o1 ) );
        Assert.assertFalse( m1.containsKey( o2 ) );
        Assert.assertFalse( m1.containsValue( "TEST1" ) );
        Assert.assertFalse( m1.containsValue( "TEST2" ) );
    }

    //-------------------------------------------------------------------Tests--
}
