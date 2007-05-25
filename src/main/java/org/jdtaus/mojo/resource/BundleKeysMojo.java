/*
 *  jDTAUS - DTAUS fileformat.
 *  Copyright (c) 2005 Christian Schulte <cs@schulte.it>
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
package org.jdtaus.mojo.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

/**
 * Mojo to generate accessor classes for <code>ResourceBundle</code>s.
 *
 * @goal generate-bundles
 * @phase process-resources
 * @requiresDependencyResolution compile
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public class BundleKeysMojo extends AbstractMojo
{
    //--Configuration-----------------------------------------------------------

    /**
     * The project's output directory.
     *
     * @parameter expression="${project.build.outputDirectory}"
     */
    private String outputDirectory;

    /**
     * The directory to output the generated sources to.
     *
     * @parameter expression="${project.build.directory}/generated-sources/bundle-keys"
     */
    private String genDirectory;

    /**
     * The suffix to append to the bundle name to form the interface name.
     *
     * @parameter expression="${nameSuffix}" default-value="Bundle"
     */
    private String nameSuffix;

    /**
     * Currently executed <code>MavenProject</code>.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * List of <code>ResourceBundle</code> names to generate interfaces for.
     *
     * @parameter expression="${bundles}"
     * @required
     */
    private String[] bundles;

    /**
     * The encoding to use for reading and writing sources. By default the
     * system's default encoding will be used.
     * @parameter expression="${encoding}"
     * @optional
     */
    private String encoding;

    /**
     * Project compile classpath.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * Flag indicating that javadoc comments should be generated.
     * @parameter expression="${javadoc}" default-value="true"
     * @optional
     */
    private Boolean javadoc;

    /**
     * Language to be used for javadoc comments. By default the system's
     * default locale will be used.
     * @parameter expression="${locale}"
     * @optional
     */
    private String locale;

    /**
     * Getter for property {@code locale}.
     *
     * @return the locale to use for javadoc comments.
     */
    protected final Locale getLocale()
    {
        return this.locale == null ?
            Locale.getDefault() : new Locale(this.locale);

    }

    protected final MavenProject getProject()
    {
        return this.project;
    }

    protected final String getGenDirectory()
    {
        return this.genDirectory;
    }

    protected final String getOutputDirectory()
    {
        return this.outputDirectory;
    }

    protected final String getNameSuffix()
    {
        return this.nameSuffix;
    }

    protected final boolean isJavadoc()
    {
        return this.javadoc.booleanValue();
    }

    //-----------------------------------------------------------Configuration--
    //--AbstractMojo------------------------------------------------------------

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final ClassLoader mavenLoader =
            Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(
            this.getClasspathClassLoader());

        if (!FileUtils.fileExists(this.getGenDirectory()))
        {
            FileUtils.mkdir(this.getGenDirectory());
        }

        this.getProject().addCompileSourceRoot(this.getGenDirectory());

        String pkg;
        final Iterator it;
        final Map bundles = this.getBundles();
        for(it = bundles.keySet().iterator(); it.hasNext();)
        {
            pkg = (String) it.next();
            this.generatePackage(pkg, (Map) bundles.get(pkg));
        }

        Thread.currentThread().setContextClassLoader(mavenLoader);
    }

    //------------------------------------------------------------AbstractMojo--
    //--BundleKeysMojo----------------------------------------------------------

    /**
     * Accessor to the compile classpath elements.
     *
     * @return a list holding strings for each compile class path element.
     */
    protected final List getClasspathElements()
    {
        return this.classpathElements;
    }

    /**
     * Provides access to the project's compile classpath.
     *
     * @return a {@code ClassLoader} initialized with the project's compile
     * classpath.
     *
     * @throws MojoFailureException for unrecoverable technical errors.
     */
    protected final ClassLoader getClasspathClassLoader() throws
        MojoFailureException
    {

        String element;
        File file;
        final Iterator it;
        final Collection urls = new LinkedList();

        try
        {
            for(it = this.getClasspathElements().iterator(); it.hasNext();)
            {
                element = (String) it.next();
                if(!urls.contains(element))
                {
                    file = new File(element);
                    urls.add(file.toURI().toURL());
                }
            }

            if(!urls.contains(this.getOutputDirectory()))
            {
                file = new File(this.getOutputDirectory());
                urls.add(file.toURI().toURL());
            }

            return new URLClassLoader(
                (URL[]) urls.toArray(new URL[urls.size()]),
                Thread.currentThread().getContextClassLoader());

        }
        catch(MalformedURLException e)
        {
            throw new MojoFailureException(e.getMessage());
        }
    }

    protected Map getBundles()
    {
        int i;
        final Map ret = new HashMap();
        Map map;
        String bundle;
        String bundleName;
        String bundlePackage;
        String bundleLocation;

        if(this.bundles != null)
        {
            for(i = this.bundles.length - 1; i >= 0; i--)
            {
                bundle = this.bundles[i];
                bundleName = BundleKeysMojo.getNameForBundle(bundle);
                bundlePackage = BundleKeysMojo.getPackageForBundle(bundle);
                bundleLocation = bundle.replaceAll("\\.", "/");

                map = (Map) ret.get(bundlePackage);
                if(map == null)
                {
                    map = new HashMap();
                    ret.put(bundlePackage, map);
                }

                try
                {
                    map.put(bundleName,
                        ResourceBundle.getBundle(bundleLocation,
                        this.getLocale(),
                        Thread.currentThread().getContextClassLoader()));

                }
                catch(MissingResourceException e)
                {
                    this.getLog().warn(e.getMessage());
                }
            }
        }

        return ret;
    }

    protected static String getPackageForBundle(final String bundleName)
    {
        if(bundleName == null)
        {
            throw new NullPointerException("bundleName");
        }

        return bundleName.substring(0, bundleName.lastIndexOf('.'));
    }

    protected static String getNameForBundle(final String bundleName)
    {
        if(bundleName == null)
        {
            throw new NullPointerException("bundleName");
        }

        return bundleName.substring(bundleName.lastIndexOf('.') + 1);
    }

    protected MessageFormat getMessage(final String key)
    {
        if(key == null)
        {
            throw new NullPointerException("key");
        }

        return new MessageFormat(ResourceBundle.
            getBundle(BundleKeysMojo.class.getName()).getString(key));

    }

    protected void generatePackage(final String pkg, final Map bundles) throws
        MojoFailureException
    {

        if(pkg == null)
        {
            throw new NullPointerException("pkg");
        }
        if(bundles == null)
        {
            throw new NullPointerException("bundles");
        }

        String bundleName;
        final Iterator it;
        final String pkgDir = this.getGenDirectory() + File.separator +
            pkg.replaceAll("\\.", "/");

        if (!FileUtils.fileExists(pkgDir))
        {
            FileUtils.mkdir(pkgDir);
        }

        for(it = bundles.keySet().iterator(); it.hasNext();)
        {
            bundleName = (String) it.next();
            this.generateBundle(bundleName, pkg,
                new File(pkgDir, bundleName + this.getNameSuffix() + ".java"),
                (ResourceBundle) bundles.get(bundleName));

        }
    }

    protected void generateBundle(final String bundleName,
        final String packageName, final File outputFile,
        final ResourceBundle bundle) throws MojoFailureException
    {

        String key;

        if(bundleName == null)
        {
            throw new NullPointerException("bundleName");
        }
        if(outputFile == null)
        {
            throw new NullPointerException("outputFile");
        }
        if(bundle == null)
        {
            throw new NullPointerException("bundle");
        }
        if(packageName == null)
        {
            throw new NullPointerException("packageName");
        }

        this.getLog().info(this.getMessage("writingBundle").
            format(new Object[] { outputFile.getName() }));

        try
        {
            final FileOutputStream out = new FileOutputStream(outputFile);
            final Writer writer = this.encoding == null ?
                new OutputStreamWriter(out) :
                new OutputStreamWriter(out, this.encoding);

            final StringBuffer buf = new StringBuffer(4096);
            buf.append("package ").append(packageName).append(";\n\n");
            buf.append("import java.util.HashMap;\n");
            buf.append("import java.util.Map;\n");
            buf.append("import java.util.Locale;\n");
            buf.append("import java.util.ResourceBundle;\n");
            buf.append("import java.text.MessageFormat;\n");
            buf.append("\nabstract class ").append(bundleName).
                append(this.getNameSuffix()).append("{ \n\n");

            for(Enumeration e = bundle.getKeys(); e.hasMoreElements();)
            {
                key = (String) e.nextElement();

                if(this.isJavadoc())
                {
                    buf.append("/** <pre> ").append(bundle.getObject(key)).
                        append("</pre>. */\n");

                }

                buf.append("public static String ").
                    append(BundleKeysMojo.getStringGetterNameForKey(key));

                buf.append("(final Locale locale) {\n");
                buf.append("    ");
                buf.append("return getMessage(\"").
                    append(key).append("\", locale);\n}\n\n");

                if(this.isJavadoc())
                {
                    buf.append("/** <pre> ").append(bundle.getObject(key)).
                        append("</pre>. */\n");

                }

                buf.append("public static MessageFormat ").
                    append(BundleKeysMojo.getMessageGetterNameForKey(key));

                buf.append("(Locale locale) {\n");
                buf.append("    ");
                buf.append("if(locale == null) { locale = Locale.getDefault(); }\n");
                buf.append("return new MessageFormat(getMessage(\"").
                    append(key).append("\", locale), locale);\n}\n\n");

            }

            buf.append("private static final Map cache = new HashMap();\n\n");
            buf.append("private static String getMessage(").
                append("final String key, Locale locale) {\n");
            buf.append("    ");
            buf.append("if(locale == null) { locale = Locale.getDefault(); }\n");
            buf.append("    ");
            buf.append("Map msgCache = (Map) cache.get(locale);\n");
            buf.append("    ");
            buf.append("if(msgCache == null)\n    {\n");
            buf.append("        ");
            buf.append("msgCache = new HashMap();\n");
            buf.append("        ");
            buf.append("cache.put(locale, msgCache);\n    }\n\n");
            buf.append("    ");
            buf.append("String msg = (String) msgCache.get(key);\n");
            buf.append("    ");
            buf.append("if(msg == null)\n    {\n");
            buf.append("        ");
            buf.append("msg = ResourceBundle.getBundle(\n");
            buf.append("        ");
            buf.append('"').append(packageName).append('.').append(bundleName);
            buf.append('"').append(", locale).getString(key);\n");
            buf.append("        ");
            buf.append("msgCache.put(key, msg);\n");
            buf.append("    };\n");
            buf.append("    return msg;\n");

            buf.append("}\n\n");

            buf.append("}\n");
            writer.write(buf.toString());
            writer.close();
        }
        catch(IOException e)
        {
            throw new MojoFailureException(e.getMessage());
        }
    }

    protected static String getMessageGetterNameForKey(String key)
    {
        if(key == null)
        {
            throw new NullPointerException("key");
        }

        final char[] c = key.toCharArray();
        if(Character.isLowerCase(c[0]))
        {
            c[0] = Character.toUpperCase(c[0]);
        }

        key = String.valueOf(c);

        return new StringBuffer(255).append("get").append(key).
            append("Message").toString();

    }

    protected static String getStringGetterNameForKey(String key)
    {
        if(key == null)
        {
            throw new NullPointerException("key");
        }

        final char[] c = key.toCharArray();
        if(Character.isLowerCase(c[0]))
        {
            c[0] = Character.toUpperCase(c[0]);
        }

        key = String.valueOf(c);

        return new StringBuffer(255).append("get").append(key).
            append("Text").toString();

    }

    //----------------------------------------------------------BundleKeysMojo--
}
