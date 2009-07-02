/*
 *  jDTAUS Core RI JDK 1.4 Executor
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
package org.jdtaus.core.lang.ri.executor;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.lang.ExceptionEvent;
import org.jdtaus.core.lang.ExceptionListener;
import org.jdtaus.core.lang.spi.Executor;

/**
 * jDTAUS Core SPI JDK 1.4 {@code Executor} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see org.jdtaus.core.container.Container
 */
public class Jdk14Executor implements Executor
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.lang.ri.executor.Jdk14Executor</code>. */
    public Jdk14Executor()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>defaultThreadGroupName</code>.
     *
     * @return Default name of the thread group backing the implementation.
     */
    private java.lang.String getDefaultThreadGroupName()
    {
        return (java.lang.String) ContainerFactory.getContainer().
            getProperty( this, "defaultThreadGroupName" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--ExceptionEventSource----------------------------------------------------

    /** Holds {@code ExceptionListener}s. */
    private final EventListenerList listeners = new EventListenerList();

    public void addExceptionListener( final ExceptionListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        synchronized ( this.listeners )
        {
            this.listeners.add( ExceptionListener.class, listener );
        }
    }

    public void removeExceptionListener( final ExceptionListener listener )
    {
        if ( listener == null )
        {
            throw new NullPointerException( "listener" );
        }

        synchronized ( this.listeners )
        {
            this.listeners.remove( ExceptionListener.class, listener );
        }
    }

    public ExceptionListener[] getExceptionListeners()
    {
        synchronized ( this.listeners )
        {
            return (ExceptionListener[]) this.listeners.getListeners(
                ExceptionListener.class );

        }
    }

    //----------------------------------------------------ExceptionEventSource--
    //--ExceptionHandler--------------------------------------------------------

    /** List of exception handled by the instance. */
    private final List handledExceptions = new ArrayList( 255 );

    public ExceptionEvent[] getExceptionEvents()
    {
        synchronized ( this.handledExceptions )
        {
            return (ExceptionEvent[]) this.handledExceptions.toArray(
                new ExceptionEvent[ this.handledExceptions.size() ] );

        }
    }

    public void handle( final Throwable t )
    {
        if ( t == null )
        {
            throw new NullPointerException( "t" );
        }

        final ExceptionEvent evt =
            new ExceptionEvent( this, Thread.currentThread(), t );

        this.fireOnException( evt );
    }

    //--------------------------------------------------------ExceptionHandler--
    //--Executor----------------------------------------------------------------

    public void executeAsynchronously( final Runnable runnable )
    {
        if ( runnable == null )
        {
            throw new NullPointerException( "runnable" );
        }

        new Thread( this.getThreadGroup(), runnable ).start();
    }

    //----------------------------------------------------------------Executor--
    //--Jdk14Executor-----------------------------------------------------------

    /** The name of the thread group backing the instance. */
    private String threadGroupName;

    /**
     * {@code ThreadGroup} of threads executing the given {@code Runnable}s
     * asynchronously.
     */
    private ThreadGroup threadGroup;

    /**
     * Creates a new {@code Jdk14Executor} instance taking the name of the
     * thread group backing the instance.
     *
     * @param threadGroupName the name of the thread group backing the instance.
     */
    public Jdk14Executor( final String threadGroupName )
    {
        if ( threadGroupName != null )
        {
            this.threadGroupName = threadGroupName;
        }
    }

    /**
     * Group of threads executing the given {@code Runnable}s asynchronously.
     */
    private final class ThreadGroup extends java.lang.ThreadGroup
    {

        /**
         * Creates a new {@code ExecutorThreadGroup} instance taking its name.
         *
         * @param name the name of the new thread group.
         */
        ThreadGroup( final String name )
        {
            super( name );
        }

        /**
         * {@inheritdDoc}
         * <p>This method notifies any registered {@code ExceptionListener}s
         * about the given exception if it is not an instance of
         * {@code ThreadDeath}. For instances of {@code ThreadDeath} the super
         * classe's {@code uncaughtException(Thread, Throwable)} method is
         * called.</p>
         *
         * @param t the thread that is about to exit.
         * @param e the uncaught exception.
         */
        public void uncaughtException( final Thread t, final Throwable e )
        {
            if ( e instanceof ThreadDeath )
            {
                super.uncaughtException( t, e );
            }
            else
            {
                fireOnException(
                    new ExceptionEvent( Jdk14Executor.this, t, e ) );

            }
        }

    }

    /**
     * Gets the value of property {@code threadGroupName}.
     *
     * @return the name of the thread group backing the instance.
     */
    private String getThreadGroupName()
    {
        if ( this.threadGroupName == null )
        {
            this.threadGroupName = this.getDefaultThreadGroupName();
        }

        return this.threadGroupName;
    }

    /**
     * Gets the group of threads executing the given {@code Runnable}s
     * asynchronously.
     *
     * @return the group of threads executing the given {@code Runnable}s
     * asynchronously.
     */
    private ThreadGroup getThreadGroup()
    {
        if ( this.threadGroup == null )
        {
            this.threadGroup =
                new Jdk14Executor.ThreadGroup( this.getThreadGroupName() );

        }

        return this.threadGroup;
    }

    /**
     * Notifies all registered {@code ExceptionListener}s about a given
     * {@code ExceptionEvent}.
     *
     * @param e the event to provide to the listeners.
     *
     * @throws NullPointerException if {@code e} is {@code null}.
     */
    private void fireOnException( final ExceptionEvent e )
    {
        if ( e == null )
        {
            throw new NullPointerException( "e" );
        }

        synchronized ( this.handledExceptions )
        {
            this.handledExceptions.add( e );
        }

        synchronized ( this.listeners )
        {
            final Object[] list = this.listeners.getListenerList();
            for ( int i = list.length - 2; i >= 0; i -= 2 )
            {
                if ( list[i] == ExceptionListener.class )
                {
                    ( (ExceptionListener) list[i + 1] ).onException( e );
                }
            }
        }

        final ExceptionListener[] exceptionListener =
            this.getExceptionListener();

        for ( int i = exceptionListener.length - 1; i >= 0; i-- )
        {
            exceptionListener[i].onException( e );
        }
    }

    //-----------------------------------------------------------Jdk14Executor--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the configured <code>ExceptionListener</code> implementation.
     *
     * @return The configured <code>ExceptionListener</code> implementation.
     */
    private ExceptionListener[] getExceptionListener()
    {
        return (ExceptionListener[]) ContainerFactory.getContainer().
            getDependency( this, "ExceptionListener" );

    }

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
}
