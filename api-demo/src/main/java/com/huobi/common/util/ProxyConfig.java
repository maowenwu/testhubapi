package com.huobi.common.util;

public class ProxyConfig {
    /**
     * 是否启用代理
     */
    private Boolean enabled;
    /**
     * 代理主机地址
     */
    private String host;
    /**
     *代理端口
     */
    private Integer port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 代理类型（1.）
     */
    private String proxyType;
    public ProxyConfig(){};
    public ProxyConfig(Boolean enabled, String host, Integer port, String username, String password, String proxyType) {
        this.enabled = enabled;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.proxyType = proxyType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    @Override
    public String toString() {
        return "ProxyConfig{" +
                "enabled=" + enabled +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", proxyType='" + proxyType + '\'' +
                '}';
    }
}
