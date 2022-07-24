# DownloadManager

A simple download manager

features:
- Support for Multiple concurrent downloads
- Support for protocols HTTP/HTTPS, FTP and SFTP
- Support retry download failures

## Build from Source

### Prerequisites
- JDK 11
- Maven 3.8.x

### Steps to build
1. Get a clone from the source from github
    ```shell
   git clone https://github.com/CharukaK/cli-download-manager.git 
   ```
2. Run the command `mvn clean install` from the root directory


## Using the tool
Once you have built the tool you can find the distribution inside the `target` folder extract the zip called `downloadman.zip`. Once you extract that you can run the program using the `downloadman.sh` file.

The command takes the following format
```shell
./downloadman.sh [config-options] -a [url1] -a [url2] ....
```

following config options are supported at the moment:
- `--retry-count` = number of retries per download
- `--retry-interval` = amount of time in milliseconds to wait until next retry
- `--output-dir` = Output directory for the downloaded files

Note: URLs for downloads should be provided with `-a` arguments, you can submit any number of downloads

URLs for SFTP and FTP should follow the format

- FTP
   `ftp://<user>:<password>@<host>:<port>/<filepath>`
- SFTP
  `sftp://<user>:<password>@<host>:<port>//<filepath>`

