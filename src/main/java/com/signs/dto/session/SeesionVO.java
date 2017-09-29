package com.signs.dto.session;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class SeesionVO implements Serializable {

    private static final long serialVersionUID = -1L;

    private HttpSession httpSession;

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}
