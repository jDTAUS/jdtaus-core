/*
 *  jDTAUS Core Utilities
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
package org.jdtaus.core.swing.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdtaus.core.container.ContainerFactory;

/**
 * {@code JComponent} for use as a blocking glasspane.
 * <p>Blocking is controlled by property {@code visible}. If {@code true},
 * the current look and feel's
 * {@link javax.swing.LookAndFeel#provideErrorFeedback(Component)
 * provideErrorFeedback} method is called whenever a mouse clicked
 * {@code MouseEvent}, or a {@code KeyEvent} of type
 * {@link KeyEvent#KEY_TYPED KEY_TYPED}, whose source is not {@code null} and
 * descending from the parent of this component, is received. Also the cursor is
 * set to the cursor to use when blocking is in effect. If {@code false}, the
 * cursor is reset to the system's default cursor and no more blocking is
 * performed. Property {@code visible} defaults to {@code false}.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class BlockingGlassPane extends JComponent
{
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>defaultCursorType</code>.
     *
     * @return Default cursor used when blocking is in effect.
     */
    private java.lang.Integer getDefaultCursorType()
    {
        return (java.lang.Integer) ContainerFactory.getContainer().
            getProperty( this, "defaultCursorType" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--JComponent--------------------------------------------------------------

    /**
     * Enables or disables blocking.
     * <p>This method registers a {@code KeyEventDispatcher} with AWT's current
     * {@code KeyboardFocusManager} blocking the keyboard for any user
     * interaction and updates the cursor to the cursor to use when blocking is
     * in effect if {@code visible} is {@code true}. If {@code visible} is
     * {@code false} the {@code KeyEventDispatcher} is removed and the cursor is
     * reset to the system's default cursor.</p>
     *
     * @param visible {@code true} to enable blocking; {@code false} to disable
     * blocking.
     */
    public void setVisible( final boolean visible )
    {
        final Runnable runnable = new Runnable()
        {

            public void run()
            {
                if ( visible )
                {
                    BlockingGlassPane.super.setVisible( true );
                    setCursor( getBlockingCursor() );
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().
                        addKeyEventDispatcher( keyDispatcher );

                }
                else
                {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().
                        removeKeyEventDispatcher( keyDispatcher );

                    setCursor( Cursor.getDefaultCursor() );
                    BlockingGlassPane.super.setVisible( false );
                    container.getContentPane().validate();
                }
            }
        };

        if ( !SwingUtilities.isEventDispatchThread() )
        {
            SwingUtilities.invokeLater( runnable );
        }
        else
        {
            runnable.run();
        }
    }

    //--------------------------------------------------------------JComponent--
    //--BlockingGlassPane-------------------------------------------------------

    /** {@code KeyEventDispatcher} used for blocking. */
    private class BlockingDispatcher implements Serializable,
        KeyEventDispatcher
    {

        public boolean dispatchKeyEvent( final KeyEvent e )
        {
            final Component source = e.getComponent();

            if ( source != null &&
                SwingUtilities.isDescendingFrom( source, getParent() ) &&
                e.getID() == KeyEvent.KEY_TYPED )
            {
                UIManager.getLookAndFeel().provideErrorFeedback( getParent() );
                e.consume();
            }

            return e.isConsumed();
        }
    }
    /**
     * Container this component is registered as the glasspane with.
     * @serial
     */
    private final RootPaneContainer container;

    /**
     * {@code KeyEventDispatcher} used for blocking keyboard interaction.
     * @serial
     */
    private final KeyEventDispatcher keyDispatcher;

    /**
     * Cursor to use when blocking is in effect.
     * @serial
     */
    private Cursor cursor;

    /**
     * Creates a new {@code BlockingGlassPane} instance taking the container
     * this component is registered with.
     *
     * @param container the container this component is registered with.
     *
     * @throws NullPointerException if {@code container} is {@code null}.
     */
    public BlockingGlassPane( final RootPaneContainer container )
    {
        super();

        if ( container == null )
        {
            throw new NullPointerException( "container" );
        }

        this.container = container;
        this.keyDispatcher = new BlockingDispatcher();
        super.setVisible( false );
        this.setOpaque( false );
        this.addMouseListener(
            new MouseAdapter()
            {

                public void mouseClicked( final MouseEvent e )
                {
                    UIManager.getLookAndFeel().
                        provideErrorFeedback( getParent() );

                }
            } );

    }

    /**
     * Creates a new {@code BlockingGlassPane} instance taking the container
     * this component is registered with and the cursor to use when blocking is
     * in effect.
     *
     * @param container the container this component is registered with.
     * @param cursor the cursor to use when blocking is in effect.
     *
     * @throws NullPointerException if either {@code container} or
     * {@code cursor} is {@code null}.
     */
    public BlockingGlassPane( final RootPaneContainer container,
                              final Cursor cursor )
    {
        this( container );

        if ( cursor == null )
        {
            throw new NullPointerException( "cursor" );
        }

        this.cursor = cursor;
    }

    /**
     * Gets the cursor used when blocking is in effect.
     * @return the cursor used when blocking is in effect.
     */
    private Cursor getBlockingCursor()
    {
        if ( this.cursor == null )
        {
            this.cursor = Cursor.getPredefinedCursor(
                this.getDefaultCursorType().intValue() );

        }

        return this.cursor;
    }

    //-------------------------------------------------------BlockingGlassPane--
}
