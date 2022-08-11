package dev.codetarget.exemploconfigapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dadosapi")
public class ApiFactoryConfig {

    private String hostApi;
    private String userApi;
    private String passwordApi;
    private String endPointAuth;
    private String endPointCategorias;

    public ApiFactoryConfig() { /* TODO document why this constructor is empty */ }

    public String getHostApi() {
        return hostApi;
    }

    public void setHostApi(String hostApi) {
        this.hostApi = hostApi;
    }

    public String getUserApi() {
        return userApi;
    }

    public void setUserApi(String userApi) {
        this.userApi = userApi;
    }

    public String getPasswordApi() {
        return passwordApi;
    }

    public void setPasswordApi(String passwordApi) {
        this.passwordApi = passwordApi;
    }

    public String getEndPointAuth() {
        return endPointAuth;
    }

    public void setEndPointAuth(String endPointAuth) {
        this.endPointAuth = endPointAuth;
    }

    public String getEndPointCategorias() {
        return endPointCategorias;
    }

    public void setEndPointCategorias(String endPointCategorias) {
        this.endPointCategorias = endPointCategorias;
    }

}
