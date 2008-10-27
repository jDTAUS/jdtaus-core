/*
 *  jDTAUS Core Resource Mojo
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
package org.jdtaus.mojo.resource.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

/**
 * Manages the {@code http://jdtaus.org/core/model/container} model.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 * @plexus.component role="org.jdtaus.mojo.resource.model.ModelManager"
 *                   role-hint="default"
 */
public class ModelManager
{

    /**
     * Unmarshals a given module descriptor.
     *
     * @param moduleDescriptor module descriptor to unmarshall.
     *
     * @return the content tree unmarshalled from {@code moduleDescriptor}.
     *
     * @throws NullPointerException if {@code moduleDescriptor} is {@code null}.
     * @throws JAXBException if unmarshalling fails.
     * @throws SAXException if parsing fails.
     * @throws IOException if reading fails.
     */
    public Module getModule( final File moduleDescriptor )
        throws JAXBException, SAXException, IOException
    {
        Module module = null;
        final JAXBContext ctx = JAXBContext.newInstance(
            "org.jdtaus.mojo.resource.model" );

        final Unmarshaller unmarshaller = ctx.createUnmarshaller();
        final Object contentTree = unmarshaller.unmarshal( moduleDescriptor );
        if ( contentTree instanceof Module )
        {
            module = (Module) contentTree;
        }

        return module;
    }

    /**
     * Gets a message.
     *
     * @param module the module to search.
     * @param messageName the name of the message to search.
     *
     * @return the message with name {@code name} from {@code module} or
     * {@code null} if no message exists.
     *
     * @throws NullPointerException if {@code module} or {@code messageName} is
     * {@code null}.
     */
    public Message getMessage( final Module module,
        final String messageName )
    {
        if ( module == null )
        {
            throw new NullPointerException( "module" );
        }
        if ( messageName == null )
        {
            throw new NullPointerException( "messageName" );
        }

        Message message = null;

        if ( module.getMessages() != null )
        {
            for ( Iterator it = module.getMessages().getMessage().iterator();
                it.hasNext(); )
            {
                final Message current = (Message) it.next();
                if ( current.getName().equals( messageName ) )
                {
                    message = current;
                    break;
                }
            }
        }

        return message;
    }

    /**
     * Computes the hashcode of an implementation bundle.
     *
     * @param module the module containing {@code implementation}.
     * @param implementation the implementation to compute the bundle hashcode
     * of.
     *
     * @return the bundle hashcode of {@code implementation}.
     *
     * @throws NullPointerException if {@code module} or {@code implementation}
     * is {@code null}.
     */
    public int getHashCode( final Module module,
        final Implementation implementation )
    {
        if ( module == null )
        {
            throw new NullPointerException( "module" );
        }
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        int bundleHash = 23;

        if ( implementation.getMessages() != null )
        {
            for ( Iterator it = implementation.getMessages().getMessage().
                iterator(); it.hasNext(); )
            {
                final Message message = (Message) it.next();
                bundleHash = 37 * bundleHash + message.getName().hashCode();
                for ( Iterator t = message.getTemplate().getText().iterator();
                    t.hasNext(); )
                {
                    final Text text = (Text) t.next();
                    bundleHash = 37 * bundleHash + text.getLanguage().hashCode();
                    bundleHash = 37 * bundleHash + text.getValue().hashCode();
                }

                if ( message.getArguments() != null )
                {
                    for ( Iterator a = message.getArguments().getArgument().
                        iterator(); a.hasNext(); )
                    {
                        final Argument argument = (Argument) a.next();
                        bundleHash = 37 * bundleHash + argument.getName().
                            hashCode();

                        bundleHash = 37 * bundleHash + argument.getType().
                            toString().hashCode();

                    }
                }
            }

            for ( Iterator it = implementation.getMessages().getReference().
                iterator(); it.hasNext(); )
            {
                final MessageReference messageReference =
                    (MessageReference) it.next();

                final Message message =
                    this.getMessage( module, messageReference.getName() );

                for ( Iterator t = message.getTemplate().getText().iterator();
                    t.hasNext(); )
                {
                    final Text text = (Text) t.next();
                    bundleHash = 37 * bundleHash + text.getLanguage().hashCode();
                    bundleHash = 37 * bundleHash + text.getValue().hashCode();
                }

                if ( message.getArguments() != null )
                {
                    for ( Iterator a = message.getArguments().getArgument().
                        iterator(); a.hasNext(); )
                    {
                        final Argument argument = (Argument) a.next();
                        bundleHash = 37 * bundleHash + argument.getName().
                            hashCode();

                        bundleHash = 37 * bundleHash + argument.getType().
                            toString().hashCode();

                    }
                }
            }
        }

        return bundleHash;
    }

    /**
     * Builds a mapping of language codes to {@code Properties} instances
     * holding the messages for the language.
     *
     * @param module the module containing {@code implementation}.
     * @param implementation the implementation to get the properties for.
     *
     * @return mapping of language codes to {@code Properties} instances
     * holding the messages for the language.
     *
     * @throws NullPointerException if {@code module} or {@code implementation}
     * is {@code null}.
     */
    public Map/*<String,Properties>*/ getBundleProperties(
        final Module module, final Implementation implementation )
    {
        if ( module == null )
        {
            throw new NullPointerException( "module" );
        }
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        final Map properties = new HashMap( 10 );

        if ( implementation.getMessages() != null )
        {
            for ( Iterator it = implementation.getMessages().getMessage().
                iterator(); it.hasNext(); )
            {
                final Message message = (Message) it.next();
                for ( Iterator t = message.getTemplate().getText().iterator();
                    t.hasNext(); )
                {
                    final Text text = (Text) t.next();
                    final String language = text.getLanguage().toLowerCase();

                    Properties bundleProperties =
                        (Properties) properties.get( language );

                    if ( bundleProperties == null )
                    {
                        bundleProperties = new Properties();
                        properties.put( language, bundleProperties );
                    }

                    bundleProperties.setProperty( message.getName(),
                        text.getValue() );

                }
            }

            for ( Iterator it = implementation.getMessages().getReference().
                iterator(); it.hasNext(); )
            {
                final MessageReference messageReference =
                    (MessageReference) it.next();

                final Message message =
                    this.getMessage( module, messageReference.getName() );

                for ( Iterator t = message.getTemplate().getText().iterator();
                    t.hasNext(); )
                {
                    final Text text = (Text) t.next();
                    final String language = text.getLanguage().toLowerCase();

                    Properties bundleProperties =
                        (Properties) properties.get( language );

                    if ( bundleProperties == null )
                    {
                        bundleProperties = new Properties();
                        properties.put( language, bundleProperties );
                    }

                    bundleProperties.setProperty( message.getName(),
                        text.getValue() );

                }
            }
        }

        return properties;
    }

    /**
     * Gets the java package name of an implementation.
     *
     * @param implementation the implementation to get the java package name of.
     *
     * @return the java package name of {@code implementation}.
     *
     * @throws NullPointerException if {@code implementation} is {@code null}.
     */
    public String getJavaPackageName( final Implementation implementation )
    {
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        return implementation.getIdentifier().
            substring( 0, implementation.getIdentifier().lastIndexOf( '.' ) );

    }

    /**
     * Gets the java type name of an implementation.
     *
     * @param implementation the implementation to get the java type name of.
     *
     * @return the java type name of {@code implementation}.
     *
     * @throws NullPointerException if {@code implementation} is {@code null}.
     */
    public String getJavaTypeName( final Implementation implementation )
    {
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        return implementation.getIdentifier().
            substring( implementation.getIdentifier().lastIndexOf( '.' ) + 1 ) +
            "Bundle";

    }

    /**
     * Formats a text to a javadoc comment.
     *
     * @param text the text to nformat to a javadoc comment.
     *
     * @return {@code text} formatted as a javadoc comment.
     *
     * @throws NullPointerException if {@code text} is {@code null}.
     */
    public String getJavadocComment( final Text text )
    {
        if ( text == null )
        {
            throw new NullPointerException( "text" );
        }

        String normalized = text.getValue();
        normalized = normalized.replaceAll( "\\/\\*\\*", "/*" );
        normalized = normalized.replaceAll( "\\*/", "/" );
        normalized = normalized.replaceAll( "\n", "\n   *" );
        return normalized;
    }

    /**
     * Gets the method name of a java accessor method for a given message.
     *
     * @param message the message to return the accessor method name for.
     *
     * @throws NullPointerException if {@code text} is {@code null}.
     */
    public String getJavaAccessorMethodName( final Message message )
    {
        if ( message == null )
        {
            throw new NullPointerException( "message" );
        }

        final char[] c = message.getName().toCharArray();
        c[0] = Character.toUpperCase( c[0] );

        return new StringBuffer( 255 ).append( "get" ).
            append( String.valueOf( c ) ).
            append( "Message" ).toString();

    }

    /**
     * Gets the java classpath location of an implementation bundle.
     *
     * @return implementation the implementation to return the bundle's java
     * classpath location of.
     *
     * @return the java classpath location of the bundle of
     * {@code implementation}.
     *
     * @throws NullPointerException if {@code implementation} is {@code null}.
     */
    public String getJavaClasspathLocation( final Implementation implementation )
    {
        if ( implementation == null )
        {
            throw new NullPointerException( "implementation" );
        }

        return ( this.getJavaPackageName( implementation ) + '.' +
            this.getJavaTypeName( implementation ) ).replace(
            '.', File.separatorChar );

    }

}
