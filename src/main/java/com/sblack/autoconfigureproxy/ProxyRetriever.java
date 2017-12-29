package com.sblack.autoconfigureproxy;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.net.InetSocketAddress;

/**
 * Gets proxy or proxy (pac) file
 */
public class ProxyRetriever {
    /**
     * The Proxy list.
     */
    List<Proxy> proxyList;

    /**
     * Instantiates a new Proxy retriever.
     */
    public ProxyRetriever() {
        DetectProxy();
    }

    /**
     * Detect proxy.
     */
    public void DetectProxy() {
        DetectProxy("https://www.google.com");
    }

    /**
     * Detect proxy.
     *
     * @param testUri the test uri
     */
    public void DetectProxy(String testUriString) {
        System.setProperty("java.net.useSystemProxies","true");

        try {
            URI testUri = new URI(testUriString);

            ProxySelector defaultProxy = ProxySelector.getDefault();
            List<Proxy> plist = defaultProxy.select(testUri);
            InetSocketAddress adr = (InetSocketAddress) plist.get(0).address();


            this.proxyList = ProxySelector.getDefault().select(testUri);
            System.out.println(adr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display hostnames to console
     */
    public void DisplayHostnames() {
        int i = 1;
        for(Proxy proxy : proxyList) {
            String proxyAddress = (proxy.address() == null) ? "None" : proxy.address().toString();
            System.out.println(String.format(" %d | Type: %s | Proxy: %s", i, proxy.type().toString(), proxyAddress));
            i++;
        }
    }

    /**
     * Get proxy string.
     *
     * @return the string
     */
    public String getProxy(){
        return proxyList.get(0).toString();
    }
}
