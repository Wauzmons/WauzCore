package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.skills.SkillTheMagician;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * A class, that can be chosen by a player.
 * Master of magic and staff fighting.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
public class ClassMage implements WauzPlayerClass {

	/**
	 * @return The name of the class.
	 */
	@Override
	public String getClassName() {
		return "Mage";
	}
	
	/**
	 * @return The description of the class.
	 */
	@Override
	public String getClassDescription() {
		return "Students gifted with a keen intellect and unwavering discipline may walk the path of the mage. The arcane magic of magi is both great and dangerous, and thus is revealed only to the most devoted practitioners.";
	}

	/**
	 * @return The color associated with the class.
	 */
	@Override
	public ChatColor getClassColor() {
		return ChatColor.RED;
	}

	/**
	 * @return The item stack representing the class.
	 */
	@Override
	public ItemStack getClassItemStack() {
		return HeadUtils.getMageItem();
	}

	/**
	 * @return The highest weight armor category the class can wear.
	 */
	@Override
	public ArmorCategory getArmorCategory() {
		return ArmorCategory.LIGHT;
	}

	/**
	 * @return The starting stats and passive skills of the class.
	 */
	@Override
	public WauzPlayerClassStats getStartingStats() {
		WauzPlayerClassStats stats = new WauzPlayerClassStats();
		stats.setStaffSkill(135000);
		stats.setStaffSkillMax(250000);
		stats.setAxeSkill(80000);
		return stats;
	}

	@Override
	public ItemStack getStartingWeapon() {
		WauzPlayerSkill skill = new SkillTheMagician();
		return WauzDebugger.getSkillgemWeapon(skill, Material.DIAMOND_HOE, false);
	}

}
