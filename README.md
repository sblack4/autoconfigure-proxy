# Autoconfigure Proxy
### ![sblack4](https://avatars2.githubusercontent.com/u/15880760?v=4&s=40) Steven Black.  On the web at [sblack.rocks](https://sblack.rocks)   

Tired of logging onto the guest network to use npm?  
Constantly resetting proxy addresses got you down?  
Cry no more! There's a script for that!

----
### Prerequisite: 
This guid was written for windows users behind an http proxy configured with a pac file, 
i.e. anyone in a corporate setting

That being said there are a few different ways of configuring proxies. 
This is most common for big corporate enviorments but if you need help with a different
type of proxy open an issue and I'd be more than happy to address it here :smiley:

----
## How to "get around" a proxy

Lots of services won't work properly behind a proxy
(e.g. npm, Github, Docker, pip)

There are 3 ways to get around this:  
*note that once you have figured a service to work with a proxy it may not work properly when you are not behind that proxy*

### 1. Log onto the guest wifi every time you want to use one of these services

### 2. Manually get proxy and set variables (and unset them when you aren't working with the proxy):

1. Go to Control Panel -> Internet Options -> Connections -> LAN Settings
2. Look at how your proxy is configured, it probably script  
![](images/LAN-Settings.PNG)
3. Download that file
4. Look for a line that starts with `proxies =`  
(e.g. `proxies = "PROXY www-proxy.us.big-corp.com:80; PROXY www-proxy2.us.big-corp.com:80; DIRECT;";`)
5. The first value is your proxy  
(e.g. `http://www-proxy.us.big-corp.com:80`)
6. Run the commands as listed in [Proxy-Commands](Proxy-Commands.md) 

<!-- configure your services! This should be a simple comman-line setting
    * npm: `npm config set http-proxy <PROXY_ADDRESS>`
    * github: `git config --global http.proxy <PROXY_ADDRESS>`
    * docker: Docker Menu -> Proxies -> Web Server. Then restart docker
        * In container: `export http_proxy="<PROXY_ADDRESS>"`
        * As a build-arg: `--build-arg http_proxy=<PROXY_ADDRESS>`
        * In Dockerfile:

        ```docker
        FROM ubuntu

        ENV http_proxy "<PROXY_ADDRESS>"
        ENV https_proxy "<PROXY_ADDRESS>"
        ```

    * pip: **TODO: haven't tried pip yet**

**TODO: finish this script/maybe write it in cmd or java which everyone has** -->
### 3. Automatically get around proxy/ unset variables:
1. install nodejs
2. run `node get_proxy.js` in this directory
3. boom. that's it. 
