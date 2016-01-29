package sk.upjs.ics.kopr.opiela.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

public class ServerGui {

	private static final String CURRENT_DIRECTORY = "/mnt/Data/";

	private final JFrame frame = new JFrame("Server");

	private final JLabel lblPort = new JLabel("Port:");

	private final JTextField txtPort = new JTextField("5000");

	private final JButton btnVyberSubor = new JButton("Vyber súbor...");

	private final JFileChooser fileChooser = new JFileChooser(CURRENT_DIRECTORY);

	private final JLabel lblSubor = new JLabel();

	private final JButton btnStart = new JButton("Spusti server");

	private final JButton btnStop = new JButton("Zastav server");

	private int port;

	private File subor;

	private ServerExecutor se;

	public ServerGui() {
		frame.setLayout(new MigLayout("wrap 2", "[][grow, fill]",
				"[][][nogrid]"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(lblPort);
		frame.add(txtPort);
		frame.add(btnVyberSubor);
		frame.add(lblSubor);
		frame.add(btnStart, "tag ok");
		frame.add(btnStop, "tag cancel");

		btnStart.setEnabled(false);
		btnStop.setEnabled(false);

		btnVyberSubor.setMnemonic('o');
		btnStop.setMnemonic('q');
		btnStart.setMnemonic('p');

		btnVyberSubor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnVyberSuborActionPerformed();
			}
		});

		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnStopActionPerformed();
			}
		});

		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnStartActionPerformed();
			}
		});

		frame.pack();
		frame.setVisible(true);
	}

	private void btnVyberSuborActionPerformed() {
		int returnValue = fileChooser.showOpenDialog(frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			subor = fileChooser.getSelectedFile();
			lblSubor.setText(subor.getName());
			btnStart.setEnabled(true);
		}
	}

	private void btnStopActionPerformed() {
		se.close();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	private void btnStartActionPerformed() {
		try {
			port = Integer.parseInt(txtPort.getText());

			txtPort.setEnabled(false);
			btnVyberSubor.setEnabled(false);
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);

			// spustenie serveru
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					se = new ServerExecutor(port, subor);
					se.start();
					return null;
				}

				@Override
				protected void done() {
					try {
						// kontrola, či nenastala výnimka
						get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			worker.execute();
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(frame, "Port musí byť celé číslo.",
					"Varovanie", JOptionPane.WARNING_MESSAGE);
		}
	}
}
