import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Ikkuna extends JFrame implements ActionListener {

    private Kentta kentta;
    private JScrollPane pane;
    private lisaa lisaaKentta;
    private lisaa poistaKentta;
    private nappi lisaanappi;
    private nappi poistaNappi;
    private File tiedosto;
    private String tulostus;
    private Path tiedostonPolku;
    private List<String> sisalto;

    public Ikkuna() {

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Haapanen Johannes Muistilista");
    this.setSize(600,850);
    this.getContentPane().setBackground(Color.BLACK);
    this.setResizable(false);
    this.setLayout(null);

    kentta = new Kentta();
    pane = new JScrollPane(kentta);
    pane.setBounds(100, 100, 400, 600);

    lisaaKentta = new lisaa();
    poistaKentta = new lisaa();
    poistaKentta.setText("Poista rivi (anna numero)...");
    poistaKentta.setBounds(100, 725, 300, 25);
    lisaanappi = new nappi();
    lisaanappi.addActionListener(this);
    poistaNappi = new nappi();
    poistaNappi.setBounds(425, 725, 75, 25);
    poistaNappi.setText("Poista");
    poistaNappi.addActionListener(this);

    try {

        tiedosto = new File("muistiinpanot.txt");
        tiedosto.createNewFile();

    } catch (IOException e) {

        kentta.setText("Tiedostoa ei löytynyt/voitu luoda");

    }

    tiedostonPolku = Paths.get("muistiinpanot.txt");

    this.lueTulosta();
    this.add(pane);
    this.add(lisaaKentta);
    this.add(poistaKentta);
    this.add(poistaNappi);
    this.add(lisaanappi);
    this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == lisaanappi) {

            if (sisalto.contains(lisaaKentta.getText())) {

                lisaaKentta.setText("Muistiinpano on jo olemassa");

            }

            else {

                kirjoita();
                lueTulosta();

            }

        }

        if (e.getSource() == poistaNappi) {

            if (tarkistus(poistaKentta.getText()) == false) {

                poistaKentta.setText("Et antanut numeroa/kokonaislukua");

            }

            else if(Integer.parseInt(poistaKentta.getText()) > sisalto.size()) {

                poistaKentta.setText("Antamaasi rivi ei ole olemassa");

            }

            else {

                poistaRivi();
                lueTulosta();

            }

        }

    }

    public void lueTulosta() {

        try {

            sisalto = Files.readAllLines(tiedostonPolku, StandardCharsets.UTF_8);

            tulostus = "";

            for (int i = 0; i < sisalto.size(); i++) {

                if (sisalto.get(i) != "" || sisalto.get(i) != null) {

                    tulostus += (i + 1) + ". " + sisalto.get(i) + "\n";

                }

            }

            lisaaKentta.setForeground(Color.BLACK);
            kentta.setText(tulostus);

            } catch (IOException e) {

                kentta.setText("Virhe muistiota luettaessa/tulostettaessa");

            }

    }

    public void kirjoita() {

        try {

            String kirjoitettava = lisaaKentta.getText() + "\n";

            sisalto = Files.readAllLines(tiedostonPolku, StandardCharsets.UTF_8);

            for (int i = 0; i < sisalto.size(); i++) {

                if (sisalto.get(i) != "" || sisalto.get(i) != null) {

                    kirjoitettava += sisalto.get(i) + "\n";

                }

            }

            Files.write(tiedostonPolku, kirjoitettava.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);

            lisaaKentta.setText("Lisää uusi muistiinpano...");

            } catch (IOException e) {

                kentta.setText("Virhe tiedostoon kirjoitettaessa!");
    
            }

    }

    public void poistaRivi() {

        try {

            int poistettava = Integer.parseInt(poistaKentta.getText()) - 1;
            sisalto = Files.readAllLines(tiedostonPolku, StandardCharsets.UTF_8);

            List<String> uusiSisalto = sisalto.stream().filter(s -> s != sisalto.get(poistettava)).toList();

            String kirjoitettava = "";

            for (int i = 0; i < uusiSisalto.size(); i++) {

                if (uusiSisalto.get(i) != "" || uusiSisalto.get(i) != null) {

                    kirjoitettava += uusiSisalto.get(i) + "\n";

                }

            }

            Files.write(tiedostonPolku, kirjoitettava.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);

            poistaKentta.setText("Poista rivi (anna numero)...");

            } catch (IOException e) {

                kentta.setText("Virhe tiedostoon kirjoitettaessa!");
    
            }

    }

    public boolean tarkistus(String tarkistettava) {

        try {  

            Integer.parseInt(tarkistettava);  
            return true;

          } catch(NumberFormatException e){  
            return false;  

          }  

    }

}

class Kentta extends JTextArea {

    Kentta() {

        this.setBackground(Color.DARK_GRAY);
        this.setCaretColor(Color.WHITE);
        this.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        this.setForeground(Color.WHITE);
        this.setBounds(100, 100, 400, 600);
        this.setLineWrap(true);
        this.setEditable(false);

    }

}

class lisaa extends JTextField {

    lisaa() {

        this.setFont(new Font("Comic Sans MS", Font.BOLD,18));
        this.setForeground(Color.BLACK);
        this.setBackground(Color.WHITE);
        this.setCaretColor(Color.BLACK);
        this.setBounds(100, 50, 300, 25);
        this.setText("Lisää uusi muistiinpano...");

    }

}

class nappi extends JButton {

    nappi() {

        this.setBackground(Color.DARK_GRAY);
        this.setForeground(Color.WHITE);
        this.setText("Lisää");
        this.setFocusable(false);
        this.setBounds(425, 50, 75, 25);

    }

}
