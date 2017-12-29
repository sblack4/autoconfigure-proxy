package com.sblack.autoconfigureproxy;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/**
 * Gets proxy or proxy file
 */
public class ProxyRetriever {
    List<Proxy> proxyList;

    public ProxyRetriever() {
        DetectProxy();
    }

    public void DetectProxy() {
        DetectProxy("https://www.google.com");
    }

    public void DetectProxy(String testUri) {
        System.setProperty("java.net.useSystemProxies","true");

        try {
            this.proxyList = ProxySelector.getDefault().select(new URI(testUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayHostnames() {
        int i = 1;
        for(Proxy proxy : proxyList) {
            String proxyAddress = (proxy.address() == null) ? "None" : proxy.address().toString();
            System.out.println(String.format(" %d | Type: %s | Proxy: %s", i, proxy.type().toString(), proxyAddress));
            i++;
        }
    }

    public String getProxy(){
        return proxyList.get(0).toString();
    }
}
