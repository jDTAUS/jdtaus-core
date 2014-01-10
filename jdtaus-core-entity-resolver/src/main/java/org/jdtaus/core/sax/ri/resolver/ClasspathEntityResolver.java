/*
 *  jDTAUS Core RI Entity Resolver
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
package org.jdtaus.core.sax.ri.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * {@code EntityResolver} implementation resolving XML schemas from classpath
 * resources.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class ClasspathEntityResolver implements EntityResolver
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.sax.ri.resolver.ClasspathEntityResolver</code>. */
    public ClasspathEntityResolver()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
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

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>defaultSchemaExtensions</code>.
     *
     * @return Default extensions to match classpath resources with (separated by ',').
     */
    private java.lang.String getDefaultSchemaExtensions()
    {
        return (java.lang.String) ContainerFactory.getContainer().
            getProperty( this, "defaultSchemaExtensions" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--EntityResolver----------------------------------------------------------

    public InputSource resolveEntity( final String publicId,
                                      final String systemId )
        throws SAXException, IOException
    {
        if ( systemId == null )
        {
            throw new NullPointerException( "systemId" );
        }

        InputSource schemaSource = null;

        try
        {
            final URI systemUri = new URI( systemId );
            String schemaName = systemUri.getPath();
            if ( schemaName != null )
            {
                final int lastIndexOfSlash = schemaName.lastIndexOf( '/' );
                if ( lastIndexOfSlash != -1 &&
                     lastIndexOfSlash < schemaName.length() )
                {
                    schemaName =
                        schemaName.substring( lastIndexOfSlash + 1 );

                }

                final URL[] urls = this.getSchemaUrls();
                for ( int i = urls.length - 1; i >= 0; i-- )
                {
                    if ( urls[i].getPath().endsWith( schemaName ) )
                    {
                        schemaSource = new InputSource();
                        schemaSource.setPublicId( publicId );
                        schemaSource.setSystemId( urls[i].toExternalForm() );

                        if ( this.getLogger().isDebugEnabled() )
                        {
                            this.getLogger().debug(
                                this.getResolvedSystemIdMessage(
                                this.getLocale(), systemId,
                                schemaSource.getSystemId() ) );

                        }

                        break;
                    }
                }
            }
            else
            {
                this.getLogger().warn( this.getUnsupportedSystemIdUriMessage(
                    this.getLocale(), systemId, systemUri.toASCIIString() ) );

            }
        }
        catch ( final URISyntaxException e )
        {
            this.getLogger().warn( this.getUnsupportedSystemIdUriMessage(
                this.getLocale(), systemId, e.getMessage() ) );

            schemaSource = null;
        }

        return schemaSource;
    }

    //----------------------------------------------------------EntityResolver--
    //--ClasspathEntityResolver-------------------------------------------------

    /** Schema extensions. */
    private String[] schemaExtensions;

    /** URLs of all available classpath schema resources. */
    private URL[] schemaUrls;

    /**
     * Creates a new {@code ClasspathEntityResolver} instance taking the
     * extensions to match classpath resouces with.
     *
     * @param schemaExtensions extensions to match classpath resouces with.
     */
    public ClasspathEntityResolver( final String[] schemaExtensions )
    {
        if ( schemaExtensions != null && schemaExtensions.length > 0 )
        {
            this.schemaExtensions = (String[]) schemaExtensions.clone();
        }
    }

    /**
     * Gets the value of property {@code schemaExtensions}.
     *
     * @return extensions to match classpath resources with.
     */
    private String[] getSchemaExtensions()
    {
        if ( this.schemaExtensions == null )
        {
            this.schemaExtensions =
                this.getDefaultSchemaExtensions().split( "," );

        }

        return this.schemaExtensions;
    }

    /**
     * Gets URLs of all available classpath schema resources.
     *
     * @return URLs of all available classpath schema resources.
     */
    private URL[] getSchemaUrls()
    {
        if ( this.schemaUrls == null )
        {
            try
            {
                this.schemaUrls = this.getSchemaResources();
            }
            catch ( final IOException e )
            {
                this.getLogger().error( this.getDisabledMessage(
                    this.getLocale(), e.getMessage() ) );

                this.schemaUrls = null;
            }
            catch ( final URISyntaxException e )
            {
                this.getLogger().error( this.getDisabledMessage(
                    this.getLocale(), e.getMessage() ) );

                this.schemaUrls = null;
            }
        }

        return this.schemaUrls != null ? this.schemaUrls : new URL[ 0 ];
    }

    /**
     * Searches all available {@code META-INF/MANIFEST.MF} resources for
     * entries whose name end with one of the extensions specified by
     * property {@code schemaExtensions}.
     *
     * @return URLs of any matching resources.
     *
     * @throws IOException if reading or parsing fails.
     * @throws URISyntaxException if creating a schema URI fails.
     */
    private URL[] getSchemaResources() throws IOException, URISyntaxException
    {
        final ClassLoader cl = this.getClass().getClassLoader();
        final Set/*<URI>*/ schemaResources = new HashSet();

        for ( final Enumeration e = cl.getResources( "META-INF/MANIFEST.MF" );
              e.hasMoreElements(); )
        {
            final String[] extensions = this.getSchemaExtensions();
            final URL manifestUrl = (URL) e.nextElement();
            final String externalForm = manifestUrl.toExternalForm();
            final String baseUrl =
                externalForm.substring( 0, externalForm.indexOf( "META-INF" ) );

            final InputStream manifestStream = manifestUrl.openStream();
            final Manifest mf = new Manifest( manifestStream );

            manifestStream.close();

            for ( final Iterator it = mf.getEntries().entrySet().iterator();
                  it.hasNext(); )
            {
                final Map.Entry entry = (Map.Entry) it.next();
                for ( int i = extensions.length - 1; i >= 0; i-- )
                {
                    if ( entry.getKey().toString().toLowerCase().
                        endsWith( '.' + extensions[i].toLowerCase() ) )
                    {
                        final URL schemaUrl =
                            new URL( baseUrl + entry.getKey().toString() );

                        schemaResources.add( new URI( schemaUrl.toString() ) );

                        if ( this.getLogger().isDebugEnabled() )
                        {
                            this.getLogger().debug(
                                this.getCandidateSchemaMessage(
                                this.getLocale(),
                                schemaUrl.toExternalForm() ) );

                        }
                    }
                }
            }
        }

        final URL[] urls = new URL[ schemaResources.size() ];
        final Iterator it = schemaResources.iterator();
        for ( int i = 0; it.hasNext(); i++ )
        {
            urls[i] = ( (URI) it.next() ).toURL();
        }

        return urls;
    }

    //-------------------------------------------------ClasspathEntityResolver--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>candidateSchema</code>.
     * <blockquote><pre>{0} zur Liste der Schema-Kandidaten hinzugefügt.</pre></blockquote>
     * <blockquote><pre>Added {0} to the list of candidate schema resources.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param schemaLocation Location of the candidate schema.
     *
     * @return Information about a schema added to the list of candidate schemas.
     */
    private String getCandidateSchemaMessage( final Locale locale,
            final java.lang.String schemaLocation )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "candidateSchema", locale,
                new Object[]
                {
                    schemaLocation
                });

    }

    /**
     * Gets the text of message <code>resolvedSystemId</code>.
     * <blockquote><pre>{0}
     *        -> {1}</pre></blockquote>
     * <blockquote><pre>{0}
     *        -> {1}</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param systemId System id of the schema.
     * @param resolvedSystemId Resolved system id of the schema.
     *
     * @return Information about a resolved schema.
     */
    private String getResolvedSystemIdMessage( final Locale locale,
            final java.lang.String systemId,
            final java.lang.String resolvedSystemId )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "resolvedSystemId", locale,
                new Object[]
                {
                    systemId,
                    resolvedSystemId
                });

    }

    /**
     * Gets the text of message <code>unsupportedSystemIdUri</code>.
     * <blockquote><pre>Nicht unterstützter System-ID URI "{0}". {1}</pre></blockquote>
     * <blockquote><pre>Unsupported system id URI "{0}". {1}</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param systemIdUri Unsupported system id URI.
     * @param cause Cause the URI is not supported.
     *
     * @return Information about an unsupported system id URI.
     */
    private String getUnsupportedSystemIdUriMessage( final Locale locale,
            final java.lang.String systemIdUri,
            final java.lang.String cause )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "unsupportedSystemIdUri", locale,
                new Object[]
                {
                    systemIdUri,
                    cause
                });

    }

    /**
     * Gets the text of message <code>disabled</code>.
     * <blockquote><pre>Klassenpfad-Resourcen konnten nicht verarbeitet werden. Resolver wurde deaktiviert ! {0}</pre></blockquote>
     * <blockquote><pre>Could not process classpath resources. Resolver is disabled ! {0}</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param cause Cause the resolver is disabled.
     *
     * @return .
     */
    private String getDisabledMessage( final Locale locale,
            final java.lang.String cause )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "disabled", locale,
                new Object[]
                {
                    cause
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
