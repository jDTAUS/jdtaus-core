/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client.test;

import java.net.URL;
import junit.framework.Assert;
import org.jdtaus.core.container.IncompatibleImplementationException;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;

/**
 * Tests the {@code DefaultModel} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class DefaultModelTest
{

    private static final String MODEL_LOCATION = "META-INF/jdtaus/module.xml";

    /**
     * Name of the system property controlling the use of the context
     * classloader.
     */
    private static final String SYS_ENABLE_CONTEXT_CLASSLOADER =
        "org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader";

    public void testBackwardsCompatibility() throws Exception
    {
        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "Specifications-1.0.xml" ),
                this.getClass().getResource( "Implementation-1.0.xml" ),
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "Specifications-1.1.xml" ),
                this.getClass().getResource( "Implementation-1.1.xml" ),
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "Specifications-1.2.xml" ),
                this.getClass().getResource( "Implementation-1.2.xml" ),
            } );

    }

    public void testCompatibilityDetection() throws Exception
    {
        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "CompatibilityTestcase1.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "CompatibilityTestcase2.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "CompatibilityTestcase3.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "CompatibilityTestcase4.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource( "CompatibilityTestcase5.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase1.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase2.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase3.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase4.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase5.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationCompatibilityTestcase6.xml" )
            } );

    }

    public void testIncompatibilityDetection() throws Exception
    {
        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource( "IncompatibilityTestcase1.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource( "IncompatibilityTestcase2.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource( "IncompatibilityTestcase3.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource( "IncompatibilityTestcase4.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource( "IncompatibilityTestcase5.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase1.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase2.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase3.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase4.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase5.xml" )
            } );

        this.assertIncompatibleImplementation(
            new URL[]
            {
                this.getClass().getResource(
                "ImplementationIncompatibilityTestcase6.xml" )
            } );

    }

    public void testMessages() throws Exception
    {
        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "MessagesTestcase1.xml" )
            } );

        this.assertValidModel(
            new URL[]
            {
                this.getClass().getResource(
                "MessagesTestcase2.xml" )
            } );

    }

    public void testIllegalMessages() throws Exception
    {
        this.assertInvalidModel(
            new URL[]
            {
                this.getClass().getResource(
                "MessagesTestcase3.xml" )
            } );

        this.assertInvalidModel(
            new URL[]
            {
                this.getClass().getResource(
                "MessagesTestcase4.xml" )
            } );

        this.assertInvalidModel(
            new URL[]
            {
                this.getClass().getResource(
                "MessagesTestcase5.xml" )
            } );

    }

    protected void assertIncompatibleImplementation( final URL[] resources )
    {
        try
        {
            this.assertValidModel( resources );
            Assert.fail( "Model with incompatible versions allowed." );
        }
        catch ( IncompatibleImplementationException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.getMessage() );
        }
    }

    protected void assertValidModel( final URL[] resources )
    {
        final ClassLoader currentLoader = Thread.currentThread().
            getContextClassLoader();

        try
        {
            final ResourceLoader resourceLoader = new ResourceLoader(
                this.getClass().getClassLoader() );

            resourceLoader.addResources( MODEL_LOCATION, resources );

            Thread.currentThread().setContextClassLoader( resourceLoader );
            System.setProperty( SYS_ENABLE_CONTEXT_CLASSLOADER,
                                Boolean.toString( true ) );

            ModelFactory.newModel();
        }
        finally
        {
            System.setProperty( SYS_ENABLE_CONTEXT_CLASSLOADER,
                                Boolean.toString( false ) );

            Thread.currentThread().setContextClassLoader( currentLoader );
        }
    }

    protected void assertInvalidModel( final URL[] resources )
    {
        try
        {
            this.assertValidModel( resources );
        }
        catch ( ModelError e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }
    }

}
