/*
 *  jDTAUS Core Utilities
 *  Copyright (C) 2005 Christian Schulte <cs@schulte.it>
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
 *  $JDTAUS$
 */
package org.jdtaus.core.sax.util;

import java.io.IOException;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * {@code EntityResolver} implementation backed by a chain of resolvers used
 * for resolving entities.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public final class EntityResolverChain implements EntityResolver
{
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
                                      final String systemId )
        throws SAXException, IOException
    {
        InputSource inputSource = null;
        for ( int i = this.getResolvers().length - 1;
              i >= 0 && inputSource == null; i-- )
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
            this.resolvers = (EntityResolver[]) resolvers.clone();
        }
    }

    /**
     * Gets the resolvers of the instance.
     *
     * @return the resolvers used for resolving entities.
     */
    public EntityResolver[] getResolvers()
    {
        if ( this.resolvers == null )
        {
            this.resolvers = this.getDefaultResolvers();

            if ( this.resolvers.length == 0 )
            {
                this.getLogger().warn(
                    this.getNoEntityResolversMessage( this.getLocale() ) );

            }
        }

        return (EntityResolver[]) this.resolvers.clone();
    }

    //-----------------------------------------------------EntityResolverChain--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>Logger</code> implementation.
     *
     * @return The configured <code>Logger</code> implementation.
     */
    private Logger getLogger()
    {
        return (Logger) ContainerFactory.getContainer().
            getDependency( this, "Logger" );

    }

    /**
     * Gets the configured <code>Locale</code> implementation.
     *
     * @return The configured <code>Locale</code> implementation.
     */
    private Locale getLocale()
    {
        return (Locale) ContainerFactory.getContainer().
            getDependency( this, "Locale" );

    }

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
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>noEntityResolvers</code>.
     * <blockquote><pre>Keine ''EntityResolver'' gefunden. Java XML-Parser öffnen standardmäßig reguläre - eventuell entfernte - URI-Verbindungen. Sie sollten mindestens eine ''EntityResolver''-Implementierung (z.B. jdtaus-core-entity-resolver-1.15-SNAPSHOT.jar) zur Verfügung stellen.</pre></blockquote>
     * <blockquote><pre>No entity resolvers found. Standard Java XML parsers fall back to opening regular - possibly remote - URI connections. You should provide at least one ''EntityResolver'' implementation (e.g. jdtaus-core-entity-resolver-1.15-SNAPSHOT.jar).</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return the text of message <code>noEntityResolvers</code>.
     */
    private String getNoEntityResolversMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "noEntityResolvers", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
