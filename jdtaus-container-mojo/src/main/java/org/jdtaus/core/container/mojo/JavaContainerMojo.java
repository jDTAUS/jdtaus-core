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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.JAXBException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.jdtaus.core.container.Argument;
import org.jdtaus.core.container.ContainerError;
import org.jdtaus.core.container.ContextError;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.Message;
import org.jdtaus.core.container.Messages;
import org.jdtaus.core.container.MissingModuleException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;
import org.jdtaus.core.container.mojo.comp.VersionParser;
import org.jdtaus.core.container.mojo.model.JavaArtifact;

/**
 * Mojo to generate java code.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 * @goal java-container
 * @phase process-resources
 * @requiresDependencyResolution test
 */
public class JavaContainerMojo extends AbstractContainerMojo
{
    //--Configuration-----------------------------------------------------------

    /** Model version 1.4. */
    private static final String V1_4 = "1.4";

    /**
     * Output directory.
     * @parameter expression="${project.build.directory}/container"
     */
    private File outputDirectory;

    /**
     * The name of the module to process.
     * @parameter expression="${moduleName}" default-value="${project.name}"
     */
    private String moduleName;

    /**
     * The name of the test module to process.
     * @parameter expression="${testModuleName}"
     *            default-value="${project.name} Tests"
     */
    private String testModuleName;

    /**
     * The string marking the starting line where to start inserting specification
     * code.
     * @parameter expression="${specificationsStartingMarker}" default-value="//--Specification-----------------------------------------------------------"
     */
    private String specificationsStartingMarker;

    /**
     * The string marking the ending line up to where specification code should
     * be inserted.
     * @parameter expression="${specificationsEndingMarker}" default-value="//-----------------------------------------------------------Specification--"
     */
    private String specificationsEndingMarker;

    /**
     * The string marking the starting line where to start inserting implementation
     * code.
     * @parameter expression="${implementationsStartingMarker}" default-value="//--Implementation----------------------------------------------------------"
     */
    private String implementationsStartingMarker;

    /**
     * The string marking the ending line up to where implementation code should
     * be inserted.
     * @parameter expression="${implementationsEndingMarker}" default-value="//----------------------------------------------------------Implementation--"
     */
    private String implementationsEndingMarker;

    /**
     * The string marking the starting line where to start inserting constructor
     * code.
     * @parameter expression="${constructorsStartingMarker}" default-value="//--Constructors------------------------------------------------------------"
     */
    private String constructorsStartingMarker;

    /**
     * The string marking the ending line up to where constructor code should be
     * inserted.
     * @parameter expression="${constructorsEndingMarker}" default-value="//------------------------------------------------------------Constructors--"
     */
    private String constructorsEndingMarker;

    /**
     * The string marking the starting line where to start inserting dependency
     * code.
     * @parameter expression="${dependenciesStartingMarker}" default-value="//--Dependencies------------------------------------------------------------"
     */
    private String dependenciesStartingMarker;

    /**
     * The string marking the ending line up to where dependency code should be
     * inserted.
     * @parameter expression="${dependenciesEndingMarker}" default-value="//------------------------------------------------------------Dependencies--"
     */
    private String dependenciesEndingMarker;

    /**
     * The string marking the starting line where to start inserting
     * implementation property code.
     * @parameter expression="${propertiesStartingMarker}" default-value="//--Properties--------------------------------------------------------------"
     */
    private String propertiesStartingMarker;

    /**
     * The string marking the ending line up to where implementation property
     * code should be inserted.
     * @parameter expression="${propertiesEndingMarker}" default-value="//--------------------------------------------------------------Properties--"
     */
    private String propertiesEndingMarker;

    /**
     * The string marking the starting line where to start inserting
     * implementation message code.
     * @parameter expression="${messagesStartingMarker}" default-value="//--Messages----------------------------------------------------------------"
     */
    private String messagesStartingMarker;

    /**
     * The string marking the ending line up to where implementation message
     * code should be inserted.
     * @parameter expression="${messagesEndingMarker}" default-value="//----------------------------------------------------------------Messages--"
     */
    private String messagesEndingMarker;

    /**
     * Specifies the target editor used for editing sourcefiles. Used for e.g.
     * generating folding markers specific to IDE editors. Currently available
     * options are {@code none} and {@code netbeans}.
     *
     * @parameter expression="${targetIde}" default-value="netbeans"
     */
    private String targetIde;

    /**
     * Source root to create new source files in.
     * @parameter expression="${sourceRoot}"
     *            default-value="${basedir}/src/main/java"
     */
    private File sourceRoot;

    /** Cached model. */
    protected Model model;

    /**
     * Gets the model of the current execution.
     *
     * @return The model of the current execution.
     */
    protected Model getModel()
    {
        if ( this.model == null )
        {
            this.model = ModelFactory.newModel();
        }

        return this.model;
    }

    /**
     * Accessor to the currently executed jDTAUS module.
     *
     * @return The currently executed jDTAUS module or {@code null} if no module
     * is defined for the currently executed jDTAUS module.
     */
    protected final Module getModule()
    {
        Module module = null;

        if ( this.moduleName != null )
        {
            try
            {
                module = this.getModel().getModules().
                    getModule( this.moduleName );

                if ( this.getLog().isDebugEnabled() )
                {
                    this.getLog().debug( module.toString() );
                }
            }
            catch ( MissingModuleException e )
            {
                this.getLog().info( JavaContainerMojoBundle.getInstance().
                    getSkippingMainModuleMessage( Locale.getDefault() ) );

            }
        }

        return module;
    }

    /**
     * Accessor to the currently executed jDTAUS test module.
     *
     * @return The currently executed jDTAUS test module or {@code null} if
     * no test module is defined for the currently executed jDTAUS module.
     */
    protected final Module getTestModule()
    {
        Module module = null;

        if ( this.testModuleName != null )
        {
            try
            {
                module = this.getModel().getModules().
                    getModule( this.testModuleName );

                if ( this.getLog().isDebugEnabled() )
                {
                    this.getLog().debug( module.toString() );
                }
            }
            catch ( MissingModuleException e )
            {
                this.getLog().info( JavaContainerMojoBundle.getInstance().
                    getSkippingTestModuleMessage( Locale.getDefault() ) );

            }
        }

        return module;
    }

    /**
     * Gets the target editor to use when generating source code.
     *
     * @return The target editor to use when generating source code.
     */
    protected final String getTargetEditor()
    {
        return this.targetIde.toLowerCase();
    }

    /**
     * Gets the source root to create new source files in.
     *
     * @return The source root to create new source files in.
     */
    protected final File getSourceRoot()
    {
        return this.sourceRoot;
    }

    /**
     * Gets the output directory of the mojo.
     *
     * @return The output directory of the mojo.
     */
    protected final File getOutputDirectory()
    {
        return this.outputDirectory;
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractMojo------------------------------------------------------------

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader = Thread.currentThread().
            getContextClassLoader();

        try
        {
            this.model = null;

            Thread.currentThread().setContextClassLoader(
                this.getRuntimeClassLoader( mavenLoader ) );

            enableThreadContextClassLoader();

            final Module mod = this.getModule();

            if ( mod != null )
            {
                final Specifications specs = mod.getSpecifications();
                final Implementations impls = mod.getImplementations();

                for ( int i = specs.size() - 1; i >= 0; i-- )
                {
                    this.generateSpecification(
                        this.getMavenProject().getCompileSourceRoots(),
                        specs.getSpecification( i ) );

                }

                for ( int i = impls.size() - 1; i >= 0; i-- )
                {
                    this.generateImplementation(
                        this.getMavenProject().getCompileSourceRoots(),
                        impls.getImplementation( i ) );

                }

                this.writeContainerReport( this.getModel(),
                                           "container-report.xml" );

                this.getLog().info( JavaContainerMojoBundle.getInstance().
                    getProcessingModuleMessage( Locale.getDefault(),
                                                mod.getName() ) );

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
    //--ContainerMojo-----------------------------------------------------------

    /** Location of the implementation template. */
    private static final String IMPLEMENTATION_TEMPLATE_LOCATION =
        "META-INF/templates/Implementation.java.vm";

    /** Adds dependency getters to an implementation. */
    private class DependencyEditor implements AbstractContainerMojo.SourceEditor
    {

        private boolean editing = false;

        private boolean modified = false;

        private final String fileName;

        private final Implementation impl;

        private final boolean markersNeeded;

        private DependencyEditor( final String fileName,
                                  final Implementation impl )
        {
            if ( fileName == null )
            {
                throw new NullPointerException( "fileName" );
            }
            if ( impl == null )
            {
                throw new NullPointerException( "impl" );
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getDeclaredDependencies().size() > 0;
        }

        public String editLine( final String line ) throws MojoFailureException
        {
            String replacement = null; // Replace with nothing.

            if ( line == null && this.editing )
            {
                throw new MojoFailureException(
                    JavaContainerMojoBundle.getInstance().
                    getUnexpectedEndOfInputMessage( Locale.getDefault(),
                                                    this.fileName ) );

            }

            if ( line != null &&
                 dependenciesStartingMarker.equals( line.trim() ) )
            {
                // Skip all input up to the ending marker.
                this.editing = true;
                final StringBuffer buf = new StringBuffer( 1024 );
                buf.append( line ).append( "\n\n" );
                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getOpeningFoldingMarker( "Dependencies" ) ).
                        append( '\n' );

                }

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getGeneratorWarningMessage( getLocale() ) );

                buf.append( "\n\n" );

                // Generate dependency getter.
                if ( this.isMarkersNeeded() )
                {
                    this.modified = true;
                    final Dependencies deps =
                        this.impl.getDeclaredDependencies();

                    for ( int i = deps.size() - 1; i >= 0; i-- )
                    {
                        final Dependency dep = deps.getDependency( i );
                        String depType = getTypeFromClassName(
                            dep.getSpecification().getIdentifier() );

                        if ( dep.getImplementation() == null &&
                             dep.getSpecification().getMultiplicity() ==
                             Specification.MULTIPLICITY_MANY )
                        {
                            depType += "[]";
                        }

                        final boolean hasDescription = dep.getDocumentation().
                            getLocales().length > 0;

                        final String description =
                            hasDescription
                            ? dep.getDocumentation().getValue( getLocale() )
                            : JavaContainerMojoBundle.getInstance().
                            getDefaultDependencyDescriptionMessage(
                            getLocale(), dep.getName() );

                        indent( buf );
                        buf.append( JavaContainerMojoBundle.getInstance().
                            getDependencyGetterCommentMessage(
                            getLocale(), dep.getName(), description ) );

                        indent( buf );
                        buf.append(
                            this.impl.isFinal() && this.impl.getParent() == null
                            ? "private "
                            : "protected " ).append( depType ).
                            append( " " );

                        buf.append( getModelManager().
                            getJavaGetterMethodName( dep ) ).
                            append( "()\n" );

                        indent( buf );
                        buf.append( "{\n" );

                        indent( buf );
                        indent( buf );
                        buf.append( "return (" ).append( depType ).
                            append( ") " ).
                            append( "ContainerFactory.getContainer().\n" );

                        indent( buf );
                        indent( buf );
                        indent( buf );
                        buf.append( "getDependency( this, \"" ).append(
                            dep.getName() ).
                            append( "\" );\n\n" );

                        indent( buf );
                        buf.append( "}\n\n" );
                    }
                }

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getClosingFoldingMarker( "Dependencies" ) ).
                        append( '\n' );

                }

                replacement = buf.toString();
            }
            else
            {
                if ( this.editing )
                {
                    if ( dependenciesEndingMarker.equals( line.trim() ) )
                    {
                        this.editing = false;
                        replacement = line;
                    }
                }
                else
                {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified()
        {
            return this.modified;
        }

        public boolean isMarkersNeeded()
        {
            return this.markersNeeded;
        }

    }

    /** Adds property getters to an implementation. */
    private class PropertyEditor implements AbstractContainerMojo.SourceEditor
    {

        private boolean editing = false;

        private boolean modified = false;

        private final String fileName;

        private final Implementation impl;

        private final boolean markersNeeded;

        private PropertyEditor( final String fileName,
                                final Implementation impl )
        {
            if ( fileName == null )
            {
                throw new NullPointerException( "fileName" );
            }
            if ( impl == null )
            {
                throw new NullPointerException( "impl" );
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getDeclaredProperties().size() > 0;
        }

        public String editLine( final String line ) throws MojoFailureException
        {
            String replacement = null; // Replace with nothing.

            if ( line == null && this.editing )
            {
                throw new MojoFailureException(
                    JavaContainerMojoBundle.getInstance().
                    getUnexpectedEndOfInputMessage( Locale.getDefault(),
                                                    this.fileName ) );

            }

            if ( line != null &&
                 propertiesStartingMarker.equals( line.trim() ) )
            {
                // Skip all input up to the ending marker.
                this.editing = true;
                final StringBuffer buf = new StringBuffer( 1024 );
                buf.append( line ).append( "\n\n" );

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getOpeningFoldingMarker( "Properties" ) ).
                        append( '\n' );

                }

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getGeneratorWarningMessage( getLocale() ) );
                buf.append( "\n\n" );

                // Generate property getters and setters.
                if ( this.isMarkersNeeded() )
                {
                    this.modified = true;
                    this.generateProperties( this.impl.getDeclaredProperties(),
                                             buf );

                }

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getClosingFoldingMarker( "Properties" ) ).
                        append( '\n' );

                }

                replacement = buf.toString();
            }
            else
            {
                if ( this.editing )
                {
                    if ( propertiesEndingMarker.equals( line.trim() ) )
                    {
                        this.editing = false;
                        replacement = line;
                    }
                }
                else
                {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified()
        {
            return this.modified;
        }

        public boolean isMarkersNeeded()
        {
            return this.markersNeeded;
        }

        private void generateProperties( final Properties properties,
                                         final StringBuffer buf )
        {
            for ( int i = properties.size() - 1; i >= 0; i-- )
            {
                final Property property = properties.getProperty( i );

                // Getter.
                final boolean hasDescription = property.getDocumentation().
                    getLocales().length > 0;

                final String description = hasDescription
                                           ? property.getDocumentation().
                    getValue( getLocale() )
                                           : JavaContainerMojoBundle.getInstance().
                    getDefaultPropertyDescriptionMessage( getLocale(),
                                                          property.getName() );

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getPropertyGetterCommentMessage(
                    getLocale(), property.getName(),
                    formatComment( description ) ) ).append( '\n' );

                indent( buf );
                buf.append( property.isApi()
                            ? "public "
                            : ( this.impl.isFinal() &&
                                this.impl.getParent() == null
                                ? "private "
                                : "protected " ) );

                buf.append( property.getType().getName() ).append( " " ).
                    append( getModelManager().
                    getJavaGetterMethodName( property ) ).
                    append( "()\n" );

                indent( buf );
                buf.append( "{\n" );

                indent( buf );
                indent( buf );
                buf.append( "return " );

                if ( property.getType().isPrimitive() )
                {
                    buf.append( "( (" ).
                        append( property.getValue().getClass().getName() ).
                        append( ") ContainerFactory.getContainer().\n" );

                    indent( buf );
                    indent( buf );
                    indent( buf );
                    buf.append( "getProperty( this, \"" ).
                        append( property.getName() ).
                        append( "\" ) )." ).
                        append( property.getType().getName() ).
                        append( "Value();\n\n" );

                }
                else
                {
                    buf.append( "(" ).append( property.getType().getName() ).
                        append( ") ContainerFactory.getContainer().\n" );

                    indent( buf );
                    indent( buf );
                    indent( buf );
                    buf.append( "getProperty( this, \"" ).
                        append( property.getName() ).
                        append( "\" );\n\n" );

                }

                indent( buf );
                buf.append( "}\n\n" );
            }
        }

    }

    /** Adds implementation constructors. */
    private class ConstructorsEditor implements
        AbstractContainerMojo.SourceEditor
    {

        private boolean editing = false;

        private boolean modified = false;

        private final String fileName;

        private final Implementation impl;

        private final boolean markersNeeded;

        private ConstructorsEditor(
            final String fileName, final Implementation impl )
            throws MojoFailureException
        {
            if ( fileName == null )
            {
                throw new NullPointerException( "fileName" );
            }
            if ( impl == null )
            {
                throw new NullPointerException( "impl" );
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getImplementedSpecifications().size() > 0;
        }

        public String editLine( final String line ) throws MojoFailureException
        {
            String replacement = null; // Replace with nothing.

            if ( line == null && this.editing )
            {
                throw new MojoFailureException(
                    JavaContainerMojoBundle.getInstance().
                    getUnexpectedEndOfInputMessage( Locale.getDefault(),
                                                    this.fileName ) );

            }

            if ( line != null &&
                 constructorsStartingMarker.equals( line.trim() ) )
            {
                // Skip all input up to the ending marker.
                this.editing = true;
                this.modified = true;
                final StringBuffer buf = new StringBuffer( 1024 );
                final String implType =
                    getTypeFromClassName( this.impl.getIdentifier() );

                buf.append( line ).append( "\n\n" );
                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getOpeningFoldingMarker( "Constructors" ) ).
                        append( '\n' );

                }

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getGeneratorWarningMessage( getLocale() ) );
                buf.append( "\n\n" );

                if ( this.impl.getImplementedSpecifications().size() > 0 )
                {
                    indent( buf );
                    buf.append(
                        JavaContainerMojoBundle.getInstance().
                        getStandardConstructorMessage(
                        getLocale(), this.impl.getIdentifier(), implType ) );

                    buf.append( '\n' );
                }

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getClosingFoldingMarker( "Constructors" ) ).
                        append( '\n' );

                }

                replacement = buf.toString();
            }
            else
            {
                if ( this.editing )
                {
                    if ( constructorsEndingMarker.equals( line.trim() ) )
                    {
                        this.editing = false;
                        replacement = line;
                    }
                }
                else
                {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified()
        {
            return this.modified;
        }

        public boolean isMarkersNeeded()
        {
            return this.markersNeeded;
        }

    }

    /** Adds message getters to an implementation. */
    private class MessageEditor implements AbstractContainerMojo.SourceEditor
    {

        private boolean editing = false;

        private boolean modified = false;

        private final String fileName;

        private final Implementation impl;

        private final boolean markersNeeded;

        public MessageEditor( final String fileName,
                              final Implementation impl )
        {
            if ( fileName == null )
            {
                throw new NullPointerException( "fileName" );
            }
            if ( impl == null )
            {
                throw new NullPointerException( "impl" );
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getDeclaredMessages().size() > 0;
        }

        public String editLine( final String line ) throws MojoFailureException
        {
            String replacement = null; // Replace with nothing.

            if ( line == null && this.editing )
            {
                throw new MojoFailureException(
                    JavaContainerMojoBundle.getInstance().
                    getUnexpectedEndOfInputMessage( Locale.getDefault(),
                                                    this.fileName ) );

            }

            if ( line != null &&
                 messagesStartingMarker.equals( line.trim() ) )
            {
                // Skip all input up to the ending marker.
                this.editing = true;
                final StringBuffer buf = new StringBuffer( 1024 );
                buf.append( line ).append( "\n\n" );

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getOpeningFoldingMarker( "Messages" ) ).
                        append( '\n' );

                }

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getGeneratorWarningMessage( getLocale() ) );
                buf.append( "\n\n" );

                // Generate property getters and setters.
                if ( this.isMarkersNeeded() )
                {
                    this.modified = true;
                    this.generateMessages( this.impl.getDeclaredMessages(),
                                           buf );

                }

                if ( !getTargetEditor().equals( "none" ) )
                {
                    buf.append( getClosingFoldingMarker( "Messages" ) ).
                        append( '\n' );

                }

                replacement = buf.toString();
            }
            else
            {
                if ( this.editing )
                {
                    if ( messagesEndingMarker.equals( line.trim() ) )
                    {
                        this.editing = false;
                        replacement = line;
                    }
                }
                else
                {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified()
        {
            return this.modified;
        }

        public boolean isMarkersNeeded()
        {
            return this.markersNeeded;
        }

        private void generateMessages( final Messages messages,
                                       final StringBuffer buf )
            throws MojoFailureException
        {
            try
            {
                for ( int i = messages.size() - 1; i >= 0; i-- )
                {
                    final Message message = messages.getMessage( i );

                    // Getter.
                    final boolean hasDescription = message.getDocumentation().
                        getLocales().length > 0;

                    final String description = hasDescription
                                               ? message.getDocumentation().
                        getValue( getLocale() )
                                               : JavaContainerMojoBundle.
                        getInstance().
                        getDefaultMessageDescriptionMessage( getLocale(),
                                                             message.getName() );

                    indent( buf );
                    buf.append( "/**\n" );
                    indent( buf );
                    buf.append( " * " );
                    buf.append( JavaContainerMojoBundle.getInstance().
                        getMessageGetterCommentMessage( getLocale(),
                                                        message.getName() ) ).
                        append( '\n' );

                    final Locale[] locales = message.getTemplate().getLocales();
                    for ( int d = locales.length - 1; d >= 0; d-- )
                    {
                        indent( buf );
                        buf.append( " * <blockquote><pre>" );
                        buf.append( formatComment( message.getTemplate().
                            getValue( locales[d] ) ) );

                        buf.append( "</pre></blockquote>\n" );
                    }

                    if ( message.getArguments().size() > 0 ||
                         VersionParser.compare( messages.getModelVersion(),
                                                V1_4 ) >= 0 )
                    {
                        indent( buf );
                        buf.append( " *\n" );

                        if ( VersionParser.compare( messages.getModelVersion(),
                                                    V1_4 ) >= 0 )
                        {
                            indent( buf );
                            buf.append( " * @param locale " ).
                                append( formatComment(
                                JavaContainerMojoBundle.getInstance().
                                getLocaleParamCommentMessage( getLocale() ) ) ).
                                append( '\n' );

                        }

                        for ( int a = 0; a < message.getArguments().size();
                              a++ )
                        {
                            indent( buf );
                            buf.append( " * @param " ).
                                append( message.getArguments().getArgument( a ).
                                getName() ).append( " " );

                            if ( message.getArguments().getArgument( a ).
                                getDocumentation().getLocales().length > 0 )
                            {
                                buf.append(
                                    formatComment(
                                    message.getArguments().getArgument( a ).
                                    getDocumentation().
                                    getValue( getLocale() ) ) );

                            }
                            else
                            {
                                buf.append( formatComment(
                                    JavaContainerMojoBundle.getInstance().
                                    getDefaultArgumentDescriptionMessage(
                                    getLocale() ) ) );

                            }

                            buf.append( '\n' );
                        }
                    }

                    indent( buf );
                    buf.append( " *\n" );

                    indent( buf );
                    buf.append( " * @return " );
                    buf.append( formatComment( description ) ).append( '\n' );
                    indent( buf );
                    buf.append( " */\n" );

                    indent( buf );
                    buf.append( this.impl.isFinal() &&
                                this.impl.getParent() == null
                                ? "private" : "protected" );

                    buf.append( " String " ).append(
                        getModelManager().getJavaGetterMethodName( message ) );

                    buf.append( "(" );
                    if ( VersionParser.compare( messages.getModelVersion(),
                                                V1_4 ) >= 0 )
                    {
                        buf.append( " final Locale locale" );
                        if ( message.getArguments().size() > 0 )
                        {
                            buf.append( ',' );
                        }
                    }

                    if ( message.getArguments().size() > 0 )
                    {
                        buf.append( "\n" );
                        indent( buf );
                        indent( buf );
                        indent( buf );

                        for ( int a = 0; a < message.getArguments().size();
                              a++ )
                        {
                            final Argument arg = message.getArguments().
                                getArgument( a );

                            final String javaType;
                            switch ( arg.getType() )
                            {
                                case Argument.TYPE_DATE:
                                case Argument.TYPE_TIME:
                                    javaType = "java.util.Date";
                                    break;

                                case Argument.TYPE_NUMBER:
                                    javaType = "java.lang.Number";
                                    break;

                                case Argument.TYPE_TEXT:
                                    javaType = "java.lang.String";
                                    break;

                                default:
                                    throw new AssertionError(
                                        Integer.toString( arg.getType() ) );

                            }

                            buf.append( "final " ).append( javaType ).
                                append( " " ).append( arg.getName() );

                            if ( a + 1 < message.getArguments().size() )
                            {
                                buf.append( ",\n" );
                                indent( buf );
                                indent( buf );
                                indent( buf );
                            }
                            else
                            {
                                buf.append( " )\n" );
                            }
                        }
                    }
                    else
                    {
                        if ( VersionParser.compare( messages.getModelVersion(),
                                                    V1_4 ) >= 0 )
                        {
                            buf.append( " " );
                        }

                        buf.append( ")\n" );
                    }

                    indent( buf );
                    buf.append( "{\n" );

                    indent( buf );
                    indent( buf );
                    buf.append( "return ContainerFactory.getContainer().\n" );
                    indent( buf );
                    indent( buf );
                    indent( buf );
                    buf.append( "getMessage( this, \"" ).
                        append( message.getName() ).append( '"' );

                    if ( VersionParser.compare( messages.getModelVersion(),
                                                V1_4 ) >= 0 )
                    {
                        buf.append( ", locale" );
                    }

                    if ( message.getArguments().size() > 0 )
                    {
                        buf.append( ",\n" );
                        indent( buf );
                        indent( buf );
                        indent( buf );
                        indent( buf );
                        buf.append( "new Object[]\n" );
                        indent( buf );
                        indent( buf );
                        indent( buf );
                        indent( buf );
                        buf.append( "{\n" );

                        for ( int a = 0; a < message.getArguments().size(); a++ )
                        {
                            final Argument arg =
                                message.getArguments().getArgument( a );

                            indent( buf );
                            indent( buf );
                            indent( buf );
                            indent( buf );
                            indent( buf );
                            buf.append( arg.getName() );

                            if ( a + 1 < message.getArguments().size() )
                            {
                                buf.append( "," );
                            }

                            buf.append( "\n" );
                        }

                        indent( buf );
                        indent( buf );
                        indent( buf );
                        indent( buf );
                        buf.append( "});\n\n" );
                    }
                    else
                    {
                        buf.append( ", null );\n\n" );
                    }

                    indent( buf );
                    buf.append( "}\n\n" );
                }
            }
            catch ( ParseException e )
            {
                throw new MojoFailureException( e.getMessage(), e );
            }
        }

    }

    /** Cleans a section. */
    private class RemovingEditor implements AbstractContainerMojo.SourceEditor
    {

        private boolean editing = false;

        private boolean modified = false;

        private final String fileName;

        private final String startingMarker;

        private final String endingMarker;

        private RemovingEditor( final String fileName,
                                final String startingMarker,
                                final String endingMarker )
        {
            if ( fileName == null )
            {
                throw new NullPointerException( "fileName" );
            }
            if ( startingMarker == null )
            {
                throw new NullPointerException( "startingMarker" );
            }
            if ( endingMarker == null )
            {
                throw new NullPointerException( "endingMarker" );
            }

            this.fileName = fileName;
            this.startingMarker = startingMarker;
            this.endingMarker = endingMarker;
        }

        public String editLine( String line ) throws MojoFailureException
        {
            if ( line == null && this.editing )
            {
                throw new MojoFailureException(
                    JavaContainerMojoBundle.getInstance().
                    getUnexpectedEndOfInputMessage( Locale.getDefault(),
                                                    this.fileName ) );

            }

            String replacement = null; // Replace with nothing.
            if ( line != null && this.startingMarker.equals( line.trim() ) )
            {
                // Skip all input up to the ending marker.
                this.editing = true;
                this.modified = true;
                // Leave the marker.
                final StringBuffer buf = new StringBuffer( 1024 );
                buf.append( line ).append( "\n\n" );

                indent( buf );
                buf.append( JavaContainerMojoBundle.getInstance().
                    getGeneratorWarningMessage( getLocale() ) );
                buf.append( "\n\n" );

                replacement = buf.toString();
            }
            else
            {
                if ( this.editing )
                {
                    if ( this.endingMarker.equals( line.trim() ) )
                    {
                        // Stop editing.
                        this.editing = false;
                        // Leave the ending marker.
                        replacement = line;
                    }
                }
                else
                {
                    replacement = line;
                }
            }

            return replacement;

        }

        public boolean isModified()
        {
            return this.modified;
        }

    }

    protected String getTypeFromClassName( final String className )
    {
        if ( className == null )
        {
            throw new NullPointerException( "className" );
        }

        return className.substring( className.lastIndexOf( '.' ) + 1 );
    }

    protected void generateImplementation( final List roots,
                                           final Implementation impl )
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            if ( roots == null )
            {
                throw new NullPointerException( "roots" );
            }

            if ( impl == null )
            {
                throw new NullPointerException( "impl" );
            }

            String edited;
            File source = this.getSource( roots, impl.getIdentifier() );

            if ( source == null )
            {
                final JavaArtifact artifact =
                    new JavaArtifact( impl.getIdentifier() );

                source = new File( this.getSourceRoot(),
                                   artifact.getPackagePath() + File.separator +
                                   artifact.getName() + ".java" );

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
                ctx.put( "implementation", impl );
                ctx.put( "constructorsStartingMarker",
                         this.constructorsStartingMarker );

                ctx.put( "constructorsEndingMarker",
                         this.constructorsEndingMarker );

                ctx.put( "dependenciesStartingMarker",
                         this.dependenciesStartingMarker );

                ctx.put( "dependenciesEndingMarker",
                         this.dependenciesEndingMarker );

                ctx.put( "propertiesStartingMarker",
                         this.propertiesStartingMarker );

                ctx.put( "propertiesEndingMarker",
                         this.propertiesEndingMarker );

                ctx.put( "messagesStartingMarker",
                         this.messagesStartingMarker );

                ctx.put( "messagesEndingMarker",
                         this.messagesEndingMarker );

                this.getVelocity().mergeTemplate(
                    IMPLEMENTATION_TEMPLATE_LOCATION, "UTF-8", ctx, writer );

                writer.close();

                this.getLog().info( JavaContainerMojoBundle.getInstance().
                    getCreatedFileMessage( Locale.getDefault(),
                                           source.getName() ) );

            }

            final String content = this.load( source );
            final String path = source.getAbsolutePath();

            final DependencyEditor depEditor =
                new DependencyEditor( path, impl );

            final PropertyEditor propEditor =
                new PropertyEditor( path, impl );

            final ConstructorsEditor ctorsEditor =
                new ConstructorsEditor( path, impl );

            final MessageEditor messageEditor = new MessageEditor( path, impl );

            edited =
                this.edit( content,
                           new RemovingEditor( path,
                                               this.implementationsStartingMarker,
                                               this.implementationsEndingMarker ) );

            edited =
                this.edit( edited,
                           new RemovingEditor( path,
                                               this.dependenciesStartingMarker,
                                               this.dependenciesEndingMarker ) );

            edited =
                this.edit( edited,
                           new RemovingEditor( path,
                                               this.propertiesStartingMarker,
                                               this.propertiesEndingMarker ) );

            edited =
                this.edit( edited,
                           new RemovingEditor( path,
                                               this.constructorsStartingMarker,
                                               this.constructorsEndingMarker ) );

            edited =
                this.edit( edited,
                           new RemovingEditor( path,
                                               this.messagesStartingMarker,
                                               this.messagesEndingMarker ) );

            edited = this.edit( edited, depEditor );
            edited = this.edit( edited, propEditor );
            edited = this.edit( edited, ctorsEditor );
            edited = this.edit( edited, messageEditor );
            edited = this.edit( edited,
                                new CleanMojo.RemoveTrailingSpacesEditor() );

            if ( depEditor.isMarkersNeeded() && !depEditor.isModified() )
            {
                throw new MojoExecutionException(
                    JavaContainerMojoBundle.getInstance().
                    getMissingMarkersMessage( Locale.getDefault(),
                                              this.dependenciesStartingMarker,
                                              path ) );

            }

            if ( propEditor.isMarkersNeeded() && !propEditor.isModified() )
            {
                throw new MojoExecutionException(
                    JavaContainerMojoBundle.getInstance().
                    getMissingMarkersMessage( Locale.getDefault(),
                                              this.propertiesStartingMarker,
                                              path ) );

            }

            if ( ctorsEditor.isMarkersNeeded() && !ctorsEditor.isModified() )
            {
                throw new MojoExecutionException(
                    JavaContainerMojoBundle.getInstance().
                    getMissingMarkersMessage( Locale.getDefault(),
                                              this.constructorsStartingMarker,
                                              path ) );

            }

            if ( messageEditor.isMarkersNeeded() &&
                 !messageEditor.isModified() )
            {
                throw new MojoExecutionException(
                    JavaContainerMojoBundle.getInstance().
                    getMissingMarkersMessage( Locale.getDefault(),
                                              this.messagesStartingMarker,
                                              path ) );

            }

            if ( !content.equals( edited ) )
            {
                this.save( source, edited );
            }
        }
        catch ( Exception e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
    }

    protected void generateSpecification( final List roots,
                                          final Specification spec )
        throws MojoExecutionException, MojoFailureException, IOException
    {
        if ( spec == null )
        {
            throw new NullPointerException( "spec" );
        }

        if ( roots == null )
        {
            throw new NullPointerException( "roots" );
        }

        final File source = this.getSource( roots, spec.getIdentifier() );
        if ( source != null )
        {
            final String path = source.getAbsolutePath();
            final String content = this.load( source );
            String edited = this.edit( content, new RemovingEditor(
                path, this.specificationsStartingMarker,
                this.specificationsEndingMarker ) );

            edited = this.edit( edited,
                                new CleanMojo.RemoveTrailingSpacesEditor() );

            if ( !content.equals( edited ) )
            {
                this.save( source, edited );
            }

        }
    }

    private String getOpeningFoldingMarker( final String section )
    {
        final StringBuffer buf = new StringBuffer( 500 );
        if ( this.getTargetEditor().equals( "netbeans" ) )
        {
            buf.append( JavaContainerMojoBundle.getInstance().
                getOpeningFoldingMarkerNetbeansMessage(
                this.getLocale(), section ) );

        }

        return buf.toString();
    }

    private String getClosingFoldingMarker( final String section )
    {
        final StringBuffer buf = new StringBuffer( 500 );
        if ( this.getTargetEditor().equals( "netbeans" ) )
        {
            buf.append( JavaContainerMojoBundle.getInstance().
                getClosingFoldingMarkerNetbeansMessage(
                this.getLocale(), section ) );

        }

        return buf.toString();
    }

    protected void writeContainerReport( final Model model,
                                         final String name )
        throws JAXBException, FileNotFoundException
    {
        final File reportFile = new File( this.getOutputDirectory(),
                                          name );

        if ( !reportFile.getParentFile().exists() )
        {
            reportFile.getParentFile().mkdirs();
        }

        this.getModelManager().getContainerMarshaller().marshal(
            this.getModelManager().getContainerModel( model.getModules() ),
            new FileOutputStream( reportFile ) );

    }

    //-----------------------------------------------------------ContainerMojo--
}
