/*
 *  jDTAUS Core SPI
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
package org.jdtaus.core.monitor.spi;

import org.jdtaus.core.text.Message;

/**
 * Extends {@code Task} providing write access to the state of a {@code Task}
 * to be used by implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public final class Task extends org.jdtaus.core.monitor.Task
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 7910325638688854657L;

    //---------------------------------------------------------------Constants--
    //--Task--------------------------------------------------------------------

    /**
     * Setter for property {@code description}.
     *
     * @param description the description for the task.
     *
     * @throws NullPointerException if {@code description} is {@code null}.
     */
    synchronized public void setDescription( final Message description )
    {
        if ( description == null )
        {
            throw new NullPointerException( "description" );
        }

        this.description = description;
    }

    /**
     * Setter for property {@code progressDescription}.
     *
     * @param progressDescription the description for the progress of the task.
     *
     * @throws NullPointerException if {@code progressDescription} is
     * {@code null}.
     */
    synchronized public void setProgressDescription(
        final Message progressDescription )
    {
        if ( progressDescription == null )
        {
            throw new NullPointerException( "progressDescription" );
        }

        this.progressDescription = progressDescription;
    }

    /**
     * Sets the lower end of the progress value.
     *
     * @param minimum an int specifying the minimum value.
     *
     * @throws IllegalStateException if the task is indeterminate.
     */
    synchronized public void setMinimum( int minimum )
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }

        this.minimum = minimum;
    }

    /**
     * Sets the higher end of the progress value.
     *
     * @param maximum an int specifying the maximum value.
     *
     * @throws IllegalStateException if the task is indeterminate.
     */
    synchronized public void setMaximum( int maximum )
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }

        this.maximum = maximum;
    }

    /**
     * Sets the progress of the task.
     *
     * @param progress an int specifying the current value, between the
     * maximum and minimum specified for this task.
     *
     * @throws IllegalStateException if the task is indeterminate.
     * @throws IllegalArgumentException if {@code progress} is lower than
     * the minimum of the range or greater than the maximum of the range.
     */
    synchronized public void setProgress( final int progress )
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }
        if ( progress < this.minimum || progress > this.maximum )
        {
            throw new IllegalArgumentException( Integer.toString( progress ) );
        }

        this.progress = progress;
    }

    /**
     * Setter for property {@code indeterminate}.
     *
     * @param indeterminate {@code true} if the operations performed by the task
     * are of unknown length; {@code false} to support properties
     * {@code minimum}, {@code maximum} and {@code progress}.
     */
    synchronized public void setIndeterminate( final boolean indeterminate )
    {
        this.indeterminate = indeterminate;
    }

    /**
     * Setter for property {@code cancelable}.
     *
     * @param cancelable {@code true} to indicate support for property
     * {@code cancelled}; {@code false} to indicate that property
     * {@code cancelled} is not supported.
     */
    synchronized public void setCancelable( final boolean cancelable )
    {
        this.cancelable = cancelable;
    }

    //--------------------------------------------------------------------Task--
}
