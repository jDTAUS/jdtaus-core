/*
 *  jDTAUS Core Test Suite
 *  Copyright (C) 2005 Christian Schulte
 *  <schulte2005@users.sourceforge.net>
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
package org.jdtaus.core.monitor.it;

import junit.framework.Assert;
import org.jdtaus.core.monitor.TaskEventSource;

/**
 * Testcase for {@code TaskEventSource} implementations.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JDTAUS$
 */
public class TaskEventSourceTest
{
    //--TaskEventSourceTest-----------------------------------------------------

    /** Implementation to test. */
    private TaskEventSource source;

    /**
     * Gets the {@code TaskEventSource} implementation tests are performed with.
     *
     * @return the {@code TaskEventSource} implementation tests are performed
     * with.
     */
    public TaskEventSource getTaskEventSource()
    {
        return this.source;
    }

    /**
     * Sets the {@code TaskEventSource} implementation tests are performed with.
     *
     * @param value the {@code TaskEventSource} implementation to perform tests
     * with.
     */
    public final void setTaskEventSource( final TaskEventSource value )
    {
        this.source = value;
    }

    //-----------------------------------------------------TaskEventSourceTest--
    //--Tests-------------------------------------------------------------------

    /**
     * Tests the {@link TaskEventSource#addTaskListener(TaskListener)} method to
     * handle {@code null} listener values correctly by throwing a corresponding
     * {@code NullPointerException}.
     */
    public void testAddTaskListener() throws Exception
    {
        assert this.getTaskEventSource() != null;

        try
        {
            this.getTaskEventSource().addTaskListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the {@link TaskEventSource#removeTaskListener(TaskListener)} method
     * to handle {@code null} listener values correctly by throwing a
     * corresponding {@code NullPointerException}.
     */
    public void testRemoveTaskListener() throws Exception
    {
        assert this.getTaskEventSource() != null;

        try
        {
            this.getTaskEventSource().removeTaskListener( null );
            throw new AssertionError();
        }
        catch ( NullPointerException e )
        {
            Assert.assertNotNull( e.getMessage() );
            System.out.println( e.toString() );
        }

    }

    /**
     * Tests the {@link TaskEventSource#getTaskListeners()} method to not
     * return a {@code null} value instead of an empty array when no listeners
     * are registered.
     */
    public void testGetTaskListeners() throws Exception
    {
        assert this.getTaskEventSource() != null;

        Assert.assertNotNull( this.getTaskEventSource().getTaskListeners() );
    }

    //-------------------------------------------------------------------Tests--
}
