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
package org.jdtaus.core.logging.spi;

/**
 * Logs events for a specific component.
 * <p>jDTAUS Core SPI {@code Logger} specification to be used by implementations
 * to log technical events.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @see org.jdtaus.core.container.Container
 */
public interface Logger
{
    //--Logger------------------------------------------------------------------

    /**
     * Getter for property {@code debugEnabled}.
     *
     * @return {@code true} if logging debug messages is enabled; {@code false}
     * if logging debug messages is disabled.
     */
    boolean isDebugEnabled();

    /**
     * Logs a message at log level {@code debug}.
     *
     * @param message the message to log.
     */
    void debug( String message );

    /**
     * Logs an exception at log level {@code debug}.
     *
     * @param t the exception to log.
     */
    void debug( Throwable t );

    /**
     * Getter for property {@code errorEnabled}.
     *
     * @return {@code true} if logging error messages is enabled; {@code false}
     * if logging error messages is disabled.
     */
    boolean isErrorEnabled();

    /**
     * Logs a message at log level {@code error}.
     *
     * @param message the message to log.
     */
    void error( String message );

    /**
     * Logs an exception at log level {@code error}.
     *
     * @param t the exception to log.
     */
    void error( Throwable t );

    /**
     * Getter for property {@code fatalEnabled}.
     *
     * @return {@code true} if logging fatal messages is enabled; {@code false}
     * if logging fatal messages is disabled.
     */
    boolean isFatalEnabled();

    /**
     * Logs a message at log level {@code fatal}.
     *
     * @param message the message to log.
     */
    void fatal( String message );

    /**
     * Logs an exception at log level {@code fatal}.
     *
     * @param t the exception to log.
     */
    void fatal( Throwable t );

    /**
     * Getter for property {@code infoEnabled}.
     *
     * @return {@code true} if logging info messages is enabled; {@code false}
     * if logging info messages is disabled.
     */
    boolean isInfoEnabled();

    /**
     * Logs a message at log level {@code info}.
     *
     * @param message the message to log.
     */
    void info( String message );

    /**
     * Logs an exception at log level {@code info}.
     *
     * @param t the exception to log.
     */
    void info( Throwable t );

    /**
     * Getter for property {@code traceEnabled}.
     *
     * @return {@code true} if logging trace messages is enabled; {@code false}
     * if logging trace messages is disabled.
     */
    boolean isTraceEnabled();

    /**
     * Logs a message at log level {@code trace}.
     *
     * @param message the message to log.
     */
    void trace( String message );

    /**
     * Logs an exception at log level {@code trace}.
     *
     * @param t the exception to log.
     */
    void trace( Throwable t );

    /**
     * Getter for property {@code warnEnabled}.
     *
     * @return {@code true} if logging warning messages is enabled;
     * {@code false} if logging warning messages is disabled.
     */
    boolean isWarnEnabled();

    /**
     * Logs a message at log level {@code warn}.
     *
     * @param message the message to log.
     */
    void warn( String message );

    /**
     * Logs an exception at log level {@code warn}.
     *
     * @param t the exception to log.
     */
    void warn( Throwable t );

    //------------------------------------------------------------------Logger--
}
