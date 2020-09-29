package eu.wauz.wauzcore.system.pets;

import java.util.HashMap;
import java.util.Map;

import eu.wauz.wauzcore.data.PetConfigurator;

/**
 * A pet, generated from the pet config file.
 * 
 * @author Wauzmons
 */
public class WauzPet {
	
	/**
	 * A map of all pets, indexed by key.
	 */
	private static Map<String, WauzPet> petMap = new HashMap<>();
	
	/**
	 * Initializes all pets and fills the internal pet map.
	 * 
	 * @see PetConfigurator#getPetKeys()
	 */
	public static void init() {
		for(String key : PetConfigurator.getPetKeys()) {
			petMap.put(key, new WauzPet(key));
		}
	}
	
	/**
	 * The key of the pet.
	 */
	private String key;
	
	/**
	 * The name of the pet.
	 */
	private String name;
	
	/**
	 * The category of the pet.
	 */
	private String category;
	
	/**
	 * The rarity of the pet.
	 */
	private WauzPetRarity rarity;
	
	/**
	 * Constructor for a new pet.
	 * 
	 * @param key The key of the pet.
	 */
	public WauzPet(String key) {
		this.key = key;
		this.name = PetConfigurator.getName(key);
		this.category = PetConfigurator.getCategory(key);
		this.rarity = PetConfigurator.getRarity(key);
	}

	/**
	 * @return The key of the pet.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The name of the pet.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The category of the pet.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return The rarity of the pet.
	 */
	public WauzPetRarity getRarity() {
		return rarity;
	}

}
