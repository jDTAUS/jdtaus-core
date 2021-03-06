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
package org.jdtaus.core.container.mojo.model;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.jdtaus.core.container.mojo.model.container.ArgumentElement;
import org.jdtaus.core.container.mojo.model.container.ArgumentType;
import org.jdtaus.core.container.mojo.model.container.ArgumentsElement;
import org.jdtaus.core.container.mojo.model.container.DependenciesElement;
import org.jdtaus.core.container.mojo.model.container.DependencyElement;
import org.jdtaus.core.container.mojo.model.container.ImplementationElement;
import org.jdtaus.core.container.mojo.model.container.ImplementationsElement;
import org.jdtaus.core.container.mojo.model.container.Message;
import org.jdtaus.core.container.mojo.model.container.MessageElement;
import org.jdtaus.core.container.mojo.model.container.MessageReference;
import org.jdtaus.core.container.mojo.model.container.MessagesElement;
import org.jdtaus.core.container.mojo.model.container.ModelObject;
import org.jdtaus.core.container.mojo.model.container.Module;
import org.jdtaus.core.container.mojo.model.container.ModuleElement;
import org.jdtaus.core.container.mojo.model.container.ModulesElement;
import org.jdtaus.core.container.mojo.model.container.Multiplicity;
import org.jdtaus.core.container.mojo.model.container.ObjectFactory;
import org.jdtaus.core.container.mojo.model.container.Properties;
import org.jdtaus.core.container.mojo.model.container.PropertiesElement;
import org.jdtaus.core.container.mojo.model.container.Property;
import org.jdtaus.core.container.mojo.model.container.PropertyElement;
import org.jdtaus.core.container.mojo.model.container.PropertyType;
import org.jdtaus.core.container.mojo.model.container.Scope;
import org.jdtaus.core.container.mojo.model.container.SpecificationElement;
import org.jdtaus.core.container.mojo.model.container.SpecificationReference;
import org.jdtaus.core.container.mojo.model.container.Specifications;
import org.jdtaus.core.container.mojo.model.container.SpecificationsElement;
import org.jdtaus.core.container.mojo.model.container.Text;
import org.jdtaus.core.container.mojo.model.container.Texts;
import org.jdtaus.core.container.mojo.model.spring.BeanElement;
import org.jdtaus.core.container.mojo.model.spring.BeansElement;

/**
 * Manages the {@code http://jdtaus.org/core/model/container} namespace.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $JDTAUS$
 * @plexus.component role="org.jdtaus.core.container.mojo.model.ModelManager"
 *                   role-hint="default"
 */
public class ModelManager extends AbstractLogEnabled
{

    /** Package names of the container model classes. */
    private static final String CONTAINER_MODEL_PACKAGES =
        "org.jdtaus.core.container.mojo.model.container";

    /** Package names of the spring model classes. */
    private static final String SPRING_MODEL_PACKAGES =
        "org.jdtaus.core.container.mojo.model.spring";

    private static final String JAXB_MODEL_PACKAGES =
        CONTAINER_MODEL_PACKAGES + ":" + SPRING_MODEL_PACKAGES;

    /** Container model schema locations. */
    private static final String[] CONTAINER_SCHEMA_LOCATIONS =
    {
        "http://jdtaus.org/core/model/container",
        "http://xml.jdtaus.org/1.0.x/jdtaus-core/jdtaus-core-schemas/jdtaus-container-1.1.xsd"
    };

    /** Spring model schema locations. */
    private static final String[] SPRING_SCHEMA_LOCATIONS =
    {
        "http://www.springframework.org/schema/beans",
        "http://www.springframework.org/schema/beans/spring-beans-2.5.xsd"
    };

    /**
     * Gets a {@code Marshaller} for marshalling the container model.
     *
     * @return a {@code Marshaller} for marshalling the container model.
     *
     * @throws JAXBException if creating a {@code Marshaller} fails.
     */
    public Marshaller getContainerMarshaller() throws JAXBException
    {
        final StringBuffer schemaLocation = new StringBuffer();
        for ( int i = 0; i < CONTAINER_SCHEMA_LOCATIONS.length; i += 2 )
        {
            schemaLocation.append( CONTAINER_SCHEMA_LOCATIONS[i] ).
                append( ' ' ).
                append( CONTAINER_SCHEMA_LOCATIONS[i + 1] ).append( ' ' );

        }

        final JAXBContext ctx =
            JAXBContext.newInstance( JAXB_MODEL_PACKAGES );

        final Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                Boolean.TRUE );

        marshaller.setProperty( Marshaller.JAXB_SCHEMA_LOCATION,
                                schemaLocation.toString().trim() );

        return marshaller;
    }

    /**
     * Gets a {@code Marshaller} for marshalling the spring container model.
     *
     * @return a {@code Marshaller} for marshalling the spring container model.
     *
     * @throws JAXBException if creating a {@code Marshaller} fails.
     */
    public Marshaller getSpringMarshaller() throws JAXBException
    {
        final StringBuffer schemaLocation = new StringBuffer();
        for ( int i = 0; i < SPRING_SCHEMA_LOCATIONS.length; i += 2 )
        {
            schemaLocation.append( SPRING_SCHEMA_LOCATIONS[i] ).append( ' ' ).
                append( SPRING_SCHEMA_LOCATIONS[i + 1] ).append( ' ' );

        }

        final JAXBContext ctx =
            JAXBContext.newInstance( JAXB_MODEL_PACKAGES );

        final Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                Boolean.TRUE );

        marshaller.setProperty( Marshaller.JAXB_SCHEMA_LOCATION,
                                schemaLocation.toString().trim() );

        return marshaller;
    }

    /**
     * Gets an {@code Unmarshaller} for unmarshalling the container model.
     *
     * @return a {@code Unmarshaller} for unmarshalling the container model.
     *
     * @throws JAXBException if creating an {@code Unmarshaller} fails.
     */
    public Unmarshaller getContainerUnmarshaller() throws JAXBException
    {
        final JAXBContext ctx =
            JAXBContext.newInstance( JAXB_MODEL_PACKAGES );

        return ctx.createUnmarshaller();
    }

    /**
     * Gets an {@code Unmarshaller} for unmarshalling the spring container
     * model.
     *
     * @return a {@code Unmarshaller} for unmarshalling the spring container
     * model.
     *
     * @throws JAXBException if creating an {@code Unmarshaller} fails.
     */
    public Unmarshaller getSpringUnmarshaller() throws JAXBException
    {
        final JAXBContext ctx =
            JAXBContext.newInstance( JAXB_MODEL_PACKAGES );

        return ctx.createUnmarshaller();
    }

    /**
     * Maps a container {@code Modules} instance to the JAXB model.
     *
     * @param cModules the container model to map.
     *
     * @return {@code model} mapped to the plugin's model.
     *
     * @throws NullPointerException if {@code cModules} is {@code null}.
     * @throws JAXBException if mapping fails.
     */
    public ModulesElement getContainerModel(
        final org.jdtaus.core.container.Modules cModules ) throws JAXBException
    {
        if ( cModules == null )
        {
            throw new NullPointerException( "cModules" );
        }

        final ObjectFactory f = new ObjectFactory();
        final ModulesElement modules = f.createModulesElement();

        for ( int i = cModules.size() - 1; i >= 0; i-- )
        {
            final Module module = f.createModuleElement();
            module.setName( cModules.getModule( i ).getName() );
            module.setVersion( cModules.getModule( i ).getVersion() );

            module.setMessages(
                this.map( cModules.getModule( i ).getMessages() ) );

            module.setImplementations(
                this.map( cModules.getModule( i ).getImplementations(),
                          module ) );

            module.setSpecifications(
                this.map( cModules.getModule( i ).getSpecifications() ) );

            module.setProperties(
                this.map( cModules.getModule( i ).getProperties() ) );

            this.map( module, cModules.getModule( i ) );
            modules.getModule().add( module );
        }

        this.map( modules, cModules );

        return modules.getModule().size() > 0 ? modules : null;
    }

    /**
     * Maps a container {@code Implementation} instance to the plugin's
     * model with any references resolved.
     *
     * @param model The model to use for resolving references.
     * @param implementation The container model to map.
     *
     * @return {@code implementation} mapped to the plugin's model with any
     * references resolved.
     *
     * @throws NullPointerException if {@code implementation} or {@code model}
     * is {@code null}.
     * @throws JAXBException if mapping fails.
     */
    public ModuleElement getResolvedImplementation(
        final org.jdtaus.core.container.Model model,
        final org.jdtaus.core.container.Implementation implementation )
        throws JAXBException
    {
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        final ObjectFactory f = new ObjectFactory();
        final ImplementationElement e = f.createImplementationElement();
        final ModuleElement m = f.createModuleElement();

        m.setImplementations( f.createImplementationsElement() );
        m.setSpecifications( f.createSpecificationsElement() );
        m.setName( implementation.getIdentifier() );
        m.setVersion( implementation.getVersion() );
        this.map( m, implementation );

        for ( int d = implementation.getDependencies().size() - 1; d >= 0; d-- )
        {
            if ( e.getDependencies() == null )
            {
                e.setDependencies( f.createDependenciesElement() );
            }

            final org.jdtaus.core.container.Dependency dep =
                implementation.getDependencies().getDependency( d );

            e.getDependencies().getDependency().
                add( this.getDependency( dep ) );

            if ( this.getSpecification(
                m.getSpecifications(), dep.getSpecification().
                getIdentifier() ) == null )
            {
                m.getSpecifications().getSpecification().add(
                    this.getSpecification( model.getModules().getSpecification(
                    dep.getSpecification().getIdentifier() ) ) );

            }
        }

        e.setFinal( implementation.isFinal() );
        e.setIdentifier( implementation.getIdentifier() );
        e.setMessages( this.map( implementation.getMessages() ) );
        e.setName( implementation.getName() );
        e.setProperties( this.map( implementation.getProperties() ) );
        e.setSpecifications( f.createSpecificationsElement() );

        for ( int i = implementation.getImplementedSpecifications().size() - 1;
              i >= 0; i-- )
        {
            final org.jdtaus.core.container.Specification s =
                implementation.getImplementedSpecifications().
                getSpecification( i );

            final SpecificationReference ref = f.createSpecificationReference();
            ref.setIdentifier( s.getIdentifier() );
            ref.setVersion( s.getVersion() );
            this.map( ref, s );

            e.getSpecifications().getReference().add( ref );

            if ( this.getSpecification( m.getSpecifications(),
                                        s.getIdentifier() ) == null )
            {
                m.getSpecifications().getSpecification().
                    add( this.getSpecification( model.getModules().
                    getSpecification( s.getIdentifier() ) ) );

            }
        }

        e.setVendor( implementation.getVendor() );
        e.setVersion( implementation.getVersion() );

        if ( implementation.getParent() != null )
        {
            e.setParent( implementation.getParent().getIdentifier() );
        }

        this.map( e, implementation );
        m.getImplementations().getImplementation().add( e );

        return m;
    }

    /**
     * Maps a container {@code Specification} instance to the plugin's
     * model.
     *
     * @param specification The container model to map.
     *
     * @return {@code specificaction} mapped to the plugin's model.
     *
     * @throws NullPointerException if {@code specificaction} is {@code null}.
     * @throws JAXBException if mapping fails.
     */
    public SpecificationElement getSpecification(
        final org.jdtaus.core.container.Specification specification )
        throws JAXBException
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }

        final ObjectFactory f = new ObjectFactory();
        final SpecificationElement e = f.createSpecificationElement();

        e.setIdentifier( specification.getIdentifier() );
        e.setMultiplicity( this.getMultiplicity( specification ) );
        e.setProperties( this.map( specification.getProperties() ) );
        e.setScope( this.getScope( specification ) );
        e.setStateless( specification.isStateless() );
        e.setVendor( specification.getVendor() );
        e.setVersion( specification.getVersion() );

        this.map( e, specification );

        return e;
    }

    /**
     * Maps a container {@code Dependency} instance to the plugin's
     * model.
     *
     * @param dependency The container model to map.
     *
     * @return {@code dependency} mapped to the plugin's model.
     *
     * @throws NullPointerException if {@code dependency} is {@code null}.
     * @throws JAXBException if mapping fails.
     */
    public DependencyElement getDependency(
        final org.jdtaus.core.container.Dependency dependency )
        throws JAXBException
    {
        if ( dependency == null )
        {
            throw new NullPointerException( "dependency" );
        }

        final ObjectFactory f = new ObjectFactory();
        final DependencyElement dep = f.createDependencyElement();

        dep.setBound( dependency.isBound() );
        dep.setName( dependency.getName() );
        dep.setIdentifier( dependency.getSpecification().getIdentifier() );

        if ( dependency.getSpecification().getVersion() != null )
        {
            dep.setVersion( dependency.getSpecification().getVersion() );
        }

        if ( dependency.getImplementation() != null )
        {
            dep.setImplementationName( dependency.getImplementation().
                getName() );

        }

        final int propertyCount = dependency.getDeclaredProperties().size();

        if ( propertyCount > 0 )
        {
            final Properties properties = f.createProperties();
            dep.setProperties( properties );

            for ( int p = propertyCount - 1; p >= 0; p-- )
            {
                final org.jdtaus.core.container.Property cProperty =
                    dependency.getDeclaredProperties().getProperty( p );

                final Property property = f.createProperty();

                property.setName( cProperty.getName() );
                property.setType( PropertyType.fromValue(
                    cProperty.getType().getName() ) );

                property.setValue( cProperty.getValue() != null
                                   ? cProperty.getValue().toString()
                                   : null );

                this.map( property, cProperty );
                properties.getProperty().add( property );
            }

            this.map( properties, dependency.getDeclaredProperties() );
        }

        this.map( dep, dependency );
        return dep;
    }

    /**
     * Gets the scope of a specification.
     *
     * @param specification The specification to get the scope of.
     *
     * @return The scope of {@code specification}.
     *
     * @throws NullPointerException if {@code specification} is {@code null}.
     */
    public Scope getScope(
        final org.jdtaus.core.container.Specification specification )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }

        final Scope scope;
        switch ( specification.getScope() )
        {
            case org.jdtaus.core.container.Specification.SCOPE_CONTEXT:
                scope = Scope.CONTEXT;
                break;

            case org.jdtaus.core.container.Specification.SCOPE_MULTITON:
                scope = Scope.MULTITON;
                break;

            case org.jdtaus.core.container.Specification.SCOPE_SINGLETON:
                scope = Scope.SINGLETON;
                break;

            default:
                throw new AssertionError( Integer.toString(
                    specification.getScope() ) );

        }

        return scope;
    }

    /**
     * Gets the multiplicity of a specification.
     *
     * @param specification The specification to get the multiplicity of.
     *
     * @return The multiplicity of {@code specification}.
     *
     * @throws NullPointerException if {@code specification} is {@code null}.
     */
    public Multiplicity getMultiplicity(
        final org.jdtaus.core.container.Specification specification )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }

        final Multiplicity multiplicity;
        switch ( specification.getMultiplicity() )
        {
            case org.jdtaus.core.container.Specification.MULTIPLICITY_MANY:
                multiplicity = Multiplicity.MANY;
                break;

            case org.jdtaus.core.container.Specification.MULTIPLICITY_ONE:
                multiplicity = Multiplicity.ONE;
                break;

            default:
                throw new AssertionError( Integer.toString(
                    specification.getMultiplicity() ) );

        }

        return multiplicity;
    }

    /**
     * Maps a container {@code Modules} instance to the spring container model.
     *
     * @param factoryBeanClassName the name of the {@code FactoryBean}
     * implementation class.
     * @param cModules the container model to map.
     *
     * @return {@code model} mapped to the spring container model.
     *
     * @throws JAXBException if mapping fails.
     */
    public BeansElement getSpringModel(
        final String factoryBeanClassName,
        final org.jdtaus.core.container.Modules cModules )
        throws JAXBException
    {
        final org.jdtaus.core.container.mojo.model.spring.ObjectFactory f =
            new org.jdtaus.core.container.mojo.model.spring.ObjectFactory();

        final BeansElement beans = f.createBeansElement();
        for ( int s = cModules.getSpecifications().size() - 1; s >= 0; s-- )
        {
            final org.jdtaus.core.container.Specification spec =
                cModules.getSpecifications().getSpecification( s );

            for ( int i = spec.getImplementations().size() - 1; i >= 0; i-- )
            {
                final org.jdtaus.core.container.Implementation impl =
                    spec.getImplementations().getImplementation( i );

                final String id = spec.getIdentifier() + '.' +
                                  impl.getName().hashCode();

                final BeanElement bean = f.createBeanElement();
                bean.setClazz( factoryBeanClassName );
                bean.setId( id );
                bean.setScope( this.getSpringScope( spec ) );

                org.jdtaus.core.container.mojo.model.spring.PropertyElement property =
                    f.createPropertyElement();

                property.setName( "specificationIdentifier" );
                property.setShortcutValue( spec.getIdentifier() );
                bean.getMetaOrConstructorArgOrProperty().add( property );

                property = f.createPropertyElement();
                property.setName( "implementationName" );
                property.setShortcutValue( impl.getName() );
                bean.getMetaOrConstructorArgOrProperty().add( property );

                beans.getImportOrAliasOrBean().add( bean );
            }
        }

        return beans;
    }

    /**
     * Gets the spring scope identifier for a given specification.
     *
     * @param specification The specificatoin to get the corresponding spring
     * scope identifier for.
     *
     * @return The spring scope identifier for {@code specification}.
     */
    public String getSpringScope(
        final org.jdtaus.core.container.Specification specification )
    {
        if ( specification == null )
        {
            throw new NullPointerException( "specification" );
        }

        final String springScope;

        if ( specification.getScope() ==
             org.jdtaus.core.container.Specification.SCOPE_CONTEXT )
        {
            springScope = "jdtaus-context";
        }
        else if ( specification.getScope() ==
                  org.jdtaus.core.container.Specification.SCOPE_MULTITON )
        {
            springScope = "prototype";
        }
        else if ( specification.getScope() ==
                  org.jdtaus.core.container.Specification.SCOPE_SINGLETON )
        {
            springScope = "singleton";
        }
        else
        {
            throw new AssertionError(
                Integer.toString( specification.getScope() ) );

        }

        return springScope;
    }

    /**
     * Gets the java field name for a property.
     *
     * @param property the property to get the java field name for.
     *
     * @return The java field name for {@code property}.
     *
     * @throws NullPointerException if {@code property} is {@code null}.
     */
    public String getJavaFieldName(
        final org.jdtaus.core.container.Property property )
    {
        if ( property == null )
        {
            throw new NullPointerException( "property" );
        }

        final char[] c = property.getName().toCharArray();
        c[0] = Character.toUpperCase( c[0] );
        return "p" + String.valueOf( c );
    }

    /**
     * Gets the java field name for a dependency.
     *
     * @param dependency the dependency to get the java field name for.
     *
     * @return The java field name for {@code dependency}.
     *
     * @throws NullPointerException if {@code dependency} is {@code null}.
     */
    public String getJavaFieldName(
        final org.jdtaus.core.container.Dependency dependency )
    {
        if ( dependency == null )
        {
            throw new NullPointerException( "dependency" );
        }

        final char[] c = dependency.getName().toCharArray();
        c[0] = Character.toUpperCase( c[0] );
        return "d" + String.valueOf( c );
    }

    /**
     * Gets the java getter method name for a property.
     *
     * @param property the property to return the getter method name for.
     *
     * @return the getter method name for {@code property}.
     *
     * @throws NullPointerException if {@code property} is {@code null}.
     */
    public String getJavaGetterMethodName(
        final org.jdtaus.core.container.Property property )
    {
        if ( property == null )
        {
            throw new NullPointerException( "property" );
        }

        final boolean isFlag =
            property.getType() == Boolean.class ||
            property.getType() == boolean.class;

        final char[] name = property.getName().toCharArray();
        name[0] = Character.toUpperCase( name[0] );
        return ( isFlag ? "is" : "get" ) + String.valueOf( name );
    }

    /**
     * Gets the java getter method name for a dependency.
     *
     * @param dependency the dependency to return the getter method name for.
     *
     * @return the getter method name for {@code dependency}.
     *
     * @throws NullPointerException if {@code dependency} is {@code null}.
     */
    public String getJavaGetterMethodName(
        final org.jdtaus.core.container.Dependency dependency )
    {
        if ( dependency == null )
        {
            throw new NullPointerException( "dependency" );
        }

        final char[] name = dependency.getName().toCharArray();
        name[0] = Character.toUpperCase( name[0] );
        return "get" + String.valueOf( name );
    }

    /**
     * Gets the java getter method name for a message.
     *
     * @param message the message to return the getter method name for.
     *
     * @return the getter method name for {@code message}.
     *
     * @throws NullPointerException if {@code message} is {@code null}.
     */
    public String getJavaGetterMethodName(
        final org.jdtaus.core.container.Message message )
    {
        if ( message == null )
        {
            throw new NullPointerException( "message" );
        }

        final char[] name = message.getName().toCharArray();
        name[0] = Character.toUpperCase( name[0] );
        return "get" + String.valueOf( name ) + "Message";
    }

    /**
     * Gets the Java type of a given property.
     *
     * @param property The property to get the Java type of.
     *
     * @return The Java type of {@code property}.
     */
    public Class getJavaType( final PropertyElement property )
    {
        final Class propertyType;
        final String typeName = property.getType().getValue();

        if ( typeName.equals( Boolean.TYPE.getName() ) )
        {
            propertyType = Boolean.TYPE;
        }
        else if ( typeName.equals( Byte.TYPE.getName() ) )
        {
            propertyType = Byte.TYPE;
        }
        else if ( typeName.equals( Character.TYPE.getName() ) )
        {
            propertyType = Character.TYPE;
        }
        else if ( typeName.equals( Double.TYPE.getName() ) )
        {
            propertyType = Double.TYPE;
        }
        else if ( typeName.equals( Float.TYPE.getName() ) )
        {
            propertyType = Float.TYPE;
        }
        else if ( typeName.equals( Integer.TYPE.getName() ) )
        {
            propertyType = Integer.TYPE;
        }
        else if ( typeName.equals( Long.TYPE.getName() ) )
        {
            propertyType = Long.TYPE;
        }
        else if ( typeName.equals( Short.TYPE.getName() ) )
        {
            propertyType = Short.TYPE;
        }
        else if ( typeName.equals( Boolean.class.getName() ) )
        {
            propertyType = Boolean.class;
        }
        else if ( typeName.equals( Byte.class.getName() ) )
        {
            propertyType = Byte.class;
        }
        else if ( typeName.equals( Character.class.getName() ) )
        {
            propertyType = Character.class;
        }
        else if ( typeName.equals( Double.class.getName() ) )
        {
            propertyType = Double.class;
        }
        else if ( typeName.equals( Float.class.getName() ) )
        {
            propertyType = Float.class;
        }
        else if ( typeName.equals( Integer.class.getName() ) )
        {
            propertyType = Integer.class;
        }
        else if ( typeName.equals( Long.class.getName() ) )
        {
            propertyType = Long.class;
        }
        else if ( typeName.equals( Short.class.getName() ) )
        {
            propertyType = Short.class;
        }
        else if ( typeName.equals( String.class.getName() ) )
        {
            propertyType = String.class;
        }
        else
        {
            throw new IllegalArgumentException( property.getType().getValue() );
        }

        return propertyType;
    }

    /**
     * Maps a container {@code Specifications} instance to the plugin's model.
     *
     * @param cSpecifications the container model to map.
     *
     * @return {@code cSpecifications} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private SpecificationsElement map(
        final org.jdtaus.core.container.Specifications cSpecifications )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final SpecificationsElement specs = f.createSpecificationsElement();

        for ( int i = cSpecifications.size() - 1; i >= 0; i-- )
        {
            specs.getSpecification().add( this.getSpecification(
                cSpecifications.getSpecification( i ) ) );

        }

        this.map( specs, cSpecifications );

        return specs.getSpecification().size() > 0 ? specs : null;
    }

    /**
     * Maps a container {@code Implementations} instance to the plugin's model.
     *
     * @param cImplementations the container model to map.
     * @param module the module containing {@code cImplementations}.
     *
     * @return {@code cImplementations} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private ImplementationsElement map(
        final org.jdtaus.core.container.Implementations cImplementations,
        final Module module )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final ImplementationsElement impls = f.createImplementationsElement();

        for ( int i = cImplementations.size() - 1; i >= 0; i-- )
        {
            final ImplementationElement impl = f.createImplementationElement();

            impl.setDependencies(
                map( cImplementations.getImplementation( i ).
                getDeclaredDependencies() ) );

            impl.setFinal( cImplementations.getImplementation( i ).isFinal() );
            impl.setIdentifier( cImplementations.getImplementation( i ).
                getIdentifier() );

            final SpecificationsElement is =
                f.createSpecificationsElement();

            for ( int s = cImplementations.getImplementation( i ).
                getDeclaredImplementedSpecifications().size() - 1;
                  s >= 0; s-- )
            {
                final SpecificationReference im =
                    f.createSpecificationReference();

                im.setIdentifier( cImplementations.getImplementation( i ).
                    getImplementedSpecifications().
                    getSpecification( s ).getIdentifier() );

                if ( cImplementations.getImplementation( i ).
                    getImplementedSpecifications().
                    getSpecification( s ).getVersion() != null )
                {
                    im.setVersion( cImplementations.getImplementation( i ).
                        getImplementedSpecifications().
                        getSpecification( s ).getVersion() );

                }

                is.getReference().add( im );
            }

            if ( is.getReference().size() > 0 )
            {
                impl.setSpecifications( is );
            }

            impl.setName( cImplementations.getImplementation( i ).getName() );

            if ( cImplementations.getImplementation( i ).getParent() != null )
            {
                impl.setParent( cImplementations.getImplementation( i ).
                    getParent().getIdentifier() );

            }

            impl.setProperties(
                map( cImplementations.getImplementation( i ).
                getDeclaredProperties() ) );

            impl.setVendor( cImplementations.getImplementation( i ).getVendor() );
            impl.setVersion(
                cImplementations.getImplementation( i ).getVersion() );

            final MessagesElement messages = f.createMessagesElement();
            for ( int m = cImplementations.getImplementation( i ).
                getMessages().size() - 1; m >= 0; m-- )
            {
                final org.jdtaus.core.container.Message cMessage =
                    cImplementations.getImplementation( i ).getMessages().
                    getMessage( m );

                if ( this.getMessage( module, cMessage.getName() ) != null )
                {
                    final MessageReference reference =
                        f.createMessageReference();

                    reference.setName( cMessage.getName() );
                    this.map( reference, cMessage );

                    messages.getReference().add( reference );
                }
                else
                {
                    final MessageElement message = f.createMessageElement();
                    message.setName( cMessage.getName() );
                    message.setArguments( this.map( cMessage.getArguments() ) );
                    message.setTemplate( this.map( cMessage.getTemplate() ) );

                    this.map( message, cMessage );
                    messages.getMessage().add( message );
                }
            }

            if ( !messages.getMessage().isEmpty() ||
                 !messages.getReference().isEmpty() )
            {
                impl.setMessages( messages );
            }

            this.map( impl, cImplementations.getImplementation( i ) );

            impls.getImplementation().add( impl );
        }

        this.map( impls, cImplementations );

        return impls.getImplementation().size() > 0 ? impls : null;
    }

    /**
     * Maps a container {@code Dependencies} instance to the plugin's model.
     *
     * @param cDependencies the container model to map.
     *
     * @return {@code cDependencies} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private DependenciesElement map(
        final org.jdtaus.core.container.Dependencies cDependencies )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final DependenciesElement deps = f.createDependenciesElement();

        for ( int i = cDependencies.size() - 1; i >= 0; i-- )
        {
            deps.getDependency().add( this.getDependency( cDependencies.
                getDependency( i ) ) );

        }

        this.map( deps, cDependencies );

        return deps.getDependency().size() > 0 ? deps : null;
    }

    /**
     * Maps a container {@code Properties} instance to the plugin's model.
     *
     * @param cProperties the container model to map.
     *
     * @return {@code cProperties} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private PropertiesElement map(
        final org.jdtaus.core.container.Properties cProperties )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final PropertiesElement properties = f.createPropertiesElement();

        for ( int i = cProperties.size() - 1; i >= 0; i-- )
        {
            final PropertyElement p = f.createPropertyElement();

            p.setName( cProperties.getProperty( i ).getName() );
            p.setType( PropertyType.fromValue( cProperties.getProperty( i ).
                getType().getName() ) );

            if ( cProperties.getProperty( i ).getValue() != null )
            {
                p.setValue( cProperties.getProperty( i ).getValue().
                    toString() );

            }

            this.map( p, cProperties.getProperty( i ) );
            properties.getProperty().add( p );
        }

        this.map( properties, cProperties );
        return properties.getProperty().size() > 0 ? properties : null;
    }

    /**
     * Maps a container {@code Messages} instance to the plugin's model.
     *
     * @param cMessages the container model to map.
     *
     * @return {@code cMessages} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private MessagesElement map(
        final org.jdtaus.core.container.Messages cMessages )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final MessagesElement messages = f.createMessagesElement();

        for ( int i = cMessages.size() - 1; i >= 0; i-- )
        {
            final MessageElement m = f.createMessageElement();
            m.setName( cMessages.getMessage( i ).getName() );
            m.setTemplate( this.map( cMessages.getMessage( i ).
                getTemplate() ) );

            m.setArguments( this.map( cMessages.getMessage( i ).
                getArguments() ) );

            this.map( m, cMessages.getMessage( i ) );
            messages.getMessage().add( m );
        }

        this.map( messages, cMessages );
        return messages.getMessage().size() > 0 ? messages : null;
    }

    /**
     * Maps a container {@code Arguments} instance to the plugin's model.
     *
     * @param cArguments the container model to map.
     *
     * @return {@code cArguments} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private ArgumentsElement map(
        final org.jdtaus.core.container.Arguments cArguments )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final ArgumentsElement arguments = f.createArgumentsElement();

        for ( int i = 0; i < cArguments.size(); i++ )
        {
            final ArgumentElement a = f.createArgumentElement();
            a.setIndex( BigInteger.valueOf( cArguments.getArgument( i ).
                getIndex() ) );

            a.setName( cArguments.getArgument( i ).getName() );

            final ArgumentType type;
            if ( org.jdtaus.core.container.Argument.TYPE_DATE ==
                 cArguments.getArgument( i ).getType() )
            {
                type = ArgumentType.DATE;
            }
            else if ( org.jdtaus.core.container.Argument.TYPE_NUMBER ==
                      cArguments.getArgument( i ).getType() )
            {
                type = ArgumentType.NUMBER;
            }
            else if ( org.jdtaus.core.container.Argument.TYPE_TEXT ==
                      cArguments.getArgument( i ).getType() )
            {
                type = ArgumentType.TEXT;
            }
            else if ( org.jdtaus.core.container.Argument.TYPE_TIME ==
                      cArguments.getArgument( i ).getType() )
            {
                type = ArgumentType.TIME;
            }
            else
            {
                throw new AssertionError(
                    Integer.toString(
                    cArguments.getArgument( i ).getType() ) );

            }
            a.setType( type );

            this.map( a, cArguments.getArgument( i ) );
            arguments.getArgument().add( a );
        }

        this.map( arguments, cArguments );
        return arguments.getArgument().size() > 0 ? arguments : null;
    }

    /**
     * Maps a container {@code Text} instance to the plugin's model.
     *
     * @param cText the container model to map.
     *
     * @return {@code cText} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    private Texts map( final org.jdtaus.core.container.Text cText )
        throws JAXBException
    {
        final ObjectFactory f = new ObjectFactory();
        final Texts texts = f.createTexts();

        final Locale[] locales = cText.getLocales();
        if ( locales.length > 0 )
        {
            for ( int i = locales.length - 1; i >= 0; i-- )
            {
                final Text text = f.createText();
                final String value = cText.getValue( locales[i] );
                text.setLanguage( locales[i].getLanguage().toLowerCase() );
                text.setValue( value );

                texts.getText().add( text );

                if ( value.equals( cText.getValue() ) )
                {
                    texts.setDefaultLanguage( locales[i].getLanguage().
                        toLowerCase() );

                }
            }
        }

        return texts.getText().size() > 0 ? texts : null;
    }

    /**
     * Maps a container {@code ModelObject} instance to the plugin's model.
     *
     * @param object the object to map {@code cObject} to.
     * @param cObject the container model to map.
     *
     * @throws JAXBException if mapping fails.
     */
    private void map( final ModelObject object,
                      final org.jdtaus.core.container.ModelObject cObject )
        throws JAXBException
    {
        object.setModelVersion( cObject.getModelVersion() );
        object.setDocumentation( this.map( cObject.getDocumentation() ) );
    }

    /**
     * Gets a message form a module.
     *
     * @param module The module to get a message from.
     * @param name The name of the message to return.
     *
     * @return the message with name {@code name} from {@code module} or
     * {@code null} if {@code module} does not contain a message with name
     * {@code name}.
     */
    private Message getMessage( final Module module, final String name )
    {
        Message message = null;

        if ( module.getMessages() != null )
        {
            for ( final Iterator it = module.getMessages().getMessage().
                iterator(); it.hasNext(); )
            {
                final Message current = (Message) it.next();
                if ( current.getName().equals( name ) )
                {
                    message = current;
                    break;
                }
            }
        }

        return message;
    }

    /**
     * Gets a specification form a set of specifications.
     *
     * @param specs The set of specifications to get a specification from.
     * @param identifier The identifier of the specification to return.
     *
     * @return The specification identified by {@code identifier} from
     * {@code specs} or {@code null} if {@code specs} does not contain a
     * specification identified by {@code identifier}.
     */
    private SpecificationElement getSpecification(
        final Specifications specs, final String identifier )
    {
        SpecificationElement s = null;
        for ( final Iterator it = specs.getSpecification().iterator();
              it.hasNext(); )
        {
            final SpecificationElement e = (SpecificationElement) it.next();
            if ( identifier.equals( e.getIdentifier() ) )
            {
                s = e;
                break;
            }
        }

        return s;
    }

}
