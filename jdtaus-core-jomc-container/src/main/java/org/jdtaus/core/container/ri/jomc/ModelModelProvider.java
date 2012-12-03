// SECTION-START[License Header]
// <editor-fold defaultstate="collapsed" desc=" Generated License ">
/*
 *   jDTAUS
 *   Copyright (C) Christian Schulte, 2012-039
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 2.1 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   $JDTAUS$
 *
 */
// </editor-fold>
// SECTION-END
package org.jdtaus.core.container.ri.jomc;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import org.jomc.model.Argument;
import org.jomc.model.ArgumentType;
import org.jomc.model.Arguments;
import org.jomc.model.Dependencies;
import org.jomc.model.Dependency;
import org.jomc.model.Implementation;
import org.jomc.model.ImplementationReference;
import org.jomc.model.Implementations;
import org.jomc.model.InheritanceModel;
import org.jomc.model.Message;
import org.jomc.model.MessageReference;
import org.jomc.model.Messages;
import org.jomc.model.Module;
import org.jomc.model.Modules;
import org.jomc.model.Multiplicity;
import org.jomc.model.Properties;
import org.jomc.model.Property;
import org.jomc.model.Specification;
import org.jomc.model.SpecificationReference;
import org.jomc.model.Specifications;
import org.jomc.model.Text;
import org.jomc.model.Texts;
import org.jomc.model.modlet.ModelHelper;
import org.jomc.modlet.Model;
import org.jomc.modlet.ModelContext;
import org.jomc.modlet.ModelException;

// SECTION-START[Documentation]
// <editor-fold defaultstate="collapsed" desc=" Generated Documentation ">
/**
 * JOMC {@code ModelProvider} providing the jDTAUS {@code Model}.
 *
 * <dl>
 *   <dt><b>Identifier:</b></dt><dd>jDTAUS Core &#8273; JOMC Container &#8273; Model ModelProvider</dd>
 *   <dt><b>Name:</b></dt><dd>jDTAUS Core &#8273; JOMC Container &#8273; Model ModelProvider</dd>
 *   <dt><b>Specifications:</b></dt>
 *     <dd>org.jomc.modlet.ModelProvider</dd>
 *   <dt><b>Abstract:</b></dt><dd>No</dd>
 *   <dt><b>Final:</b></dt><dd>No</dd>
 *   <dt><b>Stateless:</b></dt><dd>No</dd>
 * </dl>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a> 1.0
 * @version 1.15-SNAPSHOT
 */
// </editor-fold>
// SECTION-END
// SECTION-START[Annotations]
// <editor-fold defaultstate="collapsed" desc=" Generated Annotations ">
@javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
// </editor-fold>
// SECTION-END
public class ModelModelProvider
    implements
    org.jomc.modlet.ModelProvider
{
    // SECTION-START[ModelProvider]

    /** Name of the system property controlling the use of the context class loader. */
    private static final String CONTEXT_CLASSLOADER_PROPERTY_NAME =
        "org.jdtaus.core.container.ClassLoaderFactory.enableContextClassloader";

    @Override
    public Model findModel( final ModelContext context, final Model model ) throws NullPointerException, ModelException
    {
        Model provided = null;
        final String sys = System.getProperty( CONTEXT_CLASSLOADER_PROPERTY_NAME );
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try
        {
            System.setProperty( CONTEXT_CLASSLOADER_PROPERTY_NAME, Boolean.TRUE.toString() );
            Thread.currentThread().setContextClassLoader( context.getClassLoader() );

            final Modules modules =
                transformModules( context, org.jdtaus.core.container.ModelFactory.getModel().getModules() );

            if ( modules != null )
            {
                applyInheritanceAttributes( context, modules );
                provided = model.clone();
                ModelHelper.addModules( provided, modules );
            }

            return provided;
        }
        finally
        {
            Thread.currentThread().setContextClassLoader( classLoader );

            if ( sys != null )
            {
                System.setProperty( CONTEXT_CLASSLOADER_PROPERTY_NAME, sys );
            }
            else
            {
                System.clearProperty( CONTEXT_CLASSLOADER_PROPERTY_NAME );
            }
        }
    }

    // SECTION-END
    // SECTION-START[ModelModelProvider]
    private static Modules transformModules( final ModelContext context,
                                             final org.jdtaus.core.container.Modules jdtausModules )
    {
        Modules modules = null;

        if ( jdtausModules != null )
        {
            modules = new Modules();

            for ( int i = 0, s0 = jdtausModules.size(); i < s0; i++ )
            {
                final org.jdtaus.core.container.Module jdtausModule = jdtausModules.getModule( i );

                if ( !org.jdtaus.core.container.Model.class.getName().equals( jdtausModule.getName() ) )
                { // Skip platform module.
                    modules.getModule().add( transformModule( context, jdtausModule ) );
                }
            }
        }

        return modules;
    }

    private static Module transformModule( final ModelContext context,
                                           final org.jdtaus.core.container.Module jdtausModule )
    {
        Module module = null;

        if ( jdtausModule != null )
        {
            module = new Module();
            module.setDocumentation( transformText( context, jdtausModule.getDocumentation() ) );
            module.setName( jdtausModule.getName() );
            module.setVersion( jdtausModule.getVersion() );
            module.setSpecifications( transformSpecifications( context, jdtausModule.getSpecifications() ) );
            module.setImplementations( transformImplementations( context, jdtausModule.getImplementations() ) );

            if ( context.isLoggable( Level.FINEST ) )
            {
                context.log( Level.FINEST, getModuleInfo( Locale.getDefault(), module.getName(),
                                                          module.getVersion() == null ? module.getVersion() : "" ),
                             null );

            }
        }

        return module;
    }

    private static Implementations transformImplementations(
        final ModelContext context,
        final org.jdtaus.core.container.Implementations jdtausImplementations )
    {
        Implementations implementations = null;

        if ( jdtausImplementations != null )
        {
            implementations = new Implementations();
            implementations.setDocumentation( transformText( context, jdtausImplementations.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausImplementations.size(); i < s0; i++ )
            {
                implementations.getImplementation().add( transformImplementation(
                    context, jdtausImplementations.getImplementation( i ) ) );

            }
        }

        return implementations;
    }

    private static Implementation transformImplementation(
        final ModelContext context,
        final org.jdtaus.core.container.Implementation jdtausImplementation )
    {
        final Implementation implementation = new Implementation();
        implementation.setIdentifier( jdtausImplementation.getIdentifier() );
        implementation.setClazz( jdtausImplementation.getIdentifier() );
        implementation.setClassDeclaration( Boolean.TRUE );
        implementation.setFinal( jdtausImplementation.isFinal() );
        implementation.setName( jdtausImplementation.getName() );
        implementation.setVendor( jdtausImplementation.getVendor() );
        implementation.setVersion( jdtausImplementation.getVersion() );
        implementation.setDocumentation( transformText( context, jdtausImplementation.getDocumentation() ) );
        implementation.setProperties( transformProperties( context, jdtausImplementation.getDeclaredProperties() ) );
        implementation.setMessages( transformMessages( context, jdtausImplementation.getDeclaredMessages() ) );
        implementation.setDependencies( transformDependencies( context, jdtausImplementation.getDeclaredDependencies() ) );
        implementation.setSpecifications( transformSpecificationReferences(
            context, jdtausImplementation.getImplementedSpecifications() ) );

        // The jDTAUS Container has no support for synchronising object accesses. This ensures the state management of
        // the JOMC ObjectManager is in effect.
        implementation.setStateless( Boolean.FALSE );

        if ( jdtausImplementation.getParent() != null )
        {
            final Implementations references = new Implementations();
            implementation.setImplementations( references );

            final ImplementationReference reference = new ImplementationReference();
            references.getReference().add( reference );

            reference.setIdentifier( jdtausImplementation.getParent().getIdentifier() );
            reference.setVersion( jdtausImplementation.getParent().getVersion() );
        }

        return implementation;
    }

    private static Specifications transformSpecifications(
        final ModelContext context,
        final org.jdtaus.core.container.Specifications jdtausSpecifications )
    {
        Specifications specifications = null;

        if ( jdtausSpecifications != null )
        {
            specifications = new Specifications();
            specifications.setDocumentation( transformText( context, jdtausSpecifications.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausSpecifications.size(); i < s0; i++ )
            {
                specifications.getSpecification().add( transformSpecification(
                    context, jdtausSpecifications.getSpecification( i ) ) );

            }
        }

        return specifications;
    }

    private static Specifications transformSpecificationReferences(
        final ModelContext context,
        final org.jdtaus.core.container.Specifications jdtausSpecifications )
    {
        Specifications specifications = null;

        if ( jdtausSpecifications != null )
        {
            specifications = new Specifications();
            specifications.setDocumentation( transformText( context, jdtausSpecifications.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausSpecifications.size(); i < s0; i++ )
            {
                final SpecificationReference specificationReference = new SpecificationReference();
                specificationReference.setIdentifier( jdtausSpecifications.getSpecification( i ).getIdentifier() );
                specificationReference.setVersion( jdtausSpecifications.getSpecification( i ).getVersion() );
                specifications.getReference().add( specificationReference );
            }
        }

        return specifications;
    }

    private static Specification transformSpecification(
        final ModelContext context,
        final org.jdtaus.core.container.Specification jdtausSpecification )
    {
        final Specification specification = new Specification();
        specification.setClassDeclaration( Boolean.TRUE );
        specification.setClazz( jdtausSpecification.getIdentifier() );
        specification.setIdentifier( jdtausSpecification.getIdentifier() );
        specification.setVendor( jdtausSpecification.getVendor() );
        specification.setVersion( jdtausSpecification.getVersion() );
        specification.setDocumentation( transformText( context, jdtausSpecification.getDocumentation() ) );

        switch ( jdtausSpecification.getMultiplicity() )
        {
            case org.jdtaus.core.container.Specification.MULTIPLICITY_MANY:
                specification.setMultiplicity( Multiplicity.MANY );
                break;
            case org.jdtaus.core.container.Specification.MULTIPLICITY_ONE:
                specification.setMultiplicity( Multiplicity.ONE );
                break;
            default:

        }

        switch ( jdtausSpecification.getScope() )
        {
            case org.jdtaus.core.container.Specification.SCOPE_CONTEXT:
                specification.setScope( "jDTAUS Context" );
                break;

            case org.jdtaus.core.container.Specification.SCOPE_SINGLETON:
                specification.setScope( "Singleton" );
                break;
            default:

        }

        specification.setProperties( transformProperties( context, jdtausSpecification.getProperties() ) );
        return specification;
    }

    private static Texts transformText( final ModelContext context, final org.jdtaus.core.container.Text jdtausText )
    {
        Texts texts = null;

        if ( jdtausText != null )
        {
            texts = new Texts();

            for ( final Locale locale : jdtausText.getLocales() )
            {
                final Text text = new Text();
                text.setLanguage( locale.getLanguage() );
                text.setValue( jdtausText.getValue( locale ) );
                texts.getText().add( text );

                if ( text.getValue().equals( jdtausText.getValue() ) )
                {
                    texts.setDefaultLanguage( locale.getLanguage() );
                }
            }

            if ( texts.getText().isEmpty() )
            {
                texts = null;
            }
        }

        return texts;
    }

    private static Properties transformProperties( final ModelContext context,
                                                   final org.jdtaus.core.container.Properties jdtausProperties )
    {
        Properties properties = null;

        if ( jdtausProperties != null )
        {
            properties = new Properties();
            properties.setDocumentation( transformText( context, jdtausProperties.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausProperties.size(); i < s0; i++ )
            {
                properties.getProperty().add( transformProperty( context, jdtausProperties.getProperty( i ) ) );
            }
        }

        return properties;
    }

    private static Property transformProperty( final ModelContext context,
                                               final org.jdtaus.core.container.Property jdtausProperty )
    {
        Property property = null;

        if ( jdtausProperty != null )
        {
            property = new Property();
            property.setDocumentation( transformText( context, jdtausProperty.getDocumentation() ) );
            property.setName( jdtausProperty.getName() );
            property.setFinal( Boolean.FALSE );
            property.setOverride( Boolean.FALSE );
            property.setType( jdtausProperty.getType().getName() );

            if ( jdtausProperty.getValue() != null )
            {
                property.setValue( jdtausProperty.getValue().toString() );
            }
            else if ( jdtausProperty.isApi() )
            {
                if ( Byte.TYPE.equals( jdtausProperty.getType() )
                     || Short.TYPE.equals( jdtausProperty.getType() )
                     || Integer.TYPE.equals( jdtausProperty.getType() )
                     || Long.TYPE.equals( jdtausProperty.getType() )
                     || Float.TYPE.equals( jdtausProperty.getType() )
                     || Double.TYPE.equals( jdtausProperty.getType() ) )
                {
                    property.setValue( "0" );
                }
                else
                {
                    property.setValue( "" );
                }
            }
        }

        return property;
    }

    private static Messages transformMessages( final ModelContext context,
                                               final org.jdtaus.core.container.Messages jdtausMessages )
    {
        Messages messages = null;

        if ( jdtausMessages != null )
        {
            messages = new Messages();
            messages.setDocumentation( transformText( context, jdtausMessages.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausMessages.size(); i < s0; i++ )
            {
                messages.getMessage().add( transformMessage( context, jdtausMessages.getMessage( i ) ) );
            }
        }

        return messages;
    }

    private static Message transformMessage( final ModelContext context,
                                             final org.jdtaus.core.container.Message jdtausMessage )
    {
        Message message = null;

        if ( jdtausMessage != null )
        {
            message = new Message();
            message.setDocumentation( transformText( context, jdtausMessage.getDocumentation() ) );
            message.setName( jdtausMessage.getName() );
            message.setFinal( Boolean.FALSE );
            message.setOverride( Boolean.FALSE );
            message.setTemplate( transformText( context, jdtausMessage.getTemplate() ) );
            message.setArguments( transformArguments( context, jdtausMessage.getArguments() ) );
        }

        return message;
    }

    private static Arguments transformArguments( final ModelContext context,
                                                 final org.jdtaus.core.container.Arguments jdtausArguments )
    {
        Arguments arguments = null;

        if ( jdtausArguments != null )
        {
            arguments = new Arguments();
            arguments.setDocumentation( transformText( context, jdtausArguments.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausArguments.size(); i < s0; i++ )
            {
                arguments.getArgument().add( transformArgument( context, jdtausArguments.getArgument( i ) ) );
            }
        }

        return arguments;
    }

    private static Argument transformArgument( final ModelContext context,
                                               final org.jdtaus.core.container.Argument jdtausArgument )
    {
        Argument argument = null;

        if ( jdtausArgument != null )
        {
            argument = new Argument();
            argument.setDocumentation( transformText( context, jdtausArgument.getDocumentation() ) );
            argument.setIndex( jdtausArgument.getIndex() );
            argument.setName( jdtausArgument.getName() );

            switch ( jdtausArgument.getType() )
            {
                case org.jdtaus.core.container.Argument.TYPE_DATE:
                    argument.setType( ArgumentType.DATE );
                    break;

                case org.jdtaus.core.container.Argument.TYPE_NUMBER:
                    argument.setType( ArgumentType.NUMBER );
                    break;

                case org.jdtaus.core.container.Argument.TYPE_TEXT:
                    argument.setType( ArgumentType.TEXT );
                    break;
                case org.jdtaus.core.container.Argument.TYPE_TIME:
                    argument.setType( ArgumentType.TIME );
                    break;
                default:

            }
        }

        return argument;
    }

    private static Dependencies transformDependencies( final ModelContext context,
                                                       final org.jdtaus.core.container.Dependencies jdtausDependencies )
    {
        Dependencies dependencies = null;

        if ( jdtausDependencies != null )
        {
            dependencies = new Dependencies();
            dependencies.setDocumentation( transformText( context, jdtausDependencies.getDocumentation() ) );

            for ( int i = 0, s0 = jdtausDependencies.size(); i < s0; i++ )
            {
                dependencies.getDependency().add( transformDependency(
                    context, jdtausDependencies.getDependency( i ) ) );

            }
        }

        return dependencies;
    }

    private static Dependency transformDependency( final ModelContext context,
                                                   final org.jdtaus.core.container.Dependency jdtausDependency )
    {
        Dependency dependency = null;

        if ( jdtausDependency != null )
        {
            dependency = new Dependency();
            dependency.setDocumentation( transformText( context, jdtausDependency.getDocumentation() ) );
            dependency.setName( jdtausDependency.getName() );
            dependency.setFinal( Boolean.FALSE );
            dependency.setOverride( Boolean.FALSE );
            dependency.setBound( jdtausDependency.isBound() );
            dependency.setIdentifier( jdtausDependency.getSpecification().getIdentifier() );
            dependency.setVersion( jdtausDependency.getSpecification().getVersion() );

            if ( jdtausDependency.getImplementation() != null )
            {
                dependency.setImplementationName( jdtausDependency.getImplementation().getName() );
            }

            // Cannot set to optional since the JOMC ObjectManager returns null if no implementations are found and
            // jDTAUS implementations expect an empty array.
            dependency.setOptional( Boolean.FALSE );
            dependency.setProperties( transformProperties( context, jdtausDependency.getDeclaredProperties() ) );
        }

        return dependency;
    }

    private static void applyInheritanceAttributes( final ModelContext context, final Modules modules )
    {
        final InheritanceModel imodel = new InheritanceModel( modules );

        if ( modules.getImplementations() != null )
        {
            for ( int i = 0, s0 = modules.getImplementations().getImplementation().size(); i < s0; i++ )
            {
                applyInheritanceAttributes(
                    context, modules, imodel, modules.getImplementations().getImplementation().get( i ) );

            }
        }
    }

    private static void applyInheritanceAttributes( final ModelContext context, final Modules modules,
                                                    final InheritanceModel imodel, final Implementation implementation )
    {
        final Set<String> dependencyNames = imodel.getDependencyNames( implementation.getIdentifier() );
        final Set<String> messageNames = imodel.getMessageNames( implementation.getIdentifier() );
        final Set<String> propertyNames = imodel.getPropertyNames( implementation.getIdentifier() );

        for ( final String dependencyName : dependencyNames )
        {
            final Set<InheritanceModel.Node<Dependency>> effDependency =
                imodel.getDependencyNodes( implementation.getIdentifier(), dependencyName );

            for ( final InheritanceModel.Node<Dependency> d : effDependency )
            {
                if ( d.getImplementation().getIdentifier().equals( implementation.getIdentifier() )
                     && !d.getOverriddenNodes().isEmpty() )
                {
                    implementation.getDependencies().getDependency( dependencyName ).setOverride( true );
                }
            }
        }

        for ( final String messageName : messageNames )
        {
            final Set<InheritanceModel.Node<Message>> effMessage =
                imodel.getMessageNodes( implementation.getIdentifier(), messageName );

            for ( final InheritanceModel.Node<Message> m : effMessage )
            {
                if ( m.getImplementation().getIdentifier().equals( implementation.getIdentifier() )
                     && !m.getOverriddenNodes().isEmpty() )
                {
                    final Message msg = implementation.getMessages().getMessage( messageName );
                    final MessageReference ref = implementation.getMessages().getReference( messageName );

                    if ( msg != null )
                    {
                        msg.setOverride( true );
                    }
                    if ( ref != null )
                    {
                        ref.setOverride( true );
                    }
                }
            }
        }

        for ( final String propertyName : propertyNames )
        {
            final Set<InheritanceModel.Node<Property>> effProperty =
                imodel.getPropertyNodes( implementation.getIdentifier(), propertyName );

            for ( final InheritanceModel.Node<Property> p : effProperty )
            {
                if ( p.getImplementation().getIdentifier().equals( implementation.getIdentifier() )
                     && !p.getOverriddenNodes().isEmpty() )
                {
                    implementation.getProperties().getProperty( propertyName ).setOverride( true );
                }
            }
        }

        if ( implementation.getDependencies() != null )
        {
            for ( int i = 0, s0 = implementation.getDependencies().getDependency().size(); i < s0; i++ )
            {
                applyInheritanceAttributes(
                    context, modules, imodel, implementation.getDependencies().getDependency().get( i ) );

            }
        }
    }

    private static void applyInheritanceAttributes( final ModelContext context, final Modules modules,
                                                    final InheritanceModel imodel, final Dependency dependency )
    {
        final Set<Implementation> implementations = new HashSet<Implementation>();

        if ( dependency.getImplementationName() != null )
        {
            final Implementation i = modules.getImplementation( dependency.getIdentifier(),
                                                                dependency.getImplementationName() );

            if ( i != null )
            {
                implementations.add( i );
            }
        }
        else
        {
            final Implementations i = modules.getImplementations( dependency.getIdentifier() );
            if ( i != null )
            {
                implementations.addAll( i.getImplementation() );
            }
        }

        if ( dependency.getDependencies() != null )
        {
            for ( int i = 0, s0 = dependency.getDependencies().getDependency().size(); i < s0; i++ )
            {
                final Dependency d = dependency.getDependencies().getDependency().get( i );

                for ( final Implementation impl : implementations )
                {
                    final Set<InheritanceModel.Node<Dependency>> effDependencies =
                        imodel.getDependencyNodes( impl.getIdentifier(), d.getName() );

                    if ( !d.isOverride() && !effDependencies.isEmpty() )
                    {
                        d.setOverride( true );
                    }
                }

                applyInheritanceAttributes( context, modules, imodel, d );
            }
        }
        if ( dependency.getMessages() != null )
        {
            for ( int i = 0, s0 = dependency.getMessages().getMessage().size(); i < s0; i++ )
            {
                final Message m = dependency.getMessages().getMessage().get( i );

                for ( final Implementation impl : implementations )
                {
                    final Set<InheritanceModel.Node<Message>> effMessages =
                        imodel.getMessageNodes( impl.getIdentifier(), m.getName() );

                    if ( !m.isOverride() && !effMessages.isEmpty() )
                    {
                        m.setOverride( true );
                    }
                }
            }
        }
        if ( dependency.getProperties() != null )
        {
            for ( int i = 0, s0 = dependency.getProperties().getProperty().size(); i < s0; i++ )
            {
                final Property p = dependency.getProperties().getProperty().get( i );

                for ( final Implementation impl : implementations )
                {
                    final Set<InheritanceModel.Node<Property>> effProperties =
                        imodel.getPropertyNodes( impl.getIdentifier(), p.getName() );

                    if ( !p.isOverride() && !effProperties.isEmpty() )
                    {
                        p.setOverride( true );
                    }
                }
            }
        }
    }

    // SECTION-END
    // SECTION-START[Constructors]
    // <editor-fold defaultstate="collapsed" desc=" Generated Constructors ">
    /** Creates a new {@code ModelModelProvider} instance. */
    @javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
    public ModelModelProvider()
    {
        // SECTION-START[Default Constructor]
        super();
        // SECTION-END
    }
    // </editor-fold>
    // SECTION-END
    // SECTION-START[Dependencies]
    // SECTION-END
    // SECTION-START[Properties]
    // SECTION-END
    // SECTION-START[Messages]
    // <editor-fold defaultstate="collapsed" desc=" Generated Messages ">
    /**
     * Gets the text of the {@code <moduleInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param moduleName Format argument.
     * @param moduleVersion Format argument.
     * @return The text of the {@code <moduleInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated( value = "org.jomc.tools.SourceFileProcessor 1.3", comments = "See http://jomc.sourceforge.net/jomc/1.3/jomc-tools-1.3" )
    private static String getModuleInfo( final java.util.Locale locale, final java.lang.String moduleName, final java.lang.String moduleVersion )
    {
        java.io.BufferedReader reader = null;
        boolean suppressExceptionOnClose = true;

        try
        {
            final String message = java.text.MessageFormat.format( java.util.ResourceBundle.getBundle( "org/jdtaus/core/container/ri/jomc/ModelModelProvider", locale ).getString( "moduleInfo" ), moduleName, moduleVersion, (Object) null );
            final java.lang.StringBuilder builder = new java.lang.StringBuilder( message.length() );
            reader = new java.io.BufferedReader( new java.io.StringReader( message ) );
            final String lineSeparator = System.getProperty( "line.separator", "\n" );

            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                builder.append( lineSeparator ).append( line );
            }

            suppressExceptionOnClose = false;
            return builder.length() > 0 ? builder.substring( lineSeparator.length() ) : "";
        }
        catch( final java.lang.ClassCastException e )
        {
            throw new org.jomc.ObjectManagementException( e.getMessage(), e );
        }
        catch( final java.lang.IllegalArgumentException e )
        {
            throw new org.jomc.ObjectManagementException( e.getMessage(), e );
        }
        catch( final java.util.MissingResourceException e )
        {
            throw new org.jomc.ObjectManagementException( e.getMessage(), e );
        }
        catch( final java.io.IOException e )
        {
            throw new org.jomc.ObjectManagementException( e.getMessage(), e );
        }
        finally
        {
            try
            {
                if( reader != null )
                {
                    reader.close();
                }
            }
            catch( final java.io.IOException e )
            {
                if( !suppressExceptionOnClose )
                {
                    throw new org.jomc.ObjectManagementException( e.getMessage(), e );
                }
            }
        }
    }
    // </editor-fold>
    // SECTION-END
}
