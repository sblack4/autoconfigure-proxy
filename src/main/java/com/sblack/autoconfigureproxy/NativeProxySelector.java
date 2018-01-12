/** NativeProxySelector
 *
 * There is currently no good way of reliably getting cross-platform proxy settings in java
 * java.net.ProxySelector seems to work well for Mac & linux systems but
 * Windows will return "DIRECT" no matter the proxy
 *
 * The approach used here is simply extend ProxySelector to
 * provide functionality on windows. Often the proxy is set in
 * the windows registry.
 */

package com.sblack.autoconfigureproxy;

import java.net.Proxy;
import java.util.List;

public class NativeProxySelector {

    private static OsProxySelector proxySelector;

    NativeProxySelector() {

        String os = System.getProperty("os.name")
                .toLowerCase();
        // return appropriate proxy selector
        if(isWindows(os)) {
            proxySelector = new WindowsProxySelector();
        } else if (isMac(os)) {
            //
        } else if (isUnix(os)) {

        } else if (isSolaris(os)) {

        }

    }

    private boolean isWindows(String os) {
        return os.contains("win");
    }
    public static boolean isMac(String os) {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix(String OS) {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

    }

    public static boolean isSolaris(String OS) {

        return (OS.indexOf("sunos") >= 0);

    }


    public List<Proxy> getProxyList() {
        try {
            return proxySelector.getProxyList();
        } catch (Exception ex) {

        }

    }

}
