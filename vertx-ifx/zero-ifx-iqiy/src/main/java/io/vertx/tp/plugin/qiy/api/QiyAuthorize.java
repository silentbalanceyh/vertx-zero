package io.vertx.tp.plugin.qiy.api;

import feign.Param;
import feign.RequestLine;
import io.vertx.core.json.JsonObject;

public interface QiyAuthorize {

    @RequestLine("GET /iqiyi/authorize?client_id={client_id}&client_secret={client_secret}")
    JsonObject authorize(@Param("client_id") String clientId,
                         @Param("client_secret") String clientSecret);

    @RequestLine("GET /oauth2/token?client_id={client_id}&grant_type=refresh_token&refresh_token={refresh_token}")
    JsonObject refreshToken(@Param("client_id") String clientId,
                            @Param("refresh_token") String refreshToken);
}
