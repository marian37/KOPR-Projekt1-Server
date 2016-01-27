package sk.upjs.ics.kopr.opiela.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

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

	private JFrame frame = new JFrame("Server");

	private JLabel lblPort = new JLabel("Port:");

	private JTextField txtPort = new JTextField("5000");

	private JButton btnVyberSubor = new JButton("Vyber súbor...");

	private JFileChooser fileChooser = new JFileChooser(CURRENT_DIRECTORY);

	private JLabel lblSubor = new JLabel();

	private JButton btnStart = new JButton("Spusti server");

	private JButton btnStop = new JButton("Zastav server");

	private int port;

	private File subor;

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
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	private void btnStartActionPerformed() {
		try {
			port = Integer.parseInt(txtPort.getText());

			txtPort.setEnabled(false);
			btnVyberSubor.setEnabled(false);
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);

			System.out.println(port + " " + subor);

			// spustenie serveru
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					ServerExecutor se = new ServerExecutor(port, subor);
					se.start();
					return null;
				}
			};
			worker.execute();
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(frame, "Port musí byť celé číslo.",
					"Varovanie", JOptionPane.WARNING_MESSAGE);
		}
	}
}
