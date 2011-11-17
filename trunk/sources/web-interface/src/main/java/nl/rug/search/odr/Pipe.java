package nl.rug.search.odr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Pipe {

    private final OutputStream out;

    private final InputStream in;

    private final Thread thread;

    /**
     * This flag indicates whether the IOException will be thrown because
     * of a call to the stop method or for some other reason.
     */
    private boolean stopCalled;




    public Pipe(OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                pipe();
            }

        });

        thread.setDaemon(true);
    }




    public void start() {
        stopCalled = false;
        thread.start();
    }




    public void stop() {
        stopCalled = true;
        thread.interrupt();
    }




    private void pipe() {
        try {
            byte[] buffer = new byte[1000];

            int read = in.read(buffer);

            while (read != -1) {
                out.write(buffer, 0, read);

                read = in.read(buffer);
            }
        } catch (IOException ex) {
            if (!stopCalled) {
                throw new RuntimeException(ex);
            }

            stopCalled = false;
        }
    }




}



