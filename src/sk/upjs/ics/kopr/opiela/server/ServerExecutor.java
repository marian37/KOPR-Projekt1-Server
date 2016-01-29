package sk.upjs.ics.kopr.opiela.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutor {

	private final File subor;

	private final RandomAccessFile suborRAF;

	private final ExecutorService executor;

	private final ServerSocket soket;

	private boolean close = false;

	public ServerExecutor(int cisloPortu, File subor) {
		this.subor = subor;
		RandomAccessFile raf = null;
		ServerSocket socket = null;
		try {
			raf = new RandomAccessFile(subor, "r");
			socket = new ServerSocket(cisloPortu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.suborRAF = raf;
		this.soket = socket;
		executor = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors());
	}

	public void start() {
		if (soket != null) {
			while (!close) {
				try {
					Socket klient = soket.accept();
					executor.submit(new ServerJob(klient, subor, suborRAF));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		this.close = true;
		// zatvorenie súboru
		try {
			this.suborRAF.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
