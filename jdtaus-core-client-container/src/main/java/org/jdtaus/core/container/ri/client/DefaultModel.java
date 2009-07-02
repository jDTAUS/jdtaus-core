/*
 *  jDTAUS Core RI Client Container
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
package org.jdtaus.core.container.ri.client;

import java.beans.Beans;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdtaus.core.container.Argument;
import org.jdtaus.core.container.Arguments;
import org.jdtaus.core.container.Dependencies;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.DuplicateSpecificationException;
import org.jdtaus.core.container.IllegalPropertyTypeException;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.IncompatibleImplementationException;
import org.jdtaus.core.container.Implementations;
import org.jdtaus.core.container.Message;
import org.jdtaus.core.container.Messages;
import org.jdtaus.core.container.MissingImplementationException;
import org.jdtaus.core.container.MissingPropertyException;
import org.jdtaus.core.container.MissingSpecificationException;
import org.jdtaus.core.container.Model;
import org.jdtaus.core.container.ModelError;
import org.jdtaus.core.container.ModelObject;
import org.jdtaus.core.container.Module;
import org.jdtaus.core.container.Modules;
import org.jdtaus.core.container.Properties;
import org.jdtaus.core.container.Property;
import org.jdtaus.core.container.PropertyOverwriteConstraintException;
import org.jdtaus.core.container.Specification;
import org.jdtaus.core.container.Specifications;
import org.jdtaus.core.container.Text;
import org.jdtaus.core.container.ri.client.versioning.VersionParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * {@code Model} reference implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see <a href="http://sites.jdtaus.org/jdtaus-core/1.0.x/jdtaus-core-schemas/jdtaus-container-1.1.xsd">jdtaus-container-1.1.xsd</a>
 */
public class DefaultModel implements Model
{
    //--Constants---------------------------------------------------------------

    /** JAXP configuration key to the Schema implementation attribute. */
    private static final String SCHEMA_LANGUAGE_KEY =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** JAXP Schema implementation to use. */
    private static final String SCHEMA_LANGUAGE =
        "http://www.w3.org/2001/XMLSchema";

    /**
     * jDTAUS {@code Model} namespace URI for the deprecated 1.0 and 1.1 model.
     */
    private static final String MODEL_NS =
        "http://jdtaus.org/runtime/container/xml";

    /** {@code jDTAUS Container Model} namespace URI. */
    private static final String CONTAINER_NS =
        "http://jdtaus.org/core/model/container";

    /** {@code jDTAUS Container Model RI} namespace URI. */
    private static final String MODULE_NS =
        "http://jdtaus.org/core/model/container/module";

    /** Location of the document resources searched for by default. */
    private static final String MODEL_LOCATION =
        "META-INF/jdtaus/module.xml";

    /** Model versions supported by this implementation. */
    private static final String SUPPORTED_MODEL_VERSIONS[] =
    {
        "1.0", "1.1", "1.2", "1.3", "1.4"
    };

    /** String constructor arguments. */
    private static final Class[] CTOR_ARGS_STRING =
    {
        String.class
    };

    /** String constructor arguments. */
    private static final Class[] CTOR_ARGS_CHAR =
    {
        char.class
    };

    /** Constant for the name of the platform module. */
    private static final String PLATFORM_MODULE_NAME = Model.class.getName();

    /** Constant for the version of the platform module. */
    private static final String PLATFORM_MODULE_VERSION = "1.0";

    /** Constant for model version 1.1 */
    private static final String V_1_1 = "1.1";

    /** Constant for model version 1.3 */
    private static final String V_1_3 = "1.3";

    /** Constant for model version 1.4 */
    private static final String V_1_4 = "1.4";

    /** Constant for the maximum supported model version. */
    private static final String MODEL_VERSION = V_1_4;

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /**
     * Creates a new {@code DefaultModel} instance.
     *
     * @throws ModelError if no model can be read.
     *
     * @see #readModules()
     */
    public DefaultModel()
    {
        try
        {
            this.modules = this.readModules();
            // Add the platform module.
            final Module[] modulesWithPlatform =
                new Module[ this.modules.size() + 1 ];

            System.arraycopy( this.modules.getModules(), 0, modulesWithPlatform,
                              0, this.modules.size() );

            modulesWithPlatform[this.modules.size()] = this.getPlatformModule();
            this.modules.setModules( modulesWithPlatform );

            this.linkParentImplementations();
            this.assertSpecificationsAvailable();
            this.updateSpecificationReferences();
            this.linkImplementedSpecifications();
            this.linkDependencies();
            this.assertCompatibility();
            this.assertOverwrittenProperties();
            this.assertImplementedProperties();
        }
        catch ( ParserConfigurationException e )
        {
            throw new ModelError( e );
        }
        catch ( SAXException e )
        {
            throw new ModelError( e );
        }
        catch ( IOException e )
        {
            throw new ModelError( e );
        }
        catch ( ParseException e )
        {
            throw new ModelError( e );
        }
        finally
        {
            this.specifications = null;
            this.implementations = null;
            this.dependencies = null;
            this.impl2parent = null;
            this.moduleMap = null;
        }
    }

    //------------------------------------------------------------Constructors--
    //--Model-------------------------------------------------------------------

    public Modules getModules()
    {
        return this.modules;
    }

    //-------------------------------------------------------------------Model--
    //--DefaultModel------------------------------------------------------------

    /** Logger of the class. */
    private static final Logger LOGGER =
        Logger.getLogger( DefaultModel.class.getName() );

    /** Holds the instance of the platform module. */
    private Module platformModule;

    /** Holds the loaded model. */
    private Modules modules;

    /**
     * Maps specification identifiers to a list of specification instances by
     * version.
     */
    private Map/*<String,Collection>*/ specifications = new HashMap( 1000 );

    /** Maps implementation identifiers to implementation instances. */
    private Map/*<String,Implementation>*/ implementations = new HashMap( 1000 );

    /**
     * Maps identifiers of implementations to identifiers of parent
     * implementations.
     */
    private Map/*<String,String>*/ impl2parent = new HashMap( 1000 );

    /** Maps dependency keys to implementation names. */
    private Map/*<String,String>*/ dependencies = new HashMap( 1000 );

    /** Maps module names to resource URLs. */
    private Map/*<String,URL>*/ moduleMap = new HashMap( 1000 );

    /**
     * Reads the module resources.
     *
     * @return all read modules without resolved references.
     *
     * @throws IOException if reading fails.
     * @throws ParserConfigurationException if configuring the parser fails.
     * @throws SAXException if parsing documents fails.
     * @throws ParseException if parsing versions fails.
     */
    private Modules readModules()
        throws IOException, ParserConfigurationException, SAXException,
               ParseException
    {
        final Map documents = new HashMap();
        final DocumentBuilderFactory xmlFactory =
            DocumentBuilderFactory.newInstance();

        final Enumeration resources = ClassLoaderFactory.loadResources(
            this.getClass(), MODEL_LOCATION );

        xmlFactory.setNamespaceAware( true );

        try
        {
            xmlFactory.setValidating( true );
            xmlFactory.setAttribute( SCHEMA_LANGUAGE_KEY, SCHEMA_LANGUAGE );
        }
        catch ( IllegalArgumentException e )
        {
            LOGGER.log( Level.CONFIG, e.getMessage() );
            LOGGER.log( Level.WARNING, DefaultModelBundle.getInstance().
                getNoValidationWarningMessage( Locale.getDefault(),
                                               e.getMessage() ) );

            xmlFactory.setValidating( false );
        }

        final DocumentBuilder xmlBuilder = xmlFactory.newDocumentBuilder();
        xmlBuilder.setEntityResolver( new BootstrapEntityResolver() );

        // Parse and validate all files from the classpath.
        while ( resources.hasMoreElements() )
        {
            final URL resource = (URL) resources.nextElement();

            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.log( Level.FINE, DefaultModelBundle.getInstance().
                    getResourceInformationMessage(
                    Locale.getDefault(), resource.toExternalForm() ) );

            }

            final InputStream stream = resource.openStream();
            xmlBuilder.setErrorHandler(
                new ErrorHandler()
                {

                    public void warning( final SAXParseException e )
                    {
                        LOGGER.log( Level.WARNING,
                                    DefaultModelBundle.getInstance().
                            getParseExceptionMessage(
                            Locale.getDefault(), resource.toExternalForm(),
                            e.getMessage(), new Integer( e.getLineNumber() ),
                            new Integer( e.getColumnNumber() ) ) );

                    }

                    public void fatalError( final SAXParseException e )
                        throws SAXException
                    {
                        throw new SAXException(
                            DefaultModelBundle.getInstance().
                            getParseExceptionMessage(
                            Locale.getDefault(), resource.toExternalForm(),
                            e.getMessage(), new Integer( e.getLineNumber() ),
                            new Integer( e.getColumnNumber() ) ), e );

                    }

                    public void error( final SAXParseException e )
                        throws SAXException
                    {
                        throw new SAXException(
                            DefaultModelBundle.getInstance().
                            getParseExceptionMessage(
                            Locale.getDefault(), resource.toExternalForm(),
                            e.getMessage(), new Integer( e.getLineNumber() ),
                            new Integer( e.getColumnNumber() ) ), e );

                    }

                } );

            final Document doc = xmlBuilder.parse( stream );
            stream.close();
            documents.put( resource, doc );
        }

        // Transform the XML documents.
        return this.transformDocuments( documents );
    }

    private void updateSpecificationReferences()
    {
        for ( int m = this.modules.size() - 1; m >= 0; m-- )
        {
            final Module module = this.modules.getModule( m );
            for ( int s = module.getSpecifications().size() - 1; s >= 0; s-- )
            {
                final Specification spec = module.getSpecifications().
                    getSpecification( s );

                final Collection references =
                    (Collection) this.specifications.get( spec.getIdentifier() );

                assert references != null : "Expected specification meta-data.";

                for ( Iterator it = references.iterator(); it.hasNext(); )
                {
                    final Specification reference = (Specification) it.next();
                    reference.setModuleName( module.getName() );

                    if ( !reference.equals( spec ) )
                    {
                        reference.setDocumentation( spec.getDocumentation() );
                        reference.setMultiplicity( spec.getMultiplicity() );
                        reference.setProperties( spec.getProperties() );
                        reference.setScope( spec.getScope() );
                        reference.setStateless( spec.isStateless() );
                        reference.setVendor( spec.getVendor() );
                        reference.setModelVersion( spec.getModelVersion() );
                    }
                }
            }
        }
    }

    private void linkParentImplementations()
    {
        for ( Iterator it = this.implementations.entrySet().iterator();
              it.hasNext(); )
        {
            final Map.Entry e = (Map.Entry) it.next();
            final String identifier = (String) e.getKey();
            final Implementation implementation = (Implementation) e.getValue();
            final String parentIdentifier =
                (String) this.impl2parent.get( identifier );

            if ( parentIdentifier != null )
            {
                final Implementation parent =
                    (Implementation) this.implementations.get(
                    parentIdentifier );

                if ( parent == null )
                {
                    throw new MissingImplementationException(
                        parentIdentifier );

                }

                implementation.setParent( parent );
            }
        }
    }

    private void linkImplementedSpecifications()
    {
        for ( Iterator it = this.implementations.values().iterator();
              it.hasNext(); )
        {
            final Implementation implementation = (Implementation) it.next();

            for ( int i = implementation.getImplementedSpecifications().
                size() - 1; i >= 0; i-- )
            {
                final Specification implemented =
                    implementation.getImplementedSpecifications().
                    getSpecification( i );

                final Collection specs =
                    (Collection) this.specifications.get(
                    implemented.getIdentifier() );

                assert specs != null : "Expected specification meta-data.";

                for ( Iterator s = specs.iterator(); s.hasNext(); )
                {
                    final Specification spec = (Specification) s.next();
                    final Collection col = new LinkedList(
                        Arrays.asList( spec.getImplementations().
                        getImplementations() ) );

                    col.add( implementation );

                    final Implementations impls = new Implementations();
                    impls.setImplementations(
                        (Implementation[]) col.toArray(
                        new Implementation[ col.size() ] ) );

                    spec.setImplementations( impls );
                }
            }
        }
    }

    private void linkDependencies()
    {
        for ( Iterator it = this.implementations.values().iterator();
              it.hasNext(); )
        {
            final Implementation implementation = (Implementation) it.next();
            for ( int i = implementation.getDeclaredDependencies().size() - 1;
                  i >= 0; i-- )
            {
                final Dependency d = implementation.getDeclaredDependencies().
                    getDependency( i );

                final String key =
                    implementation.getIdentifier() + '/' + d.getName();

                final String name = (String) this.dependencies.get( key );

                if ( name != null )
                {
                    d.setImplementation( d.getSpecification().
                        getImplementation( name ) );

                }
            }
        }
    }

    private void assertSpecificationsAvailable()
    {
        final Implementation[] impls = this.modules.getImplementations().
            getImplementations();

        for ( int i = impls.length - 1; i >= 0; i-- )
        {
            final Specifications specs = impls[i].getImplementedSpecifications();
            final Dependencies deps = impls[i].getDependencies();

            for ( int j = specs.size() - 1; j >= 0; j-- )
            {
                try
                {
                    this.modules.getSpecification(
                        specs.getSpecification( j ).getIdentifier() );

                }
                catch ( MissingSpecificationException e )
                {
                    if ( !this.addPlatformSpecification(
                        specs.getSpecification( j ).getIdentifier(), true ) )
                    {
                        throw e;
                    }
                }
            }

            for ( int j = deps.size() - 1; j >= 0; j-- )
            {
                try
                {
                    this.modules.getSpecification(
                        deps.getDependency( j ).getSpecification().
                        getIdentifier() );

                }
                catch ( MissingSpecificationException e )
                {
                    if ( !this.addPlatformSpecification(
                        deps.getDependency( j ).getSpecification().
                        getIdentifier(), true ) )
                    {
                        throw e;
                    }
                }
            }
        }
    }

    private void assertCompatibility() throws ParseException
    {
        for ( int i = this.modules.getImplementations().size() - 1; i >= 0;
              i-- )
        {
            final Implementation impl = this.modules.getImplementations().
                getImplementation( i );

            if ( impl.getModelVersion() == null ||
                 VersionParser.compare( impl.getModelVersion(), V_1_3 ) < 0 )
            {
                continue;
            }

            final Specifications specs = impl.getImplementedSpecifications();
            final Dependencies deps = impl.getDependencies();

            for ( int s = specs.size() - 1; s >= 0; s-- )
            {
                final Specification implemented = specs.getSpecification( s );
                final Specification available =
                    this.modules.getSpecification( implemented.getIdentifier() );

                if ( available.getModelVersion() == null ||
                     VersionParser.compare( available.getModelVersion(),
                                            V_1_3 ) < 0 )
                {
                    continue;
                }

                if ( implemented.getVersion() != null )
                {
                    try
                    {
                        if ( VersionParser.compare(
                            implemented.getVersion(),
                            available.getVersion() ) < 0 )
                        {
                            throw new IncompatibleImplementationException(
                                available.getIdentifier(),
                                available.getVersion(),
                                impl.getIdentifier(), implemented.getVersion(),
                                null );

                        }
                    }
                    catch ( ParseException e )
                    {
                        final IncompatibleImplementationException iie =
                            new IncompatibleImplementationException(
                            available.getIdentifier(), available.getVersion(),
                            impl.getIdentifier(), implemented.getVersion(),
                            null );

                        iie.initCause( e );
                        throw iie;
                    }
                }
            }

            for ( int d = deps.size() - 1; d >= 0; d-- )
            {
                final Specification required =
                    deps.getDependency( d ).getSpecification();

                final Specification available =
                    this.modules.getSpecification( required.getIdentifier() );

                if ( available.getModelVersion() == null ||
                     VersionParser.compare( available.getModelVersion(),
                                            V_1_3 ) < 0 )
                {
                    continue;
                }

                if ( required.getVersion() != null )
                {
                    try
                    {
                        if ( VersionParser.compare(
                            available.getVersion(),
                            required.getVersion() ) < 0 )
                        {
                            throw new IncompatibleImplementationException(
                                available.getIdentifier(),
                                available.getVersion(),
                                impl.getIdentifier(), null,
                                required.getVersion() );

                        }
                    }
                    catch ( ParseException e )
                    {
                        final IncompatibleImplementationException iie =
                            new IncompatibleImplementationException(
                            available.getIdentifier(), available.getVersion(),
                            impl.getIdentifier(), null, required.getVersion() );

                        iie.initCause( e );
                        throw iie;
                    }
                }
            }
        }
    }

    private void assertOverwrittenProperties() throws ParseException
    {
        for ( int i = this.modules.getImplementations().size() - 1; i >= 0;
              i-- )
        {
            final Implementation impl = this.modules.getImplementations().
                getImplementation( i );

            final Dependencies deps = impl.getDependencies();
            for ( int d = deps.size() - 1; d >= 0; d-- )
            {
                final Dependency dep = deps.getDependency( d );
                if ( dep.getSpecification().getScope() !=
                     Specification.SCOPE_MULTITON &&
                     dep.getDeclaredProperties().size() > 0 )
                {
                    throw new PropertyOverwriteConstraintException(
                        impl.getIdentifier(), dep.getName() );

                }

                final Properties properties = dep.getDeclaredProperties();
                for ( int p = properties.size() - 1; p >= 0; p-- )
                {
                    final Property dependencyProperty =
                        properties.getProperty( p );

                    try
                    {
                        final Property specificationProperty =
                            dep.getSpecification().getProperties().
                            getProperty( dependencyProperty.getName() );

                        if ( !dependencyProperty.getType().
                            equals( specificationProperty.getType() ) )
                        {
                            throw new IllegalPropertyTypeException(
                                dependencyProperty.getName(),
                                dependencyProperty.getType(),
                                specificationProperty.getType() );

                        }

                        dependencyProperty.setApi( true );
                    }
                    catch ( MissingPropertyException e )
                    {
                        if ( VersionParser.compare(
                            dep.getSpecification().getModelVersion(),
                            V_1_3 ) >= 0 )
                        {
                            final PropertyOverwriteConstraintException poce =
                                new PropertyOverwriteConstraintException(
                                impl.getIdentifier(), dep.getName() );

                            poce.initCause( e );
                            throw poce;
                        }
                    }
                }
            }
        }
    }

    private void assertImplementedProperties() throws ParseException
    {
        for ( int i = this.modules.getImplementations().size() - 1; i >= 0;
              i-- )
        {
            final Map properties = new HashMap( 100 );
            final Implementation impl = this.modules.getImplementations().
                getImplementation( i );

            final Specifications specs = impl.getImplementedSpecifications();
            for ( int s = specs.size() - 1; s >= 0; s-- )
            {
                final Specification implementedSpec =
                    specs.getSpecification( s );

                final Properties props = implementedSpec.getProperties();
                for ( int p = props.size() - 1; p >= 0; p-- )
                {
                    final Property implementedProperty =
                        props.getProperty( p );

                    final Property alreadyImplemented =
                        (Property) properties.get(
                        implementedProperty.getName() );

                    if ( alreadyImplemented != null )
                    {
                        if ( !implementedProperty.getType().
                            equals( alreadyImplemented.getType() ) )
                        {
                            throw new IllegalPropertyTypeException(
                                implementedProperty.getName(),
                                implementedProperty.getType(),
                                alreadyImplemented.getType() );

                        }
                    }
                    else
                    {
                        properties.put( implementedProperty.getName(),
                                        implementedProperty );

                    }

                    try
                    {
                        final Property property =
                            impl.getProperties().getProperty(
                            implementedProperty.getName() );

                        if ( !property.getType().
                            equals( implementedProperty.getType() ) )
                        {
                            throw new IllegalPropertyTypeException(
                                property.getName(), property.getType(),
                                implementedProperty.getType() );

                        }
                    }
                    catch ( MissingPropertyException e )
                    {
                        if ( VersionParser.compare( impl.getModelVersion(),
                                                    V_1_3 ) >= 0 )
                        {
                            final PropertyOverwriteConstraintException poce =
                                new PropertyOverwriteConstraintException(
                                impl.getIdentifier(),
                                implementedSpec.getIdentifier(),
                                implementedProperty.getName() );

                            poce.initCause( e );
                            throw poce;
                        }
                    }
                }
            }

            for ( int p = impl.getProperties().size() - 1; p >= 0; p-- )
            {
                final Property property = impl.getProperties().getProperty( p );
                final Property specified =
                    (Property) properties.get( property.getName() );

                if ( specified != null &&
                     !property.getType().equals( specified.getType() ) )
                {
                    throw new IllegalPropertyTypeException(
                        property.getName(), property.getType(),
                        specified.getType() );

                }
            }
        }
    }

    private Specification getSpecification( final String identifier,
                                            final String version )
    {
        Collection c = (Collection) this.specifications.get( identifier );
        if ( c == null )
        {
            c = new LinkedList();
            this.specifications.put( identifier, c );
        }

        Specification specification = null;
        for ( Iterator it = c.iterator(); it.hasNext(); )
        {
            final Specification s = (Specification) it.next();

            if ( s.getVersion() == null
                 ? version == null
                 : s.getVersion().equals( version ) )
            {
                specification = s;
                break;
            }
        }

        if ( specification == null )
        {
            specification = new Specification();
            specification.setIdentifier( identifier );
            specification.setVersion( version );
            c.add( specification );
        }

        return specification;
    }

    private Implementation getImplementation( final String identifier )
    {
        Implementation implementation =
            (Implementation) this.implementations.get( identifier );

        if ( implementation == null )
        {
            implementation = new Implementation();
            implementation.setIdentifier( identifier );
            this.implementations.put( identifier, implementation );
        }

        return implementation;
    }

    private Modules transformDocuments( Map xml )
        throws ParseException
    {
        final Modules mods = new Modules();
        mods.setModelVersion( MODEL_VERSION );

        final List list = new LinkedList();

        for ( Iterator it = xml.entrySet().iterator(); it.hasNext(); )
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final Document doc = (Document) entry.getValue();
            final URL documentResource = (URL) entry.getKey();
            final Element e = doc.getDocumentElement();
            String namespace = doc.getDocumentElement().getNamespaceURI();

            if ( namespace == null )
            {
                throw new ModelError(
                    DefaultModelBundle.getInstance().
                    getUnknownNamespaceMessage(
                    Locale.getDefault(), namespace,
                    doc.getDocumentElement().getNodeName() ) );

            }

            final boolean deprecatedModel;
            final String modelVersion;

            if ( MODEL_NS.equals( namespace ) )
            {
                deprecatedModel = true;

                if ( !e.hasAttributeNS( namespace, "version" ) )
                {
                    throw new ModelError(
                        DefaultModelBundle.getInstance().
                        getMissingAttributeMessage(
                        Locale.getDefault(), "version", e.getLocalName() ) );

                }

                modelVersion = e.getAttributeNS( namespace, "version" );
            }
            else if ( MODULE_NS.equals( namespace ) )
            {
                deprecatedModel = true;
                namespace = CONTAINER_NS;

                if ( !e.hasAttributeNS( MODULE_NS, "modelVersion" ) )
                {
                    throw new ModelError(
                        DefaultModelBundle.getInstance().
                        getMissingAttributeMessage(
                        Locale.getDefault(), "modelVersion",
                        e.getLocalName() ) );

                }

                modelVersion = e.getAttributeNS( MODULE_NS, "modelVersion" );
            }
            else if ( CONTAINER_NS.equals( namespace ) )
            {
                deprecatedModel = false;

                if ( !e.hasAttributeNS( CONTAINER_NS, "modelVersion" ) )
                {
                    throw new ModelError(
                        DefaultModelBundle.getInstance().
                        getMissingAttributeMessage(
                        Locale.getDefault(), "modelVersion",
                        e.getLocalName() ) );

                }

                modelVersion = e.getAttributeNS( CONTAINER_NS, "modelVersion" );
            }
            else
            {
                throw new ModelError(
                    DefaultModelBundle.getInstance().
                    getUnknownNamespaceMessage(
                    Locale.getDefault(), namespace,
                    doc.getDocumentElement().getLocalName() ) );

            }

            boolean versionSupported = false;
            if ( modelVersion != null )
            {
                for ( int i = SUPPORTED_MODEL_VERSIONS.length - 1; i >= 0; i-- )
                {
                    if ( SUPPORTED_MODEL_VERSIONS[i].equals( modelVersion ) )
                    {
                        versionSupported = true;
                        break;
                    }
                }
            }

            if ( !versionSupported )
            {
                throw new ModelError(
                    DefaultModelBundle.getInstance().
                    getUnsupportedModelversionMessage(
                    Locale.getDefault(), modelVersion ) );

            }

            if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
            {
                if ( e.getLocalName().equals( "module" ) )
                {
                    final Module m = this.transformModule(
                        modelVersion, namespace, e, documentResource );

                    if ( m != null )
                    {
                        list.add( m );
                    }
                }
                else if ( e.getLocalName().equals( "modules" ) )
                {
                    final NodeList l = e.getElementsByTagNameNS( namespace,
                                                                 "module" );

                    for ( int i = l.getLength() - 1; i >= 0; i-- )
                    {
                        if ( l.item( i ).getParentNode().equals( e ) )
                        {
                            final Module m = this.transformModule(
                                modelVersion, namespace, (Element) l.item( i ),
                                documentResource );

                            if ( m != null )
                            {
                                list.add( m );
                            }
                        }
                    }
                }
                else
                {
                    throw new ModelError(
                        DefaultModelBundle.getInstance().
                        getUnsupportedElementMessage( Locale.getDefault(),
                                                      e.getLocalName() ) );

                }
            }
            else
            {
                final Module m = this.transformModule( modelVersion, namespace,
                                                       e, documentResource );

                if ( m != null )
                {
                    list.add( m );
                }
            }

            if ( deprecatedModel )
            {
                if ( LOGGER.isLoggable( Level.CONFIG ) )
                {
                    LOGGER.log( Level.CONFIG, DefaultModelBundle.getInstance().
                        getDeprecatedModelMessage( Locale.getDefault(),
                                                   namespace,
                                                   modelVersion ) );

                }
            }
        }

        // Merge any provided platform modules.
        Module providedPlatformModule = null;
        for ( Iterator it = list.iterator(); it.hasNext(); )
        {
            final Module module = (Module) it.next();
            if ( PLATFORM_MODULE_NAME.equals( module.getName() ) )
            {
                providedPlatformModule = module;
                it.remove();
                break;
            }
        }

        mods.setModules(
            (Module[]) list.toArray( new Module[ list.size() ] ) );

        this.modules = mods;

        if ( providedPlatformModule != null &&
             providedPlatformModule.getSpecifications() != null )
        {
            for ( int i = providedPlatformModule.getSpecifications().size() - 1;
                  i >= 0; i-- )
            {
                this.addPlatformSpecification(
                    providedPlatformModule.getSpecifications().
                    getSpecification( i ).getIdentifier(), true );

            }
        }

        return mods;
    }

    private Specifications transformSpecifications(
        final String modelVersion, final String namespace,
        final Element specificationsElement ) throws ParseException
    {
        final List list = new LinkedList();
        NodeList l =
            specificationsElement.getElementsByTagNameNS( namespace,
                                                          "specification" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().
                    equals( specificationsElement ) )
                {
                    list.add( this.transformSpecification(
                        modelVersion, namespace, (Element) l.item( i ) ) );

                }
            }
        }

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            l = specificationsElement.getElementsByTagNameNS( namespace,
                                                              "reference" );

            if ( l != null && l.getLength() > 0 )
            {
                for ( int i = l.getLength() - 1; i >= 0; i-- )
                {
                    if ( l.item( i ).getParentNode().
                        equals( specificationsElement ) )
                    {
                        list.add(
                            this.transformSpecificationReference(
                            namespace, (Element) l.item( i ) ) );

                    }
                }
            }
        }

        final Specifications specs = new Specifications();
        specs.setModelVersion( modelVersion );
        specs.setSpecifications( (Specification[]) list.toArray(
            new Specification[ list.size() ] ) );

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, specificationsElement, specs );
        }

        return specs;
    }

    private Implementations transformImplementations(
        final String modelVersion, final String namespace,
        final Module module, final Element implementationsElement )
        throws ParseException
    {
        final List list = new LinkedList();
        final NodeList l =
            implementationsElement.getElementsByTagNameNS( namespace,
                                                           "implementation" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().
                    equals( implementationsElement ) )
                {
                    list.add( this.transformImplementation(
                        modelVersion, namespace, module,
                        (Element) l.item( i ) ) );

                }
            }
        }

        final Implementations impls = new Implementations();
        impls.setModelVersion( modelVersion );
        impls.setImplementations( (Implementation[]) list.toArray(
            new Implementation[ list.size() ] ) );

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, implementationsElement,
                                       impls );

        }

        return impls;
    }

    private Properties transformProperties(
        final String modelVersion, final String namespace,
        final Element propertiesElement )
        throws ParseException
    {
        final Map transformed = new TreeMap();
        final NodeList l =
            propertiesElement.getElementsByTagNameNS( namespace, "property" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( propertiesElement ) )
                {
                    final Property p = this.transformProperty(
                        modelVersion, namespace, (Element) l.item( i ) );

                    if ( propertiesElement.getParentNode().getLocalName().
                        equals( "specification" ) )
                    {
                        p.setValue( null );
                        p.setApi( true );
                    }

                    transformed.put( p.getName(), p );
                }
            }
        }

        final Properties props = new Properties();
        props.setModelVersion( modelVersion );
        props.setProperties( (Property[]) transformed.values().toArray(
            new Property[ transformed.size() ] ) );

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, propertiesElement, props );
        }

        return props;
    }

    private Dependencies transformDependencies(
        final String modelVersion, final String namespace,
        final String implementationIdentifier,
        final Element dependenciesElement ) throws ParseException
    {
        final List list = new LinkedList();
        final NodeList l =
            dependenciesElement.getElementsByTagNameNS( namespace,
                                                        "dependency" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( dependenciesElement ) )
                {
                    list.add(
                        this.transformDependency( modelVersion, namespace,
                                                  implementationIdentifier,
                                                  (Element) l.item( i ) ) );

                }
            }
        }

        final Dependencies deps = new Dependencies();
        deps.setModelVersion( modelVersion );
        deps.setDependencies( (Dependency[]) list.toArray(
            new Dependency[ list.size() ] ) );

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, dependenciesElement, deps );
        }

        return deps;
    }

    private Messages transformMessages(
        final String modelVersion, final String namespace, final Module module,
        final Element messagesElement )
    {
        final List messages = new LinkedList();
        NodeList l = messagesElement.getElementsByTagNameNS( namespace,
                                                             "message" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( messagesElement ) )
                {
                    messages.add(
                        this.transformMessage( modelVersion, module.getName(),
                                               namespace,
                                               (Element) l.item( i ) ) );

                }
            }
        }

        l = messagesElement.getElementsByTagNameNS( namespace,
                                                    "reference" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( messagesElement ) )
                {
                    final String name =
                        ( (Element) l.item( i ) ).getAttributeNS(
                        namespace, "name" );

                    messages.add( module.getMessages().getMessage( name ) );
                }
            }
        }

        final Messages msgs = new Messages();
        msgs.setModelVersion( modelVersion );
        msgs.setMessages( (Message[]) messages.toArray(
            new Message[ messages.size() ] ) );

        this.transformModelObject( namespace, messagesElement, msgs );

        return msgs;
    }

    private Arguments transformArguments(
        final String modelVersion, final String namespace,
        final Element argumentsElement )
    {
        final List arguments = new LinkedList();
        final NodeList l =
            argumentsElement.getElementsByTagNameNS( namespace,
                                                     "argument" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( argumentsElement ) )
                {
                    arguments.add(
                        this.transformArgument( modelVersion, namespace,
                                                (Element) l.item( i ) ) );

                }
            }
        }

        Collections.sort( arguments, new Comparator()
        {

            public int compare( Object o1, Object o2 )
            {
                return ( (Argument) o1 ).getIndex() -
                       ( (Argument) o2 ).getIndex();

            }

        } );

        final Arguments args = new Arguments();
        args.setModelVersion( modelVersion );
        args.setArguments( (Argument[]) arguments.toArray(
            new Argument[ arguments.size() ] ) );

        this.transformModelObject( namespace, argumentsElement, args );

        return args;
    }

    private Module transformModule( final String modelVersion,
                                    final String namespace, final Element e,
                                    final URL documentResource )
        throws ParseException
    {
        NodeList l;
        Module module = new Module();
        module.setModelVersion( modelVersion );
        module.setName( e.getAttributeNS( namespace, "name" ) );
        module.setVersion( e.getAttributeNS( namespace, "version" ) );
        if ( e.hasAttributeNS( namespace, "description" ) )
        {
            final String txt = e.getAttributeNS( namespace, "description" );
            module.setDescription( txt );
        }

        l = e.getElementsByTagNameNS( namespace, "specifications" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( e ) )
                {
                    module.setSpecifications(
                        this.transformSpecifications(
                        modelVersion, namespace, ( (Element) l.item( i ) ) ) );

                    break;
                }
            }
        }

        l = e.getElementsByTagNameNS( namespace, "properties" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( e ) )
                {
                    module.setProperties(
                        this.transformProperties(
                        modelVersion, namespace, (Element) l.item( i ) ) );

                    break;
                }
            }
        }

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            l = e.getElementsByTagNameNS( namespace, "messages" );

            if ( l != null && l.getLength() > 0 )
            {
                for ( int i = l.getLength() - 1; i >= 0; i-- )
                {
                    if ( l.item( i ).getParentNode().equals( e ) )
                    {
                        module.setMessages(
                            this.transformMessages(
                            modelVersion, namespace, module,
                            (Element) l.item( i ) ) );

                        break;
                    }
                }
            }

            this.transformModelObject( namespace, e, module );
        }

        l = e.getElementsByTagNameNS( namespace, "implementations" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( e ) )
                {
                    module.setImplementations(
                        this.transformImplementations(
                        modelVersion, namespace, module,
                        ( (Element) l.item( i ) ) ) );

                    break;
                }
            }
        }

        if ( this.moduleMap.containsKey( module.getName() ) &&
             Beans.isDesignTime() )
        {
            if ( LOGGER.isLoggable( Level.CONFIG ) )
            {
                LOGGER.log( Level.CONFIG, DefaultModelBundle.getInstance().
                    getIgnoredModuleMessage(
                    Locale.getDefault(), documentResource.toExternalForm(),
                    ( (URL) this.moduleMap.get( module.getName() ) ).
                    toExternalForm() ) );

            }

            module = null;
        }
        else
        {
            this.moduleMap.put( module.getName(), documentResource );
        }

        return module;
    }

    private Specification transformSpecificationReference(
        final String namespace, final Element e )
    {
        String version = null;
        final String identifier = e.getAttributeNS( namespace, "identifier" );
        if ( e.hasAttributeNS( namespace, "version" ) )
        {
            version = e.getAttributeNS( namespace, "version" );
        }

        return this.getSpecification( identifier, version );
    }

    private Specification transformSpecification( final String modelVersion,
                                                  final String namespace,
                                                  final Element xml )
        throws ParseException
    {
        final Specification spec =
            this.transformSpecificationReference( namespace, xml );

        spec.setModelVersion( modelVersion );
        spec.setVendor( xml.getAttributeNS( namespace, "vendor" ) );
        if ( xml.hasAttributeNS( namespace, "description" ) )
        {
            final String txt = xml.getAttributeNS( namespace, "description" );
            spec.setDescription( txt );
        }

        if ( xml.hasAttributeNS( namespace, "singleton" ) )
        {
            spec.setSingleton(
                Boolean.valueOf( xml.getAttributeNS(
                namespace, "singleton" ) ).booleanValue() );

        }

        if ( VersionParser.compare( modelVersion, V_1_1 ) >= 0 )
        {
            final String multiplicity =
                xml.getAttributeNS( namespace, "multiplicity" );

            if ( "one".equalsIgnoreCase( multiplicity ) )
            {
                spec.setMultiplicity( Specification.MULTIPLICITY_ONE );
            }
            else if ( "many".equalsIgnoreCase( multiplicity ) )
            {
                spec.setMultiplicity( Specification.MULTIPLICITY_MANY );
            }
            else
            {
                throw new ModelError(
                    DefaultModelBundle.getInstance().
                    getUnsupportedMultiplicityMessage( Locale.getDefault(),
                                                       multiplicity ) );

            }
        }

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            if ( xml.hasAttributeNS( namespace, "stateless" ) )
            {
                spec.setStateless( Boolean.valueOf( xml.getAttributeNS(
                    namespace, "stateless" ) ).booleanValue() );

            }

            final String scope = xml.getAttributeNS( namespace, "scope" );
            if ( "multiton".equalsIgnoreCase( scope ) )
            {
                spec.setScope( Specification.SCOPE_MULTITON );
            }
            else if ( "context".equalsIgnoreCase( scope ) )
            {
                spec.setScope( Specification.SCOPE_CONTEXT );
            }
            else if ( "singleton".equalsIgnoreCase( scope ) )
            {
                spec.setScope( Specification.SCOPE_SINGLETON );
            }
            else
            {
                throw new ModelError(
                    DefaultModelBundle.getInstance().
                    getUnsupportedScopeMessage( Locale.getDefault(),
                                                spec.getIdentifier(), scope ) );

            }

            final NodeList l =
                xml.getElementsByTagNameNS( namespace, "properties" );

            if ( l != null && l.getLength() > 0 )
            {
                for ( int i = l.getLength() - 1; i >= 0; i-- )
                {
                    if ( l.item( i ).getParentNode().equals( xml ) )
                    {
                        spec.setProperties(
                            this.transformProperties(
                            modelVersion, namespace, (Element) l.item( i ) ) );

                        break;
                    }
                }
            }

            this.transformModelObject( namespace, xml, spec );
        }

        return spec;
    }

    private Implementation transformImplementation( final String modelVersion,
                                                    final String namespace,
                                                    final Module module,
                                                    final Element xml )
        throws ParseException
    {
        final String identifier = xml.getAttributeNS( namespace, "identifier" );
        final Implementation impl = this.getImplementation( identifier );
        impl.setModelVersion( modelVersion );
        impl.setModuleName( module.getName() );
        impl.setName( xml.getAttributeNS( namespace, "name" ) );
        impl.setVendor( xml.getAttributeNS( namespace, "vendor" ) );
        impl.setVersion( xml.getAttributeNS( namespace, "version" ) );

        if ( xml.hasAttributeNS( namespace, "parent" ) )
        {
            this.impl2parent.put( impl.getIdentifier(),
                                  xml.getAttributeNS( namespace, "parent" ) );

        }

        NodeList l = xml.getElementsByTagNameNS( namespace, "dependencies" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    impl.setDependencies(
                        this.transformDependencies(
                        modelVersion, namespace, identifier,
                        ( (Element) l.item( i ) ) ) );

                    break;
                }
            }
        }

        final String specificationsName =
            VersionParser.compare( modelVersion, V_1_3 ) >= 0
            ? "specifications"
            : "implementedSpecifications";

        l = xml.getElementsByTagNameNS( namespace, specificationsName );
        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
                    {
                        impl.setImplementedSpecifications(
                            this.transformSpecifications(
                            modelVersion, namespace, (Element) l.item( i ) ) );

                        break;
                    }
                    else
                    {
                        final Set set = new HashSet();
                        final NodeList deprecated =
                            ( (Element) l.item( i ) ).getElementsByTagNameNS(
                            namespace, "implementedSpecification" );

                        if ( deprecated != null && deprecated.getLength() > 0 )
                        {
                            for ( int d = deprecated.getLength() - 1; d >= 0;
                                  d-- )
                            {
                                if ( deprecated.item( d ).getParentNode().
                                    equals( l.item( i ) ) )
                                {
                                    set.add(
                                        this.transformSpecificationReference(
                                        namespace,
                                        (Element) deprecated.item( d ) ) );

                                }
                            }
                        }

                        final Specifications specs = new Specifications();
                        specs.setModelVersion( modelVersion );
                        specs.setSpecifications(
                            (Specification[]) set.toArray(
                            new Specification[ set.size() ] ) );

                        impl.setImplementedSpecifications( specs );
                        break;
                    }
                }
            }
        }

        l = xml.getElementsByTagNameNS( namespace, "properties" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    impl.setProperties(
                        this.transformProperties( modelVersion, namespace,
                                                  (Element) l.item( i ) ) );

                    break;
                }
            }
        }

        if ( VersionParser.compare( modelVersion, V_1_1 ) >= 0 &&
             xml.hasAttributeNS( namespace, "final" ) )
        {
            impl.setFinal( Boolean.valueOf( xml.getAttributeNS( namespace,
                                                                "final" ) ).
                booleanValue() );

        }

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            l = xml.getElementsByTagNameNS( namespace, "messages" );

            if ( l != null && l.getLength() > 0 )
            {
                for ( int i = l.getLength() - 1; i >= 0; i-- )
                {
                    if ( l.item( i ).getParentNode().equals( xml ) )
                    {
                        impl.setMessages( this.transformMessages(
                            modelVersion, namespace, module,
                            (Element) l.item( i ) ) );

                        break;
                    }
                }
            }

            this.transformModelObject( namespace, xml, impl );
        }

        return impl;
    }

    private Property transformProperty( final String modelVersion,
                                        final String namespace,
                                        final Element xml )
        throws ParseException
    {
        final Property prop = new Property();
        prop.setModelVersion( modelVersion );
        prop.setName( xml.getAttributeNS( namespace, "name" ) );

        if ( xml.hasAttributeNS( namespace, "api" ) )
        {
            final String api = xml.getAttributeNS( namespace, "api" );
            prop.setApi( Boolean.valueOf( api ).booleanValue() );
        }

        final String type = xml.hasAttributeNS( namespace, "type" )
                            ? xml.getAttributeNS( namespace, "type" )
                            : null;

        final String value = xml.hasAttributeNS( namespace, "value" )
                             ? xml.getAttributeNS( namespace, "value" )
                             : null;

        if ( type == null )
        {
            throw new ModelError(
                DefaultModelBundle.getInstance().
                getMissingPropertyTypeMessage(
                Locale.getDefault(), prop.getName(),
                xml.getParentNode().getParentNode().getLocalName() ) );

        }

        this.updatePropertyValue( type, value, prop );

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, xml, prop );
        }

        return prop;
    }

    private Dependency transformDependency(
        final String modelVersion, final String namespace,
        final String implementationIdentifier, final Element xml )
        throws ParseException
    {
        final Dependency dep = new Dependency();
        dep.setModelVersion( modelVersion );
        dep.setName( xml.getAttributeNS( namespace, "name" ) );

        if ( xml.hasAttributeNS( namespace, "bound" ) )
        {
            dep.setBound( Boolean.valueOf( xml.getAttributeNS( namespace,
                                                               "bound" ) ).
                booleanValue() );

        }

        if ( xml.hasAttributeNS( namespace, "implementationName" ) )
        {
            final String name = xml.getAttributeNS( namespace,
                                                    "implementationName" );

            final String key = implementationIdentifier + '/' +
                               dep.getName();

            this.dependencies.put( key, name );
        }

        final String specificationIdentifier =
            VersionParser.compare( modelVersion, V_1_3 ) < 0
            ? "specificationIdentifier"
            : "identifier";

        final String identifier = xml.getAttributeNS( namespace,
                                                      specificationIdentifier );

        if ( VersionParser.compare( modelVersion, V_1_3 ) < 0 )
        {
            dep.setSpecification( this.getSpecification( identifier, null ) );
        }
        else
        {
            String version = null;
            if ( xml.hasAttributeNS( namespace, "version" ) )
            {
                version = xml.getAttributeNS( namespace, "version" );
            }

            dep.setSpecification( this.getSpecification( identifier,
                                                         version ) );

        }

        final NodeList l =
            xml.getElementsByTagNameNS( namespace, "properties" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    dep.setProperties(
                        this.transformProperties(
                        modelVersion, namespace, (Element) l.item( i ) ) );

                    break;
                }
            }
        }

        if ( VersionParser.compare( modelVersion, V_1_3 ) >= 0 )
        {
            this.transformModelObject( namespace, xml, dep );
        }

        return dep;
    }

    private Text transformText( final String namespace,
                                final Element textsElement )
    {
        final Text text = new Text();
        final String defaultLanguage =
            textsElement.getAttributeNS( namespace, "defaultLanguage" );

        final NodeList l =
            textsElement.getElementsByTagNameNS( namespace, "text" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                final Element textElement = (Element) l.item( i );
                if ( textElement.getParentNode().equals( textsElement ) )
                {
                    final String language =
                        textElement.getAttributeNS( namespace, "language" );

                    final String value =
                        textElement.getFirstChild().getNodeValue();

                    final Locale locale = new Locale( language.toLowerCase() );
                    text.setValue( locale, value );

                    if ( language.equalsIgnoreCase( defaultLanguage ) )
                    {
                        text.setValue( value );
                    }
                }
            }
        }

        return text;
    }

    private void transformModelObject( final String namespace,
                                       final Element objectElement,
                                       final ModelObject modelObject )
    {
        final NodeList l = objectElement.getElementsByTagNameNS(
            namespace, "documentation" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( objectElement ) )
                {
                    modelObject.setDocumentation(
                        this.transformText( namespace,
                                            (Element) l.item( i ) ) );

                    break;
                }
            }
        }
    }

    private Message transformMessage( final String modelVersion,
                                      final String moduleName,
                                      final String namespace,
                                      final Element xml )
    {
        final Message msg = new Message();
        msg.setModelVersion( modelVersion );
        msg.setModuleName( moduleName );
        msg.setName( xml.getAttributeNS( namespace, "name" ) );

        NodeList l = xml.getElementsByTagNameNS( namespace, "template" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    final Text template = this.transformText(
                        namespace, (Element) l.item( i ) );

                    final Locale[] locales = template.getLocales();
                    for ( int t = locales.length - 1; t >= 0; t-- )
                    {
                        final String text = template.getValue( locales[t] );
                        try
                        {
                            new MessageFormat( text );
                        }
                        catch ( IllegalArgumentException e )
                        {
                            final ModelError modelError = new ModelError(
                                DefaultModelBundle.getInstance().
                                getIllegalTemplateMessage( Locale.getDefault(),
                                                           text, msg.getName(),
                                                           e.getMessage() ) );

                            modelError.initCause( e );
                            throw modelError;
                        }
                    }

                    if ( template.getValue() != null )
                    {
                        try
                        {
                            new MessageFormat( template.getValue() );
                        }
                        catch ( IllegalArgumentException e )
                        {
                            final ModelError modelError = new ModelError(
                                DefaultModelBundle.getInstance().
                                getIllegalTemplateMessage( Locale.getDefault(),
                                                           template.getValue(),
                                                           msg.getName(),
                                                           e.getMessage() ) );

                            modelError.initCause( e );
                            throw modelError;
                        }
                    }

                    msg.setTemplate( template );
                    break;
                }
            }
        }

        l = xml.getElementsByTagNameNS( namespace, "arguments" );

        if ( l != null && l.getLength() > 0 )
        {
            for ( int i = l.getLength() - 1; i >= 0; i-- )
            {
                if ( l.item( i ).getParentNode().equals( xml ) )
                {
                    msg.setArguments(
                        this.transformArguments(
                        modelVersion, namespace, (Element) l.item( i ) ) );

                    break;
                }
            }
        }

        this.transformModelObject( namespace, xml, msg );

        return msg;
    }

    private Argument transformArgument( final String modelVersion,
                                        final String namespace,
                                        final Element xml )
    {
        final Argument arg = new Argument();
        arg.setModelVersion( modelVersion );
        arg.setName( xml.getAttributeNS( namespace, "name" ) );
        arg.setIndex( Integer.valueOf( xml.getAttributeNS( namespace,
                                                           "index" ) ).
            intValue() );

        final String type = xml.getAttributeNS( namespace, "type" );
        if ( type.equalsIgnoreCase( "number" ) )
        {
            arg.setType( Argument.TYPE_NUMBER );
        }
        else if ( type.equalsIgnoreCase( "date" ) )
        {
            arg.setType( Argument.TYPE_DATE );
        }
        else if ( type.equalsIgnoreCase( "time" ) )
        {
            arg.setType( Argument.TYPE_TIME );
        }
        else if ( type.equalsIgnoreCase( "text" ) )
        {
            arg.setType( Argument.TYPE_TEXT );
        }
        else
        {
            throw new ModelError( DefaultModelBundle.getInstance().
                getUnsupportedArgumentTypeMessage(
                Locale.getDefault(), type ) );

        }

        this.transformModelObject( namespace, xml, arg );

        return arg;
    }

    /**
     * Gets the {@code Module} holding platform specifications.
     *
     * @return the {@code Module} holding platform specifications.
     */
    protected Module getPlatformModule()
    {
        if ( this.platformModule == null )
        {
            final String description =
                DefaultModelBundle.getInstance().
                getPlatformModuleDescriptionMessage( Locale.getDefault() );

            this.platformModule = new Module();
            this.platformModule.getDocumentation().setValue( description );
            this.platformModule.setName( PLATFORM_MODULE_NAME );
            this.platformModule.setVersion( PLATFORM_MODULE_VERSION );

            if ( LOGGER.isLoggable( Level.CONFIG ) )
            {
                LOGGER.log( Level.CONFIG, DefaultModelBundle.getInstance().
                    getAddedPlatformModuleMessage(
                    Locale.getDefault(),
                    this.platformModule.getDocumentation().getValue() ) );
            }
        }

        return this.platformModule;
    }

    /**
     * Checks an identifier to identify a valid platform specification.
     *
     * @param identifier the identifier to check.
     *
     * @return {@code true} if {@code identifier} identifies a valid platform
     * specification; {@code false} else.
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     */
    protected boolean isPlatformSpecification( final String identifier )
    {
        return this.addPlatformSpecification( identifier, false );
    }

    /**
     * Checks an identifier to identify a valid platform specification and
     * optionally adds a new {@code Specification} instance to the platform
     * module.
     *
     * @param identifier the identifier to check.
     * @param add {@code true} to add a new {@code Specification} instance to
     * the platform module; {@code false} to not update the platform module.
     *
     * @return {@code true} if {@code identifier} is an identifier of a valid
     * platform specification; {@code false} else.
     */
    private boolean addPlatformSpecification( final String identifier,
                                              final boolean add )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "identifier" );
        }

        boolean validPlatformSpec = false;

        try
        {
            final Class platformClass = ClassLoaderFactory.loadClass(
                this.getClass(), identifier );

            if ( Modifier.isPublic( platformClass.getModifiers() ) )
            {
                if ( platformClass.getPackage() != null )
                {
                    final String specVersion = platformClass.getPackage().
                        getSpecificationVersion();

                    String specVendor = platformClass.getPackage().
                        getSpecificationVendor();

                    if ( specVendor == null )
                    {
                        specVendor = DefaultModelBundle.getInstance().
                            getUnknownVendorMessage( Locale.getDefault() );

                    }

                    if ( specVersion != null )
                    {
                        validPlatformSpec = true;

                        if ( add )
                        {
                            // Add to the platform module.
                            try
                            {
                                final Specification platformSpec =
                                    this.getSpecification( identifier,
                                                           specVersion );

                                platformSpec.setModelVersion( MODEL_VERSION );
                                platformSpec.setDocumentation(
                                    this.getPlatformModule().
                                    getDocumentation() );

                                platformSpec.setIdentifier( identifier );
                                platformSpec.setModuleName(
                                    this.getPlatformModule().getName() );

                                platformSpec.setMultiplicity(
                                    Specification.MULTIPLICITY_MANY );

                                platformSpec.setScope(
                                    Specification.SCOPE_MULTITON );

                                platformSpec.setVendor( specVendor );

                                final Specification[] platformSpecs =
                                    this.getPlatformModule().
                                    getSpecifications().getSpecifications();

                                final Specification[] newPlatformSpecs =
                                    new Specification[ platformSpecs.length + 1 ];

                                System.arraycopy( platformSpecs, 0,
                                                  newPlatformSpecs, 0,
                                                  platformSpecs.length );

                                newPlatformSpecs[platformSpecs.length] =
                                    platformSpec;

                                final Specifications specs =
                                    new Specifications();

                                specs.setSpecifications( newPlatformSpecs );
                                this.getPlatformModule().
                                    setSpecifications( specs );

                                if ( LOGGER.isLoggable( Level.FINE ) )
                                {
                                    LOGGER.log( Level.FINE,
                                                DefaultModelBundle.getInstance().
                                        getAddedPlatformSpecificationMessage(
                                        Locale.getDefault(), identifier,
                                        specVersion,
                                        specVendor ) );

                                }

                                this.addDefaultImplementation( platformClass,
                                                               platformSpec );

                            }
                            catch ( DuplicateSpecificationException e )
                            {
                                // Specification already created.
                            }
                        }
                    }
                    else
                    {
                        LOGGER.log( Level.WARNING,
                                    DefaultModelBundle.getInstance().
                            getNoVersionAvailableMessage( Locale.getDefault(),
                                                          identifier ) );

                        validPlatformSpec = false;
                    }
                }
                else
                {
                    LOGGER.log( Level.WARNING, DefaultModelBundle.getInstance().
                        getNoPackageAvailableMessage( Locale.getDefault(),
                                                      identifier ) );

                    validPlatformSpec = false;
                }
            }
            else
            {
                LOGGER.log( Level.WARNING, DefaultModelBundle.getInstance().
                    getNotPublicMessage( Locale.getDefault(), identifier ) );

                validPlatformSpec = false;
            }
        }
        catch ( ClassNotFoundException ex )
        {
            LOGGER.log( Level.WARNING, DefaultModelBundle.getInstance().
                getClassNotFoundMessage( Locale.getDefault(), identifier ) );

            validPlatformSpec = false;
        }

        return validPlatformSpec;
    }

    private void addDefaultImplementation( final Class platformClass,
                                           final Specification platformSpec )
    {
        try
        {
            final Method accessor = platformClass.getDeclaredMethod(
                "getDefault", new Class[ 0 ] );

            if ( Modifier.isStatic( accessor.getModifiers() ) &&
                 accessor.getReturnType() == platformClass )
            {
                boolean overwritten = false;

                // Check for an overwritten implementation.
                for ( int i = this.modules.getImplementations().size() - 1;
                      i >= 0; i-- )
                {
                    final Implementation resourceImpl =
                        this.modules.getImplementations().
                        getImplementation( i );

                    if ( !"default".equals( resourceImpl.getName() ) )
                    {
                        continue;
                    }

                    for ( int s = resourceImpl.getImplementedSpecifications().
                        size() - 1; s >= 0; s-- )
                    {
                        final Specification implemented =
                            resourceImpl.getImplementedSpecifications().
                            getSpecification( s );

                        if ( implemented.getIdentifier().equals(
                            platformSpec.getIdentifier() ) )
                        {
                            if ( LOGGER.isLoggable( Level.FINE ) )
                            {
                                LOGGER.log( Level.FINE,
                                            DefaultModelBundle.getInstance().
                                    getOverwrittenDefaultImplementationMessage(
                                    Locale.getDefault(),
                                    platformSpec.getIdentifier(),
                                    platformSpec.getIdentifier(),
                                    resourceImpl.getIdentifier() ) );

                            }

                            overwritten = true;
                            break;
                        }
                    }
                }

                final String implVersion = platformClass.getPackage().
                    getImplementationVersion();

                String implVendor = platformClass.getPackage().
                    getImplementationVendor();

                if ( implVendor == null )
                {
                    implVendor = DefaultModelBundle.getInstance().
                        getUnknownVendorMessage( Locale.getDefault() );

                }

                if ( implVersion != null && !overwritten )
                {
                    final Implementation defaultImpl = this.getImplementation(
                        platformClass.getName() );

                    defaultImpl.setModelVersion( MODEL_VERSION );
                    defaultImpl.setVersion( implVersion );
                    defaultImpl.setVendor( implVendor );
                    defaultImpl.setDocumentation(
                        platformSpec.getDocumentation() );

                    defaultImpl.setFinal( true );
                    defaultImpl.setModuleName( PLATFORM_MODULE_NAME );
                    defaultImpl.setName( "default" );

                    final Specifications implemented = new Specifications();
                    implemented.setSpecifications( new Specification[]
                        {
                            platformSpec
                        } );

                    defaultImpl.setImplementedSpecifications( implemented );

                    final Implementation[] current = this.getPlatformModule().
                        getImplementations().getImplementations();

                    final Implementation[] tmp =
                        new Implementation[ current.length + 1 ];

                    System.arraycopy( current, 0, tmp, 0, current.length );
                    tmp[current.length] = defaultImpl;

                    final Implementations impls = new Implementations();
                    impls.setImplementations( tmp );

                    this.getPlatformModule().setImplementations( impls );

                    if ( LOGGER.isLoggable( Level.FINE ) )
                    {
                        LOGGER.log( Level.FINE, DefaultModelBundle.getInstance().
                            getAddedDefaultImplementationMessage(
                            Locale.getDefault(), defaultImpl.getIdentifier(),
                            platformSpec.getIdentifier(), implVersion,
                            implVendor ) );

                    }

                }
            }
        }
        catch ( NoSuchMethodException e )
        {
            // No static accessor.
        }
    }

    /**
     * Updates the value of a property.
     *
     * @param typeName the name of the type of {@code property}.
     * @param value the value of {@code property}.
     * @param property the property to update.
     */
    private void updatePropertyValue( final String typeName,
                                      final String value,
                                      final Property property )
    {
        final Class objectType;

        if ( typeName.equals( Boolean.TYPE.getName() ) )
        {
            property.setType( Boolean.TYPE );
            objectType = Boolean.class;
        }
        else if ( typeName.equals( Byte.TYPE.getName() ) )
        {
            property.setType( Byte.TYPE );
            objectType = Byte.class;
        }
        else if ( typeName.equals( Character.TYPE.getName() ) )
        {
            property.setType( Character.TYPE );
            objectType = Character.class;
        }
        else if ( typeName.equals( Double.TYPE.getName() ) )
        {
            property.setType( Double.TYPE );
            objectType = Double.class;
        }
        else if ( typeName.equals( Float.TYPE.getName() ) )
        {
            property.setType( Float.TYPE );
            objectType = Float.class;
        }
        else if ( typeName.equals( Integer.TYPE.getName() ) )
        {
            property.setType( Integer.TYPE );
            objectType = Integer.class;
        }
        else if ( typeName.equals( Long.TYPE.getName() ) )
        {
            property.setType( Long.TYPE );
            objectType = Long.class;
        }
        else if ( typeName.equals( Short.TYPE.getName() ) )
        {
            property.setType( Short.TYPE );
            objectType = Short.class;
        }
        else if ( typeName.equals( Boolean.class.getName() ) )
        {
            property.setType( Boolean.class );
            objectType = Boolean.class;
        }
        else if ( typeName.equals( Byte.class.getName() ) )
        {
            property.setType( Byte.class );
            objectType = Byte.class;
        }
        else if ( typeName.equals( Character.class.getName() ) )
        {
            property.setType( Character.class );
            objectType = Character.class;
        }
        else if ( typeName.equals( Double.class.getName() ) )
        {
            property.setType( Double.class );
            objectType = Double.class;
        }
        else if ( typeName.equals( Float.class.getName() ) )
        {
            property.setType( Float.class );
            objectType = Float.class;
        }
        else if ( typeName.equals( Integer.class.getName() ) )
        {
            property.setType( Integer.class );
            objectType = Integer.class;
        }
        else if ( typeName.equals( Long.class.getName() ) )
        {
            property.setType( Long.class );
            objectType = Long.class;
        }
        else if ( typeName.equals( Short.class.getName() ) )
        {
            property.setType( Short.class );
            objectType = Short.class;
        }
        else if ( typeName.equals( String.class.getName() ) )
        {
            property.setType( String.class );
            objectType = String.class;
        }
        else
        {
            throw new ModelError( DefaultModelBundle.getInstance().
                getUnsupportedPropertyTypeMessage( Locale.getDefault(),
                                                   typeName ) );

        }

        if ( value != null )
        {
            try
            {
                // Special handling for class Character which does not provide
                // a constructor taking a string.
                final Constructor ctor;
                final Object arg;

                if ( objectType == Character.class )
                {
                    ctor = objectType.getDeclaredConstructor( CTOR_ARGS_CHAR );
                    arg = new Character( value.charAt( 0 ) );
                }
                else
                {
                    ctor = objectType.getDeclaredConstructor( CTOR_ARGS_STRING );
                    arg = value;
                }

                property.setValue( ctor.newInstance( new Object[]
                    {
                        arg
                    } ) );

            }
            catch ( SecurityException e )
            {
                throw new ModelError( e );
            }
            catch ( NoSuchMethodException e )
            {
                throw new ModelError( e );
            }
            catch ( InvocationTargetException e )
            {
                final Throwable targetException = e.getTargetException();

                if ( targetException instanceof Error )
                {
                    throw (Error) targetException;
                }
                else if ( targetException instanceof RuntimeException )
                {
                    throw (RuntimeException) targetException;
                }
                else
                {
                    throw new ModelError( targetException == null
                                          ? e
                                          : targetException );

                }
            }
            catch ( IllegalAccessException e )
            {
                throw new ModelError( e );
            }
            catch ( InstantiationException e )
            {
                throw new ModelError( e );
            }
        }
        else
        {
            property.setValue( value );
        }
    }

    //------------------------------------------------------------DefaultModel--
}
