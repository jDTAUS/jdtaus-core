/*
 *  jDTAUS Core Utilities
 *  Copyright (c) 2005 Christian Schulte <schulte2005@users.sourceforge.net>
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
 *  $Id$
 */
package org.jdtaus.core.sax.util;

import java.io.IOException;
import org.jdtaus.core.container.ContainerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * {@code EntityResolver} implementation backed by a chain of resolvers used
 * for resolving entities.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public final class EntityResolverChain implements EntityResolver
{
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>DefaultResolvers</code> implementation.
     *
     * @return The configured <code>DefaultResolvers</code> implementation.
     */
    private EntityResolver[] getDefaultResolvers()
    {
        return (EntityResolver[]) ContainerFactory.getContainer().
            getDependency( this, "DefaultResolvers" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--EntityResolver----------------------------------------------------------

    /**
     * Resolves entities by querying all resolvers of the instance stopping at
     * the first one not returning {@code null}.
     *
     * @param publicId The public identifier of the external entity being
     * referenced, or {@code null} if none was supplied.
     * @param systemId The system identifier of the external entity being
     * referenced.
     *
     * @return An {@code InputSource} object describing the new input source, or
     * {@code null} to request that the parser open a regular URI connection to
     * the system identifier.
     *
     * @throws SAXException Any SAX exception, possibly wrapping another
     * exception.
     * @throws IOException A Java-specific IO exception, possibly the result of
     * creating a new {@code InputStream} or {@code Reader} for the
     * {@code InputSource}.
     *
     * @see EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity( final String publicId,
        final String systemId ) throws SAXException, IOException
    {
        InputSource inputSource = null;
        for ( int i = this.getResolvers().length - 1; i >= 0 &&
            inputSource == null; i-- )
        {
            final EntityResolver resolver = this.getResolvers()[i];
            inputSource = resolver.resolveEntity( publicId, systemId );
        }

        return inputSource;
    }

    //----------------------------------------------------------EntityResolver--
    //--EntityResolverChain-----------------------------------------------------

    /** Chain of resolvers to use. */
    private EntityResolver[] resolvers;

    /**
     * Creates a new {@code EntityResolverChain} instance backed by any
     * available {@code EntityResolver} implementation in the system.
     */
    public EntityResolverChain()
    {
        this( null );
    }

    /**
     * Creates a new {@code EntityResolverChain} instance taking an array of
     * resolvers to use for resolving entities.
     *
     * @param resolvers The resolvers to use for resolving entities.
     */
    public EntityResolverChain( final EntityResolver[] resolvers )
    {
        super();

        if ( resolvers != null && resolvers.length > 0 )
        {
            this.resolvers = resolvers;
        }
    }

    /**
     * Gets the resolvers of the instane.
     *
     * @return the resolvers used for resolving entities.
     */
    public EntityResolver[] getResolvers()
    {
        if ( this.resolvers == null )
        {
            this.resolvers = this.getDefaultResolvers();
        }

        return this.resolvers;
    }

    //-----------------------------------------------------EntityResolverChain--
}
