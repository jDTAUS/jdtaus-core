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
package org.jdtaus.core.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Factory for the {@code Container} singleton.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class ContainerFactory
{
    //--Constants---------------------------------------------------------------

    /** Default {@code Container} implementation. */
    private static final String DEFAULT_CONTAINER =
        "org.jdtaus.core.container.ri.client.DefaultContainer";

    /** Empty array. */
    private static final Class[] EMPTY =
    {
    };

    //---------------------------------------------------------------Constants--
    //--ContainerFactory--------------------------------------------------------

    /** Default singleton instance. */
    private static Container instance;

    /**
     * Gets the {@code Container} singleton.
     * <p>By default this class will instantiate a new container and hold it in
     * a static class variable as the singleton to return for other calls. This
     * behaviour can be changed by setting a system property with key
     * {@code org.jdtaus.core.container.ContainerFactory} to the name of a
     * class defining a {@code public static Container getContainer()} method
     * returning the singleton instance of {@code Container}.</p>
     *
     * @return the singleton {@code Container} instance.
     *
     * @throws ContainerError for unrecoverable container errors.
     *
     * @see ContainerFactory#newContainer()
     */
    public static Container getContainer()
    {
        Object ret = null;
        final String factory = System.getProperty(
            ContainerFactory.class.getName() );

        try
        {
            if ( factory != null )
            {
                // Call getContainer() on that class.
                final Class clazz = ClassLoaderFactory.loadClass(
                    ContainerFactory.class, factory );

                final Method meth = clazz.getDeclaredMethod( "getContainer",
                                                             EMPTY );

                ret = meth.invoke( null, EMPTY );
            }
            else
            {
                if ( instance == null )
                {
                    instance = newContainer();
                }

                ret = instance;
            }

            return (Container) ret;
        }
        catch ( SecurityException e )
        {
            throw new ContainerError( e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new ContainerError( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new ContainerError( e );
        }
        catch ( InvocationTargetException e )
        {
            final Throwable targetException = e.getTargetException();

            if ( targetException instanceof Error )
            {
                throw (Error) targetException;
            }
            else if ( targetException instanceof RuntimeException )
            {
                throw (RuntimeException) targetException;
            }
            else
            {
                throw new ContainerError( targetException == null
                                          ? e
                                          : targetException );
            }
        }
        catch ( ClassCastException e )
        {
            throw new ContainerError( e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new ContainerError( e );
        }
    }

    /**
     * Creates a new instance of the {@code Container} singleton implementation.
     * <p>The container implementation to be used can be controlled via a system
     * property with key {@code org.jdtaus.core.container.Container} set to a
     * class name to be loaded as the container implementation.</p>
     * <p>This method should be used by {@code getContainer()} implementors to
     * retrieve a new {@code Container} instance.</p>
     *
     * @return a new instance of the configured {@code Container}
     * implementation.
     *
     * @throws ContainerError for unrecoverable container errors.
     */
    public static Container newContainer()
    {
        final String impl = System.getProperty( Container.class.getName(),
                                                DEFAULT_CONTAINER );

        Constructor ctor = null;

        try
        {
            final Class clazz = ClassLoaderFactory.loadClass(
                ContainerFactory.class, impl );

            ctor = clazz.getDeclaredConstructor( EMPTY );
            ctor.setAccessible( true );
            return (Container) ctor.newInstance( EMPTY );
        }
        catch ( SecurityException e )
        {
            throw new ContainerError( e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new ContainerError( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new ContainerError( e );
        }
        catch ( java.lang.InstantiationException e )
        {
            throw new ContainerError( e );
        }
        catch ( InvocationTargetException e )
        {
            final Throwable targetException = e.getTargetException();

            if ( targetException instanceof Error )
            {
                throw (Error) targetException;
            }
            else if ( targetException instanceof RuntimeException )
            {
                throw (RuntimeException) targetException;
            }
            else
            {
                throw new ContainerError( targetException == null
                                          ? e
                                          : targetException );

            }
        }
        catch ( ClassCastException e )
        {
            throw new ContainerError( e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new ContainerError( e );
        }
        finally
        {
            if ( ctor != null )
            {
                ctor.setAccessible( false );
            }
        }
    }

    //--------------------------------------------------------ContainerFactory--
}