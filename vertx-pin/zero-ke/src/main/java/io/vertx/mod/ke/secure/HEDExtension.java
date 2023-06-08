package io.vertx.mod.ke.secure;

import io.horizon.atom.common.KPair;
import io.horizon.spi.cloud.HED;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HEDExtension implements HED {
    private static final String KEY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADC" +
        "BiQKBgQCgCziGJcS801iyB1qVoNh1V5acAKieJP2BG8tz+nQnNYwYkFyxuJCTdWmd9Fa" +
        "WfNvCjM0NjHDFF1q36hg9js9+2axeiUMFMC0XHuf+rhfy7pGFz2ZNon0oaK3PvTiAQ1n" +
        "Uau247IQtUOQWmXqF+0hfxI40JLjRo8nOUgQN+ddFqwIDAQAB";
    private static final String KEY_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAA" +
        "SCAl8wggJbAgEAAoGBAKALOIYlxLzTWLIHWpWg2HVXlpwAqJ4k/YEby3P6dCc1jBiQXL" +
        "G4kJN1aZ30VpZ828KMzQ2McMUXWrfqGD2Oz37ZrF6JQwUwLRce5/6uF/LukYXPZk2ifS" +
        "horc+9OIBDWdRq7bjshC1Q5BaZeoX7SF/EjjQkuNGjyc5SBA3510WrAgMBAAECfzmDzs" +
        "xmxk0VpspT2jnrk96Xm4h0tCQhJvk/A96qIxP0KmSpfKZhrZNQnqQlKvjp5hAEiaxUzD" +
        "IIMpHljtCQggCCAi2du56dC1pp+/Qj9NCE/rNs5hYDnnzaEJoMsCPOVXAhr57zDa9d9t" +
        "M6PFNp026cqJkfKDq4oqinPXGLlOECQQDfv+ANrvgurGYUnJYyY7pvNooAEVItHsvW4N" +
        "0fhJRYG20yCP08GZflS3i7Aga+wZkuo3c0RG4ZULT+Qyxbf1PXAkEAtxysDIV7WLLfEb" +
        "Qfyd4VyDvC/Pe7s2gYegCOH63USIjazAbQLoo1pDl5KW6cCgRsUuHOPwx0kzJ56ku+Cs" +
        "uiTQJBAKH8MWL2pypzGTwW2Q4F8TUOkP3XjFyqDPy1sqBcxWC8CmPM6jiWdsdDylXo86" +
        "UqV5HSOjyWxfaB3WaujvB2z+UCQQCaTjwh9R4jeeCG765WE6NxcAL539256r+tl+gsg/" +
        "S/ByyyAA/RGEDp2lDi/EXcHywVkBmYget+22tnaFqGiGGFAkBZMX9u8+9PXwnPrdVLGF" +
        "jTMyBn8slBcmBIMFD/dQkeBlVJxQCEA9dMtcb+OF7463CTTljNJdoDnbHvXScl4hMb";

    @Override
    public KPair loadRSA() {
        return new KPair(KEY_PUBLIC, KEY_PRIVATE);
    }
}
