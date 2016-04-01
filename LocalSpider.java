import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Umar on 28-Mar-16.
 */
public class LocalSpider {

    List<String> l;
    ConcurrentHashMap<String, List<String>> index;

    public LocalSpider() {
        l = Collections.synchronizedList(new ArrayList<String>());
        index= new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LocalSpider ls= new LocalSpider();

         ls.startcrawling("F:\\Movies");
        ls.startindexing();
        while (true) {
            String q = s.nextLine();
            List<String> lv = ls.index.get(q);

            if (lv != null) {
                for (int o = 0; o < lv.size(); o++)
                    System.out.println(lv.get(o));
            }
            System.out.print("Enter your key word: ");
        }
    }
    public  void startcrawling(String URL){
        File fr2 = new File(URL);
        File cnt1;

        if (fr2.exists() & fr2.isDirectory()) {

            File[] names = fr2.listFiles();                // storing all the files in the file array
            if (names == null) {                            // if directory is empty return from it

                return;
            }
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                cnt1 = names[i];
                if (cnt1.isDirectory()) {
                   Thread t = new Thread(new CrawlingThread(l,cnt1.getAbsolutePath()));
                    t.start();
                    threads.add(t);
                }
                else {
                    l.add(cnt1.getAbsolutePath());
                }

            }
            for (Thread thread : threads)
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            for( String s: l)
//                System.out.println(s);
        }
    }
    public  void startindexing(){
        int div= l.size()/3;
        List<Thread> threads = new ArrayList<>();
        Thread t = new Thread(new IndexingThread(l,0,div,index));
        t.start();
        threads.add(t);
        Thread t2 = new Thread(new IndexingThread(l,div,div*2,index));
        t2.start();
        threads.add(t2);
        Thread t3 = new Thread(new IndexingThread(l,div*2,div*3,index));
        t3.start();
        threads.add(t3);
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}
