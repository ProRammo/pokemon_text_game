//Pokemon.java
//Mostapha Rammo
//Class to control pokemon abilities and options


import java.awt.*;
import java.util.*;
import java.math.*;

public class Pokemon{
		
	//	PROPERTIES OF POKEMON
	private String name;
	private int hp;
	private int energy = 50;
	private String type;
	private String resistance;
	private String weakness;
	private boolean dead = false;
	private int maxHP;
	
	//	ATTACK PROPERTIES
	private String[] attackNames;
	private String[] attackSpecials;
	private int[] attackEnergyCosts;
	private int[] attackDamage;
	private int disableDeduction;
	
	//	CONDITIONS OF POKEMON
	private String condition = "normal";
	private String NORMAL = "normal"; 	
	private String STUN = "stun"; 		//STUNNED -> CAN'T ATTACK OR RETREAT [1 TURN]
	private String DISABLE = "disable";	//DISABLED -> DAMAGE DOES 10 LESS FOR BATTLE
	private String WILDCARD = "wild card"; 	//WILDCARD -> ONLY %50 ACCURACY
	private String RECHARGE = "recharge"; 	//RECHARGE -> ADDS 20 ENERGY TO ATTACKING POKEMON
	private String WILDSTORM = "wild storm";  //WINDSTORM -> 50% ACCURRACY.. IF HIT ATTACTS AGAIN.

	//FLAGS
	private boolean wildStorm = false;

	//COUNTERS
	private int wildStormCount = 0;
	
	//_____METHODS_____//
	
	public String getNameAlone(){return this.name;}
	public String getName(){return (this.name+"("+this.hp+" HP, "+this.energy+" ENERGY)");}
	public int getHP(){return this.hp;}
	public int getEnergy(){return this.energy;}
	public String getType(){return this.type;}
	public String getResistance(){return this.resistance;}
	public String getWeakness(){return this.weakness;}
	public int getAttackAmount(){return this.attackNames.length;}
	public String[] getAttackNames(){return this.attackNames;}
	public String[] getAttackSpecials(){return this.attackSpecials;}
	public int[] getAttackEnergyCosts(){return this.attackEnergyCosts;}
	public int[] getAttackDamage(){return this.attackDamage;}
	public Boolean isDead(){return this.dead;}
	public void resetDeduction(){this.disableDeduction = 0;}
	public void restoreEnergy(){
		this.energy = (this.energy < 40) ? this.energy+10:50 ;
	}
	public void restoreHP(){
		this.hp = (this.hp < this.maxHP - 20) ? this.hp+20:this.maxHP;
	}
	public void resetEnergy(){
		this.energy = 50;
	}
	public int smallestEnergy(){
		int s = 99*99;
		for (int i = 0; i < this.attackEnergyCosts.length; i++){
			s = (this.attackEnergyCosts[i] < s) ? this.attackEnergyCosts[i]:s;
		}
		return s;
	}

	public boolean rand50(){							//50% CALCULATOR
		Random randomNum = new Random();
		int rand = randomNum.nextInt(2);
    	if (rand == 0){
    		return true;
    	}
    	return false; 
	}													//---

	public static void realWrite(String line){			//  Make it seem like someone is typing the words
		for(int i = 0; i < line.length(); i++){
			System.out.print(line.charAt(i));
			try {
    			Thread.sleep(50);
			} catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
			}
		}
	}
	 
    public Pokemon(String identity){
		disableDeduction = 0;
		//
		String[] parts = identity.split(",");
		this.name = parts[0];
		this.hp = Integer.parseInt(parts[1]);
		this.maxHP = this.hp;
		this.type = parts[2];
		this.resistance = parts[3];
		this.weakness = parts[4];
		this.attackNames = new String[Integer.parseInt(parts[5])];
		this.attackEnergyCosts = new int[Integer.parseInt(parts[5])];
		this.attackSpecials = new String[Integer.parseInt(parts[5])];
		this.attackDamage = new int[Integer.parseInt(parts[5])];

		for (int i = 0; i < Integer.parseInt(parts[5]); i++){				//FILL ARRAYS WITH DATA
			this.attackNames[i] = parts[6+(i*4)];
			this.attackEnergyCosts[i] = Integer.parseInt(parts[6+(i*4)+1]);
			this.attackDamage[i] = Integer.parseInt(parts[6+(i*4)+2]);
			this.attackSpecials[i] = parts[6+(i*4)+3];
		}
		
    }
    
    public void Attack(Pokemon p, int move){
    	//CHECK FOR STUN
    	if (this.condition.equals(STUN)){
    		realWrite(getName() + "IS STUNNED AND CANNOT ATTACK!\n\n");
	    	this.condition = NORMAL;
	    	return;
    	}

    	//USE ENERGY
    	if (this.energy - attackEnergyCosts[move] >= 0){
			this.energy -= attackEnergyCosts[move];
    	}
    	else {
    		realWrite(this.name + " HAS NOT ENOUGH ENERGY TO ATTACK!\n");
    		return;									//IF NOT ENOUGH ENERGY, DONT ATTACK.
    	}
    	

    	//CHECK FOR WILD ABILITIES
    	
    	if (this.attackSpecials[move].equals(WILDCARD)){
    		if (rand50()){
    			realWrite(getName() + "'s ATTACK MISSED.. ");
    			return;									//50% CHANCE OF A MISS. NO ATTACK ON A MISS.
    		}
    	}
    	if (this.attackSpecials[move].equals(WILDSTORM)){
    		if (rand50()){
    			realWrite(getName() + "'s ATTACK MISSED.. ");
    			return;
    		}
    	}

    	//DAMAGE OPPONENT
    	wildStormCount = 0;
    	do {
    		if ( this.attackSpecials[move].equals(" ")){
    			realWrite("\n"+getName() + " ATTACKS " + p.name+"("+p.hp+" HP, "+p.energy+" ENERGY)" + " WITH " + this.attackNames[move] + "!!\n");
    		}else realWrite("\n"+getName() + " ATTACKS " + p.name+"("+p.hp+" HP, "+p.energy+" ENERGY)" + " WITH " + this.attackNames[move] + "...  " + this.attackNames[move] + " HAS " + this.attackSpecials[move] + "\n");


	    	if (p.weakness.equals(this.type)){
	    		p.hp -= (attackDamage[move]*2)-disableDeduction; 	//REMOVE HEALTH FROM OPPONENT.. APPLY DEDUCTIONS.
	    		p.hp = (p.hp < 0) ? 0:p.hp;
	    		realWrite(getName() + "'s MOVES ARE SUPER EFFECTIVE! ATTACK DEALS " + ((this.attackDamage[move]*2)-disableDeduction) + " DAMAGE!\n\n");
	    	}
	    	else if (p.resistance.equals(this.type)){
	    		p.hp -= (attackDamage[move]*0.5)-disableDeduction;
	    		p.hp = (p.hp < 0) ? 0:p.hp;
	    		realWrite(getName() + "'s MOVES ARE NOT VERY EFFECTIVE.. ATTACK DEALS " + ((this.attackDamage[move]/2)-disableDeduction) + " DAMAGE!\n\n");
	    	}
	    	else{
	    		p.hp -= (attackDamage[move]-disableDeduction);
	    		p.hp = (p.hp < 0) ? 0:p.hp;
	    		realWrite(getName() + "'s ATTACK HIT!  ATTACK DEALS " + (this.attackDamage[move]-disableDeduction) + " DAMAGE!\n\n");
	    	}
	    	wildStormCount ++;

	    	if (p.hp == 0){
	    		p.dead = true;
	    		realWrite(p.name+"("+p.hp+" HP, "+p.energy+" ENERGY)" + " HAS FAINTED AND CAN NO LONGER FIGHT!!!\n");
	    		return;
	    	}
	    } while (this.attackSpecials[move].equals(WILDSTORM) && rand50() && p.hp > 0);		//IF WILDSTORM && ATTACK HITS, KEEP ATTACKING


	    if (this.attackSpecials[move].equals(WILDSTORM) && p.hp > 0){
	    	realWrite(getName() + "'s ATTACK MISSED.. IT HIT " + wildStormCount + " TIME(S)!");
	    }
	    else if (this.attackSpecials[move].equals(WILDSTORM) && p.hp == 0){
	    	realWrite(getName() + " HIT " + wildStormCount + " TIME(S)!");
	    }

    	//APPLY SPECIALS
    	if (this.attackSpecials[move].equals(STUN)){
    		if (rand50()){ 			//50% PROBABILITY --
    			p.condition = STUN;
    			realWrite(p.name+"("+p.hp+" HP, "+p.energy+" ENERGY)" + " IS STUNNED!");
    		} 
    	}
    	
    	if (this.attackSpecials[move].equals(DISABLE)){
    		p.disableDeduction = 10;
    		realWrite(p.name+"("+p.hp+" HP, "+p.energy+" ENERGY)" + " IS DISABLED!");
    	}
    	
    	if (this.attackSpecials[move].equals(RECHARGE)){
    		realWrite(getName() + " USED RECHARGE, 20 HP RESTORED!");
    		if (this.energy + 20 <= 50){
    			this.energy += 20;
    		}else this.energy = 50;
    	}
    	
    }


    
}

















