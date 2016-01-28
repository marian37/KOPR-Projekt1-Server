package sk.upjs.ics.kopr.opiela.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutor {

	private final int cisloPortu;

	private final File subor;

	private final RandomAccessFile suborRAF;

	private ExecutorService executor;

	private ServerSocket socket = null;

	public ServerExecutor(int cisloPortu, File subor) {
		this.cisloPortu = cisloPortu;
		this.subor = subor;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(subor, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.suborRAF = raf;
		executor = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors());
	}

	public void start() {
		try {
			socket = new ServerSocket(cisloPortu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (socket != null) {
			while (true) {
				try {
					Socket klient = socket.accept();
					executor.submit(new ServerJob(klient, subor, suborRAF));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
