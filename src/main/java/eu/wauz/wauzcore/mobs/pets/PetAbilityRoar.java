package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectSource;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.PetAbility;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * Effect: 7s Damage Boost
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
@PetAbility
public class PetAbilityRoar implements WauzPetAbility {
	
	/**
	 * The particles used to display the ability's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.RED);

	/**
	 * @return The name of the ability.
	 */
	@Override
	public String getAbilityName() {
		return "Roar";
	}

	/**
	 * @return The description of the ability.
	 */
	@Override
	public String getAbilityDescription() {
		return "7s Damage Boost";
	}

	/**
	 * @return The needed breeding level for the ability.
	 */
	@Override
	public int getAbilityLevel() {
		return 4;
	}

	/**
	 * Executes this ability for the given player and pet.
	 * 
	 * @param player The player bound to this runnable.
	 * @param pet The pet bound to this runnable.
	 */
	@Override
	public void use(Player player, Entity pet) {
		spawnParticles(player, pet, particle);
		WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
		effects.addEffect(WauzPlayerEffectType.ATTACK_BOOST, WauzPlayerEffectSource.SUMMON, 7, 50);
	}

}
