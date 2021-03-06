package twist.net;

import twist.metier.Conteneur;
import twist.util.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Classe LecteurThread.java
 * Classe permettant de lire les messages reçu par le serveur
 * Envoi au controleur ce qu'il doit être fait
 */

public class LecteurThread implements Runnable
{
	private final ControleurReseau ctrl;

	public LecteurThread(ControleurReseau ctrl)
	{
		this.ctrl = ctrl;
	}

	final Pattern p = Pattern.compile("([1-9][A-Z][1-4])");

	@Override
	public void run()
	{
		while (this.ctrl.connecte())
		{
			try
			{
				String[] s = ctrl.lireMessage().split("[-:]", 2);

				if (s[0].length() == 1)
				{
					ctrl.setJoueurLocal(Integer.parseInt(s[0]) - 1);
				} else
				{
					switch (s[0])
					{
						case "01":
						{
							Conteneur[][] conteneurs = ctrl.interpreterMap(s[1].split("\\n")[1].substring(4));
							ctrl.creerPont(conteneurs);
						}
						break;
						case "10":
						{
							this.ctrl.setJoueurLocalActif();
							if (this.ctrl.estIA())
                            {
								((ControleurIA)this.ctrl).jouer();
                            }
						}
						break;
						case "20":
						{
							Matcher m = p.matcher(s[1]);
							if (m.find())
							{
								String jeu = m.group(1);

								int lig = jeu.charAt(0) - '1';
								int col = jeu.charAt(1) - 'A';
								int coin = jeu.charAt(2) - '1';

								ctrl.jouerLocal(col, lig, coin);
							}
						}
						break;
						case "22":
						{
							// Coup adversaire illégal
							ctrl.jouerLocal(-1, -1, -1);
						}
						break;
						case "88":
						{
							ctrl.finPartie(s[1]);
						}
						break;
					}
				}
			}
			catch (IOException e)
			{
				Logger.error("Impossible de lire un message! " + e.getMessage());
			}

		}
	}
}
