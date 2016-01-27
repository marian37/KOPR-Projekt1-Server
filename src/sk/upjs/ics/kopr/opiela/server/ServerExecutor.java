package sk.upjs.ics.kopr.opiela.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutor {

	private final int cisloPortu;

	private final File subor;

	private ExecutorService executor;

	private ServerSocket socket = null;

	public ServerExecutor(int cisloPortu, File subor) {
		this.cisloPortu = cisloPortu;
		this.subor = subor;
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
					executor.submit(new ServerJob(klient, subor));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
