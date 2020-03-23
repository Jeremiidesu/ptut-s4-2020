package twist.ihm.Launcheur;

import java.awt.*;
import javax.swing.*;
import java.awt.font.*;
import java.io.*;

import twist.Controleur;
import Ihm.*;


public class Launcheur extends JFrame
{
	private PanImage panelAccueil;
	private DemandeJoueur panelDemandeJ;
	private PanNomJoueurs panNomsJ;
	private Controleur ctrl;
	private int nbJoueurs;

	public Launcheur(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setSize(516,705);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panelAccueil  = new PanImage(this);
		this.panelDemandeJ = new DemandeJoueur(this);
		this.setContentPane(this.panelAccueil);

		this.setVisible(true);

		//initialisation nbJ
		this.nbJoueurs = 0;
	}

	public void addPanDemandeJ()
	{
		this.panelDemandeJ.setVisible(true);
	}

	public void setNbJ(int i)
	{
		this.nbJoueurs=i;
		this.panNomsJ = new PanNomJoueurs(this.nbJoueurs,this);
	}

	public void debuterPartie(String[] tabNomJoueur)
	{
		//this.ctrl.nouveauplateau(tabNomJoueur);
		this.dispose();
	}

}
