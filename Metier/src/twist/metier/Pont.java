package twist.metier;

import twist.Controleur;

/*
 * Classe Pont.java
 * Classe générale, regroupant le principal du jeu, c'est-à-dire les joueurs et les conteneurs
 * Cette classe gère également le jeu
 */

public class Pont
{
	private static final int MIN_LIGNES = 5;
	private static final int MIN_COLONNES = 5;

	private static final int MAX_LIGNES = 10;
	private static final int MAX_COLONNES = 10;

	private static final int NB_LOCKS_DEFAULT = 20;

	private int largeur;
	private int hauteur;

	private Controleur ctrl;

	private Conteneur[][] conteneurs;

	private final Joueur[] joueurs;
	private int joueurCourant;

	/*---------------*/
	/* CONSTRUCTEURS */
	/*---------------*/

	//TODO : CLEAN LES CONSTRUCTEURS DU PONT

	public Pont(String[] nomsJoueurs, int largeur, int hauteur, int nbLocks)
	{
		this(null, nomsJoueurs, new Boolean[]{false, false}, largeur, hauteur, nbLocks);
	}

	public Pont(Controleur ctrl, String[] nomsJoueurs, Boolean[] tabIA, int largeur, int hauteur, int nbLocks)
	{
		this.ctrl = ctrl;
		this.largeur = largeur;
		this.hauteur = hauteur;

		this.conteneurs = new Conteneur[largeur][hauteur];

		for (int x = 0; x < largeur; x++)
			for (int y = 0; y < hauteur; y++)
				this.conteneurs[x][y] = new Conteneur();

		this.joueurs = new Joueur[nomsJoueurs.length];

		for (int i = 0; i < nomsJoueurs.length; i++)
		{
			/*if (tabIA[i])
			{
				System.out.println("Joueur " + i + " : IA");
				this.joueurs[i] = new IAJoueur(this, nomsJoueurs[i], nbLocks);
			} else
			{*/
			System.out.println("Joueur " + i + " : Joueur");
			this.joueurs[i] = new Joueur(this, nomsJoueurs[i], nbLocks);/*
			}*/
		}
		this.joueurCourant = 0;

		definirVoisins();
	}

	public Pont(Controleur ctrl, String[] nomsJoueurs, Boolean[] ia)
	{
		this(ctrl, nomsJoueurs, ia,
				(int) (Math.random() * (MAX_LIGNES - MIN_LIGNES)) + MIN_LIGNES,
				(int) (Math.random() * (MAX_COLONNES - MIN_COLONNES)) + MIN_COLONNES,
				NB_LOCKS_DEFAULT);

		definirVoisins();
	}

	/*-------------------------------*/
	/* CONSTRUCTEURS avec CONTENEURS */
	/*-------------------------------*/

	public Pont(Controleur ctrl, String[] nomsJoueurs, Boolean[] ia, Conteneur[][] conteneurs)
	{
		this(ctrl, nomsJoueurs, ia);
		this.conteneurs = conteneurs;
		this.largeur = conteneurs.length;
		this.hauteur = conteneurs[0].length;

		definirVoisins();
	}

	/*----------*/
	/* METHODES */
	/*----------*/

	public void definirVoisins()
	{

		for (int x = 0; x < largeur; x++)
			for (int y = 0; y < hauteur; y++)
			{
				Conteneur c = this.conteneurs[x][y];

				if (y > 0 && x > 0) c.setVoisin(0, this.conteneurs[x - 1][y - 1]);
				if (y > 0) c.setVoisin(1, this.conteneurs[x][y - 1]);
				if (y > 0 && x < largeur - 1) c.setVoisin(2, this.conteneurs[x + 1][y - 1]);
				if (x < largeur - 1) c.setVoisin(3, this.conteneurs[x + 1][y]);
				if (y < hauteur - 1 && x < largeur - 1) c.setVoisin(4, this.conteneurs[x + 1][y + 1]);
				if (y < hauteur - 1) c.setVoisin(5, this.conteneurs[x][y + 1]);
				if (y < hauteur - 1 && x > 0) c.setVoisin(6, this.conteneurs[x - 1][y + 1]);
				if (x > 0) c.setVoisin(7, this.conteneurs[x - 1][y]);
			}
	}

	public int getLargeur()
	{
		return largeur;
	}

	public int getHauteur()
	{
		return hauteur;
	}

	public Conteneur[][] getConteneurs()
	{
		return conteneurs;
	}

	public boolean peutPlacerLock(int x, int y, int coin)
	{
		// Hors plateau
		if (x < 0 || x >= largeur || y < 0 || y >= hauteur || coin > 3 || coin < 0)
			return false;

		//Coin coinLibre
		return this.conteneurs[x][y].coinLibre(coin);
	}

	public boolean jouer(int x, int y, int coin)
	{
		boolean result = simulerCoup(x, y, coin);

		Joueur j = this.joueurs[this.joueurCourant];

		j.utiliserLock();
		// Perdre un lock en cas de jeu incorrect
		if (!result)
			j.utiliserLock();

		passerJoueur();

		return result;
	}

	public void passerJoueur()
	{
		this.joueurCourant = (joueurCourant + 1) % this.joueurs.length;

		while (this.joueurs[this.joueurCourant].getNbLocks() == 0 && !partieTerminee())
			this.joueurCourant = (joueurCourant + 1) % this.joueurs.length;
	}

	public boolean partieTerminee()
	{
		boolean peutJouer = false;
		boolean peutPlacer = false;

		for (Joueur joueur : this.joueurs)
		{
			if (joueur.getNbLocks() > 0)
			{
				peutJouer = true;
				break;
			}
		}

		for (Conteneur[] c1 : conteneurs)
		{
			for (Conteneur c : c1)
			{
				for (Lock l : c.getLocks())
				{
					if (l == null)
					{
						peutPlacer = true;
						break;
					}
				}
				if (peutPlacer) break;
			}
			if (peutPlacer) break;
		}

		return !(peutJouer && peutPlacer);
	}

	public Joueur getGagnant()
	{
		if (!partieTerminee()) return null;

		Joueur best = null;
		int score = -1;

		for (int i = 0; i < joueurs.length; i++)
		{
			Joueur j = joueurs[i];
			int scoreTmp = getScoreJoueur(i);
			if (scoreTmp > score)
			{
				best = j;
				score = scoreTmp;
			}
		}

		return best;
	}

	public Joueur getJoueur(int i)
	{
		if (i < 0 || i >= getNbJoueur()) return null;

		return this.joueurs[i];
	}

	public int getScoreJoueur(int i)
	{
		return getScoreJoueur(this.joueurs[i]);
	}

	public int getScoreJoueur(Joueur j)
	{
		int score = 0;

		for (int x = 0; x < largeur; x++)
			for (int y = 0; y < hauteur; y++)
			{
				Conteneur c = this.conteneurs[x][y];

				if (c.joueurMajoritaire() == j)
					score += c.getValeur();
			}

		return score;
	}

	public int getNumeroJoueur(Joueur j)
	{
		for (int i = 0; i < joueurs.length; i++)
			if (j == joueurs[i])
				return i;

		return -1;
	}

	public int getNbJoueur()
	{
		return this.joueurs.length;
	}

	public int getJoueurActif()
	{
		return this.joueurCourant;
	}

	public void setJoueurActif(int joueur)
	{
		this.joueurCourant = joueur;
	}

	//IA min max
	public boolean simulerCoup(int x, int y, int coin)
	{
		return simulerCoup(x, y, coin, false);
	}

	public boolean simulerCoup(int x, int y, int coin, boolean adversaire)
	{
		boolean result;
		Joueur j = this.joueurs[adversaire ? 1-this.joueurCourant : this.joueurCourant];

		// Hors plateau
		if (x < 0 || x >= largeur || y < 0 || y >= hauteur || coin > 3 || coin < 0)
			result = false;
		else
			result = this.conteneurs[x][y].jouerLock(coin, new Lock(j));
		return result;
	}

	public boolean annulerCoup(int x, int y, int coin)
	{
		// Hors plateau
		boolean result;
		if (x < 0 || x >= largeur || y < 0 || y >= hauteur || coin > 3 || coin < 0)
			result = false;
		else
			result = this.conteneurs[x][y].annulerLock(coin);
		return result;
	}

	public int evaluerCoup(int x, int y, int coin, int i)
	{
		if (this.joueurs.length != 2) return -214748368;
		simulerCoup(x, y, coin);

		int difference = getScoreJoueur(i) - getScoreJoueur(1 - i);

		annulerCoup(x, y, coin);

		return difference;
	}
}
