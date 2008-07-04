/*
 *  jDTAUS Core Container Mojo
 *  Copyright (c) 2005 Christian Schulte
 *
 *  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
 *  <cs@jdtaus.org> (+49 2331 3543887)
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
package org.jdtaus.core.container.mojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
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
import org.apache.maven.artifact.DependencyResolutionRequiredException;
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
public abstract class AbstractSourceMojo extends AbstractMojo
{
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
     * Project test classpath.
     *
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List testClasspathElements;

    /**
     * A regular expression used for excluding elements from the runtime
     * classpath elements. By default no elements will be excluded.
     * @parameter expression="${classPathElementsExcludeRegexp}"
     * @optional
     */
    private String classPathElementsExcludeRegexp;

    /**
     * A regular expression used for excluding elements from the test
     * classpath elements. By default no elements will be excluded.
     * @parameter expression="${testClassPathElementsExcludeRegexp}"
     * @optional
     */
    private String testClassPathElementsExcludeRegexp;

    /**
     * Accessor to the currently executed {@code MavenProject}.
     *
     * @return the currently executed {@code MavenProject}.
     */
    protected final MavenProject getMavenProject()
    {
        return this.mavenProject;
    }

    /**
     * Getter for property {@code testMode}.
     *
     * @return {@code true} if no sources will be touched; {@code false} if
     * sources will be changed.
     */
    protected final boolean isTestMode()
    {
        return this.testMode.booleanValue();
    }

    /**
     * Getter for property {@code locale}.
     *
     * @return the locale to use for javadoc comments.
     */
    protected final Locale getLocale()
    {
        return this.locale == null
            ? Locale.getDefault()
            : new Locale( this.locale );

    }

    /**
     * Accessor to the runtime classpath elements.
     *
     * @return a list holding strings for each runtime class path element.
     */
    protected final List getClasspathElements()
    {
        return this.classpathElements;
    }

    /**
     * Accessor to the test classpath elements.
     *
     * @return a list holding strings for each test class path element.
     */
    protected final List getTestClasspathElements()
    {
        return this.testClasspathElements;
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
    protected boolean isClasspathElementIncluded( final String element )
    {
        if ( element == null )
        {
            throw new NullPointerException( "element" );
        }

        boolean ret = !this.isClasspathElementDefaultExlude( element );

        if ( ret && this.classPathElementsExcludeRegexp != null )
        {
            ret = !element.matches( this.classPathElementsExcludeRegexp );
        }

        return ret;
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
    protected boolean isTestClasspathElementIncluded( final String element )
    {
        if ( element == null )
        {
            throw new NullPointerException( "element" );
        }

        boolean ret = !this.isClasspathElementDefaultExlude( element );

        if ( ret && this.testClassPathElementsExcludeRegexp != null )
        {
            ret = !element.matches( this.testClassPathElementsExcludeRegexp );
        }

        return ret;
    }

    /**
     * Indicates whether a class path element is on the list of default class
     * path element excludes.
     *
     * @param element the element to check for default classpath exclusion.
     *
     * @return {@code true} if {@code element} should be excluded by default;
     * {@code false} if not.
     *
     * @throws NullPointerException if {@code element} is {@code null}.
     */
    protected boolean isClasspathElementDefaultExlude( final String element )
    {
        if ( element == null )
        {
            throw new NullPointerException( "element" );
        }

        return element.matches( ".*jdtaus-core-api.*jar" );
    }

    /** Default source include patterns. */
    private static final String[] DEFAULT_SOURCE_INCLUDES = {
        "**/*.java",
        "**/*.xml",
        "**/*.xsd",
        "**/*.html"
    };

    //-----------------------------------------------------------Configuration--
    //--AbstractSourceMojo------------------------------------------------------

    /** Interface to manipulate source files. */
    public interface SourceEditor
    {

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
        String editLine( String line ) throws MojoFailureException;

        /**
         * Flag indicating that the editor changed a line.
         *
         * @return {@code true} if the editor changed a line of input;
         * {@code false} if not.
         */
        boolean isModified();

    }

    /**
     * Accessor to a {@code *.java} file for a given class name.
     *
     * @param roots list of source roots to search.
     * @param className The class name to return the corresponding
     * {@code *.java} file for (needs to be in the project's compile source
     * roots). No inner classes are supported yet.
     *
     * @return the source file for the class with name {@code className} or
     * {@code null} if no corresponding source file can be found.
     *
     * @throws NullPointerException if either {@code roots} or {@code className}
     * is {@code null}.
     */
    protected final File getSource( final List roots, final String className )
    {
        if ( className == null )
        {
            throw new NullPointerException( "className" );
        }
        if ( roots == null )
        {
            throw new NullPointerException( "roots" );
        }

        File file;
        String source;
        File ret = null;
        final Iterator it;
        final String fileName = className.replace( '.', File.separatorChar ).
            concat( ".java" );

        for ( it = roots.iterator(); it.hasNext();)
        {
            source = ( String ) it.next();
            file =
                new File( source.concat( File.separator ).concat( fileName ) );
            if ( file.canRead() && file.canWrite() )
            {
                ret = file;
                break;
            }
            else
            {
                ret = null;
            }
        }

        return ret;
    }

    /**
     * Gets all {@code *.java} files from the project's compile source roots.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * {@code *.java} files found in all compile source roots.
     */
    protected final Collection getAllSources()
    {
        File file;
        File parentRoot;
        String sourceRoot;
        DirectoryScanner scanner;
        final Iterator i;
        Iterator j;
        final Collection files = new LinkedList();

        for ( i = this.getMavenProject().getCompileSourceRoots().
                iterator(); i.hasNext();)
        {
            sourceRoot = ( String ) i.next();
            parentRoot = new File( sourceRoot );

            if ( !parentRoot.exists() || !parentRoot.isDirectory() )
            {
                continue;
            }

            scanner = new DirectoryScanner();
            scanner.setBasedir( sourceRoot );
            scanner.setIncludes( DEFAULT_SOURCE_INCLUDES );
            scanner.addDefaultExcludes();
            scanner.scan();

            for ( j = Arrays.asList( scanner.getIncludedFiles() ).
                    iterator(); j.hasNext();)
            {
                file = new File( parentRoot, ( String ) j.next() );
                files.add( file );
            }
        }

        return files;
    }

    /**
     * Gets all {@code *.java} files from the project's test compile source
     * roots.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * {@code *.java} files found in all test compile source roots.
     */
    protected final Collection getTestSources()
    {
        File file;
        File parentRoot;
        String sourceRoot;
        DirectoryScanner scanner;
        final Iterator i;
        Iterator j;
        final Collection files = new LinkedList();

        for ( i = this.getMavenProject().getTestCompileSourceRoots().
                iterator(); i.hasNext();)
        {
            sourceRoot = ( String ) i.next();
            parentRoot = new File( sourceRoot );

            if ( !parentRoot.exists() || !parentRoot.isDirectory() )
            {
                continue;
            }

            scanner = new DirectoryScanner();
            scanner.setBasedir( sourceRoot );
            scanner.setIncludes( DEFAULT_SOURCE_INCLUDES );
            scanner.addDefaultExcludes();
            scanner.scan();

            for ( j = Arrays.asList( scanner.getIncludedFiles() ).
                    iterator(); j.hasNext();)
            {
                file = new File( parentRoot, ( String ) j.next() );
                files.add( file );
            }
        }

        return files;
    }

    /**
     * Edits a string.
     *
     * @param str The string to edit.
     * @param editor The editor to use for editing {@code str}.
     *
     * @return {@code str} edited by {@code editor}.
     *
     * @throws NullPointerException if either {@code str} or {@code editor} is
     * {@code null}.
     * @throws MojoFailureException for unrecoverable errors.
     */
    protected final String edit(
        final String str, final AbstractSourceMojo.SourceEditor editor )
        throws MojoFailureException
    {
        if ( str == null )
        {
            throw new NullPointerException( "str" );
        }
        if ( editor == null )
        {
            throw new NullPointerException( "editor" );
        }

        int i;
        String line;
        String replacement;
        final char[] chars;
        final BufferedReader reader;
        final StringWriter writer = new StringWriter();

        try
        {
            reader = new BufferedReader( new StringReader( str ) );

            while ( ( line = reader.readLine() ) != null )
            {
                replacement = editor.editLine( line );
                if ( replacement != null )
                {
                    writer.write( replacement.concat( "\n" ) );
                }
            }

            replacement = editor.editLine( null );
            if ( replacement != null )
            {
                writer.write( replacement.concat( "\n" ) );
            }

            reader.close();
            writer.close();

            replacement = writer.toString();
            chars = replacement.toCharArray();

            // Remove trailing newlines.
            for ( i = chars.length - 1; i >= 0; i-- )
            {
                if ( chars[i] != '\n' && chars[i] != '\r' )
                {
                    break;
                }
            }

            replacement = replacement.substring( 0, i + 1 );
            return replacement + '\n';

        }
        catch ( IOException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    /**
     * Loads the contents of a file.
     *
     * @param file The file to load.
     *
     * @return the contents of {@code file}.
     *
     * @throws NullPointerException if {@code file} is {@code null}.
     * @throws MojoFailureException for unrecoverable errors.
     */
    protected final String load( final File file ) throws
        MojoFailureException
    {
        if ( file == null )
        {
            throw new NullPointerException( "file" );
        }

        String line;
        final BufferedReader reader;
        final StringWriter writer = new StringWriter();

        try
        {
            if ( this.encoding == null )
            {
                reader = new BufferedReader( new FileReader( file ) );
            }
            else
            {
                reader = new BufferedReader( new InputStreamReader(
                                             new FileInputStream( file ),
                                             this.encoding ) );

            }

            while ( ( line = reader.readLine() ) != null )
            {
                writer.write( line.concat( "\n" ) );
            }

            reader.close();
            writer.close();

            return writer.toString();
        }
        catch ( IOException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    /**
     * Saves a string to a file.
     *
     * @param file The file to save {@code str} to.
     * @param str The string to save to {@code file}.
     *
     * @throws NullPointerException if either {@code file} or {@code str} is
     * {@code null}.
     * @throws MojoFailureException for unrecoverable errors.
     */
    protected final void save( final File file, final String str ) throws
        MojoFailureException
    {
        if ( file == null )
        {
            throw new NullPointerException( "file" );
        }
        if ( str == null )
        {
            throw new NullPointerException( "str" );
        }

        final Writer fileWriter;

        try
        {
            if ( this.isTestMode() )
            {
                this.getLog().info( str );
            }
            else
            {
                if ( this.encoding == null )
                {
                    fileWriter = new FileWriter( file );
                }
                else
                {
                    fileWriter = new OutputStreamWriter(
                        new FileOutputStream( file ), this.encoding );

                }

                this.getLog().info( AbstractSourceMojoBundle.getInstance().
                                    getFileInfoMessage( this.getLocale() ).
                                    format( new Object[] {
                                            file.getName()
                                        } ) );

                fileWriter.write( str );
                fileWriter.close();
            }
        }
        catch ( IOException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    /**
     * Adds the configured number of spaces to a string buffer.
     *
     * @param stringBuffer The {@code StringBuffer} to add the number of spaces
     * to.
     *
     * @throws NullPointerException if {@code stringBuffer} is {@code null}.
     */
    protected final void indent( final StringBuffer stringBuffer )
    {
        if ( stringBuffer == null )
        {
            throw new NullPointerException( "stringBuffer" );
        }

        final char[] spaces =
            new char[ this.spacesPerIndentationLevel.intValue() ];

        Arrays.fill( spaces, ' ' );
        stringBuffer.append( spaces );
    }

    /**
     * Getter for the current context's class loader.
     *
     * @return the callee's context classloader.
     *
     * @throws MojoFailureException if no class loader is available.
     * @deprecated Use is discouraged.
     */
    protected ClassLoader getContextClassLoader() throws MojoFailureException
    {
        ClassLoader classLoader = Thread.currentThread().
            getContextClassLoader();

        if ( classLoader == null )
        {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        if ( classLoader == null )
        {
            throw new MojoFailureException( "classLoader" );
        }

        return classLoader;
    }

    /**
     * Provides access to the project's runtime classpath.
     *
     * @return a {@code ClassLoader} initialized with the project's runtime
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     * @deprecated Replaced with {@link #getRuntimeClasseLoader ( ClassLoader )}.
     */
    protected final ClassLoader getRuntimeClassLoader()
        throws MojoFailureException
    {
        return this.getRuntimeClassLoader( this.getContextClassLoader() );
    }

    /**
     * Provides access to the project's runtime classpath.
     *
     * @param parent the parent classloader to use for the runtime classloader.
     *
     * @return a {@code ClassLoader} initialized with the project's runtime
     * classpath.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getRuntimeClassLoader( final ClassLoader parent )
        throws MojoFailureException
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            for ( it = this.getClasspathElements().iterator(); it.hasNext();)
            {
                final String element = ( String ) it.next();
                final URL url = new File( element ).toURI().toURL();
                if ( !urls.contains( url ) &&
                    this.isClasspathElementIncluded( element ) )
                {
                    urls.add( url );
                }
            }

            return new URLClassLoader(
                ( URL[] ) urls.toArray( new URL[ urls.size() ] ), parent );

        }
        catch ( MalformedURLException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    /**
     * Provides access to the project's test classpath.
     *
     * @return a {@code ClassLoader} initialized with the project's test
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     * @deprecated Replaced with {@link #getTestClassLoader ( ClassLoader )}.
     */
    protected final ClassLoader getTestClassLoader()
        throws MojoFailureException
    {
        return this.getTestClassLoader( this.getContextClassLoader() );
    }

    /**
     * Provides access to the project's test classpath.
     *
     * @param parent the parent classloader to use for the test classloader.
     *
     * @return a {@code ClassLoader} initialized with the project's test
     * classpath.
     *
     * @throws NullPointerException if {@code parent} is {@code null}.
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getTestClassLoader( final ClassLoader parent )
        throws MojoFailureException
    {
        if ( parent == null )
        {
            throw new NullPointerException( "parent" );
        }

        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            for ( it = this.getMavenProject().
                    getTestClasspathElements().iterator(); it.hasNext();)
            {
                final String element = ( String ) it.next();
                final URL url = new File( element ).toURI().toURL();
                if ( !urls.contains( url ) &&
                    this.isTestClasspathElementIncluded( element ) )
                {
                    urls.add( url );
                }
            }

            return new URLClassLoader(
                ( URL[] ) urls.toArray( new URL[ urls.size() ] ), parent );

        }
        catch ( MalformedURLException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
        catch ( DependencyResolutionRequiredException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    //------------------------------------------------------AbstractSourceMojo--
}
