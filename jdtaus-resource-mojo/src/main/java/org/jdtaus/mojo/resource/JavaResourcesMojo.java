/*
 *  jDTAUS Core Resource Mojo
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
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
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
        catch ( Exception e )
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
        final Properties bundleHashcodes = new Properties();
        final File propertiesFile =
            new File( this.getBuildDirectory(), "bundles.properties" );

        if ( !propertiesFile.exists() )
        {
            propertiesFile.createNewFile();
        }

        final InputStream in = new FileInputStream( propertiesFile );
        bundleHashcodes.load( in );
        in.close();

        for ( Iterator it = module.getImplementations().getImplementation().
            iterator(); it.hasNext(); )
        {
            final Implementation impl = (Implementation) it.next();
            if ( impl.getMessages() == null )
            {
                continue;
            }

            final int bundleHash = this.getModelManager().
                getHashCode( module, impl );

            final String propertyHash =
                bundleHashcodes.getProperty( impl.getIdentifier() );

            if ( propertyHash == null ||
                Integer.valueOf( propertyHash ).intValue() != bundleHash )
            {
                bundleHashcodes.setProperty( impl.getIdentifier(),
                    Integer.toString( bundleHash ) );

                final String bundlePath =
                    ( this.getModelManager().getJavaPackageName( impl ) +
                    '.' + this.getModelManager().getJavaTypeName( impl ) ).
                    replace( '.', File.separatorChar );

                final File bundleFile = new File( this.getSourceDirectory(),
                    bundlePath + ".java" );

                this.assertDirectoryExistence( bundleFile.getParentFile() );

                final Writer writer =
                    this.getEncoding() == null
                    ? new FileWriter( bundleFile )
                    : new OutputStreamWriter( new FileOutputStream(
                    bundleFile ),
                    this.getEncoding() );

                this.getLog().info( this.getMessage( "writingBundle" ).
                    format( new Object[]
                    {
                        bundleFile.getName()
                    } ) );

                this.getBundleGenerator().generateJava( module, impl, writer );

                writer.close();

                final Map bundleProperties = this.getModelManager().
                    getBundleProperties( module, impl );

                for ( Iterator properties = bundleProperties.entrySet().
                    iterator(); properties.hasNext(); )
                {
                    final Map.Entry entry = (Map.Entry) properties.next();
                    final String language = (String) entry.getKey();
                    final Properties p = (Properties) entry.getValue();
                    final File file = new File( this.getResourceDirectory(),
                        bundlePath + "_" + language +
                        ".properties" );

                    this.getLog().info( this.getMessage( "writingBundle" ).
                        format( new Object[]
                        {
                            file.getName()
                        } ) );

                    this.assertDirectoryExistence( file.getParentFile() );

                    final OutputStream out = new FileOutputStream( file );
                    p.store( out, this.getProject().getName() );
                    out.close();

                    if ( this.getDefaultLanguage().
                        equalsIgnoreCase( language ) )
                    {
                        final File defaultFile =
                            new File( this.getResourceDirectory(),
                            bundlePath + ".properties" );

                        this.assertDirectoryExistence(
                            defaultFile.getParentFile() );

                        this.getLog().info( this.getMessage( "writingBundle" ).
                            format( new Object[]
                            {
                                defaultFile.getName()
                            } ) );

                        final OutputStream defaultOut =
                            new FileOutputStream( defaultFile );

                        p.store( defaultOut, this.getProject().getName() );
                        defaultOut.close();
                    }
                }
            }
        }

        final OutputStream out = new FileOutputStream( propertiesFile );
        bundleHashcodes.store( out, this.getClass().getName() + ": " +
            DateFormat.getDateTimeInstance().
            format( new Date() ) );

        out.close();
    }

    private void assertValidTemplates( final Module module )
        throws MojoExecutionException
    {
        if ( module.getImplementations() != null )
        {
            for ( Iterator it = module.getImplementations().getImplementation().
                iterator(); it.hasNext(); )
            {
                final Implementation impl = (Implementation) it.next();
                if ( impl.getMessages() == null )
                {
                    continue;
                }

                for ( Iterator m = impl.getMessages().getMessage().iterator();
                    m.hasNext(); )
                {
                    this.assertValidMessage( (Message) m.next() );
                }
            }
        }

        if ( module.getMessages() != null )
        {
            for ( Iterator it = module.getMessages().getMessage().iterator();
                it.hasNext(); )
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
            for ( Iterator it = message.getTemplate().getText().iterator();
                it.hasNext(); )
            {
                final Text text = (Text) it.next();
                try
                {
                    new MessageFormat( text.getValue() );
                }
                catch ( IllegalArgumentException e )
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
