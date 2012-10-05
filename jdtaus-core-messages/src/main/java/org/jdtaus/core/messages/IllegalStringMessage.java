/*
 *  jDTAUS Core Messages
 *  Copyright (C) 2005 Christian Schulte
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
package org.jdtaus.core.messages;

import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * Message stating that an illegal string was specified.
 *
 * @author Christian Schulte
 * @version $JDTAUS$
 * @since 1.10
 */
public final class IllegalStringMessage extends Message
{
    //--IllegalStringMessage----------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.10.x classes. */
    private static final long serialVersionUID = -8783693360487171440L;

    /**
     * The invalid string.
     * @serial
     */
    private String invalidString;

    /**
     * The invalid characters of the string.
     * @serial
     */
    private char[] invalidCharacters;

    /**
     * The minimum required length.
     * @serial
     */
    private Number minimumLength;

    /**
     * The maximum allowed length.
     * @serial
     */
    private Number maximumLength;

    /**
     * Creates a new {@code IllegalStringMessage} instance taking an invalid string, an array of invalid characters,
     * a minimum required length and a maximum allowed length.
     *
     * @param invalidString The invalid string or {@code null} if no such string is known.
     * @param invalidCharacters The invalid characters or {@code null} if no such characters are known.
     * @param minimumLength The minimum required length or {@code null} if no such requirement exists.
     * @param maximumLength The maximum allowed length or {@code null} if no such limit exists.
     */
    public IllegalStringMessage( final String invalidString, final char[] invalidCharacters,
                                 final Number minimumLength, final Number maximumLength )
    {
        super();
        this.invalidString = invalidString;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;

        if ( invalidCharacters != null )
        {
            this.invalidCharacters = new char[ invalidCharacters.length ];

            for ( int i = 0, s0 = invalidCharacters.length; i < s0; i++ )
            {
                this.invalidCharacters[i] = invalidCharacters[i];
            }
        }
        else
        {
            this.invalidCharacters = null;
        }
    }

    //----------------------------------------------------IllegalStringMessage--
    //--Message-----------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <ul>
     * <li>[0]: The invalid string or {@code null} if no such string is known.</li>
     * <li>[1]: The invalid characters or {@code null} if no such characters are known.</li>
     * <li>[2]: The minimum required length or {@code null} if no such requirement exists.</li>
     * <li>[3]: The maximum allowed length or {@code null} if no such limit exists.</li>
     * </ul>
     */
    public Object[] getFormatArguments( final Locale locale )
    {
        return new Object[]
            {
                this.invalidString, this.invalidCharacters, this.minimumLength, this.maximumLength
            };

    }

    public String getText( final Locale locale )
    {
        final StringBuffer b = new StringBuffer( 128 );

        if ( this.invalidString != null )
        {
            b.append( this.getIllegalStringMessage( locale, this.invalidString ) ).append( " " );
        }

        if ( this.invalidCharacters != null )
        {
            final StringBuffer c = new StringBuffer( this.invalidCharacters.length * 2 ).append( "[" );

            for ( int i = 0, s0 = this.invalidCharacters.length; i < s0; i++ )
            {
                c.append( this.invalidCharacters[i] ).append( ", " );
            }

            c.setLength( c.length() - 2 );
            c.append( "]" );

            b.append( this.getIllegalCharactersMessage( locale, c.toString() ) ).append( " " );
        }

        if ( this.minimumLength != null )
        {
            b.append( this.getIllegalMinimumStringLengthMessage( locale, this.minimumLength ) ).append( " " );
        }

        if ( this.maximumLength != null )
        {
            b.append( this.getIllegalMaximumStringLengthMessage( locale, this.maximumLength ) ).append( " " );
        }

        if ( b.length() > 0 )
        {
            b.setLength( b.length() - 1 );
        }

        return b.toString();
    }

    //-----------------------------------------------------------------Message--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>illegalString</code>.
     * <blockquote><pre>Ungültige Zeichenkette ''{0}''.</pre></blockquote>
     * <blockquote><pre>Illegal string ''{0}''.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param illegalString Illegal string.
     *
     * @return Information about an illegal string.
     */
    private String getIllegalStringMessage( final Locale locale,
            final java.lang.String illegalString )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalString", locale,
                new Object[]
                {
                    illegalString
                });

    }

    /**
     * Gets the text of message <code>illegalMaximumStringLength</code>.
     * <blockquote><pre>Mehr als {0,number} Zeichen.</pre></blockquote>
     * <blockquote><pre>More than {0,number} characters.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param maximumLength Maximum allowed length.
     *
     * @return Information about an illegal maximum string length.
     */
    private String getIllegalMaximumStringLengthMessage( final Locale locale,
            final java.lang.Number maximumLength )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalMaximumStringLength", locale,
                new Object[]
                {
                    maximumLength
                });

    }

    /**
     * Gets the text of message <code>illegalMinimumStringLength</code>.
     * <blockquote><pre>Weniger als {0,number} Zeichen.</pre></blockquote>
     * <blockquote><pre>Less than {0,number} characters.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param minimumLength Minimum required length.
     *
     * @return Information about an illegal minimum string length.
     */
    private String getIllegalMinimumStringLengthMessage( final Locale locale,
            final java.lang.Number minimumLength )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalMinimumStringLength", locale,
                new Object[]
                {
                    minimumLength
                });

    }

    /**
     * Gets the text of message <code>illegalCharacters</code>.
     * <blockquote><pre>Ungültige Zeichen {0}.</pre></blockquote>
     * <blockquote><pre>Illegal characters {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param illegalCharacters Illegal characters.
     *
     * @return Information about illegal characters.
     */
    private String getIllegalCharactersMessage( final Locale locale,
            final java.lang.String illegalCharacters )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalCharacters", locale,
                new Object[]
                {
                    illegalCharacters
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
