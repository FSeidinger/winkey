package de.do9fse.winkey.lib.core.port.in;

import de.do9fse.winkey.lib.core.model.responses.Response;

@FunctionalInterface
public interface ResponseListener {
    /**
     * Callback interface to receive responses
     *
     * <p>Responses are generated either through active commands or unsolicited status responses</p>
     * @param response A received response
     */
    void onResponse(Response response);
}
