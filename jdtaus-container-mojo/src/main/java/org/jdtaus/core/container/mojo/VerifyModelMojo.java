/*
 *  jDTAUS Core Container Mojo
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Unknown;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.MissingDependencyException;
import org.jdtaus.core.container.MissingImplementationException;
import org.jdtaus.core.container.MissingMessageException;
import org.jdtaus.core.container.MissingPropertyException;
import org.jdtaus.core.container.MissingSpecificationException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Modules;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;
import org.jdtaus.core.container.mojo.model.container.DependencyElement;
import org.jdtaus.core.container.mojo.model.container.ImplementationElement;
import org.jdtaus.core.container.mojo.model.container.MessageElement;
import org.jdtaus.core.container.mojo.model.container.ModelObject;
import org.jdtaus.core.container.mojo.model.container.ModuleElement;
import org.jdtaus.core.container.mojo.model.container.Multiplicity;
import org.jdtaus.core.container.mojo.model.container.PropertyElement;
import org.jdtaus.core.container.mojo.model.container.SpecificationElement;
import org.jdtaus.core.container.mojo.model.container.SpecificationsElement;

/**
 * Mojo to validate set of modules.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 * @goal verify-model
 * @phase verify
 * @requiresDependencyResolution test
 */
public class VerifyModelMojo extends AbstractContainerMojo
{
    //--AbstractMojo------------------------------------------------------------

    /** Flag indicating model violations. */
    private boolean validModel;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Setup all runtime dependencies to be available for classloading.
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(
                this.getClassLoader( mavenLoader ) );

            enableThreadContextClassLoader();

            this.validModel = true;
            this.assertValidModel( ModelFactory.getModel() );

            if ( this.validModel )
            {
                this.getLog().info( VerifyModelMojoBundle.getInstance().
                    getValidModelMessage( Locale.getDefault() ) );

            }
            else
            {
                throw new MojoExecutionException(
                    VerifyModelMojoBundle.getInstance().
                    getModelViolationsMessage( Locale.getDefault() ) );

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

    //------------------------------------------------------------AbstractMojo--
    //--VerifyModelMojo---------------------------------------------------------

    protected ClassLoader getClassLoader( final ClassLoader parent )
        throws Exception
    {
        return this.getRuntimeClassLoader( parent );
    }

    private void assertValidModel( final Model model )
        throws IOException, ClassNotFoundException, JAXBException
    {
        final Specifications specs = model.getModules().getSpecifications();
        for ( int i = specs.size() - 1; i >= 0; i-- )
        {
            final Specification spec = specs.getSpecification( i );
            final Class specClass =
                this.assertClassAvailable( spec.getIdentifier() );

            if ( specClass != null )
            {
                this.assertCompatibleModel( spec, specClass );
            }
        }

        final Implementations impls = model.getModules().getImplementations();
        for ( int j = impls.size() - 1; j >= 0; j-- )
        {
            final Implementation impl = impls.getImplementation( j );
            final Class clazz =
                this.assertClassAvailable( impl.getIdentifier() );

            if ( clazz != null )
            {
                this.assertImplementsAllSpecifications(
                    impl, model.getModules(), clazz );

                this.assertCompatibleModel( model, impl, clazz );
            }
        }

        // Check for unresolved dependencies.
        final Dependencies unresolved = this.getUnresolvedDependencies( model );
        final Set renderedSpecifications = new HashSet();

        if ( unresolved.size() > 0 )
        {
            this.validModel = false;

            this.getLog().error(
                VerifyModelMojoBundle.getInstance().
                getUnresolvedDependenciesMessage( Locale.getDefault() ) );

            for ( int i = unresolved.size() - 1; i >= 0; i-- )
            {
                final Dependency dep = unresolved.getDependency( i );

                if ( !renderedSpecifications.contains(
                    dep.getSpecification().getIdentifier() ) )
                {
                    this.getLog().error(
                        "  " + VerifyModelMojoBundle.getInstance().
                        getUnresolvedDependencyMessage(
                        Locale.getDefault(),
                        dep.getSpecification().getIdentifier(),
                        dep.getSpecification().getModuleName(),
                        dep.getSpecification().getVersion() ) );

                    renderedSpecifications.add(
                        dep.getSpecification().getIdentifier() );

                }
            }
        }
    }

    private Class assertClassAvailable( final String className )
    {
        Class clazz = null;

        try
        {
            clazz = Class.forName( className, true, Thread.currentThread().
                getContextClassLoader() );

        }
        catch ( ClassNotFoundException e )
        {
            this.validModel = false;

            this.getLog().error(
                VerifyModelMojoBundle.getInstance().
                getClassNotFoundMessage( Locale.getDefault(), e.getMessage() ) );

            clazz = null;
        }

        return clazz;
    }

    private void assertImplementsAllSpecifications(
        final Implementation impl, final Modules modules, final Class clazz )
    {
        final Set allInterfaces = new HashSet();
        this.getAllImplementedInterfaces( clazz, allInterfaces );
        final Class[] interfaces = (Class[]) allInterfaces.toArray(
            new Class[ allInterfaces.size() ] );

        if ( impl.getImplementedSpecifications().size() > 0 )
        {
            for ( int i = interfaces.length - 1; i >= 0; i-- )
            {
                try
                {
                    modules.getSpecification( interfaces[i].getName() ).
                        getImplementation( impl.getName() );

                }
                catch ( MissingImplementationException e )
                {
                    this.validModel = false;

                    this.getLog().error(
                        VerifyModelMojoBundle.getInstance().
                        getMissingImplementedSpecificationMessage(
                        Locale.getDefault(), impl.getIdentifier(),
                        interfaces[i].getName() ) );

                }
                catch ( MissingSpecificationException e )
                {
                    // Classes are allowed to implement additional interfaces
                    // not defined in the model.
                }
            }
        }

        for ( int i = impl.getImplementedSpecifications().size() - 1; i >= 0;
              i-- )
        {
            final Specification spec = impl.getImplementedSpecifications().
                getSpecification( i );

            final Class specClass =
                this.assertClassAvailable( spec.getIdentifier() );

            boolean isImplemented =
                spec.getIdentifier().equals( impl.getIdentifier() );

            for ( int j = interfaces.length - 1; j >= 0; j-- )
            {
                if ( interfaces[j].getName().equals( spec.getIdentifier() ) )
                {
                    isImplemented = true;
                    break;
                }
            }

            if ( !isImplemented )
            {
                final String methodName =
                    spec.getIdentifier().replace( '.', '_' );

                try
                {
                    final Method m =
                        clazz.getMethod( methodName, new Class[ 0 ] );

                    if ( m.getReturnType() != specClass )
                    {
                        this.validModel = false;

                        this.getLog().error(
                            VerifyModelMojoBundle.getInstance().
                            getMissingInterfaceMessage(
                            Locale.getDefault(), impl.getIdentifier(),
                            spec.getIdentifier() ) );

                    }
                }
                catch ( NoSuchMethodException e )
                {
                    this.validModel = false;

                    this.getLog().error(
                        VerifyModelMojoBundle.getInstance().
                        getMissingInterfaceMessage(
                        Locale.getDefault(), impl.getIdentifier(),
                        spec.getIdentifier() ) );

                }
            }
        }
    }

    /**
     * Gets all interfaces implemented by a given class.
     *
     * @param clazz the class to get all implemented interfaces of.
     * @param interfaces set for collecting interfaces recursively.
     */
    private void getAllImplementedInterfaces( final Class clazz,
                                              final Set interfaces )
    {
        final Class[] current = clazz.getInterfaces();
        for ( int i = current.length - 1; i >= 0; i-- )
        {
            this.getAllImplementedInterfaces( current[i], interfaces );
            interfaces.add( current[i] );
        }

        if ( clazz.getSuperclass() != null )
        {
            this.getAllImplementedInterfaces(
                clazz.getSuperclass(), interfaces );

        }
    }

    /**
     * Gets all unresolved dependencies from a given model.
     *
     * @param model the model to gets unresolved dependencies from.
     *
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    private Dependencies getUnresolvedDependencies( final Model model )
    {
        String unresolvedName = "Unresolved-";
        final Collection deps = new LinkedList();

        for ( int m = model.getModules().size() - 1; m >= 0; m-- )
        {
            final Module mod = model.getModules().getModule( m );

            for ( int i = mod.getImplementations().size() - 1; i >= 0; i-- )
            {
                final Implementation impl = mod.getImplementations().
                    getImplementation( i );

                for ( int d = impl.getDependencies().size() - 1; d >= 0; d-- )
                {
                    final Dependency dep = (Dependency) impl.getDependencies().
                        getDependency( d ).clone();

                    if ( dep.getImplementation() == null &&
                         dep.getSpecification().getMultiplicity() ==
                         Specification.MULTIPLICITY_ONE &&
                         dep.getSpecification().getImplementations().
                        size() != 1 )
                    {
                        dep.setName(
                            unresolvedName + "-" + m + "-" + i + "-" + d );

                        deps.add( dep );
                    }
                }
            }
        }

        final Dependencies dependencies = new Dependencies();
        dependencies.setDependencies(
            (Dependency[]) deps.toArray( new Dependency[ deps.size() ] ) );

        return dependencies;
    }

    private void assertCompatibleModel( final Model model,
                                        final Implementation impl,
                                        final Class clazz )
        throws IOException, ClassNotFoundException, JAXBException
    {
        final ModuleElement committedModule =
            this.extractImplementation( clazz );

        if ( committedModule != null )
        {
            ImplementationElement committedImpl = null;
            if ( committedModule.getImplementations() != null )
            {
                for ( Iterator it = committedModule.getImplementations().
                    getImplementation().iterator(); it.hasNext(); )
                {
                    final ImplementationElement i =
                        (ImplementationElement) it.next();

                    if ( i.getIdentifier().equals( impl.getIdentifier() ) )
                    {
                        committedImpl = i;
                        break;
                    }
                }
            }

            if ( committedImpl != null )
            {
                if ( committedImpl.getDependencies() != null )
                {
                    for ( Iterator it = committedImpl.getDependencies().
                        getDependency().iterator(); it.hasNext(); )
                    {
                        final DependencyElement d =
                            (DependencyElement) it.next();

                        try
                        {
                            final Dependency specifiedDependency =
                                impl.getDependencies().getDependency(
                                d.getName() );

                            final Class committedSpec = Class.forName(
                                d.getIdentifier(), true, Thread.currentThread().
                                getContextClassLoader() );

                            final Class specifiedSpec = Class.forName(
                                specifiedDependency.getSpecification().
                                getIdentifier(), true, Thread.currentThread().
                                getContextClassLoader() );

                            if ( !specifiedSpec.isAssignableFrom( committedSpec ) )
                            {
                                this.getLog().error(
                                    VerifyModelMojoBundle.getInstance().
                                    getIllegalSpecificationMessage(
                                    Locale.getDefault(), d.getName(),
                                    impl.getIdentifier(),
                                    specifiedDependency.getSpecification().
                                    getIdentifier(), d.getIdentifier() ) );

                                this.validModel = false;
                            }
                        }
                        catch ( MissingDependencyException e )
                        {
                            this.getLog().error(
                                VerifyModelMojoBundle.getInstance().
                                getMissingCommittedDependencyMessage(
                                Locale.getDefault(), d.getName(),
                                impl.getIdentifier() ) );

                            this.validModel = false;
                        }
                    }
                }

                if ( committedImpl.getProperties() != null )
                {
                    for ( Iterator it = committedImpl.getProperties().
                        getProperty().iterator(); it.hasNext(); )
                    {
                        final PropertyElement p = (PropertyElement) it.next();
                        try
                        {
                            final Class committedType =
                                this.getModelManager().getJavaType( p );

                            final Property specifiedProperty =
                                impl.getProperties().getProperty( p.getName() );

                            if ( !specifiedProperty.getType().
                                isAssignableFrom( committedType ) )
                            {
                                this.getLog().error(
                                    VerifyModelMojoBundle.getInstance().
                                    getIllegalPropertyMessage(
                                    Locale.getDefault(), p.getName(),
                                    impl.getIdentifier(),
                                    specifiedProperty.getType().getName(),
                                    committedType.getName() ) );

                                this.validModel = false;
                            }
                        }
                        catch ( MissingPropertyException e )
                        {
                            this.getLog().error(
                                VerifyModelMojoBundle.getInstance().
                                getMissingCommittedPropertyMessage(
                                Locale.getDefault(), p.getName(),
                                impl.getIdentifier() ) );

                            this.validModel = false;
                        }
                    }
                }

                if ( committedImpl.getMessages() != null )
                {
                    for ( Iterator it = committedImpl.getMessages().getMessage().
                        iterator(); it.hasNext(); )
                    {
                        final MessageElement m = (MessageElement) it.next();
                        try
                        {
                            impl.getMessages().getMessage( m.getName() );
                        }
                        catch ( MissingMessageException e )
                        {
                            this.getLog().error(
                                VerifyModelMojoBundle.getInstance().
                                getMissingCommittedMessageMessage(
                                Locale.getDefault(), m.getName(),
                                impl.getIdentifier() ) );

                            this.validModel = false;
                        }
                    }
                }
            }

            if ( committedModule.getSpecifications() != null )
            {
                for ( Iterator it = committedModule.getSpecifications().
                    getSpecification().iterator(); it.hasNext(); )
                {
                    final SpecificationElement s = (SpecificationElement) it.next();

                    try
                    {
                        final Specification spec = model.getModules().
                            getSpecification( s.getIdentifier() );

                        final Multiplicity specifiedMultiplicity =
                            this.getModelManager().getMultiplicity( spec );

                        if ( !this.getModelManager().getMultiplicity( spec ).
                            equals( s.getMultiplicity() ) )
                        {
                            this.getLog().error( VerifyModelMojoBundle.getInstance().
                                getIllegalMultiplicityMessage(
                                Locale.getDefault(), s.getIdentifier(),
                                specifiedMultiplicity.getValue(),
                                s.getMultiplicity().getValue() ) );

                            this.validModel = false;
                        }
                    }
                    catch ( MissingSpecificationException e )
                    {
                        this.getLog().debug( e.getMessage() );
                    }
                }
            }
        }
        else
        {
            this.getLog().debug( VerifyModelMojoBundle.getInstance().
                getUncommittedMessage( Locale.getDefault(),
                                       impl.getIdentifier() ) );

        }
    }

    private void assertCompatibleModel( final Specification spec,
                                        final Class clazz )
        throws IOException, ClassNotFoundException, JAXBException
    {
        final SpecificationElement committedSpec =
            this.extractSpecification( clazz );

        if ( committedSpec != null )
        {
            final Multiplicity specifiedMultiplicity =
                this.getModelManager().getMultiplicity( spec );

            if ( !specifiedMultiplicity.equals(
                committedSpec.getMultiplicity() ) )
            {
                this.getLog().error( VerifyModelMojoBundle.getInstance().
                    getIllegalMultiplicityMessage(
                    Locale.getDefault(),
                    committedSpec.getIdentifier(),
                    specifiedMultiplicity.getValue(),
                    committedSpec.getMultiplicity().getValue() ) );

                this.validModel = false;
            }
        }
        else
        {
            this.getLog().debug( VerifyModelMojoBundle.getInstance().
                getUncommittedMessage( Locale.getDefault(),
                                       spec.getIdentifier() ) );

        }
    }

    private ModuleElement extractImplementation( final Class clazz )
        throws IOException, ClassNotFoundException, JAXBException
    {
        return (ModuleElement) this.extractModelObject(
            clazz, Module.class.getName() );

    }

    private SpecificationElement extractSpecification( final Class clazz )
        throws IOException, ClassNotFoundException, JAXBException
    {
        return (SpecificationElement) this.extractModelObject(
            clazz, Specification.class.getName() );

    }

    private SpecificationsElement extractSpecifications( final Class clazz )
        throws IOException, ClassNotFoundException, JAXBException
    {
        return (SpecificationsElement) this.extractModelObject(
            clazz, Specifications.class.getName() );

    }

    private ModelObject extractModelObject( final Class clazz,
                                            final String attributeName )
        throws IOException, ClassNotFoundException, JAXBException
    {
        byte[] data = null;
        ModelObject e = null;

        final String classLocation =
            clazz.getName().replace( '.', '/' ) + ".class";

        final InputStream classFile =
            Thread.currentThread().getContextClassLoader().
            getResourceAsStream( classLocation );

        final ClassParser parser =
            new ClassParser( classFile, classLocation );

        final JavaClass javaClass = parser.parse();
        final Attribute[] attributes = javaClass.getAttributes();

        for ( int i = attributes.length - 1; i >= 0; i-- )
        {
            final Constant constant = javaClass.getConstantPool().
                getConstant( attributes[i].getNameIndex() );

            if ( constant instanceof ConstantUtf8 )
            {
                if ( attributeName.equals(
                    ( (ConstantUtf8) constant ).getBytes() ) )
                {
                    if ( attributes[i] instanceof Unknown )
                    {
                        data = ( (Unknown) attributes[i] ).getBytes();
                        break;
                    }
                }
            }
        }

        if ( data != null )
        {
            e = (ModelObject) this.getModelManager().getContainerUnmarshaller().
                unmarshal( new GZIPInputStream( new ByteArrayInputStream(
                data ) ) );

            if ( this.getLog().isDebugEnabled() )
            {
                final StringWriter writer = new StringWriter();
                this.getModelManager().getContainerMarshaller().
                    marshal( e, writer );

                this.getLog().debug( writer.toString() );
            }
        }

        return e;
    }

    //---------------------------------------------------------VerifyModelMojo--
}
