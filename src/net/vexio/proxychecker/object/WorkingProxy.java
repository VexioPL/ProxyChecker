package net.vexio.proxychecker.object;

import java.net.Proxy;

public class WorkingProxy {

    private final Proxy proxy;
    private final long ping;

    public WorkingProxy(Proxy proxy, long ping) {
        this.proxy = proxy;
        this.ping = ping;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public long getPing() {
        return ping;
    }
}
