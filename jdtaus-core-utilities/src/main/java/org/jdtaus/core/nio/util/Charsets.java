/*
 *  jDTAUS Core Utilities
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
package org.jdtaus.core.nio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Charset coder and decoder utility.
 * <p>This class extends the former charset provider implementations which
 * cannot be used in every environment (e.g. WebStart, Maven) without
 * installation in the JRE extensions directory where they are available to the
 * system classloader. It uses the same service provider files as the
 * platform implementation ({@code java.nio.charset.spi.CharsetProvider}) but
 * is capable of using the current thread's classloader before falling back
 * to the system classloader for loading {@code CharsetProvider} classes.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Charsets
{
    //--Charsets----------------------------------------------------------------

    /** Cached {@code CharsetProvider} instances. */
    private static final List providers = new LinkedList();

    /** Cached {@code Charset} instances by name. */
    private static final Map charsets = new HashMap( 100 );

    /** Private constructor. */
    private Charsets()
    {
        super();
    }

    /**
     * Gets a charset for the given name.
     *
     * @param name the name of the charset to return.
     *
     * @return a {@code Charset} corresponding to {@code name} or {@code null}
     * if no such {@code Charset} is available.
     *
     * @throws IOException if reading the service provider files fails.
     * @throws ClassNotFoundException if a service provider file defines
     * a class which cannot be loaded.
     * @throws InstantiationException if creating an instance of a
     * {@code CharsetProvider} fails.
     * @throws IllegalAccessException if a {@code CharsetProvider} class
     * does not define a public no-arg constructor.
     * @throws java.nio.charset.IllegalCharsetNameException if {@code name} is
     * no valid charset name.
     * @throws java.nio.charset.UnsupportedCharsetException if {@code name} is
     * not supported.
     */
    private static Charset getCharset( final String name )
        throws IOException, ClassNotFoundException, InstantiationException,
               IllegalAccessException
    {
        // Populate the provider list with available providers if it is empty.
        if ( providers.size() == 0 )
        {
            synchronized ( Charsets.class )
            {
                // Use the current thread's context classloader if available or
                // fall back to the system classloader.
                ClassLoader classLoader = Thread.currentThread().
                    getContextClassLoader();

                if ( classLoader == null )
                {
                    classLoader = ClassLoader.getSystemClassLoader();
                }

                assert classLoader != null :
                    "Expected system classloader to always be available.";

                // Read all service provider files and load all defined
                // provider classes.
                final Enumeration providerFiles = classLoader.getResources(
                    "META-INF/services/java.nio.charset.spi.CharsetProvider" );

                if ( providerFiles != null )
                {
                    while ( providerFiles.hasMoreElements() )
                    {
                        final URL url = ( URL ) providerFiles.nextElement();
                        BufferedReader reader = null;

                        try
                        {
                            String line;
                            reader = new BufferedReader(
                                new InputStreamReader( url.openStream(),
                                                       "UTF-8" ) );

                            while ( ( line = reader.readLine() ) != null )
                            {
                                // Check that the line denotes a valid Java
                                // classname and load that class using
                                // reflection.
                                if ( line.indexOf( '#' ) < 0 )
                                {
                                    providers.add(
                                        classLoader.loadClass( line ).
                                        newInstance() );

                                }
                            }

                            reader.close();
                            reader = null;
                        }
                        finally
                        {
                            if ( reader != null )
                            {
                                reader.close();
                            }
                        }
                    }
                }
            }
        }

        // Search cached charsets.
        Charset charset = ( Charset ) charsets.get( name );
        if ( charset == null )
        {
            synchronized ( Charsets.class )
            {
                // Search all available providers for a charset matching "name".
                for ( final Iterator it = providers.iterator(); it.hasNext();)
                {
                    charset =
                        ( ( CharsetProvider ) it.next() ).charsetForName( name );

                    if ( charset != null )
                    {
                        charsets.put( name, charset );
                        break;
                    }
                }
            }
        }

        // Fall back to platform charsets if nothing is found so far.
        if ( charset == null )
        {
            synchronized ( Charsets.class )
            {
                charset = Charset.forName( name );
                charsets.put( name, charset );
            }
        }

        return charset;
    }

    /**
     * Encodes a given string to an array of bytes representing the characters
     * of the string in a given charset.
     *
     * @param str the string to encode.
     * @param charset the name of the charset to use.
     *
     * @throws NullPointerException if {@code str} or {@code charset} is
     * {@code null}.
     * @throws java.nio.charset.IllegalCharsetNameException if {@code charset}
     * is no valid charset name.
     * @throws java.nio.charset.UnsupportedCharsetException if {@code charset}
     * is not supported.
     */
    public static byte[] encode( final String str, final String charset )
    {
        if ( str == null )
        {
            throw new NullPointerException( "str" );
        }
        if ( charset == null )
        {
            throw new NullPointerException( "charset" );
        }

        final byte[] ret;
        try
        {
            final Charset cset = Charsets.getCharset( charset );
            final ByteBuffer buf = cset.encode( str );

            if ( buf.hasArray() )
            {
                if ( buf.array().length == buf.limit() )
                {
                    ret = buf.array();
                }
                else
                {
                    ret = new byte[ buf.limit() ];
                    System.arraycopy( buf.array(), buf.arrayOffset(),
                                      ret, 0, ret.length );

                }
            }
            else
            {
                ret = new byte[ buf.limit() ];
                buf.rewind();
                buf.get( ret );
            }
        }
        catch ( final ClassNotFoundException e )
        {
            throw new AssertionError( e );
        }
        catch ( final InstantiationException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IOException e )
        {
            throw new AssertionError( e );
        }

        return ret;
    }

    /**
     * Decodes the bytes of a given array to a string.
     *
     * @param bytes the bytes to decode.
     * @param charset the name of the charset to use.
     *
     * @throws NullPointerException if {@code bytes} or {@code charset} is
     * {@code null}.
     * @throws java.nio.charset.IllegalCharsetNameException if {@code charset}
     * is no valid charset name.
     * @throws java.nio.charset.UnsupportedCharsetException if {@code charset}
     * is not supported.
     */
    public static String decode( final byte[] bytes, final String charset )
    {
        if ( bytes == null )
        {
            throw new NullPointerException( "bytes" );
        }
        if ( charset == null )
        {
            throw new NullPointerException( "charset" );
        }

        final String ret;
        try
        {
            final Charset cset = Charsets.getCharset( charset );
            final CharBuffer buf = cset.decode( ByteBuffer.wrap( bytes ) );

            if ( buf.hasArray() )
            {
                ret = String.valueOf( buf.array(), buf.arrayOffset(),
                                      buf.length() );

            }
            else
            {
                final char[] c = new char[ buf.length() ];
                buf.rewind();
                buf.get( c );
                ret = String.valueOf( c );
            }
        }
        catch ( final ClassNotFoundException e )
        {
            throw new AssertionError( e );
        }
        catch ( final InstantiationException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IOException e )
        {
            throw new AssertionError( e );
        }

        return ret;
    }

    /**
     * Decodes the bytes of a given array to a string.
     *
     * @param bytes the bytes to decode.
     * @param off the offset from where to start decoding.
     * @param count the number of bytes to decode starting at {@code offset}.
     * @param charset the name of the charset to use.
     *
     * @throws NullPointerException if {@code bytes} or {@code charset} is
     * {@code null}.
     * @throws IndexOutOfBoundsException if {@code off} is negative or greater
     * than the length of {@code bytes} or {@code off + count} is negative or
     * greater than the length of {@code bytes}.
     * @throws java.nio.charset.IllegalCharsetNameException if {@code charset}
     * is no valid charset name.
     * @throws java.nio.charset.UnsupportedCharsetException if {@code charset}
     * is not supported.
     */
    public static String decode( final byte[] bytes, final int off,
                                   final int count, final String charset )
    {
        if ( bytes == null )
        {
            throw new NullPointerException( "bytes" );
        }
        if ( charset == null )
        {
            throw new NullPointerException( "charset" );
        }
        if ( off < 0 || off >= bytes.length )
        {
            throw new ArrayIndexOutOfBoundsException( off );
        }
        if ( count < 0 || off + count >= bytes.length )
        {
            throw new ArrayIndexOutOfBoundsException( count + off );
        }

        final String ret;
        try
        {
            final Charset cset = Charsets.getCharset( charset );
            final CharBuffer buf = cset.decode(
                ByteBuffer.wrap( bytes, off, count ) );

            if ( buf.hasArray() )
            {
                ret = String.valueOf( buf.array(), buf.arrayOffset(),
                                      buf.length() );

            }
            else
            {
                final char[] c = new char[ buf.length() ];
                buf.rewind();
                buf.get( c );
                ret = String.valueOf( c );
            }
        }
        catch ( final ClassNotFoundException e )
        {
            throw new AssertionError( e );
        }
        catch ( final InstantiationException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new AssertionError( e );
        }
        catch ( final IOException e )
        {
            throw new AssertionError( e );
        }

        return ret;
    }

    //----------------------------------------------------------------Charsets--
}
