/*
 *  jDTAUS Core Resource Mojo
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
package org.jdtaus.mojo.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jdtaus.mojo.resource.model.Implementation;
import org.jdtaus.mojo.resource.model.Message;
import org.jdtaus.mojo.resource.model.ModelManager;
import org.jdtaus.mojo.resource.model.Module;
import org.jdtaus.mojo.resource.model.Text;
import org.jdtaus.mojo.resource.util.BundleGenerator;

/**
 * Mojo to generate java resource accessor classes backed by java
 * <code>ResourceBundle</code>s from a project's module descriptor.
 *
 * @goal java-resources
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class JavaResourcesMojo extends AbstractMojo
{
    //--JavaResourcesMojo-------------------------------------------------------

    /**
     * Currently executed <code>MavenProject</code>.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * The directory to generate sources to.
     *
     * @parameter expression="${project.build.directory}/generated-sources/java-resources"
     */
    private File sourceDirectory;

    /**
     * The directory to generate resources to.
     *
     * @parameter expression="${project.build.directory}/generated-resources/java-resources"
     */
    private File resourceDirectory;

    /**
     * The directory to use for storing hashes for already generated files.
     *
     * @parameter expression="${project.build.directory}/java-resources"
     */
    private File buildDirectory;

    /**
     * Project module descriptor to control the mojo.
     * @parameter expression="${javaResources.moduleDescriptor}"
     *            default-value="src/main/resources/META-INF/jdtaus/module.xml"
     */
    private File moduleDescriptor;

    /**
     * The encoding to use for writing sources.
     * @parameter expression="${project.build.sourceEncoding}"
     */
    private String encoding;

    /**
     * The default language for generated bundles.
     * @parameter expression="${javaResources.defaultLanguage}"
     *            default-value="en"
     */
    private String defaultLanguage;

    /** @component */
    private BundleGenerator generator;

    /** @component */
    private ModelManager modelManager;

    /** Creates a new {@code JavaResourcesMojo} instance. */
    public JavaResourcesMojo()
    {
        super();
    }

    private MavenProject getProject()
    {
        return this.project;
    }

    private File getSourceDirectory()
    {
        return this.sourceDirectory;
    }

    private File getResourceDirectory()
    {
        return this.resourceDirectory;
    }

    private File getBuildDirectory()
    {
        return this.buildDirectory;
    }

    private BundleGenerator getBundleGenerator()
    {
        return this.generator;
    }

    private ModelManager getModelManager()
    {
        return this.modelManager;
    }

    private File getModuleDescriptor()
    {
        return this.moduleDescriptor;
    }

    private String getEncoding()
    {
        return this.encoding;
    }

    private String getDefaultLanguage()
    {
        return this.defaultLanguage;
    }

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if ( !this.getModuleDescriptor().exists() )
        {
            throw new MojoExecutionException(
                this.getMessage( "fileNotFound" ).
                format( new Object[]
                {
                    this.getModuleDescriptor().getAbsolutePath()
                } ) );

        }

        try
        {
            this.assertDirectoryExistence( this.getSourceDirectory() );
            this.assertDirectoryExistence( this.getResourceDirectory() );
            this.assertDirectoryExistence( this.getBuildDirectory() );

            this.getProject().addCompileSourceRoot(
                this.getSourceDirectory().getAbsolutePath() );

            final Resource resource = new Resource();
            resource.setDirectory( this.getResourceDirectory().
                getAbsolutePath() );

            resource.setFiltering( false );

            this.getProject().addResource( resource );

            final Module module = this.getModelManager().
                getModule( this.getModuleDescriptor() );

            if ( module != null )
            {
                this.assertValidTemplates( module );
                if ( module.getImplementations() != null )
                {
                    this.generateBundles( module );
                }
            }
        }
        catch ( final Exception e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    private MessageFormat getMessage( final String key )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return new MessageFormat(
            ResourceBundle.getBundle( JavaResourcesMojo.class.getName() ).
            getString( key ) );

    }

    private void assertDirectoryExistence( final File directory )
        throws MojoExecutionException
    {
        if ( !directory.exists() && !directory.mkdirs() )
        {
            throw new MojoExecutionException(
                this.getMessage( "cannotCreateDirectory" ).
                format( new Object[]
                {
                    directory.getAbsolutePath()
                } ) );


        }
    }

    private void generateBundles( final Module module )
        throws Exception
    {
        InputStream in = null;
        OutputStream out = null;
        Writer writer = null;

        try
        {
            final Properties bundleHashcodes = new Properties();
            final File propertiesFile =
                new File( this.getBuildDirectory(), "bundles.properties" );

            if ( !propertiesFile.exists() && !propertiesFile.createNewFile() )
            {
                final MessageFormat fmt =
                    this.getMessage( "cannotCreateFile" );

                throw new MojoExecutionException( fmt.format( new Object[]
                    {
                        propertiesFile.getAbsolutePath()
                    } ) );

            }

            in = new FileInputStream( propertiesFile );
            bundleHashcodes.load( in );
            in.close();
            in = null;

            for ( final Iterator it = module.getImplementations().
                getImplementation().iterator(); it.hasNext(); )
            {
                final Implementation impl = (Implementation) it.next();
                if ( impl.getMessages() == null )
                {
                    continue;
                }

                final int bundleHash =
                    this.getModelManager().getHashCode( module, impl );

                final String propertyHash =
                    bundleHashcodes.getProperty( impl.getIdentifier() );

                if ( propertyHash == null || Integer.valueOf( propertyHash ).
                    intValue() != bundleHash )
                {
                    bundleHashcodes.setProperty(
                        impl.getIdentifier(), Integer.toString( bundleHash ) );

                    final String bundlePath =
                        ( this.getModelManager().getJavaPackageName( impl )
                          + '.' + this.getModelManager().getJavaTypeName( impl ) ).
                        replace( '.', File.separatorChar );

                    final File bundleFile = new File( this.getSourceDirectory(),
                                                      bundlePath + ".java" );

                    this.assertDirectoryExistence( bundleFile.getParentFile() );

                    writer =
                        this.getEncoding() == null
                        ? new FileWriter( bundleFile )
                        : new OutputStreamWriter( new FileOutputStream(
                        bundleFile ), this.getEncoding() );

                    this.getLog().info( this.getMessage( "writingBundle" ).
                        format( new Object[]
                        {
                            bundleFile.getName()
                        } ) );

                    this.getBundleGenerator().
                        generateJava( module, impl, writer );

                    writer.close();
                    writer = null;

                    final Map bundleProperties =
                        this.getModelManager().
                        getBundleProperties( module, impl );

                    for ( final Iterator it2 = bundleProperties.entrySet().
                        iterator(); it2.hasNext(); )
                    {
                        final Map.Entry entry = (Map.Entry) it2.next();
                        final String language = (String) entry.getKey();
                        final Properties p = (Properties) entry.getValue();
                        final File file = new File( this.getResourceDirectory(),
                                                    bundlePath + "_" + language
                                                    + ".properties" );

                        this.getLog().info( this.getMessage( "writingBundle" ).
                            format( new Object[]
                            {
                                file.getName()
                            } ) );

                        this.assertDirectoryExistence( file.getParentFile() );

                        out = new FileOutputStream( file );
                        p.store( out, this.getProject().getName() );
                        out.close();
                        out = null;

                        if ( this.getDefaultLanguage().
                            equalsIgnoreCase( language ) )
                        {
                            final File defaultFile =
                                new File( this.getResourceDirectory(),
                                          bundlePath + ".properties" );

                            this.assertDirectoryExistence(
                                defaultFile.getParentFile() );

                            this.getLog().info( this.
                                getMessage( "writingBundle" ).
                                format( new Object[]
                                {
                                    defaultFile.getName()
                                } ) );

                            out = new FileOutputStream( defaultFile );
                            p.store( out, this.getProject().getName() );
                            out.close();
                            out = null;
                        }
                    }
                }
            }

            out = new FileOutputStream( propertiesFile );
            bundleHashcodes.store( out, this.getClass().getName() + ": "
                                        + DateFormat.getDateTimeInstance().
                format( new Date() ) );

            out.close();
            out = null;
        }
        finally
        {
            try
            {
                if ( in != null )
                {
                    in.close();
                }
            }
            finally
            {
                try
                {
                    if ( out != null )
                    {
                        out.close();
                    }
                }
                finally
                {
                    if ( writer != null )
                    {
                        writer.close();
                    }
                }
            }
        }
    }

    private void assertValidTemplates( final Module module )
        throws MojoExecutionException
    {
        if ( module.getImplementations() != null )
        {
            for ( final Iterator it = module.getImplementations().
                getImplementation().iterator(); it.hasNext(); )
            {
                final Implementation impl = (Implementation) it.next();
                if ( impl.getMessages() == null )
                {
                    continue;
                }

                for ( final Iterator m = impl.getMessages().getMessage().
                    iterator(); m.hasNext(); )
                {
                    this.assertValidMessage( (Message) m.next() );
                }
            }
        }

        if ( module.getMessages() != null )
        {
            for ( final Iterator it = module.getMessages().getMessage().
                iterator(); it.hasNext(); )
            {
                this.assertValidMessage( (Message) it.next() );
            }
        }
    }

    private void assertValidMessage( final Message message )
        throws MojoExecutionException
    {
        if ( message.getTemplate() != null )
        {
            for ( final Iterator it = message.getTemplate().getText().
                iterator(); it.hasNext(); )
            {
                final Text text = (Text) it.next();
                try
                {
                    new MessageFormat( text.getValue() );
                }
                catch ( final IllegalArgumentException e )
                {
                    final MessageFormat fmt =
                        this.getMessage( "illegalTemplate" );

                    throw new MojoExecutionException( fmt.format( new Object[]
                        {
                            text.getValue(),
                            message.getName(),
                            e.getMessage()
                        } ), e );

                }
            }
        }
    }

    //-------------------------------------------------------JavaResourcesMojo--
}
