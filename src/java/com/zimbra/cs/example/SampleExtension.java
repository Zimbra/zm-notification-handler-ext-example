/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2022 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.cs.example;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.extension.ZimbraExtension;
import com.zimbra.cs.extension.ZimbraExtensionNotification;

public class SampleExtension implements ZimbraExtension {

    /**
     * Defines a name for the extension. It must be an identifier.
     *
     * @return extension name
     */
    @Override
    public String getName() {
        return "sampleExtension";
    }

    /**
     * Initializes the extension. Called when the extension is loaded.
     *
     * @throws com.zimbra.common.service.ServiceException
     */
    public void init() throws ServiceException {
        /*
         * specify a notification ID in the first argument
         */
        ZimbraExtensionNotification.register("com.zimbra.cs.service.account.Auth:validate", new SampleNotificationHandler());
        ZimbraExtensionNotification.register("com.zimbra.cs.service.admin.Auth:validate", new SampleNotificationHandler2());
    }

    /**
     * Terminates the extension. Called when the server is shut down.
     */
    @Override
    public void destroy() {
        ZimbraExtensionNotification.unregister("com.zimbra.cs.service.account.Auth:validate");
        ZimbraExtensionNotification.unregister("com.zimbra.cs.service.admin.Auth:validate");
    }
}
