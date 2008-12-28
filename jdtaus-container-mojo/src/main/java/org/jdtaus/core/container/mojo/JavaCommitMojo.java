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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;
import javax.xml.bind.JAXBException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Unknown;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Specification;

/**
 * Mojo to commit container meta-data to compiled java classes.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @goal java-commit
 * @phase process-classes
 * @requiresDependencyResolution test
 */
public class JavaCommitMojo extends JavaContainerMojo
{

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader = Thread.currentThread().
            getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(
                this.getRuntimeClassLoader( mavenLoader ) );

            enableThreadContextClassLoader();

            Module module = this.getModule();
            final Model model = ModelFactory.newModel();

            if ( module != null )
            {
                for ( int i = module.getSpecifications().size() - 1; i >= 0;
                      i-- )
                {
                    final Specification spec = module.getSpecifications().
                        getSpecification( i );

                    this.commitSpecification(
                        spec, new File( this.getMavenProject().getBuild().
                        getOutputDirectory() ) );

                }

                for ( int i = module.getImplementations().size() - 1; i >= 0;
                      i-- )
                {
                    final Implementation impl = module.getImplementations().
                        getImplementation( i );

                    this.commitImplementation(
                        model, impl, new File( this.getMavenProject().
                        getBuild().getOutputDirectory() ) );

                }
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

    protected void commitImplementation( final Model model,
                                         final Implementation impl,
                                         final File outputDirectory )
        throws IOException, JAXBException
    {
        this.enhanceClass( outputDirectory, impl.getIdentifier(),
                           Module.class.getName(),
                           this.serializeImplementation( model, impl ) );

    }

    protected void commitSpecification( final Specification spec,
                                        final File outputDirectory )
        throws IOException, JAXBException
    {
        this.enhanceClass( outputDirectory, spec.getIdentifier(),
                           Specification.class.getName(),
                           this.serializeSpecification( spec ) );

    }

    private byte[] serializeImplementation( final Model model,
                                            final Implementation impl )
        throws IOException, JAXBException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream out = new GZIPOutputStream( baos );

        this.getModelManager().getContainerMarshaller().marshal(
            this.getModelManager().getResolvedImplementation( model, impl ),
            out );

        out.close();

        if ( this.getLog().isDebugEnabled() )
        {
            final StringWriter writer = new StringWriter();
            this.getModelManager().getContainerMarshaller().marshal(
                this.getModelManager().getResolvedImplementation(
                model, impl ), writer );

            this.getLog().debug( writer.toString() );
        }

        return baos.toByteArray();
    }

    private byte[] serializeSpecification( final Specification spec )
        throws IOException, JAXBException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream out = new GZIPOutputStream( baos );

        this.getModelManager().getContainerMarshaller().marshal(
            this.getModelManager().getSpecification( spec ), out );

        out.close();

        if ( this.getLog().isDebugEnabled() )
        {
            final StringWriter writer = new StringWriter();
            this.getModelManager().getContainerMarshaller().marshal(
                this.getModelManager().getSpecification( spec ), writer );

            this.getLog().debug( writer.toString() );
        }

        return baos.toByteArray();
    }

    private void enhanceClass( final File outputDirectory,
                               final String identifier,
                               final String attributeName,
                               final byte[] meta )
        throws IOException
    {
        /*
        The JavaTM Virtual Machine Specification - Second Edition - Chapter 4.1

        A Java virtual machine implementation is required to silently ignore any
        or all attributes in the attributes table of a ClassFile structure that
        it does not recognize. Attributes not defined in this specification are
        not allowed to affect the semantics of the class file, but only to
        provide additional descriptive information (§4.7.1).
         */

        final String classLocation =
            identifier.replace( '.', File.separatorChar ) + ".class";

        final File classFile = new File( outputDirectory, classLocation );
        final long fileLength = classFile.length();
        final ClassParser parser =
            new ClassParser( new FileInputStream( classFile ),
                             classFile.getName() );

        final JavaClass clazz = parser.parse();
        Attribute[] attributes = clazz.getAttributes();

        int attributeIndex = -1;
        int nameIndex = -1;

        for ( int i = attributes.length - 1; i >= 0; i-- )
        {
            final Constant constant = clazz.getConstantPool().
                getConstant( attributes[i].getNameIndex() );

            if ( constant instanceof ConstantUtf8 )
            {
                if ( attributeName.equals(
                    ( (ConstantUtf8) constant ).getBytes() ) )
                {
                    attributeIndex = i;
                    nameIndex = attributes[i].getNameIndex();
                }
            }
        }

        if ( nameIndex == -1 )
        {
            final Constant[] pool = clazz.getConstantPool().getConstantPool();
            final Constant[] tmp = new Constant[ pool.length + 1 ];
            System.arraycopy( pool, 0, tmp, 0, pool.length );
            tmp[pool.length] = new ConstantUtf8( attributeName );
            nameIndex = pool.length;
            clazz.setConstantPool( new ConstantPool( tmp ) );
        }

        final Unknown unknown = new Unknown( nameIndex, meta.length, meta,
                                             clazz.getConstantPool() );

        if ( attributeIndex == -1 )
        {
            final Attribute[] tmp = new Attribute[ attributes.length + 1 ];
            System.arraycopy( attributes, 0, tmp, 0, attributes.length );
            tmp[attributes.length] = unknown;
            attributeIndex = attributes.length;
            attributes = tmp;
        }
        else
        {
            attributes[attributeIndex] = unknown;
        }

        clazz.setAttributes( attributes );
        clazz.dump( classFile );

        final Number bytes = new Long( classFile.length() - fileLength );

        this.getLog().info( JavaContainerMojoBundle.getInstance().
            getCommittedFileMessage( Locale.getDefault(), identifier, bytes ) );

    }

}
