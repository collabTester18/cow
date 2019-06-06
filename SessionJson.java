package com.smartbear.collaborator.qa.json;

import com.smartbear.collaborator.qa.json.api.JsonAction;
import com.smartbear.collaborator.qa.json.api.commands.sessionService.Authenticate;

import java.io.IOException;

public class SessionJson {
    public static void login(String serverUrl, String login, String password) throws IOException {
        JsonAction.initialize(serverUrl, login, password);
        JsonAction.getLoginTicket();
    }
}
