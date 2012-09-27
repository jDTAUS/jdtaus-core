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
package org.jdtaus.core.container.ri.client;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Utility for loading classes.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
abstract class ClassLoaderFactory
{

    /**
     * Name of the system property controlling the use of the context
     * classloader.
     */
    private static final String SYS_ENABLE_CONTEXT_CLASSLOADER =
        "org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader";

    /**
     * Loads a class.
     * <p>This method tries to load the class named {@code className} using the
     * current thread's context classloader, if that classloader is enabled and
     * not {@code null}, or the classloader of the given class, if no context
     * classloader is available or the context classloader is disabled. Use of
     * the context classloader must be explicitly enabled by setting the system
     * property {@code org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader}
     * to {@code true}.</p>
     *
     * @param clazz The clazz whose classloader to use for loading classes, if
     * no thread context classloader is available or if the thread context
     * classloader is disabled.
     * @param className The name of the class to load.
     *
     * @return The class with name {@code className}.
     *
     * @throws ClassNotFoundException if no class matching {@code className} was
     * found.
     * @throws NullPointerException if {@code clazz} or {@code className} is
     * {@code null}.
     */
    static Class loadClass( final Class clazz,
                            final String className )
        throws ClassNotFoundException
    {
        if ( clazz == null )
        {
            throw new NullPointerException( "clazz" );
        }
        if ( className == null )
        {
            throw new NullPointerException( "className" );
        }

        return Class.forName( className, true, getClassLoader( clazz ) );
    }

    /**
     * Loads resources.
     * <p>This method tries to load resources matching {@code resourceName}
     * using the current thread's context classloader, if that classloader is
     * enabled and not {@code null}, or using the classloader of the given
     * class, if the context classloader has been disabled. Use of the context
     * classloader must be explicitly enabled by setting the system
     * property {@code org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader}
     * to {@code true}.</p>
     *
     * @param clazz The clazz whose classloader to use for loading classes, if
     * no thread context classloader is available or if the thread context
     * classloader is disabled.
     * @param resourceName The name of the resources to load.
     *
     * @return All resources matching with name {@code resourceName}.
     *
     * @throws IOException if loading resources fails.
     * @throws NullPointerException if {@code clazz} or {@code resourceName} is
     * {@code null}.
     */
    static Enumeration loadResources( final Class clazz,
                                      final String resourceName )
        throws IOException
    {
        if ( clazz == null )
        {
            throw new NullPointerException( "clazz" );
        }
        if ( resourceName == null )
        {
            throw new NullPointerException( "resourceName" );
        }

        return getClassLoader( clazz ).getResources( resourceName );
    }

    private static ClassLoader getClassLoader( final Class clazz )
    {
        final ClassLoader classLoader;
        if ( Boolean.getBoolean( SYS_ENABLE_CONTEXT_CLASSLOADER ) &&
             Thread.currentThread().getContextClassLoader() != null )
        {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        else
        {
            classLoader = clazz.getClassLoader() != null
                          ? clazz.getClassLoader()
                          : ClassLoader.getSystemClassLoader();

        }

        return classLoader;
    }

}
