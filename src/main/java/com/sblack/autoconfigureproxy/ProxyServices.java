package com.sblack.autoconfigureproxy;

/**
 * No static (& non-nested) classes in java apparently
 * Thank you stack overflow https://stackoverflow.com/questions/7486012/static-classes-in-java
 * This class contains all the services for which we will want to set, unset, and etc the proxy 
 */
public final class ProxyServices {
     abstract class ProxyService {
        String name;
        String setCommand;
        String unsetCommand;
        public boolean inPath() {
            return false;
        }
    }

    class npm extends ProxyService {

    }
}
