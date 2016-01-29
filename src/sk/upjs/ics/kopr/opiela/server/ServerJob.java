package sk.upjs.ics.kopr.opiela.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerJob implements Callable<Void> {

	private final Socket klient;

	private final File subor;

	private final RandomAccessFile suborRAF;

	public ServerJob(Socket klient, File subor, RandomAccessFile suborRAF) {
		this.klient = klient;
		this.subor = subor;
		this.suborRAF = suborRAF;
	}

	@Override
	public Void call() throws Exception {
		InputStream is = klient.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String riadok = br.readLine();
		if ("info".equals(riadok)) { // info
			PrintWriter pw = new PrintWriter(klient.getOutputStream());
			pw.println(subor.getName()); // názov súboru
			pw.println(subor.length()); // veľkosť súboru
			pw.flush();
			pw.close();
		}

		if ("data".equals(riadok)) { // alebo data
			int offset = Integer.parseInt(br.readLine()); // plus offset
			int dlzka = Integer.parseInt(br.readLine()); // plus dĺžka
			byte[] bytes = new byte[dlzka];
			synchronized (suborRAF) {
				suborRAF.seek(offset);
				suborRAF.read(bytes);
			}
			klient.getOutputStream().write(bytes);
		}

		klient.close();

		return null;
	}
}
