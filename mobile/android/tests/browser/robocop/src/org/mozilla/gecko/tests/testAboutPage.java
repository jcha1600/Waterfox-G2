/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.gecko.tests;

import org.mozilla.gecko.Actions;

/* Tests related to the about:waterfox page:
 *  - check that about:waterfox loads from the URL bar
 *  - check that about:waterfox loads from Settings/About...
 */
public class testAboutPage extends PixelTest {

    public void testAboutPage() {
        blockForGeckoReady();

        // Load the about:waterfox page and verify its title.
        String url = mStringHelper.ABOUT_SCHEME + "firefox";
        loadAndPaint(url);

        verifyUrlInContentDescription(url);

        // Open a new page to remove the about:waterfox page from the current tab.
        url = getAbsoluteUrl(mStringHelper.ROBOCOP_BLANK_PAGE_01_URL);
        loadUrlAndWait(url);

        // At this point the page title should have been set.
        verifyUrlInContentDescription(url);

        // Set up listeners to catch the page load we're about to do.
        Actions.EventExpecter tabEventExpecter = mActions.expectGlobalEvent(Actions.EventType.UI, "Tab:Added");
        Actions.EventExpecter contentEventExpecter = mActions.expectGlobalEvent(Actions.EventType.UI, "Content:DOMContentLoaded");

        selectSettingsItem(mStringHelper.MOZILLA_SECTION_LABEL, mStringHelper.ABOUT_LABEL);

        // Wait for the new tab and page to load
        tabEventExpecter.blockForEvent();
        contentEventExpecter.blockForEvent();

        tabEventExpecter.unregisterListener();
        contentEventExpecter.unregisterListener();

        // Make sure the about:waterfox page was loaded.
        verifyUrlInContentDescription(mStringHelper.ABOUT_SCHEME + "firefox");
    }
}
