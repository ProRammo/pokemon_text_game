//Arena.java
//Mostapha Rammo
//Main program for Pokemon Game.

import java.awt.*; 
import java.util.*;
import java.math.*;
import java.io.*;

public class Arena{

	public static String[] getData() throws IOException{
		Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
		String data = inFile.nextLine();
		int size = Integer.parseInt(data);
		String[] allPokemon = new String[size];

		for (int i = 0; i < size; i++){
			data = inFile.nextLine();
			allPokemon[i] = data;
		}
		
		inFile.close();
		return allPokemon;
	}

	public static void realWrite(String line){
		for(int i = 0; i < line.length(); i++){
			System.out.print(line.charAt(i));
			try {
    			Thread.sleep(50);
			} catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
			}
		}
	}

	public static void main (String [] args) throws IOException{
		Scanner input = new Scanner(System.in);
		String[] allPokemonData = getData();
		ArrayList <Pokemon> allPokemon = new ArrayList();			//Computers list of available pokemon
		ArrayList <Pokemon> yourPokemon = new ArrayList();			//Your list of available pokemon
		for (int i = 0; i < allPokemonData.length; i++){
			allPokemon.add(new Pokemon(allPokemonData[i]));			//CREATE ALL POKEMON OBJECTS
		}

		System.out.println("\n\nWELCOME TO THE POKEMON TOURNAMENT!!!!!\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
		System.out.println("Please choose your 4 pokemon from the list below: \n(type their number located at the right of their names, 1 @ a time)\n");

		System.out.printf("%3s | "+"%15s | "+"%5S | "+"%15s | "+"%15s | "+"%15s | "+"\n", "#", "NAME", "HP", "TYPE", "RESISTANCE", "WEAKNESS");
		System.out.println("----+-----------------+-------+-----------------+-----------------+-----------------+");
		for (int i = 1; i < allPokemon.size()+1; i++){
			System.out.printf("%2d. | "+"%15s | "+"%5d | "+"%15s | "+"%15s | "+"%15s | "+"\n", i, allPokemon.get(i-1).getNameAlone(),allPokemon.get(i-1).getHP(), allPokemon.get(i-1).getType(), allPokemon.get(i-1).getResistance(), allPokemon.get(i-1).getWeakness());
			System.out.println("----+-----------------+-------+-----------------+-----------------+-----------------+");
		}

		int pokemonChoice = 20;
		Pokemon[] pokemonChoices = new Pokemon[4];
		int[] pokemonChoicesInt = new int[4];
		boolean pokemonAvailable = true;
		for (int i = 0; i < 4; i++){
			pokemonChoice = input.nextInt();
			for (int n = 0; n<4;n++){
				if (pokemonChoicesInt[n] == pokemonChoice){				//Make sure player doesn't pick the same pokemon twice
					System.out.println("ERROR TRY AGAIN..");
					i--;
					//flag2 = false;
					break;
				}
			}
			if (pokemonAvailable == true){								//Flag used to not execute code unless pokemon is chosen
				pokemonChoicesInt[i] = pokemonChoice;
				yourPokemon.add(allPokemon.get(pokemonChoice-1));
				pokemonChoices[i] = allPokemon.get(pokemonChoice-1);
			}
			pokemonAvailable = true;
						
		}
		for (int i = 0; i < 4; i++){
			allPokemon.remove(pokemonChoices[i]);				
		}

		Collections.shuffle(allPokemon);
		int enemyGroupSize = allPokemon.size();
		int moveChoice;
		int attackPossCount = 0;
		Pokemon[] battlingPokemon = new Pokemon[2];
		for (int i = 0; i < allPokemon.size(); i++){

			if (yourPokemon.size() == 0){			//if you are out of pokemon
				break;
			}

			attackPossCount = 0;
			for(int j = 0; j < yourPokemon.size(); j++){				//restore stats after battle
				yourPokemon.get(j).restoreHP();
				yourPokemon.get(j).resetEnergy();
				yourPokemon.get(j).resetDeduction();
			}
			for (int j = 0; j < allPokemon.size(); j++){
				allPokemon.get(j).restoreHP();
				allPokemon.get(j).resetEnergy();
			}

			realWrite("\n\nYOUR OPPONENT WILL BE --> " + allPokemon.get(0).getName()+"\n");
			realWrite("\nCHOOSE A POKEMON:\n");
			for (int j = 0; j < yourPokemon.size(); j++){

				System.out.printf("%d.  %s\n", j+1, yourPokemon.get(j).getName());
			}

			pokemonChoice = input.nextInt();
			pokemonChoice--;


			realWrite("\n..."+yourPokemon.get(pokemonChoice).getNameAlone()+" I choose you!!\n");
			while(yourPokemon.get(pokemonChoice).getHP() > 0 && allPokemon.get(0).getHP() > 0){
				battlingPokemon[0] = yourPokemon.get(pokemonChoice);
				battlingPokemon[1] = allPokemon.get(0);

				Collections.shuffle(Arrays.asList(battlingPokemon));		//randomize the turn order

				realWrite("\n"+battlingPokemon[0].getName()+" WILL GO FIRST\n\n");
				if (battlingPokemon[0] == yourPokemon.get(pokemonChoice)){

					// -- YOUR TURN -- //

					realWrite("WHAT WILL "+yourPokemon.get(pokemonChoice).getName()+" DO?\n\n");
					if (yourPokemon.get(pokemonChoice).smallestEnergy() <= yourPokemon.get(pokemonChoice).getEnergy()){
						System.out.println("1.  ATTACK");
					}
					System.out.println("2.  RETREAT");
					System.out.println("3.  PASS");
					moveChoice = input.nextInt();
					if (moveChoice == 2){
						realWrite("\nCHOOSE A POKEMON:\n");
						for (int j = 0; j < yourPokemon.size(); j++){
							System.out.printf("%d.  %s\n", j+1, yourPokemon.get(j).getName());
						}
						pokemonChoice = input.nextInt();
						pokemonChoice--;
					}
					else if (moveChoice == 1){
						realWrite("\nSELECT AN ATTACK:\n\n");
						System.out.printf("%s   %15s  %10s  %15s  %15s\n","#","NAME","DAMAGE","ENERGY COST","SPECIAL");
						System.out.println("-------------------------------------------------------------------");
						for (int x = 0; x < yourPokemon.get(pokemonChoice).getAttackAmount(); x++){
							if (yourPokemon.get(pokemonChoice).getEnergy() >= yourPokemon.get(pokemonChoice).getAttackEnergyCosts()[x]){
								System.out.printf("%d.  %15s  %10d  %15s  %15s\n", x+1-attackPossCount, yourPokemon.get(pokemonChoice).getAttackNames()[x], yourPokemon.get(pokemonChoice).getAttackDamage()[x], yourPokemon.get(pokemonChoice).getAttackEnergyCosts()[x], yourPokemon.get(pokemonChoice).getAttackSpecials()[x]);
							}else attackPossCount++;

						}
						
						moveChoice = input.nextInt();
						moveChoice--;
						yourPokemon.get(pokemonChoice).Attack(allPokemon.get(0), moveChoice);
					}

					if (allPokemon.get(0).isDead()){
						allPokemon.remove(0);
						yourPokemon.get(pokemonChoice).restoreEnergy();
						break;
					}


					// -- COMPUTER TURN -- //

					Random randomNum = new Random();
					int randomAttack = randomNum.nextInt(allPokemon.get(0).getAttackAmount());
					allPokemon.get(0).Attack(yourPokemon.get(pokemonChoice), randomAttack);
					if (yourPokemon.get(pokemonChoice).isDead()){
							i--;
							yourPokemon.remove(pokemonChoice);
							allPokemon.get(0).restoreEnergy();
							break;
					}
					yourPokemon.get(pokemonChoice).restoreEnergy();
					allPokemon.get(0).restoreEnergy();

				}
				else if (battlingPokemon[0] == allPokemon.get(0)){

					// -- COMPUTER TURN -- //

					Random randomNum = new Random();
					int randomAttack = randomNum.nextInt(allPokemon.get(0).getAttackAmount());
					allPokemon.get(0).Attack(yourPokemon.get(pokemonChoice), randomAttack);
					if (yourPokemon.get(pokemonChoice).isDead()){
							i--;
							yourPokemon.remove(pokemonChoice);
							break;
					}


					// -- YOUR TURN -- //

					realWrite("WHAT WILL "+yourPokemon.get(pokemonChoice).getNameAlone()+" DO?\n\n");
					if (yourPokemon.get(pokemonChoice).smallestEnergy() < yourPokemon.get(pokemonChoice).getEnergy()){
						System.out.println("1.  ATTACK");
					}
					System.out.println("2.  RETREAT");
					System.out.println("3.  PASS");
					moveChoice = input.nextInt();
					if (moveChoice == 2){
						realWrite("\nCHOOSE A POKEMON:\n");
						for (int j = 0; j < yourPokemon.size(); j++){
							System.out.printf("%d.  %s\n", j+1, yourPokemon.get(j).getName());
						}
						pokemonChoice = input.nextInt();
						pokemonChoice--;
					}
					else if (moveChoice == 1){
						realWrite("\nSELECT AN ATTACK:\n\n");
						System.out.printf("%s   %15s  %10s  %15s  %15s\n","#","NAME","DAMAGE","ENERGY COST","SPECIAL");
						System.out.println("-------------------------------------------------------------------");
						for (int x = 0; x < yourPokemon.get(pokemonChoice).getAttackAmount(); x++){
							if (yourPokemon.get(pokemonChoice).getEnergy() >= yourPokemon.get(pokemonChoice).getAttackEnergyCosts()[x]){
								System.out.printf("%d.  %15s  %10d  %15s  %15s\n", x+1-attackPossCount, yourPokemon.get(pokemonChoice).getAttackNames()[x], yourPokemon.get(pokemonChoice).getAttackDamage()[x], yourPokemon.get(pokemonChoice).getAttackEnergyCosts()[x], yourPokemon.get(pokemonChoice).getAttackSpecials()[x]);
							}else attackPossCount++;

						}
						
						moveChoice = input.nextInt();
						moveChoice--;
						yourPokemon.get(pokemonChoice).Attack(allPokemon.get(0), moveChoice);
					}

					if (allPokemon.get(0).isDead()){
						allPokemon.remove(0);
						break;
					}

				}
			}

		}

		if (yourPokemon.size() == 0){
			realWrite("\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n\n");
			realWrite("ALL YOUR POKEMON HAVE BEEN DEFEATED!\n\n");
			realWrite("YOU HAVE BEEN OVER CONQUERED!!\n\n");
			realWrite("GAME OVER!\n\n");	
		}else{
			realWrite("\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n\n");
			realWrite("ALL POKEMON HAVE BEEN DEFEATED!\n\n");
			realWrite("CONGRAGULATIONS YOUR ARE THE NEW POKEMON MASTER!\n\n");
			realWrite("GAME OVER!\n\n");
		}
















    }
}