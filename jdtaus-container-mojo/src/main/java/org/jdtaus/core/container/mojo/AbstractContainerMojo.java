/*
 *  jDTAUS Core Container Mojo
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jdtaus.core.container.mojo.model.ModelManager;

/**
 * jDTAUS Container Mojo base implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public abstract class AbstractContainerMojo extends AbstractMojo
{
    //--Configuration-----------------------------------------------------------

    /**
     * Name of the system property controlling the use of the context
     * classloader.
     */
    private static final String SYS_ENABLE_CONTEXT_CLASSLOADER =
        "org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader";

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject mavenProject;

    /**
     * Number of spaces to use per indentation level.
     * @parameter expression="${spacesPerIndentationLevel}" default-value="4"
     * @optional
     */
    private Integer spacesPerIndentationLevel;

    /**
     * The encoding to use for reading and writing sources. By default the
     * system's default encoding will be used.
     * @parameter expression="${encoding}"
     *            default-value="${project.build.sourceEncoding}"
     * @optional
     */
    private String encoding;

    /**
     * Flag indicating test mode. In test mode generated sources will be
     * printed to the console and no files will be written.
     * @parameter expression="${testMode}" default-value="false"
     * @optional
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

    /** @component */
    private ModelManager modelManager;

    /**
     * Getter for property {@code encoding}.
     *
     * @return the encoding to use for reading and writing sources or
     * {@code null} to use the system's default encoding.
     */
    protected final String getEncoding()
    {
        return this.encoding;
    }

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
     * Accessor to the project's classpath elements including runtime
     * dependencies.
     *
     * @return a set of class path element strings.
     *
     * @throws DependencyResolutionRequiredException for any unresolved
     * dependency scopes.
     */
    protected final Set getClasspathElements()
        throws DependencyResolutionRequiredException
    {
        final Set elements = new HashSet();

        elements.add( this.getMavenProject().getBuild().getOutputDirectory() );

        int i = 0;
        for ( Iterator it = this.getMavenProject().getRuntimeArtifacts().
            iterator(); it.hasNext(); )
        {
            final Artifact a = (Artifact) it.next();

            if ( a.getFile() == null )
            {
                this.getLog().warn( a.toString() + " ignored." );
                continue;
            }

            final String element = a.getFile().getAbsolutePath();

            if ( this.getLog().isDebugEnabled() )
            {
                this.getLog().debug( "Runtime classpath element[" + i++ +
                                     "]: " + element );

            }

            elements.add( element );
        }

        i = 0;
        for ( Iterator it = this.getMavenProject().getCompileArtifacts().
            iterator(); it.hasNext(); )
        {
            final Artifact a = (Artifact) it.next();

            if ( a.getFile() == null )
            {
                this.getLog().warn( a.toString() + " ignored." );
                continue;
            }

            final String element = a.getFile().getAbsolutePath();

            if ( this.getLog().isDebugEnabled() )
            {
                this.getLog().debug( "Compile classpath element[" + i++ +
                                     "]: " + element );

            }

            elements.add( element );
        }

        return elements;
    }

    /**
     * Accessor to the test classpath elements.
     *
     * @return a set of class path element strings.
     *
     * @throws DependencyResolutionRequiredException for any unresolved
     * dependency scopes.
     */
    protected final Set getTestClasspathElements()
        throws DependencyResolutionRequiredException
    {
        final Set elements = new HashSet();

        elements.add( this.getMavenProject().getBuild().getOutputDirectory() );
        elements.add( this.getMavenProject().getBuild().
            getTestOutputDirectory() );

        int i = 0;
        for ( Iterator it = this.getMavenProject().getTestArtifacts().
            iterator(); it.hasNext(); )
        {
            final Artifact a = (Artifact) it.next();

            if ( a.getFile() == null )
            {
                this.getLog().warn( a.toString() + " ignored." );
                continue;
            }

            final String element = a.getFile().getAbsolutePath();

            if ( this.getLog().isDebugEnabled() )
            {
                this.getLog().debug( "Test classpath element[" + i++ +
                                     "]: " + element );

            }

            elements.add( element );
        }

        return elements;
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
        return false;
    }

    /** Default source include patterns. */
    protected static final String[] DEFAULT_SOURCE_INCLUDES =
    {
        "**/*.java",
        "**/*.xml",
        "**/*.xsd",
        "**/*.html",
        "**/*.vm",
        "**/*.apt"
    };

    /**
     * Gets the {@code ModelManager} instance.
     *
     * @return the {@code ModelManager} instance.
     */
    protected ModelManager getModelManager()
    {
        return this.modelManager;
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractContainerMojo---------------------------------------------------

    /** Name of the velocity classpath resource loader implementation. */
    private static final String VELOCITY_RESOURCE_LOADER =
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /** {@code VelocityEngine}. */
    private VelocityEngine velocityEngine;

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

        for ( it = roots.iterator(); it.hasNext(); )
        {
            source = (String) it.next();
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
     * Gets all source files from the project's compile source roots.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * source files found in all compile source roots.
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

        for ( i = this.getMavenProject().getCompileSourceRoots().iterator();
              i.hasNext(); )
        {
            sourceRoot = (String) i.next();
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

            for ( j = Arrays.asList( scanner.getIncludedFiles() ).iterator();
                  j.hasNext(); )
            {
                file = new File( parentRoot, (String) j.next() );
                files.add( file );
            }
        }

        return files;
    }

    /**
     * Gets all source files from the project's test compile source roots.
     *
     * @return a {@code Collection} holding {@code File} instances for all
     * source files found in all test compile source roots.
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

        for ( i = this.getMavenProject().getTestCompileSourceRoots().iterator();
              i.hasNext(); )
        {
            sourceRoot = (String) i.next();
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

            for ( j = Arrays.asList( scanner.getIncludedFiles() ).iterator();
                  j.hasNext(); )
            {
                file = new File( parentRoot, (String) j.next() );
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
        final String str, final AbstractContainerMojo.SourceEditor editor )
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
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
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
            if ( this.getEncoding() == null )
            {
                reader = new BufferedReader( new FileReader( file ) );
            }
            else
            {
                reader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( file ),
                    this.getEncoding() ) );

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
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
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
                if ( this.getEncoding() == null )
                {
                    fileWriter = new FileWriter( file );
                }
                else
                {
                    fileWriter = new OutputStreamWriter(
                        new FileOutputStream( file ), this.getEncoding() );

                }

                this.getLog().info( AbstractContainerMojoBundle.getInstance().
                    getFileInfoMessage( Locale.getDefault(), file.getName() ) );

                fileWriter.write( str );
                fileWriter.close();
            }
        }
        catch ( IOException e )
        {
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
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
     * Provides access to the project's runtime classpath.
     *
     * @param parent the parent classloader to use for the runtime classloader.
     *
     * @return a {@code ClassLoader} initialized with the project's runtime
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getRuntimeClassLoader(
        final ClassLoader parent ) throws MojoFailureException
    {
        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            int i = 0;
            for ( it = this.getClasspathElements().iterator(); it.hasNext(); )
            {
                final String element = (String) it.next();
                final URL url = new File( element ).toURI().toURL();
                if ( !urls.contains( url ) &&
                     this.isClasspathElementIncluded( element ) )
                {
                    urls.add( url );

                    if ( this.getLog().isDebugEnabled() )
                    {
                        this.getLog().debug( "runtime[" + i++ + "]=" +
                                             url.toExternalForm() );

                    }
                }
            }

            return new ResourceLoader(
                (URL[]) urls.toArray( new URL[ urls.size() ] ), parent );

        }
        catch ( DependencyResolutionRequiredException e )
        {
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
        }
        catch ( MalformedURLException e )
        {
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
        }
    }

    /**
     * Provides access to the project's test classpath.
     *
     * @param parent the parent classloader to use for the test classloader.
     *
     * @return a {@code ClassLoader} initialized with the project's test
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getTestClassLoader( final ClassLoader parent )
        throws MojoFailureException
    {
        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            int i = 0;
            for ( it = this.getTestClasspathElements().iterator();
                  it.hasNext(); )
            {
                final String element = (String) it.next();
                final URL url = new File( element ).toURI().toURL();
                if ( !urls.contains( url ) &&
                     this.isTestClasspathElementIncluded( element ) )
                {
                    urls.add( url );

                    if ( this.getLog().isDebugEnabled() )
                    {
                        this.getLog().debug( "test[" + i++ + "]=" +
                                             url.toExternalForm() );

                    }
                }
            }

            return new ResourceLoader(
                (URL[]) urls.toArray( new URL[ urls.size() ] ), parent );

        }
        catch ( MalformedURLException e )
        {
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
        }
        catch ( DependencyResolutionRequiredException e )
        {
            final MojoFailureException mfe =
                new MojoFailureException( e.getMessage() );

            mfe.initCause( e );
            throw mfe;
        }
    }

    /**
     * Gets the {@code VelocityEngine} used for generating source code.
     *
     * @return the {@code VelocityEngine} used for generating source code.
     *
     * @throws Exception if initializing a new velocity engine fails.
     */
    protected final VelocityEngine getVelocity() throws Exception
    {
        if ( this.velocityEngine == null )
        {
            final VelocityEngine engine = new VelocityEngine();
            final java.util.Properties props = new java.util.Properties();
            props.put( "resource.loader", "class" );
            props.put( "class.resource.loader.class",
                       VELOCITY_RESOURCE_LOADER );

            engine.init( props );
            this.velocityEngine = engine;
        }

        return this.velocityEngine;
    }

    /**
     * Formats a given text to a javadoc comment.
     *
     * @param text The text to format.
     *
     * @return {@code text} as a javadoc comment.
     *
     * @throws NullPointerException if {@code text} is {@code null}.
     */
    protected String formatComment( final String text )
    {
        if ( text == null )
        {
            throw new NullPointerException( "text" );
        }

        String normalized = text.replaceAll( "\\/\\*\\*", "/*" );
        normalized = normalized.replaceAll( "\\*/", "/" );

        final StringBuffer commentLinebreak = new StringBuffer();
        commentLinebreak.append( '\n' );
        this.indent( commentLinebreak );
        commentLinebreak.append( " *" );

        normalized =
        normalized.replaceAll( "\n", commentLinebreak.toString() );

        return normalized;
    }

    /**
     * Enables the use of the thread context classloader by setting system
     * property {@code org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader}
     * to {@code true}.
     */
    protected static void enableThreadContextClassLoader()
    {
        System.setProperty( SYS_ENABLE_CONTEXT_CLASSLOADER,
                            Boolean.toString( true ) );

    }

    /**
     * Disables the use of the thread context classloader by setting system
     * property {@code org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader}
     * to {@code true}.
     */
    protected static void disableThreadContextClassLoader()
    {
        System.setProperty( SYS_ENABLE_CONTEXT_CLASSLOADER,
                            Boolean.toString( false ) );

    }

    //---------------------------------------------------AbstractContainerMojo--
}
