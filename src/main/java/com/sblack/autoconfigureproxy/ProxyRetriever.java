package com.sblack.autoconfigureproxy;

import com.github.markusbernhardt.proxy.*;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Gets proxy or proxy (pac) file
 */
public class ProxyRetriever {
    private List<Proxy> proxyList;
    private ProxySelector proxySelector;
    private ProxySearch proxySearch;

    public ProxyRetriever() {
        this.proxySearch = ProxySearch.getDefaultProxySearch();
        this.proxySelector = proxySearch.getProxySelector();
    }

    /**
     * Detect proxy with default URI (https://www.google.com).
     */
    private void DetectProxy() {
        if(this.proxyList == null){
            DetectProxy("http://www.google.com");
        }
    }

    /**
     * Detect proxy Overload.
     *
     * @param testUri the test uri
     */
    private void DetectProxy(String testUriString) {
        System.setProperty("java.net.useSystemProxies","true");
        try {
            URI testUri = new URI(testUriString);
            this.proxyList = this.proxySelector.select(testUri);
        } catch (URISyntaxException ex) {
            System.out.println("Malformed URI string, try 'http://www.google.com'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display host names to console
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

    public List<Proxy> getProxyList() {
        DetectProxy();
        return proxyList;
    }

    public Proxy getProxy() {
        DetectProxy();
        return proxyList.get(0);
    }

    public Proxy getProxy(String testURI) {
        DetectProxy(testURI);
        return proxyList.get(0);
    }

    /**
     * Get proxy as a string.
     *
     * @return the proxy (host:port), i.e. www-proxy.example.com:80
     */
    public String getProxyAddress(){
        DetectProxy();
        return proxyList.get(0).address().toString();
    }

    public String getProxyType() {
        DetectProxy();
        return proxyList.get(0).type().toString();
    }

    public void testProxy(){

    }
}
