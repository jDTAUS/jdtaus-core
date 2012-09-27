/*
 *  jDTAUS Core Container Mojo
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
package org.jdtaus.core.container.mojo;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classloader for providing classpath resources.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
final class ResourceLoader extends URLClassLoader
{

    /** The classloader delegated to. */
    private ClassLoader delegate;

    /** Mapping of classpath locations to a list of URLs to provide. */
    private Map locations = new HashMap();

    /**
     * Creates a new {@code ResourceLoader} instance taking a classloader
     * classloading is delegated to.
     *
     * @param delegate the classloader to delegate classloading to.
     */
    ResourceLoader( final ClassLoader delegate )
    {
        super( new URL[]
            {
            } );

        this.delegate = delegate;
    }

    /**
     * Creates a new {@code ResourceLoader} instance taking an array of
     * classpath URLs to back the instance and a classloader classloading is
     * delegated to.
     *
     * @param classpath array of classpath URLs to back the instance.
     * @param delegate the classloader to delegate classloading to.
     */
    ResourceLoader( final URL[] classpath, final ClassLoader delegate )
    {
        super( classpath );
        this.delegate = delegate;
    }

    protected Class findClass( final String name ) throws ClassNotFoundException
    {
        Class thisClass = null;

        try
        {
            thisClass = Class.forName( name, true, this.delegate );
        }
        catch ( ClassNotFoundException e )
        {
            thisClass = super.findClass( name );
        }

        return thisClass;
    }

    public URL findResource( final String name )
    {
        URL thisResource = null;

        if ( this.locations.containsKey( name ) )
        {
            final Set resources = (Set) this.locations.get( name );
            thisResource = (URL) resources.iterator().next();
        }
        else if ( ( thisResource = super.findResource( name ) ) == null )
        {
            thisResource = this.delegate.getResource( name );
        }

        return thisResource;
    }

    public Enumeration findResources( final String name ) throws IOException
    {
        Enumeration thisResources = null;

        if ( this.locations.containsKey( name ) )
        {
            final Set resources = (Set) this.locations.get( name );
            thisResources = Collections.enumeration( resources );
        }
        else
        {
            thisResources = super.findResources( name );
            if ( thisResources == null || !thisResources.hasMoreElements() )
            {
                thisResources = this.delegate.getResources( name );
            }
        }

        return thisResources;
    }

    /**
     * Adds a resource to the instance.
     *
     * @param name the name of the resource to add.
     * @param resource the resource to add.
     */
    public void addResource( final String location, final URL resource )
    {
        if ( location == null )
        {
            throw new NullPointerException( "location" );
        }
        if ( resource == null )
        {
            throw new NullPointerException( "resource" );
        }

        Set resources = (Set) this.locations.get( location );
        if ( resources == null )
        {
            resources = new HashSet();
            this.locations.put( location, resources );
        }

        resources.add( resource );
    }

}
