/*
 *  jDTAUS Core SPI
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
package org.jdtaus.core.monitor.spi;

import org.jdtaus.core.text.Message;

/**
 * Extends {@code Task} providing write access to the state of a {@code Task}
 * to be used by implementations.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public final class Task extends org.jdtaus.core.monitor.Task
{
    //--Constants---------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = 7910325638688854657L;

    //---------------------------------------------------------------Constants--
    //--Task--------------------------------------------------------------------

    /** Creates a new {@code Task} instance. */
    public Task()
    {
        super();
    }

    /**
     * Setter for property {@code description}.
     *
     * @param description the description for the task.
     *
     * @throws NullPointerException if {@code description} is {@code null}.
     */
    public synchronized void setDescription( final Message description )
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
    public synchronized void setProgressDescription(
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
    public synchronized void setMinimum( final int minimum )
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
    public synchronized void setMaximum( final int maximum )
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
    public synchronized void setProgress( final int progress )
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
    public synchronized void setIndeterminate( final boolean indeterminate )
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
    public synchronized void setCancelable( final boolean cancelable )
    {
        this.cancelable = cancelable;
    }

    //--------------------------------------------------------------------Task--
}
