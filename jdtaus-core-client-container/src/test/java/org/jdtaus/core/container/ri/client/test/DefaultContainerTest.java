/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client.test;

import junit.framework.Assert;
import org.jdtaus.core.container.Container;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.DependencyCycleException;

/**
 * Tests the {@code DefaultContainer} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class DefaultContainerTest
{

    private static final String MODULE_NAME =
        "jDTAUS Core ⁑ RI Client Container";

    public Container getContainer()
    {
        return ContainerFactory.getContainer();
    }

    /**
     * Tests the {@link Container#getObject(Class, String)} method to return the
     * correctly initialized instance of {@link TestImplementation}.
     */
    public void testInstantiation() throws Exception
    {
        assert this.getContainer() != null;

        final TestSpecification spec =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(), MODULE_NAME );

        Assert.assertTrue( spec.isInitialized() );
    }

    /**
     * Tests the {@link Container#getObject(Class, String)} method to return the
     * same instance of {@link TestImplementation} for successive calls.
     */
    public void testSingleton() throws Exception
    {
        assert this.getContainer() != null;

        final TestSpecification spec1 =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(), MODULE_NAME );

        final TestSpecification spec2 =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(), MODULE_NAME );

        final TestSpecification spec3 =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(), MODULE_NAME );

        final TestSpecification child =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(),
            MODULE_NAME + " - Child" );

        Assert.assertTrue( spec1 == spec2 );
        Assert.assertTrue( spec2 == spec3 );
        Assert.assertTrue( spec3 == spec1 );
        Assert.assertFalse( child == spec1 );
        Assert.assertFalse( child == spec2 );
        Assert.assertFalse( child == spec3 );
    }

    /**
     * Tests that model properties are correctly provided to implementations.
     */
    public void testProperties()
    {
        final TestSpecification spec1 =
            (TestSpecification) this.getContainer().
            getObject( TestSpecification.class.getName(), MODULE_NAME );

        final MultitonSpecification child =
            (MultitonSpecification) this.getContainer().
            getObject( MultitonSpecification.class.getName(),
            MODULE_NAME + " - Child" );

        this.assertValidProperties( spec1 );
        this.assertValidProperties( child );
        this.assertValidProperties( spec1.getDependency() );
        this.assertValidProperties( child.getDependency() );
    }

    /**
     * Checks the given {@code TestSpecification} to provide the correct
     * property values.
     */
    private void assertValidProperties( final TestSpecification s )
    {
        Assert.assertEquals( s.isBoolean(), true );
        Assert.assertEquals( s.getByte(), (byte) 1 );
        Assert.assertEquals( s.getChar(), 'X' );
        Assert.assertTrue( s.getDouble() == 0.1D );
        Assert.assertTrue( s.getFloat() == 0.1F );
        Assert.assertEquals( s.getInt(), 1 );
        Assert.assertEquals( s.getLong(), 1L );
        Assert.assertEquals( s.getShort(), (short) 1 );
        Assert.assertEquals( s.isBooleanObject(), Boolean.TRUE );
        Assert.assertEquals( s.getByteObject(), new Byte( (byte) 1 ) );
        Assert.assertEquals( s.getCharacterObject(), new Character( 'X' ) );
        Assert.assertEquals( s.getDoubleObject(), new Double( 0.1D ) );
        Assert.assertEquals( s.getFloatObject(), new Float( 0.1F ) );
        Assert.assertEquals( s.getIntegerObject(), new Integer( 1 ) );
        Assert.assertEquals( s.getLongObject(), new Long( 1L ) );
        Assert.assertEquals( s.getShortObject(), new Short( (short) 1 ) );
        Assert.assertEquals( s.getStringObject(), "TEST" );
    }

    /**
     * Checks that dependency cycles are detected correctly by throwing a
     * corresponding {@link DependencyCycleException}.
     */
    public void testDependencyCycleDetection() throws Exception
    {
        try
        {
            this.getContainer().getObject(
                CycleTestSpecification.class.getName(), MODULE_NAME +
                " - Cycle Test" );

            Assert.fail( "Dependency cycle not detected." );
        }
        catch ( DependencyCycleException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }
    }

    /**
     * Tests the {@link Container#getObject(Class, String)} method to corectly
     * detect non-serializable implementation classes in context scope.
     */
    public void testSerializable() throws Exception
    {
        assert this.getContainer() != null;

        Assert.assertNotNull( this.getContainer().getObject(
            ContextTestSpecification.class.getName(), MODULE_NAME ) );

    }

    /**
     * Tests the {@link Container#getObject(Class, String)} method to return an
     * array of correctly initialized instances of {@link TestImplementation}
     * when given a {@code null} value for the implementation name argument.
     */
    public void testInstantiationNullImplementationName() throws Exception
    {
        assert this.getContainer() != null;

        final TestSpecification[] spec =
            (TestSpecification[]) this.getContainer().
            getObject( TestSpecification.class.getName(), null );

        for ( int i = spec.length - 1; i >= 0; i-- )
        {
            Assert.assertTrue( spec[i].isInitialized() );
        }
    }

}
