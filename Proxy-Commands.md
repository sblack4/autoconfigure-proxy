# Proxy Commands for Common Services

This is not a comprehensive list, nor does it cover every way these 
services can be configured with a proxy. A quick google search or 
a skim of the documentation can reveal wonderous things! 


## Setting Proxies 
* npm: `npm config set http-proxy <PROXY_ADDRESS>`
* github: `git config --global http.proxy <PROXY_ADDRESS>`
* docker: Docker Menu -> Proxies -> Web Server. Then restart docker
    * In container: `export http_proxy="<PROXY_ADDRESS>"`
    * As a build-arg: `--build-arg http_proxy=<PROXY_ADDRESS>`
* pip: `pip install --proxy <PROXY_ADDRESS> <PACKAGE>`
* ssh 
` ssh -i <KEY> <USER>@<REMOTE_ADDRESS> -o "ProxyCommand=nc -X connect -x PROXYHOST:PROXYPORT %h %p"`
* ssh port-forward 
` ssh -i <KEY> -L <LOCAL_PORT>:<REMOTE_ADDRESS>:<REMOTE_PORT> <USER>@<REMOTE_ADDRESS> -o "ProxyCommand=nc -X connect -x PROXYHOST:PROXYPORT %h %p"`
* ssh on putty: click Connections -> Proxy -> select `Http`, enter proxy and port
* 

## Unsetting Proxies 
* npm: `npm config rm http-proxy`
* github: `git config --global --unset http.proxy `
* docker: Docker Menu -> Proxies -> None. Then restart docker
* pip doesn't store proxy info
* ssh can store information in a ssh config file
    * for more info, see [www.cyberciti.biz/faq/create-ssh-config-file-on-linux-unix](https://www.cyberciti.biz/faq/create-ssh-config-file-on-linux-unix/)
    * on windows I suggest using [MobaXterm](http://mobaxterm.mobatek.net/), 
    this gives you the full power of ssh as well as a plethora of other tools