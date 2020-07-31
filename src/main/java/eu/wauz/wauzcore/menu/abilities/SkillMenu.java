package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.SkillIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.classes.Learnable;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used for viewing passive skills and spending statpoints.
 * 
 * @author Wauzmons
 *
 * @see PlayerPassiveSkillConfigurator
 */
public class SkillMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "skills";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		SkillMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Lists all passive skills, to spent points in, aswell as weapon skill stats.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getHealth(Player)
	 * @see PlayerPassiveSkillConfigurator#getTrading(Player)
	 * @see PlayerPassiveSkillConfigurator#getLuck(Player)
	 * @see PlayerPassiveSkillConfigurator#getMana(Player)
	 * @see PlayerPassiveSkillConfigurator#getStrength(Player)
	 * @see PlayerPassiveSkillConfigurator#getAgility(Player)
	 * @see SkillMenu#getMasteryItemStack(int)
	 * 
	 * @see PlayerPassiveSkillConfigurator#getStaffSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getAxeSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getSwordSkill(Player)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		SkillMenu skillMenu = new SkillMenu(player);
		WauzInventoryHolder holder = new WauzInventoryHolder(skillMenu);
		int pts = PlayerPassiveSkillConfigurator.getUnusedStatpoints(player);
		int spent;
		Inventory menu = Bukkit.createInventory(holder, 18, ChatColor.BLACK + "" + ChatColor.BOLD + "Passive Skills "
				+ ChatColor.DARK_RED + ChatColor.BOLD + pts + " Points");
		
		spent = PlayerPassiveSkillConfigurator.getHealthStatpoints(player);
		ItemStack skillHealthItemStack = SkillIconHeads.getSkillHealthItem(spent);			
		ItemMeta skillHealthItemMeta = skillHealthItemStack.getItemMeta();
		skillHealthItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Health");
		List<String> skillHealthLores = new ArrayList<String>();
		skillHealthLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.GRAY + "Increases Maximum Hitpoints by 5.");
		skillHealthLores.add(ChatColor.GRAY + "Also instantly refills your Hitpoints.");
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.WHITE + "Maximum HP: " + ChatColor.RED
				+ PlayerPassiveSkillConfigurator.getHealth(player));
		skillHealthItemMeta.setLore(skillHealthLores);
		skillHealthItemStack.setItemMeta(skillHealthItemMeta);
		menu.setItem(0, skillHealthItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getTradingStatpoints(player);
		ItemStack skillTradingItemStack = SkillIconHeads.getSkillTradingItem(spent);	
		ItemMeta skillTradingItemMeta = skillTradingItemStack.getItemMeta();
		skillTradingItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Trading");
		List<String> skillTradingLores = new ArrayList<String>();
		skillTradingLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.GRAY + "Increases Sell Income by 10%.");
		skillTradingLores.add(ChatColor.GRAY + "Applies for all ways of gaining Coins.");
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.WHITE + "Coin Multiplier: " + ChatColor.GOLD
				+ PlayerPassiveSkillConfigurator.getTrading(player) + "%");
		skillTradingItemMeta.setLore(skillTradingLores);
		skillTradingItemStack.setItemMeta(skillTradingItemMeta);
		menu.setItem(1, skillTradingItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getLuckStatpoints(player);
		ItemStack skillLuckItemStack = SkillIconHeads.getSkillLuckItem(spent);	
		ItemMeta skillLuckItemMeta = skillLuckItemStack.getItemMeta();
		skillLuckItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Luck");
		List<String> skillLuckLores = new ArrayList<String>();
		skillLuckLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.GRAY + "Increases Rate on every 3rd Identify by 10%.");
		skillLuckLores.add(ChatColor.GRAY + "Enhanced Equipment has special Effects.");
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.WHITE + "Enhance-Rate: " + ChatColor.YELLOW
				+ PlayerPassiveSkillConfigurator.getLuck(player) + "%");
		skillLuckItemMeta.setLore(skillLuckLores);
		skillLuckItemStack.setItemMeta(skillLuckItemMeta);
		menu.setItem(2, skillLuckItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getManaStatpoints(player);
		ItemStack skillMagicItemStack = SkillIconHeads.getSkillMagicItem(spent);	
		ItemMeta skillMagicItemMeta = skillMagicItemStack.getItemMeta();
		skillMagicItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Magic");
		List<String> skillMagicLores = new ArrayList<String>();
		skillMagicLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.GRAY + "Increases Maximum Mana Points by 1.");
		skillMagicLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Staff Fighting.");
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.WHITE + "Maximum MP: " + ChatColor.LIGHT_PURPLE
				+ PlayerPassiveSkillConfigurator.getMana(player)
				+ ChatColor.GRAY + " (Max: 50)");
		skillMagicItemMeta.setLore(skillMagicLores);
		skillMagicItemStack.setItemMeta(skillMagicItemMeta);
		menu.setItem(3, skillMagicItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getStrengthStatpoints(player);
		ItemStack skillStrengthItemStack = SkillIconHeads.getSkillStrengthItem(spent);	
		ItemMeta skillStrengthItemMeta = skillStrengthItemStack.getItemMeta();
		skillStrengthItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Strength");
		List<String> skillStrengthLores = new ArrayList<String>();
		skillStrengthLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.GRAY + "Increases Defense from Equip by 5%.");
		skillStrengthLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Axe Combat.");
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.WHITE + "Defense Multiplier: " + ChatColor.BLUE
				+ PlayerPassiveSkillConfigurator.getStrength(player) + "%"
				+ ChatColor.GRAY + " (Max: 300%)");
		skillStrengthItemMeta.setLore(skillStrengthLores);
		skillStrengthItemStack.setItemMeta(skillStrengthItemMeta);
		menu.setItem(4, skillStrengthItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getAgilityStatpoints(player);
		ItemStack skillAgilityItemStack = SkillIconHeads.getSkillAgilityItem(spent);	
		ItemMeta skillAgilityItemMeta = skillAgilityItemStack.getItemMeta();
		skillAgilityItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Agility");
		List<String> skillAgilityLores = new ArrayList<String>();
		skillAgilityLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.GRAY + "Increases Evasion/Crit-Chance by 1%.");
		skillAgilityLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Sword Art.");
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.WHITE + "Evasion/Crit-Chance: " + ChatColor.AQUA
				+ PlayerPassiveSkillConfigurator.getAgility(player) + "%"
				+ ChatColor.GRAY + " (Max: 40%)");
		skillAgilityItemMeta.setLore(skillAgilityLores);
		skillAgilityItemStack.setItemMeta(skillAgilityItemMeta);
		menu.setItem(5, skillAgilityItemStack);
		
		for(int index = 1; index <= skillMenu.getSubclassCount(); index++) {
			menu.setItem(index + 8, skillMenu.getMasteryItemStack(index));
		}
		
		ItemStack skillAssignmentItemStack = GenericIconHeads.getColorCubeItem();
		MenuUtils.setItemDisplayName(skillAssignmentItemStack, ChatColor.DARK_AQUA + "Assign Skills");
		MenuUtils.addItemLore(skillAssignmentItemStack, ChatColor.GRAY + "Select Skills to use in Combat", true);
		menu.setItem(14, skillAssignmentItemStack);
		
		ItemStack skillWeaponStaffItemStack = new ItemStack(Material.IRON_HOE, 1);
		ItemMeta skillWeaponStaffItemMeta = skillWeaponStaffItemStack.getItemMeta();
		skillWeaponStaffItemMeta.setDisplayName(ChatColor.DARK_RED + "Staff Fighting");
		List<String> skillWeaponStaffLores = new ArrayList<String>();
		skillWeaponStaffLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getStaffSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerPassiveSkillConfigurator.getStaffSkillMax(player) / 1000) + "%)");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerPassiveSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1) + " from Magic");
		skillWeaponStaffLores.add("");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponStaffLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponStaffLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponStaffItemMeta.setLore(skillWeaponStaffLores);
		skillWeaponStaffItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponStaffItemStack.setItemMeta(skillWeaponStaffItemMeta);
		menu.setItem(6, skillWeaponStaffItemStack);
		
		ItemStack skillWeaponAxeItemStack = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta skillWeaponAxeItemMeta = skillWeaponAxeItemStack.getItemMeta();
		skillWeaponAxeItemMeta.setDisplayName(ChatColor.DARK_RED + "Axe Combat");
		List<String> skillWeaponAxeLores = new ArrayList<String>();
		skillWeaponAxeLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getAxeSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerPassiveSkillConfigurator.getAxeSkillMax(player) / 1000) + "%)");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerPassiveSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1) + " from Strength");
		skillWeaponAxeLores.add("");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponAxeLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponAxeLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponAxeItemMeta.setLore(skillWeaponAxeLores);
		skillWeaponAxeItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponAxeItemStack.setItemMeta(skillWeaponAxeItemMeta);
		menu.setItem(7, skillWeaponAxeItemStack);
		
		ItemStack skillWeaponSwordItemStack = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta skillWeaponSwordItemMeta = skillWeaponSwordItemStack.getItemMeta();
		skillWeaponSwordItemMeta.setDisplayName(ChatColor.DARK_RED + "Sword Art");
		List<String> skillWeaponSwordLores = new ArrayList<String>();
		skillWeaponSwordLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
			+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getSwordSkill(player) / 1000)) + "%"
			+ ChatColor.GRAY + " (Max: "
			+ (int) (PlayerPassiveSkillConfigurator.getSwordSkillMax(player) / 1000) + "%)");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Multiplied by "
			+ ((float) PlayerPassiveSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1) + " from Agility");
		skillWeaponSwordLores.add("");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponSwordLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponSwordLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponSwordItemMeta.setLore(skillWeaponSwordLores);
		skillWeaponSwordItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponSwordItemStack.setItemMeta(skillWeaponSwordItemMeta);
		menu.setItem(8, skillWeaponSwordItemStack);
		
		MenuUtils.setComingSoon(menu, "Breath", 15);
		MenuUtils.setComingSoon(menu, "Nutrition", 16);
		MenuUtils.setComingSoon(menu, "Weight", 17);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The player whose skills should be shown.
	 */
	private Player player;
	
	/**
	 * The subclasses of the player.
	 */
	private List<WauzPlayerSubclass> subclasses;
	
	/**
	 * Creates a new skill menu for the given player.
	 * 
	 * @param player The player whose skills should be shown.
	 */
	public SkillMenu(Player player) {
		this.player = player;
		this.subclasses = WauzPlayerClassPool.getClass(player).getSubclasses();
	}
	
	/**
	 * Creates an empty skill menu.
	 */
	public SkillMenu() {
		
	}

	/**
	 * @return The number of subclasses, of the player's class.
	 */
	public int getSubclassCount() {
		return subclasses.size();
	}
	
	/**
	 * Creates an item stack, showing information about the given mastery.
	 * 
	 * @param mastery The number of the mastery.
	 * 
	 * @return The created item stack.
	 */
	public ItemStack getMasteryItemStack(int mastery) {
		WauzPlayerSubclass subclass = subclasses.get(mastery - 1);
		int spent = PlayerPassiveSkillConfigurator.getMasteryStatpoints(player, mastery);
		ItemStack masteryItemStack;
		if(spent > 0) {
			masteryItemStack = subclass.getSubclassItemStack();
			masteryItemStack.setAmount(spent);
		}
		else {
			masteryItemStack = GenericIconHeads.getUnknownItem();
		}
		
		ItemMeta masteryItemMeta = masteryItemStack.getItemMeta();
		masteryItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Mastery: " + subclass.getSubclassName());
		List<String> masteryLores = new ArrayList<String>();
		masteryLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		masteryLores.add("");
		masteryLores.add(ChatColor.GRAY + "Unlocks new Skills every few Points.");
		masteryLores.add(subclass.getSublassColor() + subclass.getSublassDescription());
		masteryLores.add("");
		Learnable learnable = subclass.getNextLearnable(spent);
		if(learnable != null) {
			masteryLores.add(ChatColor.YELLOW + "Needed Points: " + learnable.getLevel());
			masteryLores.add(UnicodeUtils.createProgressBar(spent, learnable.getLevel(), 50, ChatColor.DARK_AQUA));
			masteryLores.add(ChatColor.YELLOW + "Unlocks Skill: " + learnable.getSkill().getSkillId());
		}
		else {
			masteryLores.add(ChatColor.DARK_AQUA + "ALL SKILLS UNLOCKED");
		}
		masteryItemMeta.setLore(masteryLores);
		masteryItemStack.setItemMeta(masteryItemMeta);
		return masteryItemStack;
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and adds a point to the selected skill, if any statpoints are left.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getTotalStatpoints(Player)
	 * @see PlayerPassiveSkillConfigurator#getSpentStatpoints(Player)
	 * @see SkillAssignMenu#open(Player)
	 * 
	 * @see PlayerPassiveSkillConfigurator#increaseHealth(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseTrading(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseLuck(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseMana(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseStrength(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseAgility(Player)
	 * @see SkillMenu#tryToIncreaseMastery(ItemStack, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD)) {
			return;
		}
		Integer total = PlayerPassiveSkillConfigurator.getTotalStatpoints(player);
		Integer spent = PlayerPassiveSkillConfigurator.getSpentStatpoints(player);
		Integer pts = total - spent;

		try {
			if(HeadUtils.isHeadMenuItem(clicked, "Assign Skills")) {
				SkillAssignMenu.open(player);
			}
			else if(pts < 1) {
				player.sendMessage(ChatColor.RED + "You don't have Skillpoints left!");
				player.closeInventory();
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Health")) {
				PlayerPassiveSkillConfigurator.increaseHealth(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Trading")) {
				PlayerPassiveSkillConfigurator.increaseTrading(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Luck")) {
				PlayerPassiveSkillConfigurator.increaseLuck(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Magic")) {
				PlayerPassiveSkillConfigurator.increaseMana(player);
				SkillMenu.open(player);
			}	
			else if(HeadUtils.isHeadMenuItem(clicked, "Strength")) {
				PlayerPassiveSkillConfigurator.increaseStrength(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Agility")) {
				PlayerPassiveSkillConfigurator.increaseAgility(player);
				SkillMenu.open(player);
			}
			else {
				tryToIncreaseMastery(clicked, event.getRawSlot());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Increases the level of a valid mastery, if not maxed already.
	 * 
	 * @param clicked The clicked mastery item stack.
	 * @param slot The inventory slot of the mastery item stack.
	 * 
	 * @see PlayerPassiveSkillConfigurator#increaseMastery(Player, int)
	 */
	private void tryToIncreaseMastery(ItemStack clicked, int slot) {
		if(slot < 9 || slot > 13) {
			return;
		}
		
		if(ItemUtils.doesLoreContain(clicked, "ALL SKILLS UNLOCKED")) {
			player.sendMessage(ChatColor.RED + "This Mastery has already reached max level!");
			player.closeInventory();
		}
		else {
			PlayerPassiveSkillConfigurator.increaseMastery(player, slot - 8);
			SkillMenu.open(player);
		}
	}
	
}
