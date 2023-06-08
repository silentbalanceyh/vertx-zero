# Reference, Istio

[Quick Start](https://istio.io/docs/setup/kubernetes/quick-start.html)

Demo example will be install istio to `~/Tool/Zero/`

## 1. Environment Preparing

1 - Install and download istio

```shell
curl -L https://git.io/getLatestIstio | sh -
...... # Wait for ISO up.god.file downloading finished.
```

2 - Set Up environment.

```shell
Add /Users/lang/Tool/Zero/istio-0.2.12/bin to your path; \
    e.g copy paste in your shell and/or ~/.profile:
export PATH="$PATH:/Users/lang/Tool/Zero/istio-0.2.12/bin"
# Then be sure the command "istioctl" could be used. ( Latest 0.2.12 )
istioctl version
Version: 0.2.12
GitRevision: 998e0e00d375688bcb2af042fc81a60ce5264009
GitBranch: release-0.2
User: releng@0d29a2c0d15f
GolangVersion: go1.8
```

3 - Install "istio" on local k8s

```shell
>> cd istio-0.2.12

>> pwd
/Users/lang/Tool/Zero/istio-0.2.12

>> kubectl get svc -n istio-system
No resources found.

>> kubectl apply -f install/kubernetes/istio.yaml ( None TLS mode )
namespace "istio-system" created
...... ( All the progress logs will be output )
deployment "istio-ca" created
```

4 - Ensure the four services: `istio-pilot, istio-mixer, istio-ingress, istio-egress`

```shell
>> kubectl get svc -n istio-system
NAME            TYPE           CLUSTER-IP       ......
istio-egress    ClusterIP      10.97.67.39     ......
istio-ingress   LoadBalancer   10.111.235.49   ......
istio-mixer     ClusterIP      10.106.88.96    ......
istio-pilot     ClusterIP      10.106.188.200  ......
>> kubectl get pods -n istio-system
istio-ca-5cd46b967c-kmx58        1/1       Running   0          4m
istio-egress-56c4d999bc-dv8md    1/1       Running   0          4m
istio-ingress-5747bb855f-n74sz   1/1       Running   0          4m
istio-mixer-77487797f6-d5ns9     2/2       Running   0          4m
istio-pilot-86ddcb7ff5-cmcr5     1/1       Running   0          4m
```

## 2. Summary

Then when you see above output, you can select `isito-system` namespace in the dashboard to check the result:

![](/doc/image/istio-system.png)When you see above screen shot it means that the istio has been run in mini k8s
environment.

