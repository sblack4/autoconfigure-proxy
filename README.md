# Autoconfigure Proxy
## ![sblack4](https://avatars2.githubusercontent.com/u/15880760?v=4&s=40) Steven Black.  On the web at [sblack.rocks](https://sblack.rocks)   

Tired of logging onto the guest network to use npm?  
Constantly resetting proxy addresses got you down?  
Cry no more! There's a script for that!

----
## How to get around a proxy

**TL;DR**...  
install `nodejs`, type `node get_proxy.js` into the command prompt, and hit enter.

Lots of services won't work properly behind a proxy
(e.g. npm, Github, Docker, pip)

There are 3 ways to get around this:  
*note that once you have figured a service to work with a proxy it may not work properly when you are not behind that proxy*

* Log onto the guest wifi every time you want to use one of these services

* Manually get proxy and set variables (and unset them when you aren't working with the proxy):
    1. Go to Control Panel -> Internet Options -> Connections -> LAN Settings
    2. The OBI's internet is configured via a script file which is listed on the page ( e.g. [http://wpad/wpad.dat](http://wpad/wpad.dat))
    3. Download that file
    4. Look for a line that starts with `proxies =`  
    (e.g. `proxies = "PROXY www-proxy-rmdc-new.us.oracle.com:80; PROXY www-proxy-adcq7-new.us.oracle.com:80; DIRECT;";`)
    5. The first value is your proxy  
    (e.g. `http://www-proxy-rmdc-new.us.oracle.com:80`)
    6. Configure your services! This should be a simple comman-line setting
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

**TODO: finish this script/maybe write it in cmd or java which everyone has**
* Automatically get around proxy/ unset variables:
    1. install nodejs
    2. run `node get_proxy.js` in this directory
    3. boom. that's it. 
