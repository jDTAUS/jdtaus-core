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
package org.jdtaus.core.client.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.jdtaus.core.container.ContainerFactory;
import org.jdtaus.core.container.ContainerInitializer;
import org.jdtaus.core.container.ContextFactory;
import org.jdtaus.core.container.ContextInitializer;
import org.jdtaus.core.container.Dependency;
import org.jdtaus.core.container.Implementation;
import org.jdtaus.core.container.ModelFactory;
import org.jdtaus.core.logging.spi.Logger;
import org.jdtaus.core.monitor.Task;
import org.jdtaus.core.monitor.TaskEvent;
import org.jdtaus.core.monitor.TaskEventSource;
import org.jdtaus.core.monitor.TaskListener;
import org.jdtaus.core.text.MessageEvent;
import org.jdtaus.core.text.MessageEventSource;
import org.jdtaus.core.text.MessageListener;

/**
 * Main entry point for client applications.
 * <p>This class provides the {@link #main(String[])} method invoked by the VM
 * during startup which is responsible for initializing the application's
 * runtime.</p>
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 *
 * @see #initialize()
 */
public class Main implements ContainerInitializer
{

    //--Implementation----------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Meta-data describing the implementation. */
    private static final Implementation META =
        ModelFactory.getModel().getModules().
        getImplementation(Main.class.getName());

    //----------------------------------------------------------Implementation--
    //--Constructors------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Protected <code>Main</code> implementation constructor.
    * @param meta Implementation meta-data.
    */
    protected Main(final Implementation meta)
    {
        super();
    }
    /** Protected <code>Main</code> dependency constructor.
    * @param meta Dependency meta-data.
    */
    protected Main(final Dependency meta)
    {
        super();
    }

    //------------------------------------------------------------Constructors--
    //--ContainerInitializer----------------------------------------------------

    /** Maps tasks to progress monitors. */
    private final Map tasks = new HashMap(100);

    /**
     * Initializes the instance.
     * <p>Registers a {@code TaskListener} with the {@code TaskEventSource}
     * configured as a dependency to this implementation and adapts
     * {@code TaskEvent}s to Swing {@code ProgressMonitor}s. Then registers a
     * {@code MessageListener} with the {@code MessageEventSource} configured as
     * a dependency to this implementation and adapts {@code MessageEvent}s to
     * Swing {@code JOptionPane}s. The last step in the initialization
     * process is to bring up the user interface by setting the property
     * {@code visible} of the root component of the {@code UserInterface}
     * configured as a dependency to this implementation to {@code true}.</p>
     */
    public void initialize()
    {

        // Listen to library task events.
        this.getTaskEventSource().addTaskListener(new TaskListener()
        {
            public void onTaskEvent(final TaskEvent taskEvent)
            {
                ProgressMonitor monitor;
                final Task task = taskEvent.getTask();

                switch(taskEvent.getType())
                {
                    case TaskEvent.STARTED:
                        monitor = new ProgressMonitor(
                            getUserInterface().getParent(),
                            task.getDescription().getText(Locale.getDefault()),
                            null, task.getMinimum(), task.getMaximum());

                        if(task.isIndeterminate())
                        {
                            monitor.setMinimum(0);
                            monitor.setMaximum(0);
                        }

                        monitor.setProgress(task.getProgress());
                        monitor.setMillisToPopup(1000);
                        tasks.put(taskEvent.getTask(), monitor);
                        break;

                    case TaskEvent.CHANGED_STATE:
                        monitor = (ProgressMonitor) tasks.get(task);
                        monitor.setMinimum(task.getMinimum());
                        monitor.setMaximum(task.getMaximum());
                        monitor.setProgress(task.getProgress());
                        break;

                    case TaskEvent.ENDED:
                        monitor = (ProgressMonitor) tasks.remove(task);
                        monitor.close();
                        break;

                    default:
                        getLogger().error(task.toString());

                }
            }
        });

        // Listen to library messages.
        this.getMessageEventSource().addMessageListener(new MessageListener()
        {
            public void onMessage(final MessageEvent messageEvent)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        switch(messageEvent.getType())
                        {
                            case MessageEvent.INFORMATION:
                                JOptionPane.showMessageDialog(
                                    getUserInterface().getParent(),
                                    messageEvent.getMessage().getText(
                                    Locale.getDefault()), MainBundle.
                                    getInformationText(Locale.getDefault()),
                                    JOptionPane.INFORMATION_MESSAGE);

                                break;

                            case MessageEvent.NOTIFICATION:
                                JOptionPane.showMessageDialog(
                                    getUserInterface().getParent(),
                                    messageEvent.getMessage().getText(
                                    Locale.getDefault()), MainBundle.
                                    getNotificationText(Locale.getDefault()),
                                    JOptionPane.INFORMATION_MESSAGE);

                                break;

                            case MessageEvent.WARNING:
                                JOptionPane.showMessageDialog(
                                    getUserInterface().getParent(),
                                    messageEvent.getMessage().getText(
                                    Locale.getDefault()), MainBundle.
                                    getWarningText(Locale.getDefault()),
                                    JOptionPane.WARNING_MESSAGE);

                                break;

                            default:
                                getLogger().warn(messageEvent.getMessage().
                                    getText(Locale.getDefault()));

                        }
                    }
                });
            }
        });

        // Application initialization.
        this.getApplication();
    }

    //----------------------------------------------------ContainerInitializer--
    //--Dependencies------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.

    /** Configured <code>Logger</code> implementation. */
    private transient Logger _dependency4;

    /** <code>Logger</code> implementation getter. */
    private Logger getLogger()
    {
        Logger ret = null;
        if(this._dependency4 != null)
        {
           ret = this._dependency4;
        }
        else
        {
            ret = (Logger) ContainerFactory.getContainer().
                getDependency(Main.class,
                "Logger");

            if(ModelFactory.getModel().getModules().
                getImplementation(Main.class.getName()).
                getDependencies().getDependency("Logger").
                isBound())
            {
                this._dependency4 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>UserInterface</code> implementation. */
    private transient UserInterface _dependency3;

    /** <code>UserInterface</code> implementation getter. */
    private UserInterface getUserInterface()
    {
        UserInterface ret = null;
        if(this._dependency3 != null)
        {
           ret = this._dependency3;
        }
        else
        {
            ret = (UserInterface) ContainerFactory.getContainer().
                getDependency(Main.class,
                "UserInterface");

            if(ModelFactory.getModel().getModules().
                getImplementation(Main.class.getName()).
                getDependencies().getDependency("UserInterface").
                isBound())
            {
                this._dependency3 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>Application</code> implementation. */
    private transient Application _dependency2;

    /** <code>Application</code> implementation getter. */
    private Application getApplication()
    {
        Application ret = null;
        if(this._dependency2 != null)
        {
           ret = this._dependency2;
        }
        else
        {
            ret = (Application) ContainerFactory.getContainer().
                getDependency(Main.class,
                "Application");

            if(ModelFactory.getModel().getModules().
                getImplementation(Main.class.getName()).
                getDependencies().getDependency("Application").
                isBound())
            {
                this._dependency2 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>MessageEventSource</code> implementation. */
    private transient MessageEventSource _dependency1;

    /** <code>MessageEventSource</code> implementation getter. */
    private MessageEventSource getMessageEventSource()
    {
        MessageEventSource ret = null;
        if(this._dependency1 != null)
        {
           ret = this._dependency1;
        }
        else
        {
            ret = (MessageEventSource) ContainerFactory.getContainer().
                getDependency(Main.class,
                "MessageEventSource");

            if(ModelFactory.getModel().getModules().
                getImplementation(Main.class.getName()).
                getDependencies().getDependency("MessageEventSource").
                isBound())
            {
                this._dependency1 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }
    /** Configured <code>TaskEventSource</code> implementation. */
    private transient TaskEventSource _dependency0;

    /** <code>TaskEventSource</code> implementation getter. */
    private TaskEventSource getTaskEventSource()
    {
        TaskEventSource ret = null;
        if(this._dependency0 != null)
        {
           ret = this._dependency0;
        }
        else
        {
            ret = (TaskEventSource) ContainerFactory.getContainer().
                getDependency(Main.class,
                "TaskEventSource");

            if(ModelFactory.getModel().getModules().
                getImplementation(Main.class.getName()).
                getDependencies().getDependency("TaskEventSource").
                isBound())
            {
                this._dependency0 = ret;
            }
        }

        if(ret instanceof ContextInitializer && !((ContextInitializer) ret).
            isInitialized(ContextFactory.getContext()))
        {
            ((ContextInitializer) ret).initialize(ContextFactory.getContext());
        }

        return ret;
    }

    //------------------------------------------------------------Dependencies--
    //--Properties--------------------------------------------------------------

    // This section is generated by jdtaus-source-plugin.


    //--------------------------------------------------------------Properties--
    //--Main--------------------------------------------------------------------

    /**
     * Main application entry point invoked by the VM.
     *
     * @param args the command line arguments provided to the VM.
     */
    public static final void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                ContainerFactory.getContainer().getImplementation(Main.class,
                    "jDTAUS Core Utilities");

            }
        });
    }

    //--------------------------------------------------------------------Main--

}
