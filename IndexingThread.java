import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Umar on 28-Mar-16.
 */
public class IndexingThread implements  Runnable {

    List<String> paths;
    ConcurrentHashMap<String, List<String>> index;
    int start, stop;
    public IndexingThread(List<String> paths, int start, int stop, ConcurrentHashMap<String, List<String>> indexing) {
        this.index=indexing;
        this.paths=paths;
        this.start=start;
        this.stop=stop;
    }

    @Override
    public void run() {
        try {
            doIndexing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void  doIndexing() throws IOException {
        File cnt1;
        for(int i=start;i<stop;i++){
            cnt1 = new File(paths.get(i));
            if (cnt1.isFile()) {
                // First store file name and splits on spaces and extension
                List<String> lc;
                String s = cnt1.getName();

                String g = cnt1.getAbsolutePath();
                String dot[] = s.split("\\.(?=[^\\.]+$)");
                lc = index.get(dot[dot.length - 1]);
                if (lc != null) {
                    lc.add(g);
                } else {
                    List<String> kc = Collections.synchronizedList(new ArrayList<String>());
                    kc.add(g);
                    index.put(dot[dot.length - 1], kc);
                }
                String f[];
                if (dot.length>1)
                    f= dot[0].split("\\s+");
                else
                    f=s.split("\\s+");;
                // store space splits names
                for (int k = 0; k < f.length; k++) {
                    lc = index.get(f[k]);
                    if (lc != null) {
                        lc.add(g);
                    } else {
                        List<String> kc = Collections.synchronizedList(new ArrayList<String>());
                        kc.add(g);
                        index.put(f[k], kc);
                    }
                }

                lc = index.get(s);
                if (lc != null) {
                    lc.add(g);
                } else {
                    List<String> kc = Collections.synchronizedList(new ArrayList<String>());
                    kc.add(g);
                    index.put(s, kc);
                }
                //index.put(getExtension(s),g);

                if (getExtension(s).equalsIgnoreCase("txt")) {        // if name matches delete the file
                    //String g=cnt1.getAbsolutePath();
                    FileReader fr = new FileReader(g);
                    BufferedReader tr = new BufferedReader(fr);
                    String sCurrentLine;
                    while ((sCurrentLine = tr.readLine()) != null) {

                        String p[] = sCurrentLine.split("\\s+");
                        for (int k = 0; k < p.length; k++) {
                            lc = index.get(p[k]);
                            if (lc != null) {
                                lc.add(g);
                            } else {
                                List<String> kc = Collections.synchronizedList(new ArrayList<String>());
                                kc.add(g);
                                index.put(p[k], kc);
                            }
                            //index.put(p[k],g);
                        }
                    }
                }

            }
        }
    }
    public  String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }


}
