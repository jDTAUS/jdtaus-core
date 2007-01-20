/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (C) 2005 - 2007 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.mojo.container;

import java.io.File;
import java.text.MessageFormat;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdtaus.common.container.Dependencies;
import org.jdtaus.common.container.Dependency;
import org.jdtaus.common.container.Implementation;
import org.jdtaus.common.container.Implementations;
import org.jdtaus.common.container.ModelFactory;
import org.jdtaus.common.container.Module;
import org.jdtaus.common.container.Properties;
import org.jdtaus.common.container.Property;
import org.jdtaus.common.container.Specification;
import org.jdtaus.common.container.Specifications;

/**
 * Mojo to generate infrastructure code.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @goal generate-container
 * @phase process-resources
 * @requiresDependencyResolution runtime
 */
public class ContainerMojo extends AbstractSourceMojo {

    //--Configuration-----------------------------------------------------------

    /**
     * The name of the module to process.
     * @parameter expression="${moduleName}" default-value="${pom.name}"
     */
    private String moduleName;

    /**
     * The string marking the starting line where to start inserting specification
     * code.
     * @parameter expression="${specificationsStartingMarker}" default-value="//--Specification-----------------------------------------------------------"
     */
    protected String specificationsStartingMarker;

    /**
     * The string marking the ending line up to where specification code should
     * be inserted.
     * @parameter expression="${specificationsEndingMarker}" default-value="//-----------------------------------------------------------Specification--"
     */
    protected String specificationsEndingMarker;

    /**
     * The string marking the starting line where to start inserting dependency
     * code.
     * @parameter expression="${dependenciesStartingMarker}" default-value="//--Dependencies------------------------------------------------------------"
     */
    protected String dependenciesStartingMarker;

    /**
     * The string marking the starting line where to start inserting implementation
     * code.
     * @parameter expression="${implementationsStartingMarker}" default-value="//--Implementation----------------------------------------------------------"
     */
    protected String implementationsStartingMarker;

    /**
     * The string marking the ending line up to where implementation code should
     * be inserted.
     * @parameter expression="${implementationsEndingMarker}" default-value="//----------------------------------------------------------Implementation--"
     */
    protected String implementationsEndingMarker;

    /**
     * The string marking the starting line where to start inserting constructor
     * code.
     * @parameter expression="${constructorsStartingMarker}" default-value="//--Constructors------------------------------------------------------------"
     */
    protected String constructorsStartingMarker;

    /**
     * The string marking the ending line up to where constructor code should be
     * inserted.
     * @parameter expression="${constructorsEndingMarker}" default-value="//------------------------------------------------------------Constructors--"
     */
    protected String constructorsEndingMarker;

    /**
     * The string marking the ending line up to where dependency code should be
     * inserted.
     * @parameter expression="${dependenciesEndingMarker}" default-value="//------------------------------------------------------------Dependencies--"
     */
    protected String dependenciesEndingMarker;

    /**
     * The string marking the starting line where to start inserting
     * implementation property code.
     * @parameter expression="${propertiesStartingMarker}" default-value="//--Properties--------------------------------------------------------------"
     */
    protected String propertiesStartingMarker;

    /**
     * The string marking the ending line up to where implementation property
     * code should be inserted.
     * @parameter expression="${propertiesEndingMarker}" default-value="//--------------------------------------------------------------Properties--"
     */
    protected String propertiesEndingMarker;

    /**
     * Accessor to the currently executed jDTAUS module.
     *
     * @return the currently executed jDTAUS module.
     */
    protected final Module getModule() {
        return this.moduleName != null ? ModelFactory.newModel().getModules().
            getModule(this.moduleName) : null;

    }

    //-----------------------------------------------------------Configuration--
    //--AbstractMojo------------------------------------------------------------

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException, MojoFailureException {
        int i;
        Implementations impls;
        Specifications specs;

        // All runtime dependencies are available so all required modules should
        // also be available.
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        Thread.currentThread().
            setContextClassLoader(this.getRuntimeClassLoader());

        final Module mod = this.getModule();

        if(mod != null) {
            specs = mod.getSpecifications();
            impls = mod.getImplementations();

            for(i = specs.size() - 1; i >= 0; i--) {
                this.generateSpecification(specs.getSpecification(i));
            }

            for(i = impls.size() - 1; i >= 0; i--) {
                this.generateImplementation(impls.getImplementation(i));
            }

        }

        Thread.currentThread().setContextClassLoader(mavenLoader);
    }

    //------------------------------------------------------------AbstractMojo--
    //--ContainerMojo-----------------------------------------------------------

    /** Adds the SPEC constant to specifications. */
    public class SpecificationEditor implements
        AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final Specification spec;

        public SpecificationEditor(final String fileName,
            final Specification spec) {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(spec == null) {
                throw new NullPointerException("spec");
            }

            this.fileName = fileName;
            this.spec = spec;
        }

        public String editLine(final String line) throws MojoFailureException {
            String replacement = null; // Replace with nothing.

            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            if(line != null &&
                specificationsStartingMarker.equals(line.trim())) {

                final StringBuffer buf = new StringBuffer(1024);
                final String specType =
                    ContainerMojo.getTypeFromClassName(spec.getIdentifier());

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                this.editing = true;
                this.modified = true;

                buf.append(line).append("\n\n");
                indent(buf);
                buf.append(warning.format(null));

                buf.append("\n\n");

                // Generate specification code.
                indent(buf);
                buf.append(ContainerMojoBundle.
                    getSpecificationMetaDataCommentText(getLocale())).
                    append('\n');

                indent(buf);
                buf.append("public static final String SPEC = ").
                    append(specType).append(".class.getName();\n");

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(specificationsEndingMarker.equals(line.trim())) {
                        this.editing = false;
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified() {
            return this.modified;
        }

    }

    /** Adds the IMPL constant to implementations. */
    public class ImplementationEditor implements
        AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final Implementation impl;

        public ImplementationEditor(final String fileName,
            final Implementation impl) {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(impl == null) {
                throw new NullPointerException("impl");
            }

            this.fileName = fileName;
            this.impl = impl;
        }

        public String editLine(final String line) throws MojoFailureException {
            String replacement = null; // Replace with nothing.

            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            if(line != null &&
                implementationsStartingMarker.equals(line.trim())) {

                // Skip all input up to the ending marker.
                this.editing = true;
                this.modified = true;
                final StringBuffer buf = new StringBuffer(1024);
                final String implType = ContainerMojo.getTypeFromClassName(
                    this.impl.getIdentifier());

                buf.append(line).append("\n\n");

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                indent(buf);
                buf.append(warning.format(null));
                buf.append("\n\n");

                // Generate implementation name constant.
                indent(buf);
                buf.append(ContainerMojoBundle.
                    getImplementationMetaDataCommentText(getLocale())).
                    append('\n');

                indent(buf);
                buf.append("private static final Implementation IMPL =\n");

                indent(buf);
                indent(buf);
                buf.append("ModelFactory.getModel().getModules().\n");

                indent(buf);
                indent(buf);
                buf.append("getImplementation(").append(implType).
                    append(".class.getName());\n");

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(implementationsEndingMarker.equals(line.trim())) {
                        this.editing = false;
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified() {
            return this.modified;
        }

    }

    /** Adds dependency getters to an implementation. */
    public class DependencyEditor implements AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final Implementation impl;
        private final boolean markersNeeded;

        public DependencyEditor(final String fileName,
            final Implementation impl) {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(impl == null) {
                throw new NullPointerException("impl");
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getDependencies().size() > 0;
        }

        public String editLine(final String line) throws MojoFailureException {
            int i;
            Dependency dep;
            Dependencies deps;
            String depType;
            String depName;
            String str;
            char[] c;
            String replacement = null; // Replace with nothing.
            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            if(line != null && dependenciesStartingMarker.equals(line.trim())) {
                // Skip all input up to the ending marker.
                this.editing = true;
                final StringBuffer buf = new StringBuffer(1024);
                buf.append(line).append("\n\n");

                String getterTemplate = ContainerMojoBundle.
                    getDependencyGetterText(getLocale());

                final String implType = ContainerMojo.
                    getTypeFromClassName(this.impl.getIdentifier());

                getterTemplate = getterTemplate.replaceAll("\\{3\\}", implType);

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                indent(buf);
                buf.append(warning.format(null));
                buf.append("\n\n");

                // Generate dependency getter.
                if(this.isMarkersNeeded()) {
                    this.modified = true;
                    deps = this.impl.getDependencies();
                    for(i = deps.size() - 1; i >= 0; i--) {
                        dep = deps.getDependency(i);
                        depName = dep.getName();
                        c = depName.toCharArray();
                        if(Character.isLowerCase(c[0])) {
                            c[0] = Character.toUpperCase(c[0]);
                        }
                        depName = String.valueOf(c);

                        depType = ContainerMojo.getTypeFromClassName(
                            dep.getSpecification().getIdentifier());

                        str = getterTemplate.replaceAll("\\{0\\}", depName);
                        str = str.replaceAll("\\{1\\}", depType);
                        str = str.replaceAll("\\{2\\}", String.valueOf(i));
                        indent(buf);
                        buf.append(str);
                    }
                }

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(dependenciesEndingMarker.equals(line.trim())) {
                        this.editing = false;
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified() {
            return this.modified;
        }

        public boolean isMarkersNeeded() {
            return this.markersNeeded;
        }
    }

    /** Adds property getters to an implementation. */
    public class PropertyEditor implements AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final Implementation impl;
        private final boolean markersNeeded;

        public PropertyEditor(final String fileName,
            final Implementation impl) {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(impl == null) {
                throw new NullPointerException("impl");
            }

            this.fileName = fileName;
            this.impl = impl;
            this.markersNeeded = impl.getProperties().size() > 0;
        }

        public String editLine(final String line) throws MojoFailureException {
            Property prop;
            Properties props;
            String replacement = null; // Replace with nothing.

            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            if(line != null && propertiesStartingMarker.equals(line.trim())) {
                // Skip all input up to the ending marker.
                this.editing = true;
                final StringBuffer buf = new StringBuffer(1024);
                buf.append(line).append("\n\n");

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                indent(buf);
                buf.append(warning.format(null));
                buf.append("\n\n");

                // Generate property getters and setters.
                if(this.isMarkersNeeded()) {
                    this.modified = true;
                    props = this.impl.getProperties();
                    for(int i = props.size() - 1; i >= 0; i--) {
                        final char[] c;
                        final String name;

                        prop = props.getProperty(i);

                        c = prop.getName().toCharArray();
                        if(Character.isLowerCase(c[0])) {
                            c[0] = Character.toUpperCase(c[0]);
                        }
                        name = String.valueOf(c);

                        // Field.
                        indent(buf);
                        buf.append("/**\n");
                        indent(buf);
                        buf.append(" * Property {@code ").
                            append(prop.getName()).append("}.\n");
                        indent(buf);
                        buf.append(" * @serial\n");
                        indent(buf);
                        buf.append(" */\n");

                        indent(buf);
                        buf.append("private ").
                            append(prop.getType().getName()).
                            append(" _").append(prop.getName()).
                            append(";\n\n");

                        // Getter.
                        indent(buf);
                        buf.append(ContainerMojoBundle.
                            getPropertyGetterCommentMessage(getLocale()).format(
                            new Object[] { prop.getName() })).append('\n');

                        indent(buf);
                        buf.append(prop.isApi() ? "public " : "protected ");

                        buf.append(prop.getType().getName()).
                            append(prop.getType() == Boolean.TYPE ||
                            prop.getType() == Boolean.class ? " is" : " get").
                            append(name).append("() {\n");

                        indent(buf);
                        indent(buf);
                        buf.append("return this._").append(prop.getName()).
                            append(";\n");

                        indent(buf);
                        buf.append("}\n\n");
                    }
                }

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(propertiesEndingMarker.equals(line.trim())) {
                        this.editing = false;
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified() {
            return this.modified;
        }

        public boolean isMarkersNeeded() {
            return this.markersNeeded;
        }
    }

    /** Adds implementation constructors. */
    public class ConstructorsEditor implements AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final Implementation impl;

        public ConstructorsEditor(final String fileName,
            final Implementation impl) throws MojoFailureException {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(impl == null) {
                throw new NullPointerException("impl");
            }

            this.fileName = fileName;
            this.impl = impl;
        }

        public String getPropertyInitializer(final Property property,
            final boolean isLastProperty) {

            if(property == null) {
                throw new NullPointerException("property");
            }

            final StringBuffer buf = new StringBuffer(255);
            final boolean isPrimitive = property.getType().isPrimitive();

            indent(buf);
            indent(buf);
            buf.append("this._").append(property.getName()).append(" = ").
                append("(").append(isPrimitive ? "(" : "").
                append(property.getValue().getClass().getName()).
                append(") meta.getProperties().\n");


            indent(buf);
            indent(buf);
            indent(buf);
            buf.append("getProperty(\"").append(property.getName()).
                append("\").getValue()");

            buf.append(isPrimitive ? ")." +
                property.getType().getName() + "Value();\n\n" : ";\n\n");

            if(!isLastProperty) {
                buf.append('\n');
            }

            return buf.toString();
        }

        public String editLine(final String line) throws MojoFailureException {
            String replacement = null; // Replace with nothing.

            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            if(line != null &&
                constructorsStartingMarker.equals(line.trim())) {

                Property property;
                Properties properties;

                // Skip all input up to the ending marker.
                this.editing = true;
                this.modified = true;
                final StringBuffer buf = new StringBuffer(1024);
                final String implType = ContainerMojo.getTypeFromClassName(
                    this.impl.getIdentifier());

                properties = this.impl.getProperties();
                final boolean hasProperties = properties.size() > 0;

                buf.append(line).append("\n\n");

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                indent(buf);
                buf.append(warning.format(null));
                buf.append("\n\n");

                indent(buf);
                buf.append(ContainerMojoBundle.
                    getConstructorCommentMessage(getLocale()).format(
                    new Object[] { implType })).append('\n');

                indent(buf);
                buf.append("protected ").append(implType).
                    append("(final Implementation meta) {\n");

                indent(buf);
                indent(buf);
                buf.append(this.impl.getParent() == null ?
                    "super();\n" : "super(meta);\n");

                if(hasProperties) {
                    for(int i = properties.size() - 1; i >= 0; i--) {
                        property = properties.getProperty(i);
                        buf.append(this.getPropertyInitializer(property,
                            i - 1 < 0));

                    }

                    indent(buf);
                    indent(buf);
                    buf.append("this.assertValidProperties();\n");
                }

                /*
                if(ContainerInitializer.class.
                    isAssignableFrom(this.implClass)) {

                    indent(buf);
                    indent(buf);
                    buf.append("this.initialize();\n");
                }

                if(ContextInitializer.class.
                    isAssignableFrom(this.implClass)) {

                    indent(buf);
                    indent(buf);
                    buf.append("this.initializeContext(ContextFactory.\n");
                    indent(buf);
                    indent(buf);
                    indent(buf);
                    buf.append("getContext());\n");
                }*/

                indent(buf);
                buf.append("}\n");

                indent(buf);
                buf.append(ContainerMojoBundle.
                    getDependencyConstructorCommentMessage(getLocale()).format(
                    new Object[] { implType })).append('\n');

                indent(buf);
                buf.append("protected ").append(implType).
                    append("(final Dependency meta) {\n");

                indent(buf);
                indent(buf);
                buf.append(this.impl.getParent() == null ?
                    "super();\n" : "super(meta);\n");

                if(hasProperties) {
                    for(int i = properties.size() - 1; i >= 0; i--) {
                        property = properties.getProperty(i);
                        buf.append(this.getPropertyInitializer(property,
                            i - 1 < 0));

                    }

                    indent(buf);
                    indent(buf);
                    buf.append("this.assertValidProperties();\n");
                }

                /*
                if(ContainerInitializer.class.
                    isAssignableFrom(this.implClass)) {

                    indent(buf);
                    indent(buf);
                    buf.append("this.initialize();n");
                }

                if(ContextInitializer.class.
                    isAssignableFrom(this.implClass)) {

                    indent(buf);
                    indent(buf);
                    buf.append("this.initializeContext(ContextFactory.\n");
                    indent(buf);
                    indent(buf);
                    indent(buf);
                    buf.append("getContext());n");
                }*/

                indent(buf);
                buf.append("}\n");

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(constructorsEndingMarker.equals(line.trim())) {
                        this.editing = false;
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;
        }

        public boolean isModified() {
            return this.modified;
        }

    }

    /** Cleans a section. */
    public class RemovingEditor implements AbstractSourceMojo.SourceEditor {

        private boolean editing = false;
        private boolean modified = false;
        private final String fileName;
        private final String startingMarker;
        private final String endingMarker;

        public RemovingEditor(final String fileName,
            final String startingMarker, final String endingMarker) {

            if(fileName == null) {
                throw new NullPointerException("fileName");
            }
            if(startingMarker == null) {
                throw new NullPointerException("startingMarker");
            }
            if(endingMarker == null) {
                throw new NullPointerException("endingMarker");
            }

            this.fileName = fileName;
            this.startingMarker = startingMarker;
            this.endingMarker = endingMarker;
        }

        public String editLine(String line) throws MojoFailureException {
            if(line == null && this.editing) {
                final MessageFormat fmt = ContainerMojoBundle.
                    getUnexpectedEndOfInputMessage(getLocale());

                throw new MojoFailureException(
                    fmt.format(new Object[] { this.fileName }));

            }

            String replacement = null; // Replace with nothing.
            if(line != null && this.startingMarker.equals(line.trim())) {
                // Skip all input up to the ending marker.
                this.editing = true;
                this.modified = true;
                // Leave the marker.
                final StringBuffer buf = new StringBuffer(1024);
                buf.append(line).append("\n\n");

                final MessageFormat warning = ContainerMojoBundle.
                    getGeneratorWarningMessage(getLocale());

                indent(buf);
                buf.append(warning.format(null));
                buf.append("\n\n");

                replacement = buf.toString();
            } else {
                if(this.editing) {
                    if(this.endingMarker.equals(line.trim())) {
                        // Stop editing.
                        this.editing = false;
                        // Leave the ending marker.
                        replacement = line;
                    }
                } else {
                    replacement = line;
                }
            }

            return replacement;

        }

        public boolean isModified() {
            return this.modified;
        }

    }

    protected static String getTypeFromClassName(final String className) {
        if(className == null) {
            throw new NullPointerException("className");
        }

        return className.substring(className.lastIndexOf('.') + 1);
    }

    protected void generateImplementation(final Implementation impl) throws
        MojoExecutionException, MojoFailureException {

        if(impl == null) {
            throw new NullPointerException("impl");
        }

        final MessageFormat fmt = ContainerMojoBundle.
            getMissingMarkersMessage(getLocale());

        final File source = this.getSource(impl.getIdentifier());
        final String path = source.getAbsolutePath();
        final ImplementationEditor implEditor =
            new ImplementationEditor(path, impl);

        final DependencyEditor depEditor =
            new DependencyEditor(path, impl);

        final PropertyEditor propEditor =
            new PropertyEditor(path, impl);

        final ConstructorsEditor ctorsEditor =
            new ConstructorsEditor(path, impl);

        // Empty all sections.
        this.editFile(source, new RemovingEditor(path,
            this.implementationsStartingMarker,
            this.implementationsEndingMarker));

        this.editFile(source, new RemovingEditor(path,
            this.dependenciesStartingMarker,
            this.dependenciesEndingMarker));

        this.editFile(source, new RemovingEditor(path,
            this.propertiesStartingMarker,
            this.propertiesEndingMarker));

        this.editFile(source, new RemovingEditor(path,
            this.constructorsStartingMarker,
            this.constructorsEndingMarker));

        // Generate the code.
        this.editFile(source, implEditor);
        if(!implEditor.isModified()) {
            throw new MojoExecutionException(fmt.format(new Object[] {
                this.implementationsStartingMarker, path
            }));
        }

        this.editFile(source, depEditor);
        if(depEditor.isMarkersNeeded() && !depEditor.isModified()) {
            throw new MojoExecutionException(fmt.format(new Object[] {
                this.dependenciesStartingMarker, path
            }));
        }

        this.editFile(source, propEditor);
        if(propEditor.isMarkersNeeded() && !propEditor.isModified()) {
            throw new MojoExecutionException(fmt.format(new Object[] {
                this.propertiesStartingMarker, path
            }));
        }

        this.editFile(source, ctorsEditor);
        if(!ctorsEditor.isModified()) {
            throw new MojoExecutionException(fmt.format(new Object[] {
                this.constructorsStartingMarker, path
            }));
        }
    }

    protected void generateSpecification(final Specification spec) throws
        MojoExecutionException, MojoFailureException {

        if(spec == null) {
            throw new NullPointerException("spec");
        }

        final MessageFormat fmt = ContainerMojoBundle.
            getMissingMarkersMessage(getLocale());

        final File source = this.getSource(spec.getIdentifier());
        final String path = source.getAbsolutePath();
        final SpecificationEditor specEditor =
            new SpecificationEditor(path, spec);

        // Empty all sections.
        this.editFile(source, new RemovingEditor(path,
            this.specificationsStartingMarker,
            this.specificationsEndingMarker));

        /* Generate code.
        this.editFile(source, specEditor);
        if(!specEditor.isModified()) {
            throw new MojoExecutionException(fmt.format(new Object[] {
                this.specificationsStartingMarker, path
            }));
        }*/
    }

    //-----------------------------------------------------------ContainerMojo--

}