# Reference, Istio Addon

If you want to enable metrics collection, you can do as following:

[Reference](https://istio-releases.github.io/v0.1/docs/tasks/installing-istio.html)

```shell
kubectl apply -f install/kubernetes/addons/prometheus.yaml
kubectl apply -f install/kubernetes/addons/grafana.yaml
kubectl apply -f install/kubernetes/addons/servicegraph.yaml
```

It may take some time to process all the components installed.

## 1. Install Grafana

1 - You can type following command to configure port-forwarding for `grafana`

```shell
kubectl -n istio-system port-forward \
    $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') \
        3000:3000 &
```

2 - Then open uri `http://localhost:3000/dashboard/db/istio-dashboard` with your browser, you should see following
page:![](/doc/image/istio-grafana.png)

## 2. Install Service Graph

1 - You can type following command to configure port-forwarding for `servicegraph`

```shell
kubectl -n istio-system port-forward \
    $(kubectl get pod -n istio-system -l app=servicegraph -o jsonpath='{.items[0].metadata.name}') \
        8088:8088 &
```

2 - The open url `http://localhost:8088/dotviz` or `http://localhost:8088/graph` with your browser, if there exist the
services, you should see JSON data or graph.

## 3. Zipkin Dashboard

1 - You can type following command to configure port-forwarding for `zipkin`

```shell
kubectl -n istio-system port-forward \
    $(kubectl get pod -n istio-system -l app=zipkin -o jsonpath='{.items[0].metadata.name}') \
        9411:9411 &
```

2 - Then open uri `http://localhost:9411/zipkin/` with your browser, you should see following
page:![](/doc/image/istio-zipkin.png)

## 4. Prometheus

1 - You can type following command to configure port-forwarding for `prometheus`

```shell
 kubectl -n istio-system port-forward \
     $(kubectl -n istio-system get pod -l app=prometheus -o jsonpath='{.items[0].metadata.name}') \
         9090:9090 &
```

2 - Then open uri `http://localhost:9090/graph` with your browser, you should see following page:

![](/doc/image/istio-prome.png)

## 5. Summary

Then the istio addon environment has been finished and prepared.

