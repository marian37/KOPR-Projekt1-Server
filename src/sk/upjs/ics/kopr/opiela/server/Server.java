package sk.upjs.ics.kopr.opiela.server;

import javax.swing.SwingUtilities;

public class Server {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ServerGui();
			}

		});
	}
}
