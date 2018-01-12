package com.sblack.autoconfigureproxy;

import com.sun.jna.platform.win32.Advapi32Util;

import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

public class WindowsProxySelector extends OsProxySelector {

    public WindowsProxySelector() {
    }

    private Map<String, Object> getInternetSettings() {
        final String REG_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings";
        return Advapi32Util.registryGetValues(HKEY_CURRENT_USER,REG_PATH);
    }

    public List<Proxy> getProxyList(String defaultUri){

        return new ArrayList<Proxy>();
    }

    public List<Proxy> getProxyList() {
        try {
            return getProxyList(defaultUri);
        } catch (URISyntaxException uEx) {
            // this won't happen... right?
            uEx.printStackTrace();
        }
        return new ArrayList<Proxy>();
    }

}
