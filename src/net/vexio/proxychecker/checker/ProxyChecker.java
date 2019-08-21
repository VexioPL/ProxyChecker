package net.vexio.proxychecker.checker;

import net.vexio.proxychecker.comparator.ProxyComparator;
import net.vexio.proxychecker.object.WorkingProxy;
import net.vexio.proxychecker.object.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ProxyChecker
 * @author VexioPL
 * @version 1.0.1
 * Kod moze nie jest super, ale dziala i to szybko.
 */

public class ProxyChecker {

    private final List<Proxy> proxies = new ArrayList<>();
    private final List<WorkingProxy> workingProxies = new ArrayList<>();
    private final Writer writer = new Writer();

    public void main(String[] args) {
        try {
            final Scanner scanner = new Scanner(new File("proxies.txt"));
            while (scanner.hasNext()) {
                String[] split = scanner.next().split(":");
                proxies.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1]))));
            }
            System.out.println("[ProxyChecker] Zaladowano " + proxies.size() + " proxy!");
            final ExecutorService es = Executors.newFixedThreadPool(20);
            proxies.forEach(proxy -> this.proxyIsWorking(es, proxy, 500));
            es.shutdown();
            System.out.println("[ProxyChecker] Znaleziono " + workingProxies.size() + " dzialajacych proxy! (timeout 500ms)");
            System.out.println("[ProxyChecker] Trwa sortowanie...");
            this.workingProxies.sort(new ProxyComparator<>());
            this.workingProxies.forEach(workingProxy -> this.writer.write(this.writer.sortedProxies, workingProxy.getProxy().address().toString().split("/")[1]));
            System.out.println("[ProxyChecker] Zakonczono sortowanie.");
        } catch (FileNotFoundException e) {
            System.out.println("[ProxyChecker] Blad: " + e.getMessage());
        }
    }

    private void proxyIsWorking(ExecutorService es, Proxy proxy, int timeout) {
        es.submit(() -> {
            try {
                final long time = System.currentTimeMillis();
                URLConnection connection = new URL("http://hproxy.pl/").openConnection(proxy);
                connection.setConnectTimeout(timeout);
                connection.connect();
                final long ping = System.currentTimeMillis() - time;
                String address = proxy.address().toString().split("/")[1];
                System.out.println("[ProxyChecker] Proxy " + address + " dziala. (" + ping + "ms)");
                this.workingProxies.add(new WorkingProxy(proxy, ping));
                this.writer.write(this.writer.workingProxies, address);
            } catch (Exception ignored) {}
        });
    }
}
