/*
 *  jDTAUS Core Container Mojo
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
package org.jdtaus.core.container.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.mojo.model.JavaArtifact;
import org.jdtaus.core.container.mojo.model.spring.BeansElement;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Mojo to generate a spring beans descriptor with corresponding support
 * classes from a project.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @goal spring-descriptor
 * @phase process-resources
 * @requiresDependencyResolution test
 */
public class SpringDescriptorMojo extends AbstractContainerMojo
{

    /** Location of the implementation template. */
    private static final String FACTORY_BEAN_TEMPLATE_LOCATION =
                                "META-INF/templates/FactoryBean.java.vm";

    /**
     * Name of the spring beans descriptor to generate.
     *
     * @parameter expression="${springDescriptor}"
     *            default-value="${project.build.directory}/classes/META-INF/jdtaus/spring-beans.xml"
     */
    private File springDescriptor;

    /**
     * Class name of the {@code FactoryBean} implementation to generate.
     * @parameter expression="${factoryBean}"
     * @required
     */
    private String factoryBean;

    /**
     * Source root to create the factory bean class in.
     * @parameter expression="${sourceRoot}"
     *            default-value="${project.build.directory}/generated-sources/container"
     */
    protected File sourceRoot;

    /**
     * Gets the spring descriptor file to write.
     *
     * @return the spring descriptor file to write.
     */
    protected File getSpringDescriptorFile()
    {
        return this.springDescriptor;
    }

    /**
     * Gets the source root to create new source files in.
     *
     * @return the source root to create new source files in.
     */
    protected File getSourceRoot()
    {
        return this.sourceRoot;
    }

    /**
     * Gets the class name of the {@code FactoryBean} implementation to
     * generate.
     *
     * @return the class name of the {@code FactoryBean} implementation
     * to generate.
     */
    protected String getFactoryBean()
    {
        return this.factoryBean;
    }

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader =
                          Thread.currentThread().getContextClassLoader();

        try
        {
            final ClassLoader runtimeLoader =
                              this.getRuntimeClassLoader( mavenLoader );

            Thread.currentThread().setContextClassLoader( runtimeLoader );
            enableThreadContextClassLoader();

            final BeansElement springModel = this.getModelManager().
                getSpringModel( this.getFactoryBean(),
                                ModelFactory.newModel().getModules() );

            if ( springModel.getImportOrAliasOrBean().size() > 0 )
            {
                if ( !this.getSpringDescriptorFile().getParentFile().exists() )
                {
                    this.getSpringDescriptorFile().getParentFile().mkdirs();
                }

                this.getModelManager().getSpringMarshaller().
                    marshal( springModel, new FileOutputStream(
                    this.getSpringDescriptorFile() ) );

                this.getLog().info(
                    SpringDescriptorMojoBundle.getInstance().
                    getGeneratedDescriptorMessage(
                    Locale.getDefault(), this.getSpringDescriptorFile().
                    getCanonicalPath() ) );

                final BeanFactory beanFactory = new XmlBeanFactory(
                    new FileSystemResource( this.getSpringDescriptorFile() ) );

                beanFactory.containsBean( "TEST" );

                final JavaArtifact artifact =
                                   new JavaArtifact( this.getFactoryBean() );

                final File source =
                           new File( this.getSourceRoot(),
                                     artifact.getPackagePath() +
                                     File.separator + artifact.getName() +
                                     ".java" );

                if ( !source.getParentFile().exists() )
                {
                    source.getParentFile().mkdirs();
                }

                final Writer writer;
                if ( this.getEncoding() == null )
                {
                    writer = new FileWriter( source );
                }
                else
                {
                    writer = new OutputStreamWriter(
                        new FileOutputStream( source ), this.getEncoding() );

                }

                final VelocityContext ctx = new VelocityContext();
                ctx.put( "artifact", artifact );
                ctx.put( "project", this.getMavenProject() );

                this.getVelocity().mergeTemplate(
                    FACTORY_BEAN_TEMPLATE_LOCATION, "UTF-8", ctx, writer );

                writer.close();

                this.getMavenProject().addCompileSourceRoot(
                    this.getSourceRoot().getAbsolutePath() );

                this.getLog().info(
                    SpringDescriptorMojoBundle.getInstance().
                    getGeneratedFactoryBeanMessage(
                    Locale.getDefault(), source.getCanonicalPath() ) );

            }
            else
            {
                this.getLog().info(
                    SpringDescriptorMojoBundle.getInstance().
                    getEmptyDescriptorMessage(
                    Locale.getDefault(),
                    this.springDescriptor.getCanonicalPath() ) );

            }
        }
        catch ( ContextError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( ContainerError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( ModelError e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        finally
        {
            disableThreadContextClassLoader();
            Thread.currentThread().setContextClassLoader( mavenLoader );
        }
    }

}
