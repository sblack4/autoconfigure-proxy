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

import java.net.*;
import java.util.ArrayList;
import java.util.Map;
import com.sun.jna.platform.win32.Advapi32Util;
import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import java.io.IOException;
import java.util.List;

public class NativeProxySelector {

    private static ProxySelector proxySelector;

    NativeProxySelector() {

    }

    private Map<String, Object> getInternetSettings() {
        final String REG_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
        return Advapi32Util.registryGetValues(HKEY_CURRENT_USER,REG_PATH);
    }

    private boolean isWindows() {
        return System.getProperty("os.name")
                .toLowerCase()
                .contains("win");
    }

    public List<Proxy> getProxyList(){
        final String defaultUri = "https://www.google.com";

        try {
            return getProxyList(defaultUri);
        } catch (URISyntaxException uEx) {
            // this won't happen... right?
            uEx.printStackTrace();
        }
        return new ArrayList<>();
    }

    private boolean testURI(URI uriInQuestion) throws Exception {
        String protocol = uriInQuestion.getScheme();
        String host = uriInQuestion.getHost();
        //int port = uriInQuestion.getPort();

        switch(protocol) {
            case "ftp":
                // wat?
            case "http":
            case "https":
                // internet.. okay good
                return InetAddress.getByName(host).isReachable(20);
            case "file":
                // why?!
                throw new IllegalArgumentException("file? really? ");
            default:
                return InetAddress.getByName(host).isReachable(20);
        }
    }

    public List<Proxy> getProxyList(String uri) throws URISyntaxException {

        List<Proxy> proxyList;

        if(isWindows()){

        } else {
            proxyList = ProxySelector.getDefault().select(new URI(uri));
        }
        return proxyList;
    }


}
