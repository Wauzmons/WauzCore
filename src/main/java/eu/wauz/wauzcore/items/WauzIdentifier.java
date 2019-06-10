package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import net.md_5.bungee.api.ChatColor;

public class WauzIdentifier {

	public static List<Equipment> material = new ArrayList<>();
	
	public static List<String> equipNames = new ArrayList<>();
	public static List<String> runeNames = new ArrayList<>();
	public static List<String> shrineNames = new ArrayList<>();
	
	public static final String EMPTY_SKILL_SLOT =
			ChatColor.WHITE + "Skill Slot (" + ChatColor.DARK_RED + "Empty" + ChatColor.WHITE + ")";
	
	public static final String EMPTY_RUNE_SLOT =
			ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")";
	
	public static void identifyItem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();	
		String name = item.getItemMeta().getDisplayName();
		
		Random random = new Random();
		String verb = equipNames.get(random.nextInt(equipNames.size()));
		int rarity = random.nextInt(1000);
	
		String identifiedName = null;

// Set Item Material
		
		double power = 0;
		Equipment equip = material.get(random.nextInt(material.size()));
		
		item.setType(equip.getMaterial());
		power = equip.getDamage();
		
// Set Item Rarity
		
		String rareName = null;
		ChatColor color = null;
		double rare = 0;
		
		if(rarity <= 550) {
			color = ChatColor.GREEN;
			rareName = "Normal ";
			rare = 1.00;
		}
		else if(rarity <= 800) {
			color = ChatColor.BLUE;
			rareName = "Magic ";
			rare = 1.50;
		}
		else if(rarity <= 920) {
			color = ChatColor.GOLD;
			rareName = "Rare ";
			rare = 2.00;
		}
		else if(rarity <= 975) {
			color = ChatColor.DARK_PURPLE;
			rareName = "Epic ";
			rare = 2.50;
		}
		else if(rarity <= 999) {
			color = ChatColor.DARK_RED;
			rareName = "Unique ";
			rare = 3.00;
		}
		
// Set Item Tier
		
		String tierName = null;
		int tier = 0;
		
		if(name.contains("T1")) {
			tier = 1;
			tierName = "Lesser ";
		}
		else if(name.contains("T2")) {
			tier = 2;
			tierName = "Greater ";
		}
		else if(name.contains("T3")) {
			tier = 3;
			tierName = "Angelic ";
		}
		
// Generate Identified Item
	
		identifiedName =  color + verb + equip.getName();
		
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(identifiedName);
		
		double randomizer = random.nextDouble();
		int attack = (int) (((2+randomizer)*(power))*((Math.pow(2, tier))*rare));
		
// Scaling and Main Stat
		
		List<String> lores = new ArrayList<String>();
		
		float scalingLevel = player.getLevel() - (tier * 10 - 10);
		scalingLevel = (float) (scalingLevel < 1 ? 3 : (scalingLevel + 2 > 10 ? 10 : scalingLevel + 2)) / 10;	
		WauzDebugger.log(player, "Level-Scaling Weapon: " + attack + " * " + scalingLevel);
		int level = Math.max(Math.min((tier * 10), player.getLevel()), tier * 10 - 15);
		String levelString = ChatColor.YELLOW + "lvl " + ChatColor.AQUA + level + ChatColor.DARK_GRAY + ")";
		String scalingString = scalingLevel == 1
				? " " + ChatColor.DARK_GRAY + "(" + levelString
				: " " + ChatColor.DARK_GRAY + "(Scaled x" + scalingLevel + " " + levelString;
		attack = (int) (attack * scalingLevel);
		
		int defense = (attack / 4) + 1;
		attack += 1;
		
		String mainStatString = "";
		if(equip.getType().equals("Weapon")) {	
			lores.add(ChatColor.WHITE + tierName + rareName + "Weapon");
			lores.add("");
			mainStatString = "Attack:" + ChatColor.RED + " " + attack + scalingString;
			lores.add(mainStatString);
		}
		else if(equip.getType().equals("Armor")) {		
			lores.add(ChatColor.WHITE + tierName + rareName + "Armor");
			lores.add("");
			mainStatString = "Defense:" + ChatColor.BLUE + " " + defense + scalingString;
			lores.add(mainStatString);
		}
		
// Add Enhancements
		
		if(Chance.oneIn(3)) {
			int enhancementLevel = 0;
			int luck = PlayerConfigurator.getCharacterLuck(player);
			WauzDebugger.log(player, "Rolling for Enhancement with: " + luck + "% Luck");
			while(luck >= 100) {
				enhancementLevel++;
				luck -= 100;
			}
			if(Chance.percent(luck))
				enhancementLevel++;
			
			if(enhancementLevel > 0) {
				String enhancementName = "";
				String enhancementDescription = "";
				
				if(equip.getType().equals("Weapon")) {
					int enhancementType = random.nextInt(4);
					
					if(enhancementType == 0) {
						enhancementName = "Destruction";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Attack Boost";
						double newDamage = 1 + attack * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.RED + " " + attack, ChatColor.RED + " " + (int) newDamage));
						WauzDebugger.log(player, "Rolled Attack Boost: " + attack + " -> " + (int) newDamage);
					}
					else if(enhancementType == 1) {
						enhancementName = "Nourishment";
						enhancementDescription = (enhancementLevel * 3) + " " + ChatColor.GRAY + "HP on Kill";
						WauzDebugger.log(player, "Rolled HP on Kill");
					}
					else if(enhancementType == 2) {
						enhancementName = "Consumption";
						enhancementDescription = (enhancementLevel * 1) + " " + ChatColor.GRAY + "MP on Kill";
						WauzDebugger.log(player, "Rolled MP on Kill");
					}
					else if(enhancementType ==  3) {
						enhancementName = "Ferocity";
						enhancementDescription = (enhancementLevel * 20) + " " + ChatColor.GRAY + "% Crit Multiplier";
						WauzDebugger.log(player, "Rolled Crit Multiplier");
					}
					
					im.setDisplayName(im.getDisplayName() + " of " + enhancementName + " + " + enhancementLevel);
					lores.add("Enhancement:" + ChatColor.RED + " " + enhancementDescription);
				}
				
				else if(equip.getType().equals("Armor")) {
					int enhancementType = random.nextInt(1);
					
					if(enhancementType == 0) {
						enhancementName = "Numbing";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Defense Boost";
						double newDefense = 1 + defense * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.BLUE + " " + defense, ChatColor.BLUE + " " + (int) newDefense));
						WauzDebugger.log(player, "Rolled Defense Boost: " + defense + " -> " + (int) newDefense);
					}
					
					im.setDisplayName(im.getDisplayName() + " of " + enhancementName + " + " + enhancementLevel);
					lores.add("Enhancement:" + ChatColor.BLUE + " " + enhancementDescription);
				}
				
				WauzDebugger.log(player, "Rolled Enhancement Level: " + enhancementLevel);
			}
			else
				WauzDebugger.log(player, "Rolled Nothing...");
		}
		
// Add Rune and Skill Slots
		
		if(equip.getName().contains("Bow")) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Click while Sneaking to switch Arrows");
			lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		}
		else if(rare >= 1.5) {
			if(equip.getType().equals("Weapon") && Chance.oneIn(2)) {
				lores.add("");
				lores.add(EMPTY_SKILL_SLOT);
			}
			
			lores.add("");
			lores.add(EMPTY_RUNE_SLOT);
			if(rare >= 2.5)
				lores.add(EMPTY_RUNE_SLOT);
		}
		
		im.setLore(lores);	
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	public static void identifyRune(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();	
		String name = item.getItemMeta().getDisplayName();
		
		Random random = new Random();
		String rune = runeNames.get(random.nextInt(runeNames.size()));
		int rarity = random.nextInt(1000);
		
		String identifiedName = null;
		
// Set Rune Rarity
		
		String rareName = null;
		ChatColor color = null;
		double rare = 0;
				
		if(rarity <= 800) {
			color = ChatColor.GREEN;
			rareName = "Whispering ";
			rare = 1.00;
		}
		else if(rarity <= 975) {
			color = ChatColor.BLUE;
			rareName = "Screaming ";
			rare = 1.50;
		}
		else if(rarity <= 999) {
			color = ChatColor.GOLD;
			rareName = "Deafening ";
			rare = 2.00;
		}
				
// Set Rune Tier
				
		String tierName = null;
		int tier = 0;
				
		if(name.contains("T1")) {
			tier = 8;
			tierName = "Lesser ";
		}
		else if(name.contains("T2")) {
			tier = 12;
			tierName = "Greater ";
		}
		else if(name.contains("T3")) {
			tier = 16;
			tierName = "Angelic ";
		}
		
// Generate Identified Rune
		
		identifiedName =  color + "Rune of " + rune;
			
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(identifiedName);
			
		double r = random.nextDouble();
		int power = (int) ((2+r/1.5)*tier*rare);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.WHITE + tierName + rareName + "Rune");
		lores.add("");
		lores.add(ChatColor.GRAY + "Can be inserted into Equipment,");
		lores.add(ChatColor.GRAY + "which possesses an empty Rune Slot.");
		lores.add("");
		lores.add("Might:" + ChatColor.YELLOW + " " + power);
		
		im.setLore(lores);	
		item.setItemMeta(im);
		item.setType(Material.FIREWORK_STAR);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	public static void identifyShrine(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();	
		
		Random random = new Random();
		String shrineName = ChatColor.RESET 
				+ shrineNames.get(random.nextInt(shrineNames.size())) + "-" 
				+ shrineNames.get(random.nextInt(shrineNames.size())) + " Shrine";
		
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(shrineName);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.RED + "Shrine Map");
		lores.add("");
		lores.add(ChatColor.YELLOW + "" + (random.nextInt(9) + 5) + " Rooms");
		lores.add("");
		lores.add(ChatColor.GRAY + "Right Click to enter Shrine.");
		lores.add(ChatColor.GRAY + "Map resets upon Death.");
		
		im.setLore(lores);	
		item.setItemMeta(im);
		item.setType(Material.PAPER);
	}
	
	public static void identifySkillgem(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		
		Random random = new Random();
		List<String> skills = new ArrayList<>(WauzPlayerSkillExecutor.playerSkillMap.keySet());
		String skillgemName = skills.get(random.nextInt(skills.size()));
		
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.DARK_RED + "Skillgem: " + ChatColor.LIGHT_PURPLE + skillgemName);
		
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.playerSkillMap.get(skillgemName);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Can be inserted into a Weapon,");
		lores.add(ChatColor.GRAY + "which possesses an empty Skill Slot.");
		lores.add("");
		lores.add(ChatColor.WHITE + "Adds Right-Click Skill:");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		
		im.setLore(lores);
		item.setItemMeta(im);
		item.setType(Material.REDSTONE);
	}
	
}
