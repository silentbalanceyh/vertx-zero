package io.vertx.up.plugin.ali.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.error.sms._424MessageSendException;
import io.vertx.up.error.sms._424ProfileEndPointException;
import io.vertx.up.fn.Fn;

public class SmsClientImpl implements SmsClient {

    private static final Annal LOGGER = Annal.get(SmsClientImpl.class);

    private transient final Vertx vertx;
    private transient SmsConfig config;
    private transient IAcsClient client;

    SmsClientImpl(final Vertx vertx, final SmsConfig config) {
        this.vertx = vertx;
        this.config = config;
        this.initClient();
    }

    private void initClient() {
        // Extract data from config
        final JsonObject params = this.config.getConfig();
        Fn.runAt(() -> {
            // Set default timeout
            final String connect = params.containsKey(SmsConfig.TIMEOUT_CONN) ?
                params.getInteger(SmsConfig.TIMEOUT_CONN).toString() : "10000";
            final String read = params.containsKey(SmsConfig.TIMEOUT_READ) ?
                params.getInteger(SmsConfig.TIMEOUT_READ).toString() : "10000";
            System.setProperty("sun.net.client.defaultConnectTimeout", connect);
            System.setProperty("sun.net.client.defaultReadTimeout", read);
            // AscClient initialized.
            final IClientProfile profile = DefaultProfile.getProfile(SmsConfig.DFT_REGION,
                this.config.getAccessId(), this.config.getAccessSecret());
            try {
                DefaultProfile.addEndpoint(SmsConfig.DFT_REGION, SmsConfig.DFT_REGION,
                    SmsConfig.DFT_PRODUCT, this.config.getDomain());
            } catch (final ClientException ex) {
                Fn.outWeb(true, LOGGER,
                    _424ProfileEndPointException.class,
                    this.getClass(), ex);
            }
            this.client = new DefaultAcsClient(profile);
        }, params);
    }

    @Override
    public SmsClient init(final JsonObject params) {
        this.config = SmsConfig.create(params);
        this.initClient();
        return this;
    }

    @Override
    public SmsClient send(final String mobile, final String tplCode, final JsonObject params,
                          final Handler<AsyncResult<JsonObject>> handler) {
        final SendSmsRequest request = this.getRequest(mobile, this.config.getTpl(tplCode), params);
        handler.handle(this.getResponse(request));
        return this;
    }

    private Future<JsonObject> getResponse(final SendSmsRequest request) {
        try {
            final SendSmsResponse response = this.client.getAcsResponse(request);
            final JsonObject data = new JsonObject();
            data.put(SmsConfig.RESPONSE_REQUEST_ID, response.getRequestId());
            data.put(SmsConfig.RESPONSE_BUSINESS_ID, response.getBizId());
            data.put(SmsConfig.RESPONSE_CODE, response.getCode());
            data.put(SmsConfig.RESPONSE_MESSAGE, response.getMessage());
            return Future.succeededFuture(data);
        } catch (final ClientException ex) {
            Fn.outWeb(true, LOGGER,
                _424MessageSendException.class,
                this.getClass(), ex);
            return Future.failedFuture(ex);
        }
    }

    private SendSmsRequest getRequest(final String mobile, final String tplCode, final JsonObject params) {
        final SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);
        request.setSignName(this.config.getSignName());
        request.setTemplateCode(tplCode);
        request.setTemplateParam(params.encode());
        return request;
    }
}
