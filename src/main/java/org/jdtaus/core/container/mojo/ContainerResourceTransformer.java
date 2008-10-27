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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.xml.bind.JAXBException;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.MissingModuleException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.mojo.model.ModelManager;
import org.jdtaus.core.container.mojo.model.container.Implementation;
import org.jdtaus.core.container.mojo.model.container.Message;
import org.jdtaus.core.container.mojo.model.container.MessageReference;
import org.jdtaus.core.container.mojo.model.container.Module;
import org.jdtaus.core.container.mojo.model.container.Modules;
import org.jdtaus.core.container.mojo.model.container.ObjectFactory;

/**
 * {@code ResourceTransformer} implementation for use with the
 * {@code maven-shade-plugin} for merging container models.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see <a href="http://maven.apache.org/plugins/maven-shade-plugin/index.html">maven-shade-plugin</a>
 * @plexus.component role="org.apache.maven.plugins.shade.resource.ResourceTransformer"
 *                   role-hint="jDTAUS Container Transformer"
 */
public class ContainerResourceTransformer implements ResourceTransformer
{

    /** Location to transform. */
    String location = "META-INF/jdtaus/module.xml";

    /**
     * Name of the module to merge all modules in or {@code null} to not merge
     * a single module.
     */
    String mergeModule;

    /** Processed models. */
    private Modules modules;

    /** @plexus.requirement */
    private ModelManager modelManager = new ModelManager();

    /**
     * Gets the {@code ModelManager} instance.
     *
     * @return the {@code ModelManager} instance.
     */
    protected ModelManager getModelManager()
    {
        return this.modelManager;
    }

    public boolean canTransformResource( final String resource )
    {
        return this.location.equals( resource );
    }

    public void processResource( final InputStream in ) throws IOException
    {
        try
        {
            if ( this.modules == null )
            {
                this.modules = new ObjectFactory().createModulesElement();
                this.modules.setModelVersion( "1.3" );
            }

            final Object o = this.getModelManager().
                getContainerUnmarshaller().unmarshal( in );

            if ( o instanceof Module )
            {
                this.modules.getModule().add( o );
            }
            else if ( o instanceof Modules )
            {
                this.modules.getModule().
                    addAll( ( (Modules) o ).getModule() );

            }
        }
        catch ( JAXBException e )
        {
            final IOException ioe = new IOException( e.getMessage() );
            ioe.initCause( e );
            throw ioe;
        }
    }

    public boolean hasTransformedResource()
    {
        return this.modules != null && this.modules.getModule().size() > 0;
    }

    public void modifyOutputStream( final JarOutputStream out )
        throws IOException
    {
        final ClassLoader currentLoader =
            Thread.currentThread().getContextClassLoader();

        try
        {
            final ResourceLoader resourceLoader =
                new ResourceLoader( this.getClass().getClassLoader() );

            for ( Iterator it = this.modules.getModule().iterator();
                it.hasNext();)
            {
                final Module module = (Module) it.next();
                final File tmpFile =
                    File.createTempFile( "jdtaus-container-mojo", "xml" );

                tmpFile.deleteOnExit();

                final OutputStream s = new FileOutputStream( tmpFile );

                this.getModelManager().getContainerMarshaller().
                    marshal( module, s );

                s.close();

                resourceLoader.addResource( this.location,
                                            tmpFile.toURI().toURL() );

            }

            Thread.currentThread().setContextClassLoader( resourceLoader );

            final Model model = ModelFactory.newModel();
            final Modules linkedModules = this.getModelManager().
                getContainerModel( model.getModules() );

            out.putNextEntry( new JarEntry( this.location ) );

            if ( this.mergeModule != null )
            {
                final Module mergedModule =
                    this.findModule( linkedModules, this.mergeModule );

                if ( mergedModule == null )
                {
                    throw new MissingModuleException( this.mergeModule );
                }

                for ( Iterator it = linkedModules.getModule().iterator();
                    it.hasNext();)
                {
                    final Module current = (Module) it.next();
                    if ( !current.getName().equals( this.mergeModule ) )
                    {
                        // Ignore modules not introduced as a resource.
                        if ( this.findModule( this.modules,
                                              current.getName() ) != null )
                        {
                            this.mergeModule( mergedModule, current );
                        }

                        it.remove();
                    }
                }

                this.getModelManager().getContainerMarshaller().
                    marshal( mergedModule, out );

            }
            else
            {
                this.getModelManager().getContainerMarshaller().
                    marshal( linkedModules, out );

            }

            this.modules = null;
        }
        catch ( Exception e )
        {
            final IOException ioe = new IOException( e.getMessage() );
            ioe.initCause( e );
            throw ioe;
        }
        catch ( ModelError e )
        {
            final IOException ioe = new IOException( e.getMessage() );
            ioe.initCause( e );
            throw ioe;
        }
        catch ( ContextError e )
        {
            final IOException ioe = new IOException( e.getMessage() );
            ioe.initCause( e );
            throw ioe;
        }
        catch ( ContainerError e )
        {
            final IOException ioe = new IOException( e.getMessage() );
            ioe.initCause( e );
            throw ioe;
        }
        finally
        {
            Thread.currentThread().setContextClassLoader( currentLoader );
        }
    }

    private Module findModule( final Modules modules, final String name )
    {
        Module module = null;

        for ( Iterator it = modules.getModule().iterator(); it.hasNext();)
        {
            final Module current = (Module) it.next();
            if ( current.getName().equals( name ) )
            {
                module = current;
                break;
            }
        }

        return module;
    }

    private void mergeModule( final Module mergedModule, final Module module )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();

        if ( module.getSpecifications() != null )
        {
            if ( mergedModule.getSpecifications() == null )
            {
                mergedModule.setSpecifications( f.createSpecifications() );
            }

            mergedModule.getSpecifications().getSpecification().
                addAll( module.getSpecifications().getSpecification() );

        }

        if ( module.getProperties() != null )
        {
            if ( mergedModule.getProperties() == null )
            {
                mergedModule.setProperties( f.createProperties() );
            }

            mergedModule.getProperties().getProperty().
                addAll( module.getProperties().getProperty() );

        }

        if ( module.getImplementations() != null )
        {
            if ( mergedModule.getImplementations() == null )
            {
                mergedModule.setImplementations( f.createImplementations() );
            }

            for ( Iterator it = module.getImplementations().getImplementation().
                iterator(); it.hasNext();)
            {
                final Implementation impl = (Implementation) it.next();
                if ( impl.getMessages() != null )
                {
                    for ( Iterator m = impl.getMessages().getReference().
                        iterator(); m.hasNext();)
                    {
                        final MessageReference message =
                            (MessageReference) m.next();

                        message.setName( message.getName() + '@' +
                                         module.getName() );

                    }
                }
            }

            mergedModule.getImplementations().getImplementation().
                addAll( module.getImplementations().getImplementation() );

        }

        if ( module.getMessages() != null )
        {
            if ( mergedModule.getMessages() == null )
            {
                mergedModule.setMessages( f.createMessages() );
            }

            for ( Iterator it = module.getMessages().getMessage().iterator();
                it.hasNext();)
            {
                final Message current = (Message) it.next();
                current.setName( current.getName() + '@' + module.getName() );
            }

            mergedModule.getMessages().getMessage().
                addAll( module.getMessages().getMessage() );

        }
    }
}
