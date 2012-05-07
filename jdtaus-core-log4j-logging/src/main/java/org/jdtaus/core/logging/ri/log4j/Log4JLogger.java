/*
 *  jDTAUS Core RI Log4J Logging
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.logging.ri.log4j;

import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;

/**
 * jDTAUS Core SPI Log4J {@code Logger} implementation.
 * <p>The name of the Log4J logger is specified by property {@code name}.
 * Property {@code name} defaults to {@code org.jdtaus.runtime}.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see org.jdtaus.core.container.Container
 */
public class Log4JLogger implements Logger
{
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.logging.ri.log4j.Log4JLogger</code>. */
    public Log4JLogger()
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
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.DEBUG );
    }

    public void debug( String string )
    {
        this.getLogger().debug( string );
    }

    public void debug( Throwable throwable )
    {
        this.getLogger().debug( throwable.getMessage(), throwable );
    }

    public boolean isErrorEnabled()
    {
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.ERROR );
    }

    public void error( String string )
    {
        this.getLogger().error( string );
    }

    public void error( Throwable throwable )
    {
        this.getLogger().error( throwable.getMessage(), throwable );
    }

    public boolean isFatalEnabled()
    {
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.FATAL );
    }

    public void fatal( String string )
    {
        this.getLogger().fatal( string );
    }

    public void fatal( Throwable throwable )
    {
        this.getLogger().fatal( throwable.getMessage(), throwable );
    }

    public boolean isInfoEnabled()
    {
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.INFO );
    }

    public void info( String string )
    {
        this.getLogger().info( string );
    }

    public void info( Throwable throwable )
    {
        this.getLogger().info( throwable.getMessage(), throwable );
    }

    public boolean isTraceEnabled()
    {
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.TRACE );
    }

    public void trace( String string )
    {
        this.getLogger().trace( string );
    }

    public void trace( Throwable throwable )
    {
        this.getLogger().trace( throwable.getMessage(), throwable );
    }

    public boolean isWarnEnabled()
    {
        return this.getLogger().isEnabledFor( org.apache.log4j.Level.WARN );
    }

    public void warn( String string )
    {
        this.getLogger().warn( string );
    }

    public void warn( Throwable throwable )
    {
        this.getLogger().warn( throwable.getMessage(), throwable );
    }

    //------------------------------------------------------------------Logger--
    //--Log4JLogger-------------------------------------------------------------

    /**
     * Requests a Log4J logger for the name given by property {@code name}.
     *
     * @return the Log4J logger for the name given by property {@code name}.
     */
    public org.apache.log4j.Logger getLogger()
    {
        return org.apache.log4j.Logger.getLogger( this.getName() );
    }

    //-------------------------------------------------------------Log4JLogger--
}
