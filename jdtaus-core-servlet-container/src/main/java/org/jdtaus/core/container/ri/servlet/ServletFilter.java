/*
 *  jDTAUS Core RI Servlet Container
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
package org.jdtaus.core.container.ri.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.Context;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ModelFactory;

/**
 * jDTAUS webapp integration filter.
 * <p>This filter needs to be setup in each application's {@code web.xml}
 * descriptor like:<pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;jDTAUS Integration Filter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.jdtaus.core.container.ri.servlet.ServletFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;</pre></p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 *
 * @see #init(FilterConfig)
 */
public class ServletFilter implements Filter
{
    //--ServletFilter-----------------------------------------------------------

    /** Holds thread local {@code ServletContext}s. */
    private static final ThreadLocal CONTEXTS = new ThreadLocal();

    /** Holds thread local {@code HttpSession}s. */
    private static final ThreadLocal SESSIONS = new ThreadLocal();

    /** Holds thread local {@code Locale}s. */
    private static final ThreadLocal LOCALES = new ThreadLocal();

    /** Holds the {@code ServletContext} the filter got initialized with. */
    private ServletContext servletContext;

    /**
     * Gets the thread local servlet context.
     *
     * @return the thread local servlet context.
     *
     * @throws ContextLostException if no servlet context is bound to the
     * current thread of execution.
     */
    public static ServletContext getServletContext()
    {
        final ServletContext context = (ServletContext) CONTEXTS.get();

        if ( context == null )
        {
            throw new ContextLostException( getLocale(),
                                            Thread.currentThread() );

        }

        return context;
    }

    /**
     * Gets the thread local HTTP session.
     *
     * @return the thread local HTTP session.
     *
     * @throws SessionLostException if no session is bound to the current
     * thread of execution or if the requesting {@code ServletRequest} is not
     * an instance of {@code HttpServletRequest}.
     */
    public static HttpSession getHttpSession()
    {
        final HttpSession session = (HttpSession) SESSIONS.get();

        if ( session == null )
        {
            throw new SessionLostException( getLocale(),
                                            Thread.currentThread() );

        }

        return session;
    }

    /**
     * Gets the thread local locale.
     *
     * @return the thread local locale.
     */
    public static Locale getLocale()
    {
        Locale locale = (Locale) LOCALES.get();
        return locale == null ? Locale.getDefault() : locale;
    }

    //-----------------------------------------------------------ServletFilter--
    //--Filter------------------------------------------------------------------

    /**
     * Initializes the filter by configuring {@code ContainerFactory},
     * {@code ModelFactory} and {@code ContextFactory} to use the
     * {@code ServletContextFactories} implementation.
     *
     * @see ServletContextFactories
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        this.servletContext = filterConfig.getServletContext();

        // Initialize system properties if nothing is provided.
        if ( System.getProperty( ContainerFactory.class.getName() ) == null )
        {
            System.setProperty( ContainerFactory.class.getName(),
                                ServletContextFactories.class.getName() );

            filterConfig.getServletContext().log( this.getSystemPropertyMessage(
                Locale.getDefault(), ContainerFactory.class.getName(),
                ServletContextFactories.class.getName() ) );

        }
        if ( System.getProperty( ContextFactory.class.getName() ) == null )
        {
            System.setProperty( ContextFactory.class.getName(),
                                ServletContextFactories.class.getName() );

            filterConfig.getServletContext().log( this.getSystemPropertyMessage(
                Locale.getDefault(), ContextFactory.class.getName(),
                ServletContextFactories.class.getName() ) );

        }
        if ( System.getProperty( ModelFactory.class.getName() ) == null )
        {
            System.setProperty( ModelFactory.class.getName(),
                                ServletContextFactories.class.getName() );

            filterConfig.getServletContext().log( this.getSystemPropertyMessage(
                Locale.getDefault(), ModelFactory.class.getName(),
                ServletContextFactories.class.getName() ) );

        }
        if ( System.getProperty( Context.class.getName() ) == null )
        {
            System.setProperty( Context.class.getName(),
                                HttpSessionContext.class.getName() );

            filterConfig.getServletContext().log( this.getSystemPropertyMessage(
                Locale.getDefault(), Context.class.getName(),
                ServletContextFactories.class.getName() ) );

        }
    }

    /**
     * Attaches the request's {@code HttpSession} with corresponding
     * {@code ServletContext} to the current thread of execution.
     *
     * @see #getServletContext()
     * @see #getHttpSession()
     */
    public void doFilter( final ServletRequest req, final ServletResponse rsp,
                          final FilterChain chain )
        throws IOException, ServletException
    {
        try
        {
            if ( req instanceof HttpServletRequest )
            {
                final HttpServletRequest request = (HttpServletRequest) req;
                final HttpSession session = request.getSession( true );

                CONTEXTS.set( session.getServletContext() );
                SESSIONS.set( session );
            }
            else
            {
                CONTEXTS.set( this.servletContext );
                SESSIONS.set( null );
            }

            LOCALES.set( req.getLocale() );
            chain.doFilter( req, rsp );
        }
        finally
        {
            this.removeThreadLocal( CONTEXTS );
            this.removeThreadLocal( SESSIONS );
            this.removeThreadLocal( LOCALES );
        }
    }

    public void destroy()
    {
        this.servletContext = null;
    }

    //------------------------------------------------------------------Filter--
    //--ServletFilter-----------------------------------------------------------

    private void removeThreadLocal( final ThreadLocal threadLocal )
    {
        try
        {
            // Try remove method introduced in JDK 1.5.
            final Method removeMethod = threadLocal.getClass().
                getDeclaredMethod( "remove", new Class[ 0 ] );

            removeMethod.invoke( threadLocal, null );
        }
        catch ( IllegalAccessException e )
        {
            threadLocal.set( null );
            this.servletContext.log( e.getMessage(), e );
        }
        catch ( IllegalArgumentException e )
        {
            threadLocal.set( null );
            this.servletContext.log( e.getMessage(), e );
        }
        catch ( InvocationTargetException e )
        {
            threadLocal.set( null );
            this.servletContext.log( e.getMessage(), e );
        }
        catch ( NoSuchMethodException e )
        {
            threadLocal.set( null );
        }
    }

    //-----------------------------------------------------------ServletFilter--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>systemProperty</code>.
     * <blockquote><pre>System Eigenschaft {0} auf {1} gesetzt.</pre></blockquote>
     * <blockquote><pre>System property {0} set to {1}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param propertyName Name of the updated system property.
     * @param value Value the system property got updated to.
     *
     * @return Information about an updated system property.
     */
    private String getSystemPropertyMessage( final Locale locale,
            final java.lang.String propertyName,
            final java.lang.String value )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "systemProperty", locale,
                new Object[]
                {
                    propertyName,
                    value
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
