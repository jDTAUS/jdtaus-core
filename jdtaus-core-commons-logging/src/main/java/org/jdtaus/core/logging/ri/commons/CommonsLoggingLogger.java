/*
 *  jDTAUS Core RI Commons Logging
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
package org.jdtaus.core.logging.ri.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;

/**
 * jDTAUS Core SPI Commons Logging {@code Logger} implementation.
 * <p>The name of the commons logger is specified by property {@code name}.
 * Property {@code name} defaults to {@code org.jdtaus.runtime}.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.Container
 */
public class CommonsLoggingLogger implements Logger
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.logging.ri.commons.CommonsLoggingLogger</code>. */
    public CommonsLoggingLogger()
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
        return this.getLogger().isDebugEnabled();
    }

    public void debug( String message )
    {
        this.getLogger().debug( message );
    }

    public void debug( Throwable throwable )
    {
        this.getLogger().debug( throwable.getMessage(), throwable );
    }

    public boolean isErrorEnabled()
    {
        return this.getLogger().isErrorEnabled();
    }

    public void error( String message )
    {
        this.getLogger().error( message );
    }

    public void error( Throwable throwable )
    {
        this.getLogger().error( throwable.getMessage(), throwable );
    }

    public boolean isFatalEnabled()
    {
        return this.getLogger().isFatalEnabled();
    }

    public void fatal( String message )
    {
        this.getLogger().fatal( message );
    }

    public void fatal( Throwable throwable )
    {
        this.getLogger().fatal( throwable.getMessage(), throwable );
    }

    public boolean isInfoEnabled()
    {
        return this.getLogger().isInfoEnabled();
    }

    public void info( String message )
    {
        this.getLogger().info( message );
    }

    public void info( Throwable throwable )
    {
        this.getLogger().info( throwable.getMessage(), throwable );
    }

    public boolean isTraceEnabled()
    {
        return this.getLogger().isTraceEnabled();
    }

    public void trace( String message )
    {
        this.getLogger().trace( message );
    }

    public void trace( Throwable throwable )
    {
        this.getLogger().trace( throwable.getMessage(), throwable );
    }

    public boolean isWarnEnabled()
    {
        return this.getLogger().isWarnEnabled();
    }

    public void warn( String message )
    {
        this.getLogger().warn( message );
    }

    public void warn( Throwable throwable )
    {
        this.getLogger().warn( throwable.getMessage(), throwable );
    }

    //------------------------------------------------------------------Logger--
    //--CommonsLoggingLogger----------------------------------------------------

    /**
     * Requests a commons logging logger for the name given by property
     * {@code name}.
     *
     * @return the commons logging logger for the name given by property
     * {@code name}.
     */
    public Log getLogger()
    {
        return LogFactory.getLog( this.getName() );
    }

    //----------------------------------------------------CommonsLoggingLogger--
}
