/*
 *  jDTAUS Core RI JDK 1.4 Logging
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
package org.jdtaus.core.logging.ri.jdk14;

import java.util.logging.Level;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;

/**
 * jDTAUS Core SPI JDK 1.4 {@code Logger} implementation.
 * <p>The name of the JDK logger is specified by property {@code name}.
 * Property {@code name} defaults to {@code org.jdtaus.runtime}.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see org.jdtaus.core.container.Container
 */
public class JDK14Logger implements Logger
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.logging.ri.jdk14.JDK14Logger</code>. */
    public JDK14Logger()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Properties--------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausProperties
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the value of property <code>name</code>.
     *
     * @return Name uniquely identifying the logger.
     */
    public java.lang.String getName()
    {
        return (java.lang.String) ContainerFactory.getContainer().
            getProperty( this, "name" );

    }

// </editor-fold>//GEN-END:jdtausProperties

    //--------------------------------------------------------------Properties--
    //--Logger------------------------------------------------------------------

    public boolean isDebugEnabled()
    {
        return this.getLogger().isLoggable( Level.FINE );
    }

    public void debug( String message )
    {
        this.log( Level.FINE, message, null );
    }

    public void debug( Throwable throwable )
    {
        this.log( Level.FINE, throwable.getMessage(), throwable );
    }

    public boolean isErrorEnabled()
    {
        return this.getLogger().isLoggable( Level.SEVERE );
    }

    public void error( String message )
    {
        this.log( Level.SEVERE, message, null );
    }

    public void error( Throwable throwable )
    {
        this.log( Level.SEVERE, throwable.getMessage(), throwable );
    }

    public boolean isFatalEnabled()
    {
        return this.getLogger().isLoggable( Level.SEVERE );
    }

    public void fatal( String message )
    {
        this.log( Level.SEVERE, message, null );
    }

    public void fatal( Throwable throwable )
    {
        this.log( Level.SEVERE, throwable.getMessage(), throwable );
    }

    public boolean isInfoEnabled()
    {
        return this.getLogger().isLoggable( Level.INFO );
    }

    public void info( String message )
    {
        this.log( Level.INFO, message, null );
    }

    public void info( Throwable throwable )
    {
        this.log( Level.INFO, throwable.getMessage(), throwable );
    }

    public boolean isTraceEnabled()
    {
        return this.getLogger().isLoggable( Level.FINEST );
    }

    public void trace( String message )
    {
        this.log( Level.FINEST, message, null );
    }

    public void trace( Throwable throwable )
    {
        this.log( Level.FINEST, throwable.getMessage(), throwable );
    }

    public boolean isWarnEnabled()
    {
        return this.getLogger().isLoggable( Level.WARNING );
    }

    public void warn( String message )
    {
        this.log( Level.WARNING, message, null );
    }

    public void warn( Throwable throwable )
    {
        this.log( Level.WARNING, throwable.getMessage(), throwable );
    }

    //------------------------------------------------------------------Logger--
    //--JDK14Logger-------------------------------------------------------------

    /**
     * Requests the JDK logger for the name given by property {@code name}.
     *
     * @return the JDK logger for the name given by property {@code name}.
     */
    public java.util.logging.Logger getLogger()
    {
        return java.util.logging.Logger.getLogger( this.getName() );
    }

    private void log( final Level level, final String msg, final Throwable t )
    {
        if ( this.getLogger().isLoggable( level ) )
        {
            StackTraceElement caller;
            final Throwable x = new Throwable();
            final StackTraceElement[] elements = x.getStackTrace();

            String cname = "unknown";
            String method = "unknown";

            if ( elements != null && elements.length > 2 )
            {
                caller = elements[2];
                cname = caller.getClassName();
                method = caller.getMethodName();
            }

            if ( t == null )
            {
                this.getLogger().logp( level, cname, method, msg );
            }
            else
            {
                this.getLogger().logp( level, cname, method, msg, t );
            }
        }
    }

    //-------------------------------------------------------------JDK14Logger--
}
