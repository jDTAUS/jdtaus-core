/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (C) 2005 - 2007 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.mojo.container;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Base Mojo for manipulating source files.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public abstract class AbstractSourceMojo extends AbstractMojo {

    //--Configuration-----------------------------------------------------------

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject mavenProject;

    /**
     * Number of spaces to use per indentation level.
     * @parameter expression="${spacesPerIndentationLevel}" default-value="4"
     */
    private Integer spacesPerIndentationLevel;

    /**
     * The encoding to use for reading and writing sources. By default the
     * system's default encoding will be used.
     * @parameter expression="${encoding}"
     * @optional
     */
    private String encoding;

    /**
     * Flag indicating test mode. In test mode generated sources will be
     * printed to the console and no files will be written.
     * @parameter expression="${testMode}" default-value="false"
     */
    private Boolean testMode;

    /**
     * Language to be used for javadoc comments. By default the system's
     * default locale will be used.
     * @parameter expression="${locale}"
     * @optional
     */
    private String locale;

    /**
     * Project runtime classpath.
     *
     * @parameter expression="${project.runtimeClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * A regular expression used for excluding elements from the runtime
     * classpath elements. By default no elements will be excluded.
     * @parameter expression="${classPathElementsExcludeRegexp}"
     * @optional
     */
    private String classPathElementsExcludeRegexp;

    /**
     * Accessor to the currently executed {@code MavenProject}.
     *
     * @return the currently executed {@code MavenProject}.
     */
    protected final MavenProject getMavenProject() {
        return this.mavenProject;
    }

    /**
     * Getter for property {@code testMode}.
     *
     * @return {@code true} if no sources will be touched; {@code false} if
     * sources will be changed.
     */
    protected final boolean isTestMode() {
        return this.testMode.booleanValue();
    }

    /**
     * Getter for property {@code locale}.
     *
     * @return the locale to use for javadoc comments.
     */
    protected final Locale getLocale() {
        return this.locale == null ?
            Locale.getDefault() : new Locale(this.locale);

    }

    /**
     * Accessor to the runtime classpath elements.
     *
     * @return a list holding strings for each compile class path element.
     */
    protected final List getClasspathElements() {
        return this.classpathElements;
    }

    /**
     * Indicates wheter a class path element should be included in the
     * classpath.
     *
     * @param element the element to check for classpath inclusion.
     *
     * @return {@code true} if {@code element} should be included in the
     * classpath; {@code false} if not.
     *
     * @throws NullPointerException if {@code element} is {@code null}.
     */
    protected boolean isClasspathElementIncluded(final String element) {
        boolean ret = true;

        if(element == null) {
            throw new NullPointerException("element");
        }

        if(this.classPathElementsExcludeRegexp != null) {
            ret = !element.matches(this.classPathElementsExcludeRegexp);
        }

        return ret;
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractSourceMojo------------------------------------------------------

    /** Interface to manipulate source files. */
    public interface SourceEditor {

        /**
         * Replaces a line in a source file with some string.
         *
         * @param line next line of the currently edited source file or
         * {@code null} if the end of the file has been reached.
         *
         * @return the string to replace {@code line} with or {@code null}
         * to replace {@code line} with nothing.
         *
         * @throws MojoFailureException for unrecoverable errors.
         */
        String editLine(String line) throws MojoFailureException;

        /**
         * Flag indicating that the editor changed a line.
         *
         * @return {@code true} if the editor did change a line of input;
         * {@code false} if not.
         */
        boolean isModified();

    }

    /**
     * Accessor to messages. Gets the message for {@code key} from a
     * {@code ResourceBundle} named equally to the name returned by
     * {@code clazz.getName()}.
     *
     * @param clazz The class used to decide which bundle to use.
     * @param key The key of the message to return.
     *
     * @return the message for {@code key}.
     */
    protected final String getMessage(
        final Class clazz, final String key) {

        return ResourceBundle.getBundle(clazz.getName(),
            this.getLocale()).getString(key);

    }

    /**
     * Accessor to a {@code *.java} file for a given class name.
     *
     * @param className The class name to return the corresponding
     * {@code *.java} file for (needs to be in the project's compile source
     * roots). No inner classes are supported yet.
     *
     * @return the source file for the class with name {@code className}.
     *
     * @throws NullPointerException if {@code className} is {@code null}.
     * @throws MojoFailureException if no source file can be found.
     */
    protected final File getSource(
        final String className) throws MojoFailureException {

        if(className == null) {
            throw new NullPointerException("className");
        }

        File file;
        String source;
        File ret = null;
        final Iterator it;
        final String fileName = className.replace('.', File.separatorChar).
            concat(".java");

        for(it = this.getMavenProject().getCompileSourceRoots().
            iterator(); it.hasNext();) {

            source = (String) it.next();
            file = new File(source.concat(File.separator).concat(fileName));
            if(file.canRead() && file.canWrite()) {
                ret = file;
                break;
            } else {
                throw new MojoFailureException(AbstractSourceMojoBundle.
                    getCannotReadOrWriteFileMessage(this.getLocale()).
                    format(new Object[] { file.getAbsolutePath() }));

            }
        }

        if(ret == null) {
            throw new MojoFailureException(AbstractSourceMojoBundle.
                getSourceNotFoundMessage(this.getLocale()).
                format(new Object[] { className }));

        }

        return ret;
    }

    /**
     * Accessor to all {@code *.java} files.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * {@code *.java} files found in all compile source roots.
     *
     * @throws MojoFailureException if no source files could be found.
     */
    protected final Collection getAllSources() throws MojoFailureException {
        File file;
        File parentRoot;
        String sourceRoot;
        DirectoryScanner scanner;
        final Iterator i;
        Iterator j;
        final Collection files = new LinkedList();

        for(i = this.getMavenProject().getCompileSourceRoots().
            iterator(); i.hasNext();) {

            sourceRoot = (String) i.next();
            parentRoot = new File(sourceRoot);
            scanner = new DirectoryScanner();
            scanner.setBasedir(sourceRoot);
            scanner.setIncludes(new String[] {"**/*.java"});
            scanner.addDefaultExcludes();
            scanner.scan();

            for (j = Arrays.asList(scanner.getIncludedFiles()).
                iterator(); j.hasNext();) {

                file = new File(parentRoot, (String) j.next());
                files.add(file);
            }
        }

        return files;
    }

    /**
     * Edits the contents of a file.
     *
     * @param file The file to edit.
     * @param editor The editor to use for editing the file.
     *
     * @throws NullPointerException if either {@code file} or {@code editor} is
     * {@code null}.
     * @throws MojoFailureException for unrecoverable errors.
     */
    protected final void editFile(final File file,
        final AbstractSourceMojo.SourceEditor editor)
        throws MojoFailureException {

        if(file == null) {
            throw new NullPointerException("file");
        }
        if(editor == null) {
            throw new NullPointerException("editor");
        }

        int i;
        String line;
        String replacement;
        final StringWriter writer = new StringWriter();

        try {
            char c;
            final char[] chars;
            final Writer fileWriter;
            final BufferedReader reader;
            if(this.encoding == null) {
                reader = new BufferedReader(new FileReader(file));
            } else {
                reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), this.encoding));

            }

            while((line = reader.readLine()) != null) {
                replacement = editor.editLine(line);
                if(replacement != null) {
                    writer.write(replacement.concat("\n"));
                }
            }

            replacement = editor.editLine(null);
            if(replacement != null) {
                writer.write(replacement.concat("\n"));
            }

            reader.close();
            writer.close();

            replacement = writer.toString();
            chars = replacement.toCharArray();

            // Remove trailine newlines.
            for(i = chars.length - 1; i >= 0; i--) {
                if(chars[i] != '\n' && chars[i] != '\r') {
                    break;
                }
            }

            replacement = replacement.substring(0,
                i + System.getProperty("line.separator").length());


            if(this.isTestMode()) {
                System.out.println(replacement);
            } else if (i < chars.length - System.getProperty("line.separator").
                length() || editor.isModified()) {

                if(this.encoding == null) {
                    fileWriter = new FileWriter(file);
                } else {
                    fileWriter = new OutputStreamWriter(
                        new FileOutputStream(file), this.encoding);

                }

                fileWriter.write(replacement);
                fileWriter.close();
            }
        } catch(IOException e) {
            throw new MojoFailureException(e.getMessage());
        }
    }

    /**
     * Edits the contents of a source file for a given class name.
     *
     * @param className The name of the class whose defining source file should
     * be edited.
     * @param editor The editor to use for editing the file.
     *
     * @throws NullPointerException if either {@code className} or
     * {@code editor} is {@code null}.
     * @throws MojoFailureException for unrecoverable errors.
     *
     * @see #getSource(String)
     */
    protected final void editFile(final String className,
        final AbstractSourceMojo.SourceEditor editor)
        throws MojoFailureException {

        if(className == null) {
            throw new NullPointerException("className");
        }
        if(editor == null) {
            throw new NullPointerException("editor");
        }

        this.editFile(this.getSource(className), editor);
    }

    /**
     * Adds the configured number of spaces to a string buffer.
     *
     * @param stringBuffer The {@code StringBuffer} to add the number of spaces
     * to.
     *
     * @throws NullPointerException if {@code stringBuffer} is {@code null}.
     */
    protected final void indent(final StringBuffer stringBuffer) {
        if(stringBuffer == null) {
            throw new NullPointerException("stringBuffer");
        }

        final char[] spaces =
            new char[this.spacesPerIndentationLevel.intValue()];

        Arrays.fill(spaces, ' ');
        stringBuffer.append(spaces);
    }

    /**
     * Getter for the current context's class loader.
     *
     * @return the callee's context classloader.
     *
     * @throws MojoFailureException if no class loader is available.
     */
    protected ClassLoader getContextClassLoader() throws MojoFailureException {
        ClassLoader classLoader = Thread.currentThread().
            getContextClassLoader();

        if(classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        if(classLoader == null) {
            throw new MojoFailureException("classLoader");
        }

        return classLoader;
    }

    /**
     * Provides access to the project's compile classpath.
     *
     * @return a {@code ClassLoader} initialized with the project's runtime
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getRuntimeClassLoader() throws
        MojoFailureException {

        String element;
        File file;
        final Iterator it;
        final Collection urls = new LinkedList();

        try {
            for(it = this.getClasspathElements().iterator(); it.hasNext();) {
                element = (String) it.next();
                if(!urls.contains(element) &&
                    this.isClasspathElementIncluded(element)) {

                    file = new File(element);
                    urls.add(file.toURI().toURL());
                }
            }

            return new URLClassLoader(
                (URL[]) urls.toArray(new URL[urls.size()]),
                this.getContextClassLoader());

        } catch(MalformedURLException e) {
            throw new MojoFailureException(e.getMessage());
        }
    }

    //------------------------------------------------------AbstractSourceMojo--

}
