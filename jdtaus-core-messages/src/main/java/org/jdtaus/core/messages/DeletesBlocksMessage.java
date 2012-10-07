/*
 *  jDTAUS Core Messages
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
package org.jdtaus.core.messages;

import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * {@code Message} stating that blocks are being deleted.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public final class DeletesBlocksMessage extends Message
{
    //--Contstants--------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -6844483443532007265L;

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
     * Deletes blocks.
     * </pre></blockquote>
     */
    public String getText( final Locale locale )
    {
        return this.getDeletesBlocksMessage( locale );
    }

    //-----------------------------------------------------------------Message--
    //--DeletesBlocksMessage----------------------------------------------------

    /** Creates a new {@code DeletesBlocksMessage} instance. */
    public DeletesBlocksMessage()
    {
        super();
    }

    //----------------------------------------------------DeletesBlocksMessage--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>deletesBlocks</code>.
     * <blockquote><pre>Entfernt Satzabschnitte.</pre></blockquote>
     * <blockquote><pre>Deleting blocks.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     *
     * @return Progress description when blocks are deleted.
     */
    private String getDeletesBlocksMessage( final Locale locale )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "deletesBlocks", locale, null );

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
