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
package org.jdtaus.core.client;

import java.util.Locale;
import javax.swing.ImageIcon;

/**
 * A set of icons.
 *
 * @author <a href="mailto:cs@schulte.it">Christian Schulte</a>
 * @version $Id$
 */
public interface IconSet
{

    //--Constants---------------------------------------------------------------

    /** Constant for an icon size of 16x16 pixels. */
    final String SIZE_16X16 = "16x16";

    /** Constant for an icon size of 24x24 pixels. */
    final String SIZE_24X24 = "24x24";

    /** Create, select, or open an Applet. */
    final String DEV_APPLET = "Applet";

    /** Create, select, or open an Application.. */
    final String DEV_APPLICATION = "Application";

    /** Deploy a J2EE Application to a J2EE Server. */
    final String DEV_DEPLOY_APPLICATION = "DeployApplication";

    /** Create, select, or open a Bean. */
    final String DEV_BEAN = "Bean";

    /** Add a Bean to an existing set of objects. */
    final String DEV_BEAN_ADD = "BeanAdd";

    /** Create, select, or open an Enterprise JavaBean. */
    final String DEV_EJB = "EnterpriseJavaBean";

    /** Create, select, or open an Enterprise JavaBean Jar. */
    final String DEV_EJB_JAR = "EnterpriseJavaBeanJar";

    /** Create, select, or open a Host. */
    final String DEV_HOST = "Host";

    /** Create, select, or open a J2EE Application. */
    final String DEV_J2EE_APPLICATION = "J2EEApplication";

    /** Create, select, or open a J2EE Application Client. */
    final String DEV_J2EE_APP_CLIENT = "J2EEApplicationClient";

    /** Add a J2EE Application Client to a J2EE Application. */
    final String DEV_J2EE_APP_CLIENT_ADD = "J2EEApplicationClientAdd";

    /** Create, select, or open a J2EE Server. */
    final String DEV_J2EE_SERVER = "J2EEServer";

    /** Create, select, or open a Jar. */
    final String DEV_JAR = "Jar";

    /** Add a Jar to an existing set of objects. */
    final String DEV_JAR_ADD = "JarAdd";

    /** Create, select, or open a Server. */
    final String DEV_SERVER = "Server";

    /** Create, select, or open a War. */
    final String DEV_WAR = "War";

    /** Add a War to an existing set of objects. */
    final String DEV_WAR_ADD = "WarAdd";

    /** Create, select, or open a Web Component. */
    final String DEV_WEB_COMPONENT = "WebComponent";

    /** Add a Web Component to a War file. */
    final String DEV_WEB_COMPONENT_ADD = "WebComponentAdd";

    /** Advance rapidly through a time-based media. */
    final String MEDIA_FAST_FORWARD = "FastForward";

    /** Create, select, or open a movie. */
    final String MEDIA_MOVIE = "Movie";

    /**
     * Halt the display of the current media.
     * Do not change the position of the "play head".
     */
    final String MEDIA_PAUSE = "Pause";

    /** Render the time-based media. */
    final String MEDIA_PLAY = "Play";

    /** Move quickly backward through the time-based media. */
    final String MEDIA_REWIND = "Rewind";

    /** Move the "play head" back one unit. */
    final String MEDIA_STEP_BACK = "StepBack";

    /** Move the "play head" forward one unit. */
    final String MEDIA_STEP_FORWARD = "StepForward";

    /**
     * Halt the presentation of the media.
     * Move the "play head" to the beginning of the media.
     */
    final String MEDIA_STOP = "Stop";

    /** Provide a mechanism to adjust the volume level. */
    final String MEDIA_VOLUME = "Volume";

    /** Move to the previous location. */
    final String NAV_BACK = "Back";

    /** Move to the next location. */
    final String NAV_DOWN = "Down";

    /** Move to the next location . */
    final String NAV_FORWARD = "Forward";

    /** Move to an initial, well-known location. */
    final String NAV_HOME = "Home";

    /** Move to the previous location. */
    final String NAV_UP = "Up";

    /** Remove the current column. */
    final String TAB_COLUMN_DELETE = "ColumnDelete";

    /** Add a new column after the current column. */
    final String TAB_COLUMN_INSERT_AFTER = "ColumnInsertAfter";

    /** Add a new column before the current column. */
    final String TAB_COLUMN_INSERT_BEFORE = "ColumnInsertBefore";

    /** Remove the current row. */
    final String TAB_ROW_DELETE = "RowDelete";

    /** Add a new row after the current row. */
    final String TAB_ROW_INSERT_AFTER = "RowInsertAfter";

    /** Add a new row before the current row. */
    final String TAB_ROW_INSERT_BEFORE = "RowInsertBefore";

    /**
     * Adjust the placement of text such that it is in the middle of its line.
     */
    final String TXT_ALIGN_CENTER = "AlignCenter";

    /** Adjust the placement of text such that it fills its line. */
    final String TXT_ALIGN_JUSTIFY = "AlignJustify";

    /**
     * Adjust the placement of text such that it is along the left edge of its
     * line.
     */
    final String TXT_ALIGN_LEFT = "AlignLeft";

    /**
     * Adjust the placement of text such that it is along the right edge of its
     * line.
     */
    final String TXT_ALIGN_RIGHT = "AlignRight";

    /** Text that is rendered in a thick, heavy variety of type. */
    final String TXT_BOLD = "Bold";

    /** Text that is rendered at a slant. */
    final String TXT_ITALIC = "Italic";

    /** Text without any modifications. */
    final String TXT_NORMAL = "Normal";

    /** Text rendered with a thin line under each glyph. */
    final String TXT_UNDERLINE = "Underline";

    /** Provide information regarding the application. */
    final String ABOUT = "About";

    /** Add an object to an existing set of objects. */
    final String ADD = "Add";

    /**
     * Adjust the placement of an object such that it is along the bottom of its
     * container.
     */
    final String ALIGN_BOTTOM = "AlignBottom";

    /**
     * Adjust the placement of an object such that it is in the middle of its
     * container (along both axes).
     */
    final String ALIGN_CENTER = "GeneralAlignCenter";

    /**
     * Adjust the placement of an object such that it fills the middle of its
     * container along its Y-axis.
     */
    final String ALIGN_JUSTIFY_HORIZONTAL = "AlignJustifyHorizontal";

    /**
     * Adjust the placement of an object such that it fills the middle of its
     * container along its X-axis.
     */
    final String ALIGN_JUSTIFY_VERTICAL = "AlignJustifyVertical";

    /**
     * Adjust the placement of an object such that it is along the left side of
     * its container.
     */
    final String ALIGN_LEFT = "GeneralAlignLeft";

    /**
     * Adjust the placement of an object such that it is along the right side of
     * its container.
     */
    final String ALIGN_RIGHT = "GeneralAlignRight";

    /**
     * Adjust the placement of an object such that it is along the top edge of
     * its container.
     */
    final String ALIGN_TOP = "AlignTop";

    /** Display a list of documents marked for later retrieval. */
    final String BOOKMARKS = "Bookmarks";

    /** Create a new electronic mail message. */
    final String COMPOSE_MAIL = "ComposeMail";

    /** Display information based on where you are in the program. */
    final String CONTEXTUAL_HELP = "ContextualHelp";

    /**
     * Create a duplicate of the selected object.
     * This duplicate is now available to be pasted elsewhere.
     */
    final String COPY = "Copy";

    /**
     * Remove the selected item from its current context.
     * It is now available to be pasted elsewhere.
     */
    final String CUT = "Cut";

    /** Remove the selected item. */
    final String DELETE = "Delete";

    /** Allow the current object to be modified. */
    final String EDIT = "Edit";

    /**
     * Save the object/data in a format other than the application's native
     * format.
     */
    final String EXPORT = "Export";

    /** Locate a specific object. */
    final String FIND = "Find";

    /**
     * Locate the next instance of the object specified by the previous "Find"
     * command.
     */
    final String FIND_AGAIN = "FindAgain";

    /** Provide information which may aid the user. */
    final String HELP = "Help";

    /** Display a list of previously visited locations. */
    final String HISTORY = "History";

    /**
     * Open an object stored in a format other than the application's native
     * format.
     */
    final String IMPORT = "Import";

    /** Display information about an object or task. */
    final String INFORMATION = "Information";

    /** Create a new object. */
    final String NEW = "New";

    /** Open the specified object. */
    final String OPEN = "Open";

    /**
     * Display an editable list of the properties that affect the printing of
     * this information.
     */
    final String PAGE_SETUP = "PageSetup";

    /**
     * Insert an object or data previously placed into a temporary holding area
     * by "Copy" or "Cut".
     */
    final String PASTE = "Paste";

    /** Display, and possibly edit, attributes for the current application. */
    final String PREFERENCES = "Preferences";

    /** Send an object, or set of objects, to a printer. */
    final String PRINT = "Print";

    /** Preview the output that would be generated from the "Print" command. */
    final String PRINT_PREVIEW = "PrintPreview";

    /** Display, and possibly edit, attributes of the current object. */
    final String PROPERTIES = "Properties";

    /** Restore an undone transaction. */
    final String REDO = "Redo";

    /** Update with new data. */
    final String REFRESH = "Refresh";

    /** Remove the selected item from its current context. */
    final String REMOVE = "Remove";

    /** Substitute one object for another. */
    final String REPLACE = "Replace";

    /** Commit an object's changes to a permanent storage area. */
    final String SAVE = "Save";

    /** Commit all object's changes to a permanent storage area. */
    final String SAVE_ALL = "SaveAll";

    /**
     * Save an object to a permanent storage area with properties different than
     * those currently used.
     */
    final String SAVE_AS = "SaveAs";

    /** Locate a specific object. */
    final String SEARCH = "Search";

    /** Deliver an electronic mail message. */
    final String SEND_MAIL = "SendMail";

    /** Halt the execution of a task. */
    final String STOP = "GeneralStop";

    /** Provide a short hint about an application's use. */
    final String TIP_OF_THE_DAY = "TipOfTheDay";

    /** Reverse a transaction. */
    final String UNDO = "Undo";

    /** Change the magnification level used to view an object. */
    final String ZOOM = "Zoom";

    /** Increase the magnification level used to view an object. */
    final String ZOOM_IN = "ZoomIn";

    /** Decrease the magnification level used to view an object. */
    final String ZOOM_OUT = "ZoomOut";

    //---------------------------------------------------------------Constants--
    //--IconSet-----------------------------------------------------------------

    /**
     * Gets all supported icons from an implementation.
     *
     * @return an array of all logical icon names supported by an
     * implementation.
     */
    String[] getSupportedIcons();

    /**
     * Gets an icon for a name and a given size.
     *
     * @param name logical icon name of the icon to return.
     * @param size size of the icon to return.
     * @param locale the locale to be used for the icon or {@code null} for
     * {@code Locale.getDefault()}.
     *
     * @return the icon identified by {@code name} sized to {@code size}.
     *
     * @throws IllegalArgumentException for illegal values of {@code name}
     * and {@code size}.
     *
     * @see IconSet#SIZE_16X16
     * @see IconSet#SIZE_24X24
     */
    ImageIcon getIcon(String name, String size, Locale locale);

    //-----------------------------------------------------------------IconSet--

}
