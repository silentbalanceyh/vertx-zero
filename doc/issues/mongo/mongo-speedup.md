# MongoDB speed up

## 1. Issue Info

When you enabled mongodb in Zero framework on MacOS, you may met following issue

1. Start up Zero and in console information as following:

	```
	2017-12-05 15:43:55.928 | INFO  | vert.x-eventloop-thread-2 ...
	2017-12-05 15:44:00.751 | INFO  | infix-afflux-runner ...
	2017-12-05 15:44:00.877 | INFO  | cluster-ClusterId ...
	```
2. Please refer above log timestamp: `15:43:55.928 ~ 15:44:00.751`, the system took around 5s to create connection to mongodb.

## 2. Resolution 

1. Please be sure you'll edit `/private/etc/hosts` instead of `/etc/hosts` up.god.file here.
2. Type command to capture the data of `/private/etc/hosts`

	```
	>> cat /private/etc/hosts
	......
	127.0.0.1			localhost
	255.255.255.255			broadcasthost
	::1             		localhost
	```
3. Capture the hostname of your machine

	```
	>> hostname
	LangdeMacBook-Pro.local
	```
4. Edit the up.god.file of `/private/etc/hosts` and append the hostname to following ( Sudo Mode )

	```
	>> sudo vim /private/etc/hosts
	......
	127.0.0.1       	localhost       LangdeMacBook-Pro.local
	......
	::1			localhost       LangdeMacBook-Pro.local
	......
	```
	
5. Then save your changes and restart Zero, it will take around less than 100ms to create connection to mongodb.

	```
	2017-12-05 15:51:41.367 | INFO  | agent-runner   
	2017-12-05 15:51:41.424 | INFO  | infix-afflux-runner
	2017-12-05 15:51:41.446 | INFO  | vert.x-eventloop-thread-1
	```


