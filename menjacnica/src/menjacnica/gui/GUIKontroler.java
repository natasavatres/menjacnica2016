package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {

	private static MenjacnicaGUI m;
	private static MenjacnicaInterface menjacnica;
	private static Menjacnica sistem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenjacnicaGUI frame = new MenjacnicaGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(null, "Da li zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(null, "Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(m.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnica.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(m, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla(TableModel model) {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(m.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute(model);
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(m, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziSveValute(TableModel menjacnicaModel) {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnicaModel);
		model.staviSveValuteUModel(sistem.vratiKursnuListu());
	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI(JTable table) {

		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(m, model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(m.getContentPane());
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(m, model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(m);
			prozor.setVisible(true);
		}
	}

	public static JTable vratiTabelu() {
		return m.vratiTabelu();
	}

	public static void unesiKurs(String naziv, String skraceniNaziv, int sifra, String kursP, String kursK,
			String kursS) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(Double.parseDouble(kursP));
			valuta.setKupovni(Double.parseDouble(kursK));
			valuta.setSrednji(Double.parseDouble(kursS));

			// Dodavanje valute u kursnu listu
			menjacnica.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute(vratiTabelu().getModel());

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(m, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void zaistaObrisiKurs(JCheckBox chckbx, JButton btnDodaj) {
		if (chckbx.isSelected())
			btnDodaj.setEnabled(true);
		else
			btnDodaj.setEnabled(false);
	}

	public static void obrisiValutu(Valuta valuta) {
		try {
			menjacnica.obrisiValutu(valuta);
			prikaziSveValute(vratiTabelu().getModel());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(m.getContentPane(), e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziValutu(Valuta valuta, JTextField textFieldNaziv, JTextField textFieldSkraceniNaziv,
			JTextField textFieldSifra, JTextField textFieldProdajniKurs, JTextField textFieldKupovniKurs,
			JTextField textFieldSrednjiKurs) {
		// Prikaz podataka o valuti
		textFieldNaziv.setText(valuta.getNaziv());
		textFieldSkraceniNaziv.setText(valuta.getSkraceniNaziv());
		textFieldSifra.setText("" + valuta.getSifra());
		textFieldProdajniKurs.setText("" + valuta.getProdajni());
		textFieldKupovniKurs.setText("" + valuta.getKupovni());
		textFieldSrednjiKurs.setText("" + valuta.getSrednji());
	}

}
