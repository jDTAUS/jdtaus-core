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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jdtaus.mojo.resource.model.Artifact;
import org.jdtaus.mojo.resource.model.Bundle;

/**
 * Mojo to generate accessor classes for <code>ResourceBundle</code>s.
 *
 * @goal generate-bundles
 * @phase process-resources
 * @requiresDependencyResolution compile
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public final class BundleKeysMojo extends AbstractMojo
{
    //--Configuration-----------------------------------------------------------

    /**
     * The project's output directory.
     *
     * @parameter expression="${project.build.outputDirectory}"
     */
    private String outputDirectory;

    /**
     * The directory to output the generated sources to.
     *
     * @parameter expression="${project.build.directory}/generated-sources/bundle-keys"
     */
    private String genDirectory;

    /**
     * The directory to use for storing hashes for already generated files.
     *
     * @parameter expression="${project.build.directory}/bundle-hashcodes"
     */
    private String hashDirectory;

    /**
     * The suffix to append to the bundle name to form the interface name.
     *
     * @parameter expression="${nameSuffix}" default-value="Bundle"
     */
    private String nameSuffix;

    /**
     * Currently executed <code>MavenProject</code>.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * List of <code>ResourceBundle</code> names to generate interfaces for.
     *
     * @parameter expression="${bundles}"
     * @required
     */
    private String[] bundles;

    /**
     * The encoding to use for reading and writing sources. By default the
     * system's default encoding will be used.
     * @parameter expression="${encoding}"
     * @optional
     */
    private String encoding;

    /**
     * Project compile classpath.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * Flag indicating that javadoc comments should be generated.
     * @parameter expression="${javadoc}" default-value="true"
     * @optional
     */
    private Boolean javadoc;

    /**
     * Language to be used for javadoc comments. By default the system's
     * default locale will be used.
     * @parameter expression="${locale}"
     * @optional
     */
    private String locale;

    /**
     * Getter for property {@code locale}.
     *
     * @return the locale to use for javadoc comments.
     */
    protected Locale getLocale()
    {
        return this.locale == null
            ? Locale.getDefault()
            : new Locale( this.locale );

    }

    private MavenProject getProject()
    {
        return this.project;
    }

    private File getGenDirectory()
    {
        return new File( this.genDirectory );
    }

    private File getOutputDirectory()
    {
        return new File( this.outputDirectory );
    }

    private File getHashDirectory()
    {
        return new File( this.hashDirectory );
    }

    private String getNameSuffix()
    {
        return this.nameSuffix;
    }

    public boolean isJavadoc()
    {
        return this.javadoc.booleanValue();
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractMojo------------------------------------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(
                this.getClasspathClassLoader() );

            if ( !this.getGenDirectory().exists() &&
                !this.getGenDirectory().mkdirs() )
            {
                throw new MojoExecutionException(
                    this.getMessage( "cannotCreateDirectory" ).
                    format( new Object[] { this.getGenDirectory().
                                           getAbsolutePath()
                        } ) );

            }

            this.getProject().addCompileSourceRoot(
                this.getGenDirectory().getAbsolutePath() );

            for ( Iterator it = this.getBundles().entrySet().iterator();
                it.hasNext();)
            {
                final Map.Entry e = ( Map.Entry ) it.next();
                this.generatePackage( e.getKey().toString(),
                                      ( Map ) e.getValue() );

            }

        }
        finally
        {
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

    //------------------------------------------------------------AbstractMojo--
    //--BundleKeysMojo----------------------------------------------------------

    /** Location of the {@code Bundle.java.vm} template. */
    private static final String TEMPLATE_LOCATION =
        "META-INF/templates/Bundle.java.vm";

    /** Name of the classpath resource loader implementation. */
    private static final String VELOCITY_RESOURCE_LOADER =
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /** Velocity engine. */
    private VelocityEngine velocity;

    /**
     * Gets the {@code VelocityEngine} used for generating source code.
     *
     * @return the {@code VelocityEngine} used for generating source code.
     *
     * @throws MojoFailureException if creating a new {@code VelocityEngine}
     * instance fails.
     */
    private VelocityEngine getVelocity() throws MojoFailureException
    {
        try
        {
            if ( this.velocity == null )
            {
                final Properties props = new Properties();
                props.put( "resource.loader", "class" );
                props.put( "class.resource.loader.class",
                           VELOCITY_RESOURCE_LOADER );

                final VelocityEngine engine = new VelocityEngine();
                engine.init( props );

                this.velocity = engine;
            }

            return this.velocity;
        }
        catch ( Exception e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    /**
     * Accessor to the compile classpath elements.
     *
     * @return a list holding strings for each compile class path element.
     */
    private List getClasspathElements()
    {
        return this.classpathElements;
    }

    /**
     * Provides access to the project's compile classpath.
     *
     * @return a {@code ClassLoader} initialized with the project's compile
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    private ClassLoader getClasspathClassLoader() throws
        MojoFailureException
    {
        String element;
        File file;
        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            for ( it = this.getClasspathElements().iterator(); it.hasNext();)
            {
                element = ( String ) it.next();
                if ( !urls.contains( element ) )
                {
                    file = new File( element );
                    urls.add( file.toURI().toURL() );
                }
            }

            if ( !urls.contains( this.getOutputDirectory().toURI().toURL() ) )
            {
                urls.add( this.getOutputDirectory().toURI().toURL() );
            }

            return new URLClassLoader(
                ( URL[] ) urls.toArray( new URL[ urls.size() ] ),
                Thread.currentThread().getContextClassLoader() );

        }
        catch ( MalformedURLException e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    private Map getBundles()
    {
        int i;
        final Map ret = new HashMap();
        Map map;
        String bundle;
        String bundleName;
        String bundlePackage;
        String bundleLocation;

        if ( this.bundles != null )
        {
            for ( i = this.bundles.length - 1; i >= 0; i-- )
            {
                bundle = this.bundles[i];
                bundleName = BundleKeysMojo.getNameForBundle( bundle );
                bundlePackage = BundleKeysMojo.getPackageForBundle( bundle );
                bundleLocation = bundle.replaceAll( "\\.", "/" );

                map = ( Map ) ret.get( bundlePackage );
                if ( map == null )
                {
                    map = new HashMap();
                    ret.put( bundlePackage, map );
                }

                try
                {
                    final ResourceBundle resourceBundle =
                        ResourceBundle.getBundle( bundleLocation,
                                                  this.getLocale(),
                                                  Thread.currentThread().
                                                  getContextClassLoader() );

                    map.put( bundleName, resourceBundle );
                }
                catch ( MissingResourceException e )
                {
                    this.getLog().warn( e.getMessage() );
                }
            }
        }

        return ret;
    }

    private static String getPackageForBundle( final String bundleName )
    {
        if ( bundleName == null )
        {
            throw new NullPointerException( "bundleName" );
        }

        return bundleName.substring( 0, bundleName.lastIndexOf( '.' ) );
    }

    private static String getNameForBundle( final String bundleName )
    {
        if ( bundleName == null )
        {
            throw new NullPointerException( "bundleName" );
        }

        return bundleName.substring( bundleName.lastIndexOf( '.' ) + 1 );
    }

    private MessageFormat getMessage( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return new MessageFormat(
            ResourceBundle.getBundle( BundleKeysMojo.class.getName() ).
            getString( key ) );

    }

    private void generatePackage( final String pkg, final Map bundles )
        throws
        MojoFailureException
    {
        if ( pkg == null )
        {
            throw new NullPointerException( "pkg" );
        }
        if ( bundles == null )
        {
            throw new NullPointerException( "bundles" );
        }

        final String pkgPath = pkg.replaceAll( "\\.", "/" );
        final File hashDir = new File( this.getHashDirectory(), pkgPath );
        final File pkgDir = new File( this.getGenDirectory(), pkgPath );

        if ( !pkgDir.exists() )
        {
            pkgDir.mkdirs();
        }
        if ( !hashDir.exists() )
        {
            hashDir.mkdirs();
        }

        for ( Iterator it = bundles.keySet().iterator(); it.hasNext();)
        {
            final String bundleName = ( String ) it.next();
            final String bundlePath =
                bundleName + this.getNameSuffix() + ".java";

            final String hashPath =
                bundleName + this.getNameSuffix() + ".hash";

            final Artifact artifact = new Artifact();
            final Bundle bundle = new Bundle();

            artifact.setClassName( bundleName + this.getNameSuffix() );
            artifact.setPackageName( pkg );

            bundle.setPackageName( pkg );
            bundle.setClassName( bundleName );
            bundle.setResourceBundle(
                ( ResourceBundle ) bundles.get( bundleName ) );

            this.generateBundle( artifact, bundle,
                                 new File( pkgDir, bundlePath ),
                                 new File( hashDir, hashPath ) );

        }
    }

    private void generateBundle( final Artifact artifact,
                                  final Bundle bundle,
                                  final File outputFile,
                                  final File hashFile )
        throws MojoFailureException
    {
        if ( artifact == null )
        {
            throw new NullPointerException( "artifact" );
        }
        if ( outputFile == null )
        {
            throw new NullPointerException( "outputFile" );
        }
        if ( hashFile == null )
        {
            throw new NullPointerException( "hashFile" );
        }
        if ( bundle == null )
        {
            throw new NullPointerException( "bundle" );
        }

        try
        {
            if ( !this.checkHashFile( hashFile, bundle.getResourceBundle() ) )
            {
                this.getLog().info( this.getMessage( "writingBundle" ).
                                    format( new Object[] {
                                            outputFile.getName()
                                        } ) );

                final VelocityContext ctx = new VelocityContext();
                ctx.put( "artifact", artifact );
                ctx.put( "bundle", bundle );
                ctx.put( "builder", this );

                final FileOutputStream out =
                    new FileOutputStream( outputFile );

                final Writer writer = this.encoding == null
                    ? new OutputStreamWriter( out )
                    : new OutputStreamWriter( out, this.encoding );

                this.getVelocity().mergeTemplate( TEMPLATE_LOCATION,
                                                  ctx, writer );

                writer.close();
            }
        }
        catch ( Exception e )
        {
            throw new MojoFailureException( e.getMessage() );
        }
    }

    public String getMessageGetterNameForKey( String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        final char[] c = key.toCharArray();
        if ( Character.isLowerCase( c[0] ) )
        {
            c[0] = Character.toUpperCase( c[0] );
        }

        key = String.valueOf( c );

        return new StringBuffer( 255 ).append( "get" ).append( key ).
            append( "Message" ).toString();

    }

    public String getStringGetterNameForKey( String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        final char[] c = key.toCharArray();
        if ( Character.isLowerCase( c[0] ) )
        {
            c[0] = Character.toUpperCase( c[0] );
        }

        key = String.valueOf( c );

        return new StringBuffer( 255 ).append( "get" ).append( key ).
            append( "Text" ).toString();

    }

    private boolean checkHashFile( final File hashFile,
                                    final ResourceBundle bundle )
        throws IOException
    {
        boolean hashEqual = false;
        boolean writeHashFile = true;
        int bundleHash = 23;

        for ( Enumeration en = bundle.getKeys(); en.hasMoreElements();)
        {
            bundleHash = 37 * bundleHash + en.nextElement().hashCode();
        }

        if ( hashFile.exists() )
        {
            final DataInputStream in =
                new DataInputStream( new FileInputStream( hashFile ) );

            hashEqual = bundleHash == in.readInt();
            writeHashFile = !hashEqual;
            in.close();
        }

        if ( writeHashFile )
        {
            final DataOutputStream out =
                new DataOutputStream( new FileOutputStream( hashFile ) );

            out.writeInt( bundleHash );
            out.close();
        }

        return hashEqual;
    }

    public String normalizeMessage( final String message )
    {
        String normalized = message == null
            ? ""
            : message;

        normalized = normalized.replaceAll( "\\/\\*\\*", "/*" );
        normalized = normalized.replaceAll( "\\*/", "/" );

        return normalized;
    }

    //----------------------------------------------------------BundleKeysMojo--
}
