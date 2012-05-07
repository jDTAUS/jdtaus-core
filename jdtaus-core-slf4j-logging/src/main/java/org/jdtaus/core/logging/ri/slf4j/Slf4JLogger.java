/*
 *  jDTAUS Core RI Slf4J Logging
 *  Copyright (C) 2005 Christian Schulte <schulte2005@users.sourceforge.net>
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
 *  $JDTAUS$
 */
package org.jdtaus.core.logging.ri.slf4j;

import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.logging.spi.Logger;

/**
 * jDTAUS Core SPI Slf4J {@code Logger} implementation.
 * <p>The name of the Slf4J logger is specified by property {@code name}.
 * Property {@code name} defaults to {@code org.jdtaus.runtime}.</p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class Slf4JLogger implements Logger
{
    //--Logger------------------------------------------------------------------

    public boolean isDebugEnabled()
    {
        return this.getLogger().isDebugEnabled();
    }

    public void debug( final String message )
    {
        this.getLogger().debug( message );
    }

    public void debug( final Throwable t )
    {
        this.getLogger().debug( t.getMessage(), t );
    }

    public boolean isErrorEnabled()
    {
        return this.getLogger().isErrorEnabled();
    }

    public void error( final String message )
    {
        this.getLogger().error( message );
    }

    public void error( final Throwable t )
    {
        this.getLogger().error( t.getMessage(), t );
    }

    public boolean isFatalEnabled()
    {
        return this.isErrorEnabled();
    }

    public void fatal( final String message )
    {
        this.error( message );
    }

    public void fatal( final Throwable t )
    {
        this.error( t );
    }

    public boolean isInfoEnabled()
    {
        return this.getLogger().isInfoEnabled();
    }

    public void info( final String message )
    {
        this.getLogger().info( message );
    }

    public void info( final Throwable t )
    {
        this.getLogger().info( t.getMessage(), t );
    }

    public boolean isTraceEnabled()
    {
        return this.getLogger().isTraceEnabled();
    }

    public void trace( final String message )
    {
        this.getLogger().trace( message );
    }

    public void trace( final Throwable t )
    {
        this.getLogger().trace( t.getMessage(), t );
    }

    public boolean isWarnEnabled()
    {
        return this.getLogger().isWarnEnabled();
    }

    public void warn( final String message )
    {
        this.getLogger().warn( message );
    }

    public void warn( final Throwable t )
    {
        this.getLogger().warn( t.getMessage(), t );
    }

    //------------------------------------------------------------------Logger--
    //--Constructors------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausConstructors
    // This section is managed by jdtaus-container-mojo.

    /** Standard implementation constructor <code>org.jdtaus.core.logging.ri.slf4j.Slf4JLogger</code>. */
    public Slf4JLogger()
    {
        super();
    }

// </editor-fold>//GEN-END:jdtausConstructors

    //------------------------------------------------------------Constructors--
    //--Dependencies------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausDependencies
    // This section is managed by jdtaus-container-mojo.

// </editor-fold>//GEN-END:jdtausDependencies

    //------------------------------------------------------------Dependencies--
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
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
    //--Slf4JLogger-------------------------------------------------------------

    /**
     * Requests the Slf4J logger for the name given by property {@code name}.
     *
     * @return the Slf4J logger for the name given by property {@code name}.
     */
    public org.slf4j.Logger getLogger()
    {
        return org.slf4j.LoggerFactory.getLogger( this.getName() );
    }

    //-------------------------------------------------------------Slf4JLogger--
}
