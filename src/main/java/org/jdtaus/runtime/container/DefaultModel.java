/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (C) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.runtime.container;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdtaus.common.container.ContainerError;
import org.jdtaus.common.container.Dependencies;
import org.jdtaus.common.container.Dependency;
import org.jdtaus.common.container.Implementation;
import org.jdtaus.common.container.Implementations;
import org.jdtaus.common.container.Model;
import org.jdtaus.common.container.Module;
import org.jdtaus.common.container.Modules;
import org.jdtaus.common.container.Properties;
import org.jdtaus.common.container.Property;
import org.jdtaus.common.container.Specification;
import org.jdtaus.common.container.Specifications;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Default {@code Model} implementation.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see <code>"META-INF/jdtaus/module.xml"</code>
 */
public class DefaultModel implements Model {

    //--Constants---------------------------------------------------------------

    /** JAXP configuration key to the Schema implementation attribute. */
    public static final String SCHEMA_LANGUAGE_KEY =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** JAXP Schema implementation to use. */
    public static final String SCHEMA_LANGUAGE =
        "http://www.w3.org/2001/XMLSchema";

    /** JAXP configuration key for setting the Schema source. */
    public static final String SCHEMA_SOURCE_KEY =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /** jDTAUS {@code Model} namespace URI. */
    public static final String MODEL_NS =
        "http://jdtaus.org/runtime/container/xml";

    /** Location of the XML files to load. */
    public static final String MODEL_LOCATION =
        "META-INF/jdtaus/module.xml";

    /** Location of the jdtaus.xsd schema. */
    public static final String MODEL_XSD =
        "org/jdtaus/runtime/container/xml/jdtaus-module.xsd";

    /** Version supported by this implementation. */
    public static final String SUPPORTED_VERSION = "1.0";

    /** String constructor arguments. */
    private static final Class[] CTOR_ARGS = { String.class };

    //---------------------------------------------------------------Constants--
    //--Constructors------------------------------------------------------------

    /** Protected default constructor. */
    protected DefaultModel() {
        try {
            this._modules = this.readModules();
        } catch (ParserConfigurationException e) {
            Logger.getLogger(DefaultModel.class.getName()).
                log(Level.SEVERE, e.getMessage(), e);

            throw new ContainerError(e);
        } catch (SAXException e) {
            Logger.getLogger(DefaultModel.class.getName()).
                log(Level.SEVERE, e.getMessage(), e);

            throw new ContainerError(e);
        } catch (IOException e) {
            Logger.getLogger(DefaultModel.class.getName()).
                log(Level.SEVERE, e.getMessage(), e);

            throw new ContainerError(e);
        }
    }

    //------------------------------------------------------------Constructors--
    //--Model-------------------------------------------------------------------

    /** Holds the loaded model. */
    private final Modules _modules;

    public Modules getModules() {
        return this._modules;
    }

    //-------------------------------------------------------------------Model--
    //--DefaultModel------------------------------------------------------------

    private Modules readModules() throws IOException,
        ParserConfigurationException, SAXException, ContainerError {

        Document doc;
        Iterator it;
        final Collection modules = new LinkedList();
        final Collection documents = new LinkedList();
        final DocumentBuilder xmlBuilder;
        final DocumentBuilderFactory xmlFactory =
            DocumentBuilderFactory.newInstance();

        final Enumeration resources = DefaultContainer.getClassLoader().
            getResources(DefaultModel.MODEL_LOCATION);

        xmlFactory.setNamespaceAware(true);
        try {
            xmlFactory.setValidating(true);
            xmlFactory.setAttribute(DefaultModel.SCHEMA_LANGUAGE_KEY,
                DefaultModel.SCHEMA_LANGUAGE);

            final URL schema = DefaultContainer.getClassLoader().getResource(
                DefaultModel.MODEL_XSD);


            xmlFactory.setAttribute(DefaultModel.SCHEMA_SOURCE_KEY,
                schema.openStream());

        } catch(IllegalArgumentException e) {
            Logger.getLogger(DefaultModel.class.getName()).
                log(Level.CONFIG, e.getMessage());

            Logger.getLogger(DefaultModel.class.getName()).log(Level.WARNING,
                DefaultModelBundle.getNoValidationWarningMessage(
                Locale.getDefault()).format(new Object[] { e.getMessage() }));

            xmlFactory.setValidating(false);
        }

        xmlBuilder = xmlFactory.newDocumentBuilder();
        xmlBuilder.setErrorHandler(new ErrorHandler() {
            public void warning(final SAXParseException e) {
                Logger.getLogger(DefaultModel.class.getName()).
                    log(Level.WARNING, e.getMessage(), e);

            }
            public void fatalError(final SAXParseException e) {
                Logger.getLogger(DefaultModel.class.getName()).
                    log(Level.SEVERE, e.getMessage(), e);

                throw new ContainerError(e);
            }
            public void error(final SAXParseException e) {
                Logger.getLogger(DefaultModel.class.getName()).
                    log(Level.WARNING, e.getMessage(), e);

            }
        });

        // Parse and validate all files from the classpath.
        while(resources.hasMoreElements()) {
            final URL url;
            InputStream stream = null;

            try {
                url = (URL) resources.nextElement();
                stream = url.openStream();
                doc = xmlBuilder.parse(stream);
                documents.add(doc);
            } finally {
                if(stream != null) {
                    stream.close();
                    stream = null;
                }
            }
        }

        return this.transformModules(documents);
    }

    private Modules transformModules(Collection xml) {
        int i;
        final Iterator it;
        final Modules mods = new Modules();
        final Module[] transformed = new Module[xml.size()];
        for(it = xml.iterator(), i = 0; it.hasNext(); i++) {
            transformed[i] = this.transformModule((Document) it.next());
        }

        mods.setModules(transformed);
        this.linkXMLModel(mods);
        return mods;
    }

    private Specifications transformSpecifications(final String moduleName,
        final NodeList xml) {

        final Specification[] transformed = new Specification[xml.getLength()];
        final Collection c = new ArrayList(xml.getLength());
        for(int i = xml.getLength() - 1; i >= 0; i--) {
            transformed[i] = this.transformSpecification(moduleName,
                (Element) xml.item(i));
        }

        final Specifications specs = new Specifications();
        specs.setSpecifications(transformed);
        return specs;
    }

    private Implementations transformImplementations(final String moduleName,
        final NodeList xml) {

        final Implementation[] transformed =
            new Implementation[xml.getLength()];

        final Collection c = new ArrayList(xml.getLength());
        for(int i = xml.getLength() - 1; i >= 0; i--) {
            transformed[i] = this.transformImplementation(moduleName,
                (Element) xml.item(i));

        }

        final Implementations impls = new Implementations();
        impls.setImplementations(transformed);
        return impls;
    }

    private Properties transformProperties(final NodeList xml) {
        final Property[] transformed = new Property[xml.getLength()];
        final Collection c = new ArrayList(xml.getLength());
        for(int i = xml.getLength() - 1; i >= 0; i--) {
            transformed[i] = this.transformProperty((Element) xml.item(i));
        }

        final Properties props = new Properties();
        props.setProperties(transformed);
        return props;
    }

    private Dependencies transformDependencies(final NodeList xml) {
        final Dependency[] transformed = new Dependency[xml.getLength()];
        final Collection c = new ArrayList(xml.getLength());
        for(int i = xml.getLength() - 1; i >= 0; i--) {
            transformed[i] = this.transformDependency((Element) xml.item(i));
        }

        final Dependencies deps = new Dependencies();
        deps.setDependencies(transformed);
        return deps;
    }

    private String[] transformImplementedSpecifications(final NodeList xml) {
        final String[] transformed = new String[xml.getLength()];
        final Collection c = new ArrayList(xml.getLength());
        for(int i = xml.getLength() - 1; i >= 0; i--) {
            transformed[i] = this.transformImplementedSpecification(
                (Element) xml.item(i));

        }

        return transformed;
    }

    private Module transformModule(Document xml) {
        NodeList l;
        final Module module = new Module();
        final Element e = xml.getDocumentElement();
        module.setName(e.getAttributeNS(DefaultModel.MODEL_NS, "name"));
        module.setDescription(e.getAttributeNS(DefaultModel.MODEL_NS,
            "description"));

        module.setVersion(e.getAttributeNS(DefaultModel.MODEL_NS, "version"));
        l = e.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "specifications");

        if(l != null && l.getLength() > 0) {
            module.setSpecifications(this.transformSpecifications(
                module.getName(), ((Element) l.item(0)).getElementsByTagNameNS(
                DefaultModel.MODEL_NS, "specification")));

        }

        l = e.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "implementations");

        if(l != null && l.getLength() > 0) {
            module.setImplementations(this.transformImplementations(
                module.getName(), ((Element) l.item(0)).getElementsByTagNameNS(
                DefaultModel.MODEL_NS, "implementation")));

        }

        l = e.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "properties");

        if(l != null && l.getLength() > 0) {
            // Find the module properties.
            for(int i = l.getLength() - 1; i >= 0; i--) {
                if("module".equals(l.item(i).getParentNode().getLocalName())) {
                    module.setProperties(this.transformProperties(
                        ((Element) l.item(i)).getElementsByTagNameNS(
                        DefaultModel.MODEL_NS, "properties")));

                    break;
                }
            }

        }

        if(module.getVersion() == null || !module.getVersion().
            equals(DefaultModel.SUPPORTED_VERSION)) {

            throw new ContainerError(module.getVersion() +
                " != " + DefaultModel.SUPPORTED_VERSION);

        }

        return module;
    }

    private Specification transformSpecification(final String moduleName,
        final Element xml) {

        String attr;
        final Specification spec = new Specification();
        spec.setDescription(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "description"));

        spec.setIdentifier(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "identifier"));

        spec.setVendor(xml.getAttributeNS(DefaultModel.MODEL_NS, "vendor"));
        spec.setVersion(xml.getAttributeNS(DefaultModel.MODEL_NS, "version"));

        spec.setModuleName(moduleName);
        attr = xml.getAttributeNS(DefaultModel.MODEL_NS, "singleton");
        spec.setSingleton(attr != null ?
            Boolean.valueOf(attr).booleanValue() : false);

        return spec;
    }

    private Implementation transformImplementation(final String moduleName,
        final Element xml) {

        NodeList l;
        final XMLImplementation impl = new XMLImplementation();
        impl.setIdentifier(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "identifier"));

        impl.setModuleName(moduleName);
        impl.setName(xml.getAttributeNS(DefaultModel.MODEL_NS, "name"));
        impl.setParentIdentifier(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "parent"));

        impl.setVendor(xml.getAttributeNS(DefaultModel.MODEL_NS, "vendor"));
        impl.setVersion(xml.getAttributeNS(DefaultModel.MODEL_NS, "version"));

        l = xml.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "dependencies");

        if(l != null && l.getLength() > 0) {
            impl.setDependencies(this.transformDependencies(
                ((Element) l.item(0)).getElementsByTagNameNS(
                DefaultModel.MODEL_NS, "dependency")));

        }

        l = xml.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "implementedSpecifications");

        if(l != null && l.getLength() > 0) {
            impl.setImplementedIdentifiers(
                this.transformImplementedSpecifications(((Element) l.item(0)).
                getElementsByTagNameNS(DefaultModel.MODEL_NS,
                "implementedSpecification")));

        }

        l = xml.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "properties");

        if(l != null && l.getLength() > 0) {
            // Find the implementation properties.
            for(int i = l.getLength() - 1; i >= 0; i--) {
                if("implementation".equals(l.item(i).getParentNode().
                    getLocalName())) {

                    impl.setProperties(this.transformProperties(
                        ((Element) l.item(i)).getElementsByTagNameNS(
                        DefaultModel.MODEL_NS, "property")));

                    break;
                }
            }
        }

        return impl;
    }

    private Property transformProperty(final Element xml) {
        String attr;
        String t;
        Class objectType = null;
        Class type = null;
        final Property prop = new Property();
        prop.setName(xml.getAttributeNS(DefaultModel.MODEL_NS, "name"));

        attr = xml.getAttributeNS(DefaultModel.MODEL_NS, "api");
        prop.setApi(attr != null ?
            Boolean.valueOf(attr).booleanValue() : false);

        t = xml.getAttributeNS(DefaultModel.MODEL_NS, "type");
        if(t.equals(Boolean.TYPE.getName())) {
            type = Boolean.TYPE;
            objectType = Boolean.class;
        } else if(t.equals(Byte.TYPE.getName())) {
            type = Byte.TYPE;
            objectType = Byte.class;
        } else if(t.equals(Character.TYPE.getName())) {
            type = Character.TYPE;
            objectType = Character.class;
        } else if(t.equals(Double.TYPE.getName())) {
            type = Double.TYPE;
            objectType = Double.class;
        } else if(t.equals(Float.TYPE.getName())) {
            type = Float.TYPE;
            objectType = Float.class;
        } else if(t.equals(Integer.TYPE.getName())) {
            type = Integer.TYPE;
            objectType = Integer.class;
        } else if(t.equals(Long.TYPE.getName())) {
            type = Long.TYPE;
            objectType = Long.class;
        } else if(t.equals(Short.TYPE.getName())) {
            type = Short.TYPE;
            objectType = Short.class;
        } else if(t.equals(Boolean.class.getName())) {
            type = objectType = Boolean.class;
        } else if(t.equals(Byte.class.getName())) {
            type = objectType = Byte.class;
        } else if(t.equals(Character.class.getName())) {
            type = objectType = Character.class;
        } else if(t.equals(Double.class.getName())) {
            type = objectType = Double.class;
        } else if(t.equals(Float.class.getName())) {
            type = objectType = Float.class;
        } else if(t.equals(Integer.class.getName())) {
            type = objectType = Integer.class;
        } else if(t.equals(Long.class.getName())) {
            type = objectType = Long.class;
        } else if(t.equals(Short.class.getName())) {
            type = objectType = Short.class;
        } else if(t.equals(String.class.getName())) {
            type = objectType = String.class;
        } else {
            throw new ContainerError(t);
        }
        prop.setType(type);

        attr = xml.getAttributeNS(DefaultModel.MODEL_NS, "value");
        try {
            final Constructor ctor = objectType.
                getDeclaredConstructor(DefaultModel.CTOR_ARGS);

            prop.setValue(ctor.newInstance(new Object[] { attr }));
        } catch (SecurityException e) {
            throw new ContainerError(e);
        } catch (NoSuchMethodException e) {
            throw new ContainerError(e);
        } catch (InvocationTargetException e) {
            throw new ContainerError(e.getTargetException() == null ?
                e : e.getTargetException());

        } catch (IllegalAccessException e) {
            throw new ContainerError(e);
        } catch (InstantiationException e) {
            throw new ContainerError(e);
        }

        return prop;
    }

    private Dependency transformDependency(final Element xml) {
        String attr;
        NodeList l;
        final XMLDependency dep = new XMLDependency();
        dep.setName(xml.getAttributeNS(DefaultModel.MODEL_NS, "name"));
        attr = xml.getAttributeNS(DefaultModel.MODEL_NS, "bound");
        dep.setBound(attr != null ?
            Boolean.valueOf(attr).booleanValue() : false);

        dep.setImplementationName(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "implementationName"));

        dep.setSpecificationIdentifier(xml.getAttributeNS(DefaultModel.MODEL_NS,
            "specificationIdentifier"));

        l = xml.getElementsByTagNameNS(DefaultModel.MODEL_NS,
            "properties");

        if(l != null && l.getLength() > 0) {
            dep.setProperties(this.transformProperties(
                ((Element) l.item(0)).getElementsByTagNameNS(
                DefaultModel.MODEL_NS, "property")));

        }

        return dep;
    }

    private String transformImplementedSpecification(final Element xml) {
        return xml.getAttributeNS(DefaultModel.MODEL_NS, "identifier");
    }

    protected void linkXMLModel(final Modules modules) {
        if(modules == null) {
            throw new NullPointerException("modules");
        }

        Collection col;
        Dependencies deps;
        Implementations impls;
        Iterator it;
        Map map;
        Specification spec;
        Specifications specs;
        XMLDependency dep;
        XMLImplementation impl;
        XMLImplementation parent;
        final Map specsToImpls = new HashMap();

        // Link implementations.
        impls = modules.getImplementations();
        for(int i = impls.size() - 1; i >= 0; i--) {
            impl = (XMLImplementation) impls.getImplementation(i);
            // Build specification mapping.
            final String[] implemented = impl.getImplementedIdentifiers();
            specs = new Specifications();
            col = new LinkedList();
            for(int j = implemented.length - 1; j >= 0; j--) {
                if((map = (Map) specsToImpls.get(implemented[j])) == null) {
                    map = new HashMap();
                    specsToImpls.put(implemented[j], map);
                }

                if(map.put(impl.getName(), impl) != null) {
                    throw new ContainerError("implementation:" +
                        impl.getIdentifier());

                }

                col.add(modules.getSpecifications().
                    getSpecification(implemented[j]));

            }

            specs.setSpecifications((Specification[]) col.
                toArray(new Specification[col.size()]));

            impl.setImplementedSpecifications(specs);

            if(impl.getParentIdentifier() != null &&
                impl.getParentIdentifier().length() > 0) {

                parent = (XMLImplementation) impls.
                    getImplementation(impl.getParentIdentifier());

                if(parent == null) {
                    throw new ContainerError("parent: " +
                        impl.getParentIdentifier());

                }

                impl.setParent(parent);
            } else {
                impl.setParent(null);
            }
        }

        // Link specifications.
        specs = modules.getSpecifications();
        for(int i = specs.size() - 1; i >= 0; i--) {
            col = new LinkedList();
            spec = specs.getSpecification(i);
            impls = new Implementations();
            map = (Map) specsToImpls.get(spec.getIdentifier());
            if(map != null) {
                for(it = map.keySet().iterator(); it.hasNext();) {
                    col.add((XMLImplementation) map.get(it.next()));
                }

                impls.setImplementations((Implementation[]) col.
                    toArray(new Implementation[col.size()]));

            }
            spec.setImplementations(impls);
        }

        // Link dependencies.
        impls = modules.getImplementations();
        for(int i = impls.size() - 1; i >= 0; i--) {
            impl = (XMLImplementation) impls.getImplementation(i);
            deps = impl.getDependencies();
            for(int j = deps.size() - 1; j >= 0; j--) {
                dep = (XMLDependency) deps.getDependency(j);
                spec = modules.getSpecifications().
                    getSpecification(dep.getSpecificationIdentifier());

                if(spec == null) {
                    throw new ContainerError("specificationIdentifier: " +
                        dep.getSpecificationIdentifier());

                } else {
                    dep.setSpecification(spec);
                }

                impl = (XMLImplementation) spec.
                    getImplementation(dep.getImplementationName());

                if(impl != null) {
                    dep.setImplementation(impl);
                }
            }
        }
    }

    //------------------------------------------------------------DefaultModel--

}
