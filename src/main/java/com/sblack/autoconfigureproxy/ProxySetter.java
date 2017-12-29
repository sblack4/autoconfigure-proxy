package com.sblack.autoconfigureproxy;

public class ProxySetter {
    private Proxy proxy;

    public ProxySetter(Proxy proxy){
        this.proxy=proxy;
    }

    private void setAll(){}
    private void setNPM(){}
    private void setSSH(){}
    private void setSVN(){}
    private void setPIP(){}
    private void setDocker(){}
    private void setEnvironment(){}

    private void unsetAll(){}
    private void unsetNPM(){}
    private void unsetSSH(){}
    private void unsetSVN(){}
    private void unsetPIP(){}
    private void unsetDocker(){}
    private void unsetEnvironment(){}

}
