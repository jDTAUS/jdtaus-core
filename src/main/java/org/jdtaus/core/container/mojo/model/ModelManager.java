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
import org.jdtaus.core.container.mojo.model.container.Dependency;
import org.jdtaus.core.container.mojo.model.container.ImplementationElement;
import org.jdtaus.core.container.mojo.model.container.ImplementationsElement;
import org.jdtaus.core.container.mojo.model.container.Message;
import org.jdtaus.core.container.mojo.model.container.MessageElement;
import org.jdtaus.core.container.mojo.model.container.MessageReference;
import org.jdtaus.core.container.mojo.model.container.MessagesElement;
import org.jdtaus.core.container.mojo.model.container.ModelObject;
import org.jdtaus.core.container.mojo.model.container.Module;
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
import org.jdtaus.core.container.mojo.model.container.SpecificationsElement;
import org.jdtaus.core.container.mojo.model.container.Text;
import org.jdtaus.core.container.mojo.model.container.Texts;
import org.jdtaus.core.container.mojo.model.spring.BeanElement;
import org.jdtaus.core.container.mojo.model.spring.BeansElement;

/**
 * Manages the {@code http://jdtaus.org/core/model/container} model.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @plexus.component role="org.jdtaus.core.container.mojo.model.ModelManager"
 *                   role-hint="default"
 */
public class ModelManager extends AbstractLogEnabled
{

    /** Component role. */
    public static final String ROLE = ModelManager.class.getName();

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
        "http://sites.jdtaus.org/jdtaus-core/1.0.x/jdtaus-core-schemas/jdtaus-container-1.1.xsd"
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
     * @throws JAXBexception if creating a {@code Marshaller} fails.
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
     * @throws JAXBexception if creating a {@code Marshaller} fails.
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
     * @throws JAXBexception if creating an {@code Unmarshaller} fails.
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
     * @throws JAXBexception if creating an {@code Unmarshaller} fails.
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
     * @param model the container model to map.
     *
     * @return {@code model} mapped to the plugin's model.
     *
     * @throws JAXBException if mapping fails.
     */
    public ModulesElement getContainerModel(
        final org.jdtaus.core.container.Modules cModules ) throws JAXBException
    {
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
     * Maps a container {@code Modules} instance to the spring container model.
     *
     * @param factoryBeanClassName the name of the {@code FactoryBean}
     * implementation class.
     * @param model the container model to map.
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
        SpecificationsElement specs = f.createSpecificationsElement();

        for ( int i = cSpecifications.size() - 1; i >= 0; i-- )
        {
            final SpecificationElement spec = f.createSpecificationElement();

            spec.setIdentifier( cSpecifications.getSpecification( i ).
                                getIdentifier() );

            spec.setMultiplicity( cSpecifications.getSpecification( i ).
                                  getMultiplicity() ==
                                  org.jdtaus.core.container.Specification.MULTIPLICITY_ONE
                                  ? Multiplicity.ONE
                                  : Multiplicity.MANY );

            final int cScope = cSpecifications.getSpecification( i ).getScope();
            if ( org.jdtaus.core.container.Specification.SCOPE_CONTEXT == cScope )
            {
                spec.setScope( Scope.CONTEXT );
            }
            else if ( org.jdtaus.core.container.Specification.SCOPE_MULTITON ==
                cScope )
            {
                spec.setScope( Scope.MULTITON );
            }
            else if ( org.jdtaus.core.container.Specification.SCOPE_SINGLETON ==
                cScope )
            {
                spec.setScope( Scope.SINGLETON );
            }
            else
            {
                throw new AssertionError( Integer.toString( cScope ) );
            }

            spec.setVendor( cSpecifications.getSpecification( i ).
                            getVendor() );

            spec.setStateless( cSpecifications.getSpecification( i ).
                               isStateless() );

            if ( cSpecifications.getSpecification( i ).getVersion() != null )
            {
                spec.setVersion( cSpecifications.getSpecification( i ).
                                 getVersion() );

            }

            spec.setProperties( this.map( cSpecifications.getSpecification( i ).
                                          getProperties() ) );

            this.map( spec, cSpecifications.getSpecification( i ) );

            specs.getSpecification().add( spec );
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
            final Dependency dep = f.createDependencyElement();
            dep.setBound( cDependencies.getDependency( i ).isBound() );
            dep.setName( cDependencies.getDependency( i ).getName() );
            dep.setIdentifier( cDependencies.getDependency( i ).
                               getSpecification().getIdentifier() );

            if ( cDependencies.getDependency( i ).getSpecification().
                getVersion() != null )
            {
                dep.setVersion( cDependencies.getDependency( i ).
                                getSpecification().getVersion() );

            }

            if ( cDependencies.getDependency( i ).getImplementation() != null )
            {
                dep.setImplementationName( cDependencies.getDependency( i ).
                                           getImplementation().getName() );

            }

            final int propertyCount = cDependencies.getDependency( i ).
                getDeclaredProperties().size();

            if ( propertyCount > 0 )
            {
                final Properties properties = f.createProperties();
                dep.setProperties( properties );

                for ( int p = propertyCount - 1; p >= 0; p-- )
                {
                    final org.jdtaus.core.container.Property cProperty =
                        cDependencies.getDependency( i ).
                        getDeclaredProperties().getProperty( p );

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

                this.map( properties, cDependencies.getDependency( i ).
                          getDeclaredProperties() );

            }

            this.map( dep, cDependencies.getDependency( i ) );
            deps.getDependency().add( dep );
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

        for ( Iterator it = module.getMessages().getMessage().iterator();
            it.hasNext();)
        {
            final Message current = (Message) it.next();
            if ( current.getName().equals( name ) )
            {
                message = current;
                break;
            }
        }

        return message;
    }
}
