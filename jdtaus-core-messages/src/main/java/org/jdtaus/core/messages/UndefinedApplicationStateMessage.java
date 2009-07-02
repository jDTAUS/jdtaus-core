/*
 *  jDTAUS Core Messages
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
package org.jdtaus.core.messages;

import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * {@code Message} stating that the application's state is undefined.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class UndefinedApplicationStateMessage extends Message
{
    //--Contstants--------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -224877321182624137L;

    //---------------------------------------------------------------Constants--
    //--Message-----------------------------------------------------------------

    /** Empty array. */
    private static final Object[] ARGUMENTS =
    {
    };

    /**
     * {@inheritDoc}
     *
     * @return an empty array, since the message has no arguments.
     */
    public Object[] getFormatArguments( final Locale locale )
    {
        return ARGUMENTS;
    }

    /**
     * {@inheritDoc}
     *
     * @return The corresponding text from the message's {@code ResourceBundle}:
     * <blockquote><pre>
     * The application may not work correctly any more. A shutdown is recommended!
     * Please close any open files and restart the application.
     * </pre></blockquote>
     */
    public String getText( final Locale locale )
    {
        return this.getUndefinedApplicationStateMessage( locale );
    }

    //-----------------------------------------------------------------Message--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>undefinedApplicationState</code>.
     * <blockquote><pre>Die Anwendung arbeitet eventuell nicht mehr korrekt weiter. Ein Neustart wird empfohlen.
     *Bitte schließen Sie alle momentan geöffneten Dateien und starten die Anwendung erneut.
     *
     *</pre></blockquote>
     * <blockquote><pre>The application may not work correctly any more. A shutdown is recommended!
     *Please close any open files and restart the application.
     *
     *</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Information about an illegal application state.
     */
    private String getUndefinedApplicationStateMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "undefinedApplicationState", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
