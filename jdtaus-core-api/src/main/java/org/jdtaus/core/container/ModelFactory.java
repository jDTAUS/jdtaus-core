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
 * Factory for the {@code Model} singleton.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class ModelFactory
{
    //--Constants---------------------------------------------------------------

    /** Default {@code Model} implementation. */
    private static final String DEFAULT_MODEL =
        "org.jdtaus.core.container.ri.client.DefaultModel";

    /** Empty array. */
    private static final Class[] EMPTY =
    {
    };

    //---------------------------------------------------------------Constants--
    //--ModelFactory------------------------------------------------------------

    /** Default singleton instance. */
    private static Model instance;

    /**
     * Gets the {@code Model} singleton.
     * <p>By default this class will instantiate a new instance and hold it in
     * a static class variable as the singleton to return for other calls. This
     * behaviour can be changed by setting a system property with key
     * {@code org.jdtaus.core.container.ModelFactory} to the name of a
     * class defining a {@code public static Model getModel()} method
     * returning the singleton instance of the model.</p>
     *
     * @return the singleton {@code Model} instance.
     *
     * @throws ModelError for unrecoverable model errors.
     *
     * @see ModelFactory#newModel()
     */
    public static Model getModel()
    {
        Object ret = null;
        final String factory = System.getProperty(
            ModelFactory.class.getName() );

        try
        {
            if ( factory != null )
            {
                // Call getModel() on that class.
                final Class clazz = ClassLoaderFactory.loadClass(
                    ModelFactory.class, factory );

                final Method meth = clazz.getDeclaredMethod( "getModel",
                                                             EMPTY );

                ret = meth.invoke( null, EMPTY );
            }
            else
            {
                if ( instance == null )
                {
                    instance = newModel();
                }

                ret = instance;
            }

            return (Model) ret;
        }
        catch ( SecurityException e )
        {
            throw new ModelError( e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new ModelError( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new ModelError( e );
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
                throw new ModelError( targetException == null
                                      ? e
                                      : targetException );

            }
        }
        catch ( ClassCastException e )
        {
            throw new ModelError( e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new ModelError( e );
        }
    }

    /**
     * Creates a new instance of the configured {@code Model} implementation.
     * <p>The implementation to be used can be controlled via a system property
     * with key {@code org.jdtaus.core.container.Model} set to a class
     * name to be loaded as the {@code Model} implementation.</p>
     * <p>This method should be used by {@code getModel()} implementors to
     * retrieve a new {@code Model} instance.</p>
     *
     * @return a new instance of the configured {@code Model} implementation.
     *
     * @throws ModelError for unrecoverable model errors.
     */
    public static Model newModel()
    {
        Constructor ctor = null;

        try
        {
            final String className = System.getProperty( Model.class.getName(),
                                                         DEFAULT_MODEL );

            final Class clazz = ClassLoaderFactory.loadClass(
                ModelFactory.class, className );

            ctor = clazz.getDeclaredConstructor( EMPTY );
            ctor.setAccessible( true );
            return (Model) ctor.newInstance( EMPTY );
        }
        catch ( SecurityException e )
        {
            throw new ModelError( e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new ModelError( e );
        }
        catch ( IllegalArgumentException e )
        {
            throw new ModelError( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new ModelError( e );
        }
        catch ( java.lang.InstantiationException e )
        {
            throw new ModelError( e );
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
                throw new ModelError( targetException == null
                                      ? e
                                      : targetException );

            }
        }
        catch ( ClassCastException e )
        {
            throw new ModelError( e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new ModelError( e );
        }
        finally
        {
            if ( ctor != null )
            {
                ctor.setAccessible( false );
            }
        }
    }

    //------------------------------------------------------------ModelFactory--
}
