package com.sblack.autoconfigureproxy;

import java.net.InetAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.List;

public abstract class OsProxySelector {

    public abstract List<Proxy> getProxyList();


    public boolean testURI(URI uriInQuestion) throws Exception {
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
}
