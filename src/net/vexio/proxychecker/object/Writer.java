package net.vexio.proxychecker.object;

import java.io.*;

public class Writer {

    public final File workingProxies;
    public final File sortedProxies;

    public Writer() {
        this.workingProxies = new File("working.txt");
        this.sortedProxies = new File("sorted.txt");
        try {
            new PrintWriter(workingProxies.getName()).close();
            new PrintWriter(sortedProxies.getName()).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(File file, String address) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(address);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
