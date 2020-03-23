package twist;

import twist.metier.Pont;
import twist.ihm.launcher.*;

public class Controleur
{
    private Pont pont;

    public Controleur()
    {
        this.pont = new Pont();
        new Launcheur(this);
    }

    public static void main(String[] args)
    {
        new Controleur();
    }
}
