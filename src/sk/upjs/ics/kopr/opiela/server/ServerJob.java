package sk.upjs.ics.kopr.opiela.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerJob implements Callable<Void> {

	private static final int VELKOST_CHUNKU = 128 * 1024;

	private final Socket klient;

	private final File subor;

	public ServerJob(Socket klient, File subor) {
		this.klient = klient;
		this.subor = subor;
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
			pw.println(VELKOST_CHUNKU); // dĺžka jednej časti
			pw.flush();
			pw.close();
		} else {
			if ("data".equals(riadok)) { // alebo data
				String riadok2 = br.readLine();
				int offset = Integer.parseInt(riadok2); // plus offset
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(subor));
				byte[] bytes = new byte[VELKOST_CHUNKU];
				bis.read(bytes, offset, VELKOST_CHUNKU);
				klient.getOutputStream().write(bytes, offset, VELKOST_CHUNKU);
				// Je nutné použiť flush?
				bis.close();
			}
		}

		return null;
	}
}
