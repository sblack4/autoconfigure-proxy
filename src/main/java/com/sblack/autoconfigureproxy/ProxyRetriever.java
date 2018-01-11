package com.sblack.autoconfigureproxy;

import sun.net.spi.DefaultProxySelector;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

            Properties props =  System.getProperties();

            Map<String, String> props1 =  System.getenv();

            ProxySelector defaultProxy = ProxySelector.getDefault();

            System.out.println("wat");

            ProxySelector np = new DefaultProxySelector();

            List<Proxy> psps = np.select(testUri);

            System.out.println("wat");

            List<Proxy> plist = defaultProxy.select(testUri);

            this.proxyList = ProxySelector.getDefault().select(testUri);

            System.out.println("wat");

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
