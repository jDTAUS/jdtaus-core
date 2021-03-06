/*
 *  jDTAUS Core API
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
package org.jdtaus.core.monitor;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Comparator;
import org.jdtaus.core.text.Message;

/**
 * A task of execution.
 * <p>A task is a sequence of operations taking time. This class defines
 * properties to be used by applications to query a task for information and for
 * canceling a task. Property {@code indeterminate} indicates if a task
 * supports properties {@code minimum}, {@code maximum} and {@code progress}.
 * Property {@code cancelable} indicates if a task may be canceled by an
 * application by setting property {@code cancelled} to {@code true}.
 * Properties {@code description} and {@code progressDescription} hold
 * presentation descriptions of a task itself and of the work currently
 * performed by a task.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see TaskEvent
 */
public abstract class Task implements Cloneable, Serializable
{
    //--Constants---------------------------------------------------------------

    /**
     * Comparator for sorting tasks in ascending order of creation time.
     * <p><b>Note:</b><br/>
     * This comparator imposes orderings that are inconsistent with equals.</p>
     */
    public static final Comparator ASCENDING = new AscendingTaskComparator();

    /**
     * Comparator for sorting tasks in descending order of creation time.
     * <p><b>Note:</b><br/>
     * This comparator imposes orderings that are inconsistent with equals.</p>
     */
    public static final Comparator DESCENDING =
        new DescendingTaskComparator();

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -3919376355708819307L;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /** Creates a new {@code Task} instance. */
    public Task()
    {
        super();
        this.uid = new UID();
        this.indeterminate = true;
        this.timestamp = System.currentTimeMillis();
    }

    //------------------------------------------------------------Constructors--
    //--Task--------------------------------------------------------------------

    /**
     * Unique task identifier.
     * @serial
     */
    private UID uid;

    /**
     * The timestamp this task got created.
     * @serial
     */
    private long timestamp;

    /**
     * Flag indicating that an application cancelled the task.
     * @serial
     */
    private boolean cancelled;

    /**
     * Description of the {@code Task}.
     * @serial
     */
    protected Message description;

    /**
     * Description of the progress of the {@code Task}.
     * @serial
     */
    protected Message progressDescription;

    /**
     * The lower bound of the range.
     * @serial
     */
    protected int minimum;

    /**
     * The upper bound of the range.
     * @serial
     */
    protected int maximum;

    /**
     * Indicates the progress of the task.
     * @serial
     */
    protected int progress;

    /**
     * Flag indicating if the operations performed by the task are of unknown
     * length.
     * @serial
     */
    protected boolean indeterminate;

    /**
     * Flag indicating that an application may cancel the task.
     * @serial
     */
    protected boolean cancelable;

    /**
     * Getter for property {@code timestamp}.
     *
     * @return the timestamp this task got created.
     */
    public final synchronized long getTimestamp()
    {
        return this.timestamp;
    }

    /**
     * Getter for property {@code description}.
     *
     * @return description of the task.
     */
    public final synchronized Message getDescription()
    {
        return this.description;
    }

    /**
     * Getter for property {@code progressDescription}.
     *
     * @return description of the progress of the task or {@code null}.
     */
    public final synchronized Message getProgressDescription()
    {
        return this.progressDescription;
    }

    /**
     * Gets the lower end of the progress value.
     *
     * @return an int representing the minimum value.
     *
     * @throws IllegalStateException if the task is indeterminate.
     */
    public final synchronized int getMinimum()
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }

        return this.minimum;
    }

    /**
     * Gets the higher end of the progress value.
     *
     * @return an int representing the maximum value.
     *
     * @throws IllegalStateException if the task is indeterminate.
     */
    public final synchronized int getMaximum()
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }

        return this.maximum;
    }

    /**
     * Gets the progress of the task.
     *
     * @return the progress of the task.
     *
     * @throws IllegalStateException if the task is indeterminate.
     */
    public final synchronized int getProgress()
    {
        if ( this.isIndeterminate() )
        {
            throw new IllegalStateException();
        }

        return this.progress;
    }

    /**
     * Flag indicating if the task supports properties {@code minimum},
     * {@code maximum} and {@code progress}.
     *
     * @return {@code true} if the operations performed by the task are of
     * unknown length; {@code false} if properties {@code minimum},
     * {@code maximum} and {@code progress} hold progress information.
     */
    public final synchronized boolean isIndeterminate()
    {
        return this.indeterminate;
    }

    /**
     * Flag indicating that the task supports property {@code cancelled}.
     *
     * @return {@code true} if the task supports property {@code cancelled};
     * {@code false} if property {@code cancelled} is ignored by the task.
     */
    public final synchronized boolean isCancelable()
    {
        return this.cancelable;
    }

    /**
     * Flag indicating that the task is canceled.
     *
     * @return {@code true} if the task is canceled; {@code false} else.
     *
     * @throws IllegalStateException if the task is not cancelable.
     */
    public final synchronized boolean isCancelled()
    {
        if ( !this.isCancelable() )
        {
            throw new IllegalStateException();
        }

        return this.cancelled;
    }

    /**
     * Setter for property {@code cancelled}.
     * <p>Applications may request cancellation of a {@code Task} by setting
     * property {@code cancelled} to {@code true}. Implementations indicate
     * support for task cancellation by property {@code cancelable}.</p>
     *
     * @param value {@code true} to cancel the task; {@code false} else.
     *
     * @throws IllegalStateException if the task is not cancelable or is already
     * canceled.
     */
    public final synchronized void setCancelled( final boolean value )
    {
        if ( !this.isCancelable() || this.isCancelled() )
        {
            throw new IllegalStateException();
        }

        this.cancelled = value;
    }

    /**
     * Creates a string representing the properties of the instance.
     *
     * @return a string representing the properties of the instance.
     */
    private String internalString()
    {
        return new StringBuffer( 500 ).append( '{' ).
            append( "uid=" ).append( this.uid ).
            append( ", indeterminate=" ).append( this.indeterminate ).
            append( ", cancelable=" ).append( this.cancelable ).
            append( ", cancelled=" ).append( this.cancelled ).
            append( ", maximum=" ).append( this.maximum ).
            append( ", minimum=" ).append( this.minimum ).
            append( ", progress=" ).append( this.progress ).
            append( ", description=" ).append( this.description ).
            append( ", progressDescription=" ).
            append( this.progressDescription ).
            append( ", timestamp=" ).append( this.timestamp ).
            append( '}' ).toString();

    }

    //--------------------------------------------------------------------Task--
    //--Object------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return super.toString() + this.internalString();
    }

    /**
     * Returns a hash code value for this object.
     *
     * @return a hash code value for this object.
     */
    public final int hashCode()
    {
        return this.uid.hashCode();
    }

    /**
     * Indicates whether some other object is equal to this one.
     * <p>Tasks internally cary a UID which is created during instantiation.
     * This UID is used for comparing {@code o} with the instance.</p>
     *
     * @param o the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as {@code o};
     * {@code false} otherwise.
     *
     * @see UID
     */
    public final boolean equals( final Object o )
    {
        return o == this || ( o instanceof Task &&
            ( (Task) o ).uid.equals( this.uid ) );

    }

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( final CloneNotSupportedException e )
        {
            throw new AssertionError( e );
        }
    }

    //------------------------------------------------------------------Object--
}

/**
 * Comparator for sorting tasks in ascending order of creation time.
 * <p><b>Note:</b><br/>
 * This comparator imposes orderings that are inconsistent with equals.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class AscendingTaskComparator implements Comparator, Serializable
{
    /** Creates a new {@code AscendingTaskComparator} instance. */
    AscendingTaskComparator()
    {
        super();
    }

    //--Comparator--------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if either {@code o1} or {@code o2} is
     * {@code null}.
     * @throws ClassCastException if either {@code o1} or {@code o2} is not an
     * instance of {@code Task}.
     */
    public int compare( final Object o1, final Object o2 )
    {
        if ( o1 == null )
        {
            throw new NullPointerException( "o1" );
        }
        if ( o2 == null )
        {
            throw new NullPointerException( "o2" );
        }
        if ( !( o1 instanceof Task ) )
        {
            throw new ClassCastException( o1.getClass().getName() );
        }
        if ( !( o2 instanceof Task ) )
        {
            throw new ClassCastException( o2.getClass().getName() );
        }

        // TODO JDK 1.5 Long.valueOf(long)
        final Long l1 = new Long( ( (Task) o1 ).getTimestamp() );
        final Long l2 = new Long( ( (Task) o2 ).getTimestamp() );
        return l1.compareTo( l2 );
    }

    //--------------------------------------------------------------Comparator--
}

/**
 * Comparator for sorting tasks in descending order of creation time.
 * <p><b>Note:</b><br/>
 * This comparator imposes orderings that are inconsistent with equals.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
class DescendingTaskComparator
    extends AscendingTaskComparator
{

    /** Creates a new {@code DescendingTaskComparator} instance. */
    DescendingTaskComparator()
    {
        super();
    }

    //--Comparator--------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if either {@code o1} or {@code o2} is
     * {@code null}.
     * @throws ClassCastException if either {@code o1} or {@code o2} is not an
     * instance of {@code Task}.
     */
    public int compare( final Object o1, final Object o2 )
    {
        return super.compare( o2, o1 );
    }

    //--------------------------------------------------------------Comparator--
}
