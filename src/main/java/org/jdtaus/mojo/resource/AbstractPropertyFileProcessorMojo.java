/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.mojo.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Base class to process properties of property files found in the project
 * resources.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class AbstractPropertyFileProcessorMojo extends AbstractMojo {

    /**
     * The project resources.
     *
     * @parameter expression="${project.resources}"
     * @required
     */
    private List resources;

    /**
     * Regular expression to match files to ignore. By default all files ending
     * in <code>.properties</code> will be processed.
     *
     * @parameter expression="${fileExcludeRegexp}"
     * @optional
     */
    private String fileExcludeRegexp;

    /**
     * Regular expression a property key must match to be processed. By default
     * all property keys in a <code>.properties</code> file will be processed.
     *
     * @parameter expression="${keyMatchRegexp}" default-value=".*"
     */
    private String keyMatchRegexp;

    /**
     * Acessor to this classes messages.
     *
     * @param key key of the message to return.
     *
     * @return {@code MessageFormat} of the message with key {@code key}.
     *
     * @throws NullPointerException {@code if(key == null)}
     */
    private MessageFormat i18n(final String key) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        return new MessageFormat(
            ResourceBundle.getBundle(
            AbstractPropertyFileProcessorMojo.class.getName()).getString(key));

    }

    //--AbstractMojo------------------------------------------------------------

    /** Include all property files by default. */
    private static final String[] DEFAULT_INCLUDES = { "**/*.properties" };

    /**
     * Processes all property files from the projects resources. If a file's
     * name matches the regular expression of the optional property
     * {@code fileExcludeRegexp} it will be ignored. For all other property
     * files {@code processProperty(}} is called for all properties whose keys
     * are matching the value of property {@code keyMatchRegexp}.
     *
     * @throws MojoExecutionException for runtime errors.
     * @throws MojoFailureException for build failures.
     *
     * @see #processProperty(String, String)
     */
    public final void execute() throws MojoExecutionException,
        MojoFailureException {

        Resource rsrc;
        Properties props;
        DirectoryScanner scanner;
        final String[] msgArgs = new String[2];
        final Pattern fileExcludePat = this.fileExcludeRegexp != null ?
            Pattern.compile(this.fileExcludeRegexp) : null;

        for (Iterator i = this.resources.iterator(); i.hasNext();) {
            rsrc = (Resource) i.next();
            if (!new File(rsrc.getDirectory()).exists()) {
                continue;
            }

            scanner = new DirectoryScanner();
            scanner.setBasedir(rsrc.getDirectory());
            scanner.setIncludes(
                AbstractPropertyFileProcessorMojo.DEFAULT_INCLUDES);

            scanner.addDefaultExcludes();
            scanner.scan();

            for (Iterator j = Arrays.asList(scanner.getIncludedFiles()).
                iterator(); j.hasNext();) {

                msgArgs[0] = (String) j.next();
                if(fileExcludePat != null &&
                    fileExcludePat.matcher(msgArgs[0]).matches()) {

                    msgArgs[1] = this.fileExcludeRegexp;
                    this.getLog().info(this.i18n("ignoringFile").
                        format(msgArgs));

                } else {
                    props = new Properties();
                    try {
                        this.getLog().debug(this.i18n("testingFile").
                            format(msgArgs));

                        props.load(new FileInputStream(
                            new File(rsrc.getDirectory(), msgArgs[0])));

                        this.processProperties(props);
                    } catch (IOException e) {
                        throw new MojoExecutionException(
                            this.i18n("technicalError").format(null), e);

                    }
                }
            }
        }
    }

    private void processProperties(final Properties properties) {
        final String[] args = new String[2];
        final Pattern pat = Pattern.compile(this.keyMatchRegexp);
        args[1] = this.keyMatchRegexp;

        for(Iterator i = properties.keySet().iterator(); i.hasNext();) {
            args[0] = (String) i.next();
            if(pat.matcher(args[0]).matches()) {
                this.processProperty(args[0],
                    properties.getProperty(args[0]));

            } else {
                this.getLog().info(this.i18n("notMatchingKey").format(args));
            }
        }
    }

    //------------------------------------------------------------AbstractMojo--
    //--AbstractPropertyFileProcessor-------------------------------------------

    /**
     * Called for all properties to process.
     *
     * @param key the properties key.
     * @param value the properties value.
     */
    protected abstract void processProperty(String key, String value);

    //-------------------------------------------AbstractPropertyFileProcessor--

}