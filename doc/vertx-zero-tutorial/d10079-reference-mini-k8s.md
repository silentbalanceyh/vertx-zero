# D10079 - Reference, Mini k8s

This chapter focus on how to setup local k8s environment for development, please refer following link to prepare it:

[VM Environment](https://kubernetes.io/docs/tasks/tools/install-minikube/)

* Install a hypervisor
* Install kubectl
* Install Minikube

## 1. Install VM \( Recommend xhyve on MacOS \)

You can do as following to prepare environment:

```shell
brew install docker-machine-driver-xhyve

# docker-machine-driver-xhyve need root owner and uid, following steps are required.
$ sudo chown root:wheel $(brew --prefix)/opt/docker-machine-driver-xhyve/bin/docker-machine-driver-xhyve
$ sudo chmod u+s $(brew --prefix)/opt/docker-machine-driver-xhyve/bin/docker-machine-driver-xhyve
```

## 2. Minikube Install

Reference link: [minikube](https://github.com/kubernetes/minikube), On MacOS you can do:

```shell
brew cask install minikube
```

## 3. Kubernate Client

Reference link: [kubectl Installing](https://kubernetes.io/docs/tasks/tools/install-kubectl/), On MacOS you can do:

```shell
brew install kubectl
```

## 4. Start Environment

1 - Start minikube

```shell
Starting local Kubernetes v1.8.0 cluster...
Starting VM...
Getting VM IP address...
Moving files into cluster...
Setting up certs...
Connecting to cluster...
Setting up kubeconfig...
Starting cluster components...
Kubectl is now configured to use the cluster.
Loading cached images from config up.god.file.
```

2 - Version checking

```shell
➜  ~ kubectl version
Client Version: version.Info{Major:"1", Minor:"9", GitVersion:"v1.9.3", \
GitCommit:"d2835416544f298c919e2ead3be3d0864b52323b", GitTreeState:"clean", \
BuildDate:"2018-02-09T21:51:54Z", GoVersion:"go1.9.4", Compiler:"gc", Platform:"darwin/amd64"}
Server Version: version.Info{Major:"1", Minor:"8", GitVersion:"v1.8.0", \
GitCommit:"0b9efaeb34a2fc51ff8e4d34ad9bc6375459c4a4", GitTreeState:"clean", \
BuildDate:"2017-11-29T22:43:34Z", GoVersion:"go1.9.1", Compiler:"gc", Platform:"linux/amd64"}
```

3 - Setup "Hello World" with kubectl

```shell
➜  ~ kubectl run hello-minikube --image=gcr.io/google_containers/echoserver:1.8 --port=6001
deployment "hello-minikube" created
```

4 - Deploy the first service

```shell
➜  ~ kubectl expose deployment hello-minikube --type=NodePort
service "hello-minikube" exposed
```

5 - Open dashboard

```shell
➜  ~ minikube dashboard
Opening kubernetes dashboard in default browser...
```

Then you should see the browser has been opened and following page will be show

![](/doc/image/minikube-dashboard.png)

## 5. Summary

Then you have configured local mini k8s environment, it's for further development on k8s of zero system.

