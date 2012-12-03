/*
 *  jDTAUS Core Messages
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
package org.jdtaus.core.messages;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.text.Message;

/**
 * {@code Message} stating how to report a bug.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
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
        if ( this.trackerUrl != null && this.reportAddress != null )
        {
            return new Object[]
                {
                    this.logDirectory.getAbsolutePath(),
                    this.trackerUrl.toExternalForm(),
                    this.reportAddress
                };
        }
        else if ( this.trackerUrl != null )
        {
            return new Object[]
                {
                    this.logDirectory.getAbsolutePath(),
                    this.trackerUrl.toExternalForm()
                };

        }
        else if ( this.reportAddress != null )
        {
            return new Object[]
                {
                    this.logDirectory.getAbsolutePath(),
                    this.reportAddress
                };

        }
        else
        {
            return new Object[]
                {
                    this.logDirectory.getAbsolutePath()
                };

        }
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
        if ( this.trackerUrl != null && this.reportAddress != null )
        {
            return this.getBugReportUrlAndEmailMessage(
                locale, this.logDirectory.getAbsolutePath(),
                this.trackerUrl.toExternalForm(), this.reportAddress );

        }
        else if ( this.trackerUrl != null )
        {
            return this.getBugReportUrlMessage(
                locale, this.logDirectory.getAbsolutePath(),
                this.trackerUrl.toExternalForm() );

        }
        else if ( this.reportAddress != null )
        {
            return this.getBugReportEmailMessage(
                locale, this.logDirectory.getAbsolutePath(),
                this.reportAddress );

        }
        else
        {
            return this.getBugReportMessage(
                locale, this.logDirectory.getAbsolutePath() );

        }
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
     * @throws NullPointerException if either {@code logDirectory} is
     * {@code null}.
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
     * <blockquote><pre>Bitte melden Sie dieses Problem. Fügen Sie Ihrem Fehlerbericht bitte eine Kopie der aktuellen Protokolldateien der Anwendung aus Verzeichnis {0} bei.</pre></blockquote>
     * <blockquote><pre>Please report this including a copy of the logfiles located in directory {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param logDirectory Directory holding the application's logfiles.
     *
     * @return Information about how to report a bug.
     */
    private String getBugReportMessage( final Locale locale,
            final java.lang.String logDirectory )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "bugReport", locale,
                new Object[]
                {
                    logDirectory
                });

    }

    /**
     * Gets the text of message <code>bugReportUrl</code>.
     * <blockquote><pre>Bitte melden Sie dieses Problem unter {1}. Fügen Sie Ihrem Fehlerbericht bitte eine Kopie der aktuellen Protokolldateien der Anwendung aus Verzeichnis {0} bei.</pre></blockquote>
     * <blockquote><pre>Please report this at {1} including a copy of the logfiles located in directory {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param logDirectory Directory holding the application's logfiles.
     * @param trackerUrl URL to the application's online bugtracking system.
     *
     * @return Information about how to report a bug.
     */
    private String getBugReportUrlMessage( final Locale locale,
            final java.lang.String logDirectory,
            final java.lang.String trackerUrl )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "bugReportUrl", locale,
                new Object[]
                {
                    logDirectory,
                    trackerUrl
                });

    }

    /**
     * Gets the text of message <code>bugReportEmail</code>.
     * <blockquote><pre>Bitte melden Sie dieses Problem per eMail an {1}. Fügen Sie Ihrem Fehlerbericht bitte eine Kopie der aktuellen Protokolldateien der Anwendung aus Verzeichnis {0} bei.</pre></blockquote>
     * <blockquote><pre>Please report this by sending an email to {1} including a copy of the logfiles located in directory {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param logDirectory Directory holding the application's logfiles.
     * @param reportAddress Email address to send bugreports to.
     *
     * @return Information about how to report a bug.
     */
    private String getBugReportEmailMessage( final Locale locale,
            final java.lang.String logDirectory,
            final java.lang.String reportAddress )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "bugReportEmail", locale,
                new Object[]
                {
                    logDirectory,
                    reportAddress
                });

    }

    /**
     * Gets the text of message <code>bugReportUrlAndEmail</code>.
     * <blockquote><pre>Bitte melden Sie dieses Problem entweder unter {1} oder per eMail an {2}. Fügen Sie Ihrem Fehlerbericht bitte eine Kopie der aktuellen Protokolldateien der Anwendung aus Verzeichnis {0} bei.</pre></blockquote>
     * <blockquote><pre>Please report this at {1} or send an email to {2} including a copy of the logfiles located in directory {0}.</pre></blockquote>
     *
     * @param locale The locale of the message instance to return.
     * @param logDirectory Directory holding the application's logfiles.
     * @param trackerUrl URL to the application's online bugtracking system.
     * @param reportAddress Email address to alternatively send bugreports to.
     *
     * @return Information about how to report a bug.
     */
    private String getBugReportUrlAndEmailMessage( final Locale locale,
            final java.lang.String logDirectory,
            final java.lang.String trackerUrl,
            final java.lang.String reportAddress )
    {
        return ContainerFactory.getContainer().
            getMessage( this, "bugReportUrlAndEmail", locale,
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
