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
package org.jdtaus.core.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Factory for the {@code Context} singleton.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public abstract class ContextFactory
{
    //--Constants---------------------------------------------------------------

    /** Default {@code Context} implementation. */
    private static final String DEFAULT_CONTEXT =
        "org.jdtaus.core.container.ri.client.DefaultContext";

    /** Empty array. */
    private static final Class[] EMPTY =
    {
    };

    //---------------------------------------------------------------Constants--
    //--ContextFactory----------------------------------------------------------

    /** Default singleton instance. */
    private static Context instance;

    /**
     * Gets the {@code Context} singleton.
     * <p>By default this class will instantiate a new context and hold it in a
     * static class variable as the singleton to return for other calls. This
     * behaviour can be changed by setting a system property with key
     * {@code org.jdtaus.core.container.ContextFactory} to the name of a
     * class defining a {@code public static Context getContext()} method
     * returning the singleton instance of {@code Context}.</p>
     *
     * @return the singleton {@code Context} instance.
     *
     * @throws ContextError for unrecoverable context errors.
     *
     * @see ContextFactory#newContext()
     */
    public static Context getContext()
    {
        Object ret = null;
        final String factory = System.getProperty(
            ContextFactory.class.getName() );

        try
        {
            if ( factory != null )
            {
                // Call getContext() on that class.
                final Class clazz = ClassLoaderFactory.loadClass(
                    ContextFactory.class, factory );

                final Method meth = clazz.getDeclaredMethod( "getContext",
                                                             EMPTY );

                ret = meth.invoke( null, EMPTY );
            }
            else
            {
                if ( instance == null )
                {
                    instance = newContext();
                }

                ret = instance;
            }

            return (Context) ret;
        }
        catch ( final SecurityException e )
        {
            throw new ContextError( e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new ContextError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new ContextError( e );
        }
        catch ( final InvocationTargetException e )
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
                throw new ContextError( targetException == null
                                        ? e
                                        : targetException );

            }
        }
        catch ( final ClassCastException e )
        {
            throw new ContextError( e );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ContextError( e );
        }
    }

    /**
     * Creates a new instance of the configured {@code Context} implementation.
     * <p>The context implementation to be used can be controlled via a system
     * property with key {@code org.jdtaus.core.container.Context} set
     * to a class name to be loaded as the context implementation.</p>
     * <p>This method should be used by {@code getContext()} implementors to
     * retrieve a new {@code Context} instance.</p>
     *
     * @return a new instance of the configured {@code Context}
     * implementation.
     *
     * @throws ContextError for unrecoverable context errors.
     */
    public static Context newContext()
    {
        final String impl = System.getProperty( Context.class.getName(),
                                                DEFAULT_CONTEXT );

        Constructor ctor = null;

        try
        {
            final Class clazz = ClassLoaderFactory.loadClass(
                ContextFactory.class, impl );

            ctor = clazz.getDeclaredConstructor( EMPTY );
            ctor.setAccessible( true );
            return (Context) ctor.newInstance( EMPTY );
        }
        catch ( final SecurityException e )
        {
            throw new ContextError( e );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new ContextError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new ContextError( e );
        }
        catch ( final java.lang.InstantiationException e )
        {
            throw new ContextError( e );
        }
        catch ( final InvocationTargetException e )
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
                throw new ContextError( targetException == null
                                        ? e
                                        : targetException );

            }
        }
        catch ( final ClassCastException e )
        {
            throw new ContextError( e );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ContextError( e );
        }
        finally
        {
            if ( ctor != null )
            {
                ctor.setAccessible( false );
            }
        }
    }

    //----------------------------------------------------------ContextFactory--
}
