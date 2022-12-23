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

import java.util.Map;

import com.zimbra.common.account.Key.AccountBy;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AccountConstants;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.Element;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.extension.ZimbraExtensionNotification;
import com.zimbra.cs.util.AccountUtil;
import com.zimbra.soap.DocumentHandler;
import com.zimbra.soap.ZimbraSoapContext;

public class SampleNotificationHandler2 extends ZimbraExtensionNotification {
    @Override
    public void execute(Object[] args) throws ServiceException {
        /*
         * It is responsible for an extension to cast a parameter to a right class
         */
        Element request = (Element) args[0];

        @SuppressWarnings("unchecked")
        Map<String, Object> context = (Map<String, Object>) args[1];


        /*
         * The first part is based on com.zimbra.cs.service.admin.Auth#handle(Element request, Map<String, Object> context)
         */
        ZimbraSoapContext zsc = DocumentHandler.getZimbraSoapContext(context);
        Provisioning prov = Provisioning.getInstance();
        String name = request.getAttribute(AdminConstants.E_NAME, null);
        Element acctEl = request.getOptionalElement(AccountConstants.E_ACCOUNT);
        AccountBy by;
        String acctName = null;
        Element virtualHostEl = request.getOptionalElement(AccountConstants.E_VIRTUAL_HOST);
        String virtualHost = virtualHostEl == null ? null : virtualHostEl.getText().toLowerCase();
        if (name != null) {
            acctName = name;
            by = AccountBy.name;
        } else {
            acctName = acctEl.getText();
            String byStr = acctEl.getAttribute(AccountConstants.A_BY, AccountBy.name.name());
            by = AccountBy.fromString(byStr);
        }
        Account acct = AccountUtil.getAccount(by, acctName, virtualHost, zsc.getAuthToken(), prov);


        /*
         * Add custom validation.
         * In this example, if email address contains "invalid", the access is denied.
         */
        if (acct != null) {
            String accountName = acct.getName();
            ZimbraLog.extensions.info("SampleExt: name=" + accountName);
            if (!"zimbra".equals(accountName) && !accountName.contains("valid-admin")) {
                throw ServiceException.PERM_DENIED("Access Denied. name=" + accountName);
            }
        }
    }
}
