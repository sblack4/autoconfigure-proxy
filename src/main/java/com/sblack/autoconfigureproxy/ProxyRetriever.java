package com.sblack.autoconfigureproxy;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;


/**
 * Gets proxy or proxy (pac) file
 */
public class ProxyRetriever {
    /**
     * The Proxy list.
     */
    private List<Proxy> proxyList;

    /**
     * Instantiates a new Proxy retriever.
     */
    public ProxyRetriever() {
        DetectProxy();
    }

    /**
     * Detect proxy with default URI (https://www.google.com).
     */
    public void DetectProxy() {
        DetectProxy("http://www.google.com");
    }

    /**
     * Detect proxy Overload.
     *
     * @param testUri the test uri
     */
    public void DetectProxy(String testUriString) {
        System.setProperty("java.net.useSystemProxies","true");
        try {
            URI testUri = new URI(testUriString);


            this.proxyList = ProxySelector.getDefault().select(testUri);


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
            try {
                String proxyAddress = (proxy.address() == null) ? "None" : proxy.address().toString();
                System.out.println(String.format(" %d | Type: %s | Proxy: %s", i, proxy.type().toString(), proxyAddress));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            i++;
        }
    }

    public Proxy getProxy() {
        return proxyList.get(0);
    }

    /**
     * Get proxy as a string.
     *
     * @return the proxy (host:port), i.e. www-proxy.example.com:80
     */
    public String getProxyString(){
        return proxyList.get(0).toString();
    }


}
