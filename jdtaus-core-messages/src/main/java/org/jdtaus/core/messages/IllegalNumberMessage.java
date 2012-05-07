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
 * Message stating that an illegal number was specified.
 *
 * @author Christian Schulte
 * @version $JDTAUS$
 * @since 1.10
 */
public final class IllegalNumberMessage extends Message
{
    //--IllegalNumberMessage----------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 6578496762473822182L;

    /**
     * The invalid number.
     * @serial
     */
    private Number invalidNumber;

    /**
     * The minimum required value.
     * @serial
     */
    private Number minimum;

    /**
     * The maximum allowed value.
     * @serial
     */
    private Number maximum;

    /**
     * Creates a new {@code IllegalNumberMessage} instance taking an invalid number, a minimum required value and a
     * maximum allowed value.
     *
     * @param invalidNumber The invalid number or {@code null} if no such number is known.
     * @param minimum The minimum required value or {@code null} if no such requirement exists.
     * @param maximum The maximum allowed value or {@code null} if no such limit exists.
     */
    public IllegalNumberMessage( final Number invalidNumber, final Number minimum, final Number maximum )
    {
        super();
        this.invalidNumber = invalidNumber;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    //----------------------------------------------------IllegalNumberMessage--
    //--Message-----------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <ul>
     * <li>[0]: The invalid number or {@code null} if no such number is known.</li>
     * <li>[1]: The minimum required value or {@code null} if no such requirement exists.</li>
     * <li>[2]: The maximum allowed value or {@code null} if no such limit exists.</li>
     * </ul>
     */
    public Object[] getFormatArguments( final Locale locale )
    {
        return new Object[]
            {
                this.invalidNumber, this.minimum, this.maximum
            };

    }

    public String getText( final Locale locale )
    {
        final StringBuffer b = new StringBuffer( 128 );

        if ( this.invalidNumber != null )
        {
            b.append( this.getIllegalValueMessage( locale, this.invalidNumber ) ).append( " " );
        }

        if ( this.minimum != null )
        {
            b.append( this.getIllegalMinimumValueMessage( locale, this.minimum ) ).append( " " );
        }

        if ( this.maximum != null )
        {
            b.append( this.getIllegalMaximumValueMessage( locale, this.maximum ) ).append( " " );
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
     * Gets the text of message <code>illegalValue</code>.
     * <blockquote><pre>Ungültiger Wert {0,number}.</pre></blockquote>
     * <blockquote><pre>Illegal value {0,number}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param value Illegal value.
     *
     * @return Information about an illegal value.
     */
    private String getIllegalValueMessage( final Locale locale,
            final java.lang.Number value )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalValue", locale,
                new Object[]
                {
                    value
                });

    }

    /**
     * Gets the text of message <code>illegalMaximumValue</code>.
     * <blockquote><pre>Größer als {0,number}.</pre></blockquote>
     * <blockquote><pre>Greater than {0,number}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param maximum Maximum allowed value.
     *
     * @return Information about an illegal maximum value.
     */
    private String getIllegalMaximumValueMessage( final Locale locale,
            final java.lang.Number maximum )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalMaximumValue", locale,
                new Object[]
                {
                    maximum
                });

    }

    /**
     * Gets the text of message <code>illegalMinimumValue</code>.
     * <blockquote><pre>Kleiner als {0,number}.</pre></blockquote>
     * <blockquote><pre>Less than {0,number}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param minimum Minimum required value.
     *
     * @return Information about an illegal minimum value.
     */
    private String getIllegalMinimumValueMessage( final Locale locale,
            final java.lang.Number minimum )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "illegalMinimumValue", locale,
                new Object[]
                {
                    minimum
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
