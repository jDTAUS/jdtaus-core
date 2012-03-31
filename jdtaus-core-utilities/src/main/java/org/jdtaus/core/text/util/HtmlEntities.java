/*
 *  jDTAUS Core Utilities
 *  Copyright (C) 2012 Christian Schulte
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
package org.jdtaus.core.text.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides static methods for encoding/decoding HTML.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id: SwingMessagePane.java 8486 2012-03-23 00:07:37Z schulte2005 $
 * @since 1.11
 */
class HtmlEntities
{

    /** Mapping of HTML entity names to characters. */
    private static volatile Reference/*<Map<String,Character>>*/ entityMap;

    /** Mapping of characters to HTML entity names. */
    private static volatile Reference/*<Map<Character,String>>*/ characterMap;

    /** Constant for the prefix of keys of HTML entity properties. */
    private static final String PROPERTY_PREFIX = "HtmlEntities.entityName.";

    /**
     * Decodes an entity name to a character.
     *
     * @param entityName The name of the entity to decode.
     *
     * @return The character represented by {@code entityName} or {@code null}, if no such character is found.
     *
     * @throws NullPointerException if {@code entityName} is {@code null}.
     */
    static Character toCharacter( final String entityName )
    {
        if ( entityName == null )
        {
            throw new NullPointerException( "entityName" );
        }

        Map map = entityMap != null ? (Map) entityMap.get() : null;

        if ( map == null )
        {
            map = new HashMap/*<String,Character>*/( 512 );
            InputStream in = null;
            boolean close = true;

            final URL resources = HtmlEntities.class.getResource( "HtmlEntities.properties" );
            final Properties properties = new Properties();

            try
            {
                in = resources.openStream();
                properties.load( in );
                in.close();
                close = false;
            }
            catch ( final IOException e )
            {
                throw new AssertionError( e );
            }
            finally
            {
                try
                {
                    if ( close && in != null )
                    {
                        in.close();
                    }
                }
                catch ( final IOException e )
                {
                    throw new AssertionError( e );
                }
            }

            for ( final Enumeration e = properties.propertyNames(); e.hasMoreElements(); )
            {
                final String name = (String) e.nextElement();
                final String value = properties.getProperty( name );

                if ( name.startsWith( PROPERTY_PREFIX ) )
                {
                    map.put( name.substring( PROPERTY_PREFIX.length() ), Character.valueOf( value.charAt( 0 ) ) );
                }
            }

            entityMap = new SoftReference( Collections.synchronizedMap( map ) );
        }

        return (Character) map.get( entityName );
    }

    /**
     * Encodes a character to an entity name.
     *
     * @param character The character to encode.
     *
     * @return The entity name representing {@code character} or {@code null}, if no such entity name is found.
     *
     * @throws NullPointerException if {@code character} is {@code null}.
     */
    static String toEntity( final Character character )
    {
        if ( character == null )
        {
            throw new NullPointerException( "character" );
        }

        Map map = characterMap != null ? (Map) characterMap.get() : null;

        if ( map == null )
        {
            map = new HashMap/*<Character,String>*/( 512 );
            InputStream in = null;
            boolean close = true;

            final URL resources = HtmlEntities.class.getResource( "HtmlEntities.properties" );
            final Properties properties = new Properties();

            try
            {
                in = resources.openStream();
                properties.load( in );
                in.close();
                close = false;
            }
            catch ( final IOException e )
            {
                throw new AssertionError( e );
            }
            finally
            {
                try
                {
                    if ( close && in != null )
                    {
                        in.close();
                    }
                }
                catch ( final IOException e )
                {
                    throw new AssertionError( e );
                }
            }

            for ( final Enumeration e = properties.propertyNames(); e.hasMoreElements(); )
            {
                final String name = (String) e.nextElement();
                final String value = properties.getProperty( name );

                if ( name.startsWith( PROPERTY_PREFIX ) )
                {
                    map.put( Character.valueOf( value.charAt( 0 ) ), name.substring( PROPERTY_PREFIX.length() ) );
                }
            }

            characterMap = new SoftReference( Collections.synchronizedMap( map ) );
        }

        return (String) map.get( character );
    }

    /**
     * Encodes a string to HTML.
     *
     * @param str The string to encode or {@code null}.
     *
     * @return {@code str} encoded to HTML or {@code null}.
     */
    static String escapeHtml( final String str )
    {
        String encoded = null;

        if ( str != null )
        {
            final StringBuffer b = new StringBuffer( str.length() );

            for ( int i = 0, s0 = str.length(); i < s0; i++ )
            {
                final Character c = Character.valueOf( str.charAt( i ) );
                final String entityName = toEntity( c );

                if ( entityName != null )
                {
                    b.append( '&' ).append( entityName ).append( ';' );
                }
                else
                {
                    b.append( c );
                }
            }

            encoded = b.toString();
        }

        return encoded;
    }

    /**
     * Decodes HTML to a string.
     *
     * @param html The HTML to decode or {@code null}.
     *
     * @return {@code html} decoded or {@code null}.
     */
    static String unescapeHtml( final String html )
    {
        String decoded = null;

        if ( html != null )
        {
            final StringBuffer b = new StringBuffer( html.length() );
            final StringBuffer entityName = new StringBuffer( 8 );
            boolean parsingEntityName = false;

            for ( int i = 0, s0 = html.length(); i < s0; i++ )
            {
                final char c = html.charAt( i );

                if ( c == '&' )
                {
                    parsingEntityName = true;
                    entityName.setLength( 0 );
                }

                if ( parsingEntityName )
                {
                    if ( c == ';' )
                    {
                        final Character character = toCharacter( entityName.toString() );

                        if ( character != null )
                        {
                            b.append( character );
                        }
                        else
                        {
                            b.append( '&' ).append( entityName.toString() ).append( ';' );
                        }

                        parsingEntityName = false;
                    }
                    else
                    {
                        entityName.append( c );
                    }
                }
                else
                {
                    b.append( c );
                }
            }

            decoded = b.toString();
        }

        return decoded;
    }

}
