/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Math.floor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author imineyou
 */
public class Allumette {
    
    

public static int jeu_ordi (int nb_allum, int prise_max)
{
	int prise = 0;
	int s = 0;
	float t = 0;
	s = prise_max + 1;
	t = ((float) (nb_allum - s)) / (prise_max + 1);
	while (t != floor(t))
	{
		s--;
		t = ((float) (nb_allum-s)) / (prise_max + 1);
	}
	prise = s - 1;
	if (prise == 0)
	prise = 1;
	return (prise);
}

public static String afficher_allu(int n)
{
	String temp = "";
int i;
 temp = temp + "\n" ;
 for (i=0; i<n ;i++)
	 temp = temp +"  o";
 temp = temp + "\n" ;
 for (i=0; i<n; i++)
	 temp = temp + "  |";
 temp = temp + "\n" ;
 for (i=0; i<n; i++)
	 temp = temp + "  |";
 temp = temp + "\n" ;

	return temp;
}


public static void main(String[] args) throws IOException {
	ServerSocket conn = new ServerSocket(10081);
	Socket comm = conn.accept();


	DataOutputStream outs = new DataOutputStream(comm.getOutputStream());
	outs.writeUTF("voici un message du serveur");



int nb_max_d=0; /*nbre d'allumettes maxi au départ*/
int nb_allu_max=0; /*nbre d'allumettes maxi que l'on peut tirer au maxi*/
int qui=0; /*qui joue? 0=Nous --- 1=PC*/
int prise=0; /*nbre d'allumettes prises par le joueur*/
int nb_allu_rest=0; /*nbre d'allumettes restantes*/
Scanner sc=new Scanner(System.in);
do{
	outs.writeUTF("Nombre d'allumettes disposées entre les deux joueurs (entre 10 et 60) :\"");

	DataInputStream nbAllumettes = new DataInputStream(comm.getInputStream());
	nb_max_d=nbAllumettes.readInt();
	System.out.println(nb_max_d);

}
while((nb_max_d<10) || (nb_max_d>60));
do
{
	outs.writeUTF("Nombre maximal d'allumettes que l'on peut retirer : ");
	DataInputStream nbMaxARetirer = new DataInputStream(comm.getInputStream());
	nb_allu_max=nbMaxARetirer.readInt();
	System.out.println(nb_allu_max);

	if (nb_allu_max >= nb_max_d)
		System.out.println ("Erreur !");
}
while ((nb_allu_max >= nb_max_d)||(nb_allu_max == 0));
/* On répète la demande de prise tant que le nombre donné n'est pas correct */
do
{
	outs.writeUTF("Quel joueur commence? 0--> Joueur ; 1--> Ordinateur : ");
	DataInputStream joueurCommence = new DataInputStream(comm.getInputStream());
	qui=joueurCommence.readInt();
	System.out.println(qui);

if ((qui != 0) && (qui != 1))
System.out.println ("\nErreur");
}
while ((qui != 0) && (qui != 1));
nb_allu_rest = nb_max_d;
do
{
	outs.writeUTF("Nombre d'allumettes restantes :"+nb_allu_rest);


	outs.writeUTF(afficher_allu(nb_allu_rest));
	if (qui==0)
	{
		do{
			outs.writeUTF("Combien d'allumettes souhaitez-vous piocher ? ");
			DataInputStream nbAlluAPiocher = new DataInputStream(comm.getInputStream());
			prise=nbAlluAPiocher.readInt();

			System.out.println("nb d'allumettes piochées par client "+prise);
			if ((prise > nb_allu_rest) || (prise > nb_allu_max))
			{
				System.out.println ("Erreur !\n");
			}
		} while ((prise > nb_allu_rest) || (prise > nb_allu_max));
		/* On répète la demande de prise tant que le nombre donné n'est pas correct */
		nb_allu_rest= nb_allu_rest - prise;
		outs.writeUTF(afficher_allu(nb_allu_rest));
	}
	else
	{
		prise = jeu_ordi (nb_allu_rest , nb_allu_max);
		nb_allu_rest= nb_allu_rest - prise;
		outs.writeUTF("Le serveur joue "+prise+" coups\n");
		System.out.println ("\nPrise de l'ordi :"+prise);
	}
	qui=(qui+1)%2;
} while (nb_allu_rest >0);


if (qui == 0) /* Cest à nous de jouer */
	outs.writeUTF("Vous avez gagné");

else
	outs.writeUTF("Vous avez perdu");

outs.writeUTF("END");

}

    
    
}
