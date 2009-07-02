/*
 *  jDTAUS Core API
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
package org.jdtaus.core.container;

import java.util.Locale;

/**
 * Object container.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id$
 *
 * @see ContainerFactory
 */
public interface Container
{
    //--Container---------------------------------------------------------------

    /**
     * Gets an instance of an implementation of a specification.
     * <p>When creating objects, use of the classloader associated with the
     * given class, as returned by method {@link Class#getClassLoader()}, is
     * recommended. Only if that method returns {@code null}, indicating the
     * class has been loaded by the bootstrap classloader, use of the bootstrap
     * classloader is recommended.</p>
     *
     * @param specification The class of the specification to return an
     * implementation instance of.
     * @param implementationName The logical name of the implementation to
     * return an instance of.
     *
     * @return An instance of an implementation as specified by the
     * specification defined for class {@code specification}.
     *
     * @throws NullPointerException if {@code specification} or
     * {@code implementationName} is {@code null}.
     * @throws MissingSpecificationException if no specification is defined
     * for {@code specification}.
     * @throws MissingImplementationException if no implementation named
     * {@code implementationName} is defined for the specification identified
     * by {@code specification}.
     * @throws org.jdtaus.core.container.InstantiationException if creating
     * an instance of the implementation fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     * @deprecated Renamed to {@link #getObject(java.lang.Class, java.lang.String) }.
     */
     Object getImplementation( Class specification, String implementationName );

    /**
     * Gets an instance of an implementation of a dependency.
     * <p>When creating objects, use of the classloader associated with the
     * given class, as returned by method {@link Class#getClassLoader()}, is
     * recommended. Only if that method returns {@code null}, indicating the
     * class has been loaded by the bootstrap classloader, use of the bootstrap
     * classloader is recommended.</p>
     *
     * @param implementation The class of the implementation to return an
     * instance of a dependency of.
     * @param dependencyName The logical name of the dependency to return an
     * instance of.
     *
     * @return An instance of the implementation of the dependency named
     * {@code dependencyName} for {@code implementation}.
     *
     * @throws NullPointerException if {@code implementation} or
     * {@code dependencyName} is {@code null}.
     * @throws MissingImplementationException if no implementation is defined
     * for {@code implementation}.
     * @throws MissingDependencyException if no dependency named
     * {@code dependencyName} is defined for the implementation identified by
     * {@code implementation}.
     * @throws org.jdtaus.core.container.InstantiationException if creating
     * an instance of the dependency fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     * @deprecated Replaced by {@link #getDependency(java.lang.Object, java.lang.String)}.
     * This method does not take an argument providing information regarding
     * the identity of the object to return a dependency of.
     */
     Object getDependency( Class implementation, String dependencyName );

    /**
     * Gets an instance of an implementation of a specification.
     * <p>The object to return is resolved based on the multiplicity of the
     * specification defined for the specification identified by the given
     * identifier. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_ONE}, the returned object will be an
     * instance of the class of that specification. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_MANY}, the returned object will be a
     * one-dimensional array of instances of the class of that specification.</p>
     *
     * @param specificationIdentifier The identifier of the specification to
     * return an implementation instance of.
     *
     * @return An instance of an implementation as specified by the
     * specification identified by {@code specificationIdentifier}.
     *
     * @throws NullPointerException if {@code specificationIdentifier} is
     * {@code null}.
     * @throws MissingSpecificationException if no specification is defined
     * for {@code specificationIdentifier}.
     * @throws MultiplicityConstraintException if the multiplicity of the
     * specification equals {@code MULTIPLICITY_ONE} and the model holds either
     * no or more than one implementation.
     * @throws org.jdtaus.core.container.InstantiationException if creating an
     * implementation instance fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     * @deprecated Replaced by {@link #getObject(java.lang.Class) }. This method
     * does not take an argument providing information regarding the classloader
     * in use. It is recommended to use the classloader which loaded the
     * implementation of this interface as returned by method
     * {@link Class#getClassLoader()}. Only if that method returns {@code null},
     * use of the bootstrap classloader is recommended.
     */
     Object getObject( String specificationIdentifier );

    /**
     * Gets an instance of an implementation of a specification.
     *
     * @param specificationIdentifier The identifier of the specification to
     * return an implementation instance of.
     * @param implementationName The logical name of the implementation to
     * return an instance of.
     *
     * @return An instance of the implementation named
     * {@code implementationName} as specified by the specification identified
     * by {@code specificationIdentifier}.
     *
     * @throws NullPointerException if {@code specificationIdentifier} or
     * {@code implementationName} is {@code null}.
     * @throws MissingSpecificationException if no specification is defined
     * for {@code specificationIdentifier}.
     * @throws MissingImplementationException if no implementation named
     * {@code implementationName} is defined for the specification identified by
     * {@code specificationIdentifier}.
     * @throws org.jdtaus.core.container.InstantiationException if creating an
     * implementation instance fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     * @deprecated Replaced by {@link #getObject(java.lang.Class, java.lang.String) }.
     * This method does not take an argument providing information regarding the
     * classloader in use. It is recommended to use the classloader which loaded
     * the implementation of this interface as returned by method
     * {@link Class#getClassLoader()}. Only if that method returns {@code null},
     * use of the bootstrap classloader is recommended.
     */
     Object getObject( String specificationIdentifier,
                       String implementationName );

    /**
     * Gets an instance of an implementation of a specification.
     * <p>The object to return is resolved based on the multiplicity of the
     * specification defined for the given class. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_ONE}, the returned object will be an
     * instance of the given class. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_MANY}, the returned object will be a
     * one-dimensional array of instances of the given class.</p>
     * <p>When creating objects, use of the classloader associated with the
     * given class, as returned by method {@link Class#getClassLoader()}, is
     * recommended. Only if that method returns {@code null}, indicating the
     * class has been loaded by the bootstrap classloader, use of the bootstrap
     * classloader is recommended.</p>
     *
     * @param specification The class of the specification to return an
     * implementation instance of.
     *
     * @return An instance of an implementation as specified by the
     * specification defined for class {@code specification}.
     *
     * @throws NullPointerException if {@code specification} is {@code null}.
     * @throws MissingSpecificationException if no specification is defined
     * for {@code specification}.
     * @throws MultiplicityConstraintException if the multiplicity of the
     * specification defined for {@code specification} equals
     * {@code MULTIPLICITY_ONE} and the model holds either no or more than one
     * implementation.
     * @throws org.jdtaus.core.container.InstantiationException if creating an
     * implementation instance fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     */
    Object getObject( Class specification );

    /**
     * Gets an instance of an implementation of a specification.
     * <p>When creating objects, use of the classloader associated with the
     * given class, as returned by method {@link Class#getClassLoader()}, is
     * recommended. Only if that method returns {@code null}, indicating the
     * class has been loaded by the bootstrap classloader, use of the bootstrap
     * classloader is recommended.</p>
     *
     * @param specification The class of the specification to return an
     * implementation instance of.
     * @param implementationName The logical name of the implementation to
     * return an instance of.
     *
     * @return An instance of the implementation named
     * {@code implementationName} as specified by the specification defined for
     * class {@code specification}.
     *
     * @throws NullPointerException if {@code specification} or
     * {@code implementationName} is {@code null}.
     * @throws MissingSpecificationException if no specification is defined
     * for {@code specification}.
     * @throws MissingImplementationException if no implementation named
     * {@code implementationName} is defined for the specification for
     * {@code specification}.
     * @throws org.jdtaus.core.container.InstantiationException if creating an
     * implementation instance fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     */
    Object getObject( Class specification, String implementationName );

    /**
     * Gets an instance of a dependency of an object.
     * <p>The object to return is resolved based on the multiplicity of the
     * specification defined for the dependency. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_ONE}, the returned object will be an
     * instance of the class of that specification. For a multiplicity equal to
     * {@link Specification#MULTIPLICITY_MANY}, the returned object will be a
     * one-dimensional array of instances of the class of that specification.</p>
     * <p>When creating objects, use of the classloader associated with the
     * class of the given object, as returned by method
     * {@link Class#getClassLoader()}, is recommended. Only if that method
     * returns {@code null}, indicating the class has been loaded by the
     * bootstrap classloader, use of the bootstrap classloader is recommended.</p>
     *
     * @param object The object to return a dependency instance of.
     * @param dependencyName The logical name of the dependency to return an
     * instance of.
     *
     * @return An instance of the dependency named {@code dependencyName} of
     * {@code object}.
     *
     * @throws NullPointerException if {@code object} or {@code dependencyName}
     * is {@code null}.
     * @throws MissingImplementationException if no implementation is defined
     * for {@code object}.
     * @throws MissingDependencyException if no dependency named
     * {@code dependencyName} is defined for {@code object}.
     * @throws MultiplicityConstraintException if the specification of the
     * dependency has a multiplicity of {@code MULTIPLICITY_ONE} without any
     * implementation being available.
     * @throws org.jdtaus.core.container.InstantiationException if creating an
     * instance of the dependency fails.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     */
    Object getDependency( Object object, String dependencyName );

    /**
     * Gets an instance of a property of an object.
     * <p>When creating objects, use of the classloader associated with the
     * class of the given object, as returned by method
     * {@link Class#getClassLoader()}, is recommended. Only if that method
     * returns {@code null}, indicating the class has been loaded by the
     * bootstrap classloader, use of the bootstrap classloader is recommended.</p>
     *
     * @param object The object to return a property instance of.
     * @param propertyName The logical name of the property to return an
     * instance of.
     *
     * @return An instance of the property named {@code propertyName} of
     * {@code object}.
     *
     * @throws NullPointerException if {@code object} or {@code propertyName} is
     * {@code null}.
     * @throws MissingImplementationException if no implementation is defined
     * for {@code object}.
     * @throws MissingPropertyException if no property named
     * {@code propertyName} is defined for {@code object}.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     */
    Object getProperty( Object object, String propertyName );

    /**
     * Gets an instance of a message of an object.
     * <p>When creating objects, use of the classloader associated with the
     * class of the given object, as returned by method
     * {@link Class#getClassLoader()}, is recommended. Only if that method
     * returns {@code null}, indicating the class has been loaded by the
     * bootstrap classloader, use of the bootstrap classloader is recommended.</p>
     *
     * @param object The object to return a message instance of.
     * @param messageName The logical name of the message to return an instance
     * of.
     * @param arguments Arguments to format the message instance with or
     * {@code null}.
     *
     * @return An instance of the message named {@code messageName} of
     * {@code object} formatted with {@code arguments}.
     *
     * @throws NullPointerException if {@code object} or {@code messageName} is
     * {@code null}.
     * @throws MissingImplementationException if no implementation is defined
     * for {@code object}.
     * @throws MissingMessageException if no message named
     * {@code messageName} is defined for {@code object}.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     * @deprecated Replaced by {@link #getMessage(java.lang.Object, java.lang.String, java.util.Locale, java.lang.Object) }
     * This method does not take information regarding the locale to use when
     * formatting text.
     */
     String getMessage( Object object, String messageName, Object arguments );

    /**
     * Gets an instance of a message of an object for a given locale.
     * <p>When creating objects, use of the classloader associated with the
     * class of the given object, as returned by method
     * {@link Class#getClassLoader()}, is recommended. Only if that method
     * returns {@code null}, indicating the class has been loaded by the
     * bootstrap classloader, use of the bootstrap classloader is recommended.</p>
     *
     * @param object The object to return a message instance of.
     * @param messageName The logical name of the message to return an instance
     * of.
     * @param locale The locale of the message instance to return.
     * @param arguments Arguments to format the message instance with or
     * {@code null}.
     *
     * @return An instance of the message named {@code messageName} of
     * {@code object} formatted with {@code arguments} for {@code locale}.
     *
     * @throws NullPointerException if {@code object}, {@code locale} or
     * {@code messageName} is {@code null}.
     * @throws MissingImplementationException if no implementation is defined
     * for {@code object}.
     * @throws MissingMessageException if no message named
     * {@code messageName} is defined for {@code object}.
     * @throws ContainerError for unrecoverable container errors.
     * @throws ContextError for unrecoverable context errors.
     * @throws ModelError for unrecoverable model errors.
     */
    String getMessage( Object object, String messageName, Locale locale,
                       Object arguments );

    //---------------------------------------------------------------Container--
}
