package io.vertx.up.uca.micro.center;

interface Info {

    String ETCD_STATUS = "( Etcd Center ) The status {0} of service {1} " +
        "has been registry to {2}";

    String ETCD_CLEAN = "( Etcd Center ) The service {0} has been clean up from {1}";

    String ETCD_READ = "( Etcd Catalog ) Read the path {0} service lists.";

    String ETCD_ROUTE = "( Etcd Catalog ) The following routes has been push to:" +
        "\n\t[ Up Micro ] <Application Name> = \"{0}\"," +
        "\n\t[ Up Micro ] Configuration Path = {1}, " +
        "\n\t[ Up Micro ] Service Name = {2}," +
        "\n\t[ Up Micro ] EndPoint = {3}" +
        "\n\t[ Up Micro ] Route Uris = {4}" +
        "\n\t[ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √";

    String ETCD_IPCS = "( Etcd Catalog ) The following routes has been push to:" +
        "\n\t[ Up Rpc   ] <Application Name> = \"{0}\"," +
        "\n\t[ Up Rpc   ] Configuration Rpc Point = {1}, " +
        "\n\t[ Up Rpc   ] Service Name = {2}," +
        "\n\t[ Up Rpc   ] Ipc Channel = {3}" +
        "\n\t[ Up Rpc   ] Ipc Address = {4}" +
        "\n\t[ Up Rpc   ] √ Successfully to registered IPCs, wait for community......SUCCESS √";
}
