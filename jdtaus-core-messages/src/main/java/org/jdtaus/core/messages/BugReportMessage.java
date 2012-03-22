/*
 *  jDTAUS Core Messages
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
package org.jdtaus.core.messages;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * {@code Message} stating how to report a bug.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 */
public final class BugReportMessage extends Message
{
    //--Contstants--------------------------------------------------------------

    /** Serial version UID for backwards compatibility with 1.0.x classes. */
    private static final long serialVersionUID = -6031830488657149254L;

    //---------------------------------------------------------------Constants--
    //--Message-----------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @return Strings giving information for where to report bugs and where
     * to find any data to attach to any bug reports.
     * <ul>
     * <li>[0]: the absolute path of the directory holding the application's
     * logfiles.</li>
     * <li>[1]: URL of the online bugtracking system.</li>
     * <li>[2]: email address to alternatively send the bugreport to.</li>
     * </ul>
     */
    public Object[] getFormatArguments( final Locale locale )
    {
        return new Object[]
            {
                this.logDirectory.getAbsolutePath(),
                this.trackerUrl.toExternalForm(),
                this.reportAddress
            };
    }

    /**
     * {@inheritDoc}
     *
     * @return The corresponding text from the message's {@code ResourceBundle}:
     * <blockquote><pre>
     * Please report this at {1} or send
     * an email to {2} including a copy of the logfiles located in directory
     * {0}.
     * </pre></blockquote>
     */
    public String getText( final Locale locale )
    {
        return this.getBugReportMessage( locale,
                                         this.logDirectory.getAbsolutePath(),
                                         this.trackerUrl.toExternalForm(),
                                         this.reportAddress );

    }

    //-----------------------------------------------------------------Message--
    //--BugReportMessage--------------------------------------------------------

    /**
     * Directory holding the application's log files.
     * @serial
     */
    private File logDirectory;

    /**
     * URL of the online bugtracking system.
     * @serial
     */
    private URL trackerUrl;

    /**
     * Mail address to send the bugreport to.
     * @serial
     */
    private String reportAddress;

    /**
     * Creates a new {@code BugReportMessage} taking the application's logfile
     * directory, an URL to the application's online bugtracking system, and
     * an email address where to send bugreports to alternatively.
     *
     * @param logDirectory the directory holding the application's logfiles.
     * @param trackerUrl an URL to the application's online bugtracking system.
     * @param reportAddress an email address to alternatively send bugreports
     * to.
     *
     * @throws NullPointerException if either {@code logDirectory},
     * {@code trackerUrl} or {@code reportAddress} is {@code null}.
     * @throws IllegalArgumentException if {@code logDirectory} is not a
     * directory.
     */
    public BugReportMessage( final File logDirectory, final URL trackerUrl,
                             final String reportAddress )
    {
        if ( logDirectory == null )
        {
            throw new NullPointerException( "logDirectory" );
        }
        if ( !logDirectory.isDirectory() )
        {
            throw new IllegalArgumentException( logDirectory.getAbsolutePath() );
        }
        if ( trackerUrl == null )
        {
            throw new NullPointerException( "trackerUrl" );
        }
        if ( reportAddress == null )
        {
            throw new NullPointerException( "reportAddress" );
        }

        this.logDirectory = logDirectory;
        this.trackerUrl = trackerUrl;
        this.reportAddress = reportAddress;
    }

    //--------------------------------------------------------BugReportMessage--
    //--Messages----------------------------------------------------------------

// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:jdtausMessages
    // This section is managed by jdtaus-container-mojo.

    /**
     * Gets the text of message <code>bugReport</code>.
     * <blockquote><pre>Bitte berichten Sie dieses Problem entweder unter {1} oder per eMail an {2}. Fügen Sie Ihrem Fehlerbericht bitte eine Kopie der aktuellen Protokolldateien der Anwendung aus Verzeichnis {0} hinzu.</pre></blockquote>
     * <blockquote><pre>Please report this at {1} or send an email to {2} including a copy of the logfiles located in directory {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param logDirectory Directory holding the application's logfiles.
     * @param trackerUrl URL to the application's online bugtracking system.
     * @param reportAddress Email address to alternatively send bugreports to.
     *
     * @return Information about how to report a bug.
     */
    private String getBugReportMessage( final Locale locale,
            final java.lang.String logDirectory,
            final java.lang.String trackerUrl,
            final java.lang.String reportAddress )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "bugReport", locale,
                new Object[]
                {
                    logDirectory,
                    trackerUrl,
                    reportAddress
                });

    }

// </editor-fold>//GEN-END:jdtausMessages

    //----------------------------------------------------------------Messages--
}
