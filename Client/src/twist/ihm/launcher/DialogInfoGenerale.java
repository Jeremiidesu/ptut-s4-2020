package twist.ihm.launcher;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import twist.ihm.Apparence;

public class DialogInfoGenerale extends JDialog implements ActionListener
{
    private Launcher ihm;
    private SpinnerModel spinNbJoueur;
    private SpinnerModel spinNbLock;
    private SpinnerModel spinNbLigne;
    private SpinnerModel spinNbColone;
    private JSpinner spinnerJoueur;
    private JSpinner spinnerLock;
    private JSpinner spinnerLigne;
    private JSpinner spinnerColone;
    private JPanel panHaut, panBas;
    private JButton buttonOk, buttonCancel;

    public DialogInfoGenerale(Launcher ihm)
    {
        super(ihm, true);
        this.setSize(320, 140);
        this.ihm = ihm;
        this.setLocationRelativeTo(this.ihm);
        this.spinNbJoueur = new SpinnerNumberModel(2, 2, 4, 1);
        this.spinNbLock = new SpinnerNumberModel(20, 1, 30, 1);
        this.spinNbLigne = new SpinnerNumberModel(10, 5, 20, 1);
        this.spinNbColone = new SpinnerNumberModel(10, 5, 20, 1);
        this.setUndecorated(true);
        this.spinnerJoueur = new JSpinner(this.spinNbJoueur);
        this.spinnerLock = new JSpinner(this.spinNbLock);
        this.spinnerLigne = new JSpinner(this.spinNbLigne);
        this.spinnerColone = new JSpinner(this.spinNbColone);
        this.spinnerJoueur.setBounds(100, 100, 50, 30);
        this.spinnerLock.setBounds(100, 100, 50, 30);
        this.spinnerLigne.setBounds(100, 100, 50, 30);
        this.spinnerColone.setBounds(100, 100, 50, 30);

        this.panHaut = new JPanel();
        this.panBas = new JPanel();
        this.buttonOk = new JButton("Valider");
        Apparence.setStyleBtnAction(this.buttonOk);
        this.buttonOk.addActionListener(this);
        this.buttonCancel = new JButton("Quitter");
        Apparence.setStyleBtnAction(this.buttonCancel);
        this.buttonCancel.addActionListener(this);

        this.panHaut.setLayout(new GridLayout(4, 2, 10, 10));
        this.panHaut.setBackground(new Color(223, 224, 226));
        this.panBas.setLayout(new GridLayout(1, 2));

        this.panHaut.add(new JLabel("Nombre de Joueurs  : "));
        this.panHaut.add(this.spinnerJoueur);
        this.panHaut.add(new JLabel("Nombre de Locks    : "));
        this.panHaut.add(this.spinnerLock);
        this.panHaut.add(new JLabel("Nombre de Lignes   : "));
        this.panHaut.add(this.spinnerLigne);
        this.panHaut.add(new JLabel("Nombre de Colonnes : "));
        this.panHaut.add(this.spinnerColone);

        this.panBas.add(this.buttonOk);
        this.panBas.add(this.buttonCancel);


        this.add(this.panHaut, BorderLayout.NORTH);
        this.add(this.panBas, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.buttonCancel) this.dispose();

        if (e.getSource() == this.buttonOk)
        {
            this.dispose();
            this.ihm.setNbJoueur(Integer.parseInt(this.spinnerJoueur.getValue().toString()));
            this.ihm.setNbLock(Integer.parseInt(this.spinnerLock.getValue().toString()));
            this.ihm.setNbCol(Integer.parseInt(this.spinnerLigne.getValue().toString()));
            this.ihm.setNbLgn(Integer.parseInt(this.spinnerColone.getValue().toString()));
            this.ihm.addDialogNomJoueur();
        }
    }
}
