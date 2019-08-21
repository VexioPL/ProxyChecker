package net.vexio.proxychecker.comparator;

import net.vexio.proxychecker.object.WorkingProxy;

import java.util.Comparator;

public class ProxyComparator<Object> implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        final Long c1 = ((WorkingProxy) o1).getPing();
        final Long c2 = ((WorkingProxy) o2).getPing();
        return c1.compareTo(c2);
    }
}