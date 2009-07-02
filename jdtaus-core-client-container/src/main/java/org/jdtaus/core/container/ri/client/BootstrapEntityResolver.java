/*
 *  jDTAUS Core RI Client Container
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <schulte2005@users.sourceforge.net> (+49 2331 3543887)
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * {@code EntityResolver} implementation resolving any container specific
 * system ids to classpath resources.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class BootstrapEntityResolver implements EntityResolver
{
    //--Constants---------------------------------------------------------------

    /** Mapping of schema names to the corresponding classpath location. */
    private static final String[] SCHEMA_LOCATIONS =
    {
        "jdtaus-module-1.0.xsd", "org/jdtaus/core/container/xml/",
        "jdtaus-module-1.1.xsd", "org/jdtaus/core/container/xml/",
        "jdtaus-module-1.2.xsd", "org/jdtaus/core/model/container/module/",
        "jdtaus-core-1.0.xsd", "org/jdtaus/core/model/",
        "jdtaus-core-1.1.xsd", "org/jdtaus/core/model/",
        "jdtaus-text-1.0.xsd", "org/jdtaus/core/model/text/",
        "jdtaus-text-1.1.xsd", "org/jdtaus/core/model/text/",
        "jdtaus-monitor-1.0.xsd", "org/jdtaus/core/model/monitor/",
        "jdtaus-monitor-1.1.xsd", "org/jdtaus/core/model/monitor/",
        "jdtaus-container-1.0.xsd", "org/jdtaus/core/model/container/",
        "jdtaus-container-1.1.xsd", "org/jdtaus/core/model/container/"
    };

    //---------------------------------------------------------------Constants--
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
                    schemaName = schemaName.substring( lastIndexOfSlash + 1 );
                }

                for ( int i = SCHEMA_LOCATIONS.length - 2; i >= 0; i -= 2 )
                {
                    if ( SCHEMA_LOCATIONS[i].equals( schemaName ) )
                    {
                        final String schemaLocation =
                                     SCHEMA_LOCATIONS[i + 1] + schemaName;

                        final URL schemaUrl = this.getClass().getClassLoader().
                            getResource( schemaLocation );

                        if ( schemaUrl == null )
                        {
                            Logger.getLogger( this.getClass().getName() ).log(
                                Level.WARNING,
                                BootstrapEntityResolverBundle.getInstance().
                                getResourceNotAvailableMessage(
                                Locale.getDefault(), schemaLocation ) );

                            continue;
                        }

                        schemaSource = new InputSource();
                        schemaSource.setPublicId( publicId );
                        schemaSource.setSystemId( schemaUrl.toExternalForm() );

                        Logger.getLogger( this.getClass().getName() ).log(
                            Level.FINE,
                            BootstrapEntityResolverBundle.getInstance().
                            getResolvedSystemIdUriMessage(
                            Locale.getDefault(), systemUri.toASCIIString(),
                            schemaSource.getSystemId() ) );

                        break;
                    }

                }
            }
        }
        catch ( URISyntaxException e )
        {
            Logger.getLogger( this.getClass().getName() ).log(
                Level.WARNING,
                BootstrapEntityResolverBundle.getInstance().
                getUnsupportedSystemIdUriMessage( Locale.getDefault(), systemId,
                                                  e.getMessage() ) );

            schemaSource = null;
        }

        return schemaSource;
    }

    //----------------------------------------------------------EntityResolver--
}
