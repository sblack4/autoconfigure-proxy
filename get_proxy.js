/* Steven Black github.com/sblack4
 * Configure your system for your proxy!
 * 1. Find proxy 
 * 2. Set proxy / lack thereof
 * 3. update Services (npm, github, docker, etc...)
 * WARNING this is configured for windows
 *   in unix it's export <variable>=<enviormental variable> not set
 */
const http = require('http');
const fs = require('fs');
const exec = require('child_process').exec;


/**
 * Writes the proxy config file locally
 * @param {*body of file} datFileBody 
 */
function writeFile(datFileBody) {
    console.log(`Writing ${url} to ${writeToFile}`);
    try {
        fs.writeFile(writeToFile, datFileBody, function(error) {
            if (error) throw error;
        });
    } catch (exception) {
        console.log(`Unable to write local file (that's okay), exception ${exception}`);
    }
}

/**
 * gets a list of services installed on this machine
 * git, npm, docker, pip
 * @param {*the path (from this directory)} path_string 
 */
function getServices(path_string) {
    let services = [
        "git",
        "npm",
        "docker",
        "pip"
    ];

    return services.filter((service) => {
        return path_string.includes(`\\${service}`);
    })
}

/**
 * returns commands to set proxy
 * for different programs
 * @param {*proxy url} proxy 
 */
function setProxyCommands(proxy) {
    const npm = `npm config set http-proxy ${proxy}`;
    const git = `git config --global http.proxy ${proxy}`;
    // docker, pip supposedly use this
    const enviormental = `SET HTTP_PROXY=${proxy}`;
    // const pip = `pip --proxy ${proxy}`;
    return [npm, git, enviormental];
}

/**
 * returns commands to remove proxy
 * for different programs
 */
function removeProxyCommands() {
    const npm = "npm config rm http-proxy";
    const git = "git config --global --unset http.proxy"
        // TODO: NO_PROXY for enviorment/docker
        // or if it's "setx FOOBAR "" " to set
        // then "REG delete HKCU\Environment /F /V FOOBAR" to unset 
        // https://stackoverflow.com/questions/13222724/command-line-to-remove-an-environment-variable-from-the-os-level-configuration
    const enviormental = "";

    return [npm, git, enviormental];
}

/**
 * runs array of commands to console
 * @param {*Array<string> of console commands} consoleComands 
 */
function runCommands(consoleComands) {
    consoleComands.forEach(function(command) {
        console.log(`executing '${command}'`);
        exec(command, (error, stdout, stderr) => {
            if (error) {
                console.error(`error executing '${command}' \n ${error}`);
            }
            if (stderr) {
                console.log(`${stdout}`);
                
            }
            if (stdout) {
                console.log(`${stderr}`);                
            }
        });
    });
}

/**
 * retrieves the proxy address
 *  @param {*url of pac file} url 
 */
function getProxy(url) {
    let proxy_name = "";
    const proxyRegex = new RegExp(/proxies = \"PROXY (.+?(?=;))/); //regex to get the proxy


    return new Promise(function(resolve, reject) {
        var request = http.request(url, function(response) {
            let datFileBody = ""; // but the file body here 
            console.log(`Requesting ${url}...`);

            response.setEncoding('utf8');

            response.on('data', (chunk) => {
                datFileBody += chunk;
                if (chunk.includes("proxies = \"PROXY ")) {
                    const matches_array = chunk.match(proxyRegex);
                    proxy_name = matches_array[1];

                    console.log(`Found Proxy! it's ${proxy_name} \n  (use http://${proxy_name} though)`);
                }
            });

            response.on("end", () => {
                // writeFile(datFileBody);
                resolve("http://" + proxy_name);
            });
        });

        request.on('error', (error) => {
            console.log(`There was an error with the request: ${error}`);
            reject("");
        });

        // make request
        request.end();
    });
}


/**
 * runs the program
 */
if (require.main === module) {

    const url = "http://wpad/wpad.dat"; // file the proxy is in
    const writeToFile = "wpdat.dat"; // local file name

    getProxy(url).then((response, error) => {
        if (error) {
            console.log("error \n " + error);
            return;
        }

        // const path = process.env.path;
        // const installedServices = getServices(path);
        console.log("Adding proxy to services");
        runCommands(setProxyCommands(response));

    }).catch(() => {
        console.log("Removing proxy from services");
        runCommands(removeProxyCommands());
    });
}