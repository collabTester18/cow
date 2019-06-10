+package com.smartbear.collaborator.qa.entities.user;

import com.smartbear.collaborator.qa.entities.integrations.RemoteSystemIntegration;
import com.smartbear.collaborator.qa.json.api.commands.user.data.UserInfo;

public class User {
    private UserInfo userData;
    //private Map<Integer, String> remoteMapping;

    public User(UserInfo userData) throws Exception {
        this.userData = userData;
        /*this.remoteMapping = new HashMap<Integer, String>();
        String[] chunks = this.userData.getRemoteMapping().split("\"");
        for (int i = 1; i < chunks.length; i += 4 ) {
            int id = Integer.parseInt(chunks[i]);
            this.remoteMapping.put(id, chunks[i]);
        }*/
        /*ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(this.userData.getRemoteMapping());
        this.remoteMapping = mapper.convertValue(node, Map.class);*/
    }

    public User() {
        this.userData = new UserInfo();
        //this.remoteMapping = new HashMap<Integer, String>();
    }

    public User(String login, String password) {
        this.userData = new UserInfo();
        this.userData.setLogin(login);
        this.userData.setPassword(password);
    }

    public UserInfo getUserData() throws Exception {
        //String mapping = new ObjectMapper().writeValueAsString(this.remoteMapping);
        //this.userData.setRemoteMapping(mapping);
        return this.userData;
    }

    public User setRemoteAccount(RemoteSystemIntegration integration, String remoteSystemUserName) {
        String id = Integer.valueOf(integration.getId()).toString();

        /*this.remoteMapping.put(id, remoteSystemUserName);*/
        this.userData.addRemoteMapping(id, remoteSystemUserName);
        return this;
    }

    public String getLogin() {
        return this.userData.getLogin();
    }

    public User setLogin(String login) {
        this.userData.setLogin(login);
        return this;
    }

    public boolean isEnabled() {
        return this.userData.isEnabled();
    }

    public User setEnabled(boolean enabled) {
        this.userData.setEnabled(enabled);
        return this;
    }

    public String getFullName() {
        return this.userData.getFullName();
    }

    public User setFullName(String fullName) {
        this.userData.setFullName(fullName);
        return this;
    }

    public boolean isAdmin() {
        return this.userData.isAdmin();
    }

    public User setAdmin(boolean admin) {
        this.userData.setAdmin(admin);
        return this;
    }

    public String getEmail() {
        return this.userData.getEmail();
    }

    public User setEmail(String email) {
        this.userData.setEmail(email);
        return this;
    }

    public String getPhone() {
        return this.userData.getPhone();
    }

    public User setPhone(String phone) {
        this.userData.setPhone(phone);
        return this;
    }

    public String getPassword() {
        return this.userData.getPassword();
    }

    public User setPassword(String password) {
        this.userData.setPassword(password);
        return this;
    }
}
