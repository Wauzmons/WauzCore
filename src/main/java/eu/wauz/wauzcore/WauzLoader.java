package eu.wauz.wauzcore;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;

import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementDurability;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementMastery;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementNumbing;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementConsumption;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementDestruction;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementExpertise;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementFerocity;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementNourishment;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.items.runes.RuneHardening;
import eu.wauz.wauzcore.items.runes.RuneKnowledge;
import eu.wauz.wauzcore.items.runes.RunePower;
import eu.wauz.wauzcore.items.runes.RuneThorns;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.skills.SkillDeath;
import eu.wauz.wauzcore.skills.SkillJudgement;
import eu.wauz.wauzcore.skills.SkillJustice;
import eu.wauz.wauzcore.skills.SkillStrength;
import eu.wauz.wauzcore.skills.SkillTemperance;
import eu.wauz.wauzcore.skills.SkillTheChariot;
import eu.wauz.wauzcore.skills.SkillTheDevil;
import eu.wauz.wauzcore.skills.SkillTheEmperor;
import eu.wauz.wauzcore.skills.SkillTheEmpress;
import eu.wauz.wauzcore.skills.SkillTheFool;
import eu.wauz.wauzcore.skills.SkillTheHangedMan;
import eu.wauz.wauzcore.skills.SkillTheHermit;
import eu.wauz.wauzcore.skills.SkillTheHierophant;
import eu.wauz.wauzcore.skills.SkillTheHighPriestess;
import eu.wauz.wauzcore.skills.SkillTheLovers;
import eu.wauz.wauzcore.skills.SkillTheMagician;
import eu.wauz.wauzcore.skills.SkillTheMoon;
import eu.wauz.wauzcore.skills.SkillTheStar;
import eu.wauz.wauzcore.skills.SkillTheSun;
import eu.wauz.wauzcore.skills.SkillTheTower;
import eu.wauz.wauzcore.skills.SkillTheWorld;
import eu.wauz.wauzcore.skills.SkillWheelOfFortune;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.commands.TabCompleterSkills;
import eu.wauz.wauzcore.system.commands.TabCompleterEnhancements;
import eu.wauz.wauzcore.system.commands.TabCompleterGuilds;
import eu.wauz.wauzcore.system.commands.TabCompleterRunes;
import net.md_5.bungee.api.ChatColor;

public class WauzLoader {
	
	public static void init() {
		WauzRegion.init();
		WauzQuest.init();
		WauzPlayerGuild.init();
		
		InstanceManager.removeInactiveInstances();
		StatisticsFetcher.calculate();
		
		// Command Completers
		Bukkit.getPluginCommand("apply").setTabCompleter(new TabCompleterGuilds());
		Bukkit.getPluginCommand("wzGetRune").setTabCompleter(new TabCompleterRunes());
		Bukkit.getPluginCommand("wzEnhanced").setTabCompleter(new TabCompleterEnhancements());
		Bukkit.getPluginCommand("wzSkill").setTabCompleter(new TabCompleterSkills());
		Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new TabCompleterSkills());
		
		// Currencies and Reputation
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Tokens", "tokens");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Coins", "reput.cow");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Soulstones", "reput.souls");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Republic", "reput.wauzland");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Eternal", "reput.empire");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Dark", "reput.legion");
		
		// Skillgems
		WauzPlayerSkillExecutor.registerSkill(new SkillTheFool());			// Tarot (00) 0
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMagician());		// Tarot (01) I
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHighPriestess());	// Tarot (02) II
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmpress());		// Tarot (03) III
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmperor());		// Tarot (04) IV
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHierophant());	// Tarot (05) V
		WauzPlayerSkillExecutor.registerSkill(new SkillTheLovers());		// Tarot (06) VI
		WauzPlayerSkillExecutor.registerSkill(new SkillTheChariot());		// Tarot (07) VII
		WauzPlayerSkillExecutor.registerSkill(new SkillStrength());			// Tarot (08) VIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHermit());		// Tarot (09) IX
		WauzPlayerSkillExecutor.registerSkill(new SkillWheelOfFortune());	// Tarot (10) X
		WauzPlayerSkillExecutor.registerSkill(new SkillJustice());			// Tarot (11) XI
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHangedMan());		// Tarot (12) XII
		WauzPlayerSkillExecutor.registerSkill(new SkillDeath());			// Tarot (13) XIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTemperance());		// Tarot (14) XIV
		WauzPlayerSkillExecutor.registerSkill(new SkillTheDevil());			// Tarot (15) XV
		WauzPlayerSkillExecutor.registerSkill(new SkillTheTower());			// Tarot (16) XVI
		WauzPlayerSkillExecutor.registerSkill(new SkillTheStar());			// Tarot (17) XVII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMoon());			// Tarot (18) XVIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheSun());			// Tarot (19) XIX
		WauzPlayerSkillExecutor.registerSkill(new SkillJudgement());		// Tarot (20) XX
		WauzPlayerSkillExecutor.registerSkill(new SkillTheWorld());			// Tarot (21) XXI
		
		// Runes
		WauzRuneInserter.registerRune(new RunePower());
		WauzRuneInserter.registerRune(new RuneKnowledge());
		WauzRuneInserter.registerRune(new RuneThorns());
		WauzRuneInserter.registerRune(new RuneHardening());
		
		// Weapon Enhancements
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementDestruction());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementNourishment());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementConsumption());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementFerocity());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementExpertise());
		
		// Armor Enhancements
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementNumbing());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementDurability());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementMastery());
		
		// Equipment Types
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_SWORD, " Shortsword")
				.withMainStat(1.50).withDurabilityStat(32));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_SWORD, " Rapier")
				.withMainStat(1.55).withDurabilityStat(64));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_SWORD, " Longsword")
				.withMainStat(1.60).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_SWORD, " Claymore")
				.withMainStat(1.65).withDurabilityStat(256));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_SWORD, " Excalibur")
				.withMainStat(1.70).withDurabilityStat(512));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_AXE, " Hatchet")
				.withMainStat(1.70).withDurabilityStat(32));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_AXE, " Halberd")
				.withMainStat(1.75).withDurabilityStat(64));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_AXE, " Waraxe")
				.withMainStat(1.80).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_AXE, " Greataxe")
				.withMainStat(1.85).withDurabilityStat(256));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_AXE, " Worldbreaker")
				.withMainStat(1.90).withDurabilityStat(512));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_HOE, " Staff")
				.withMainStat(1.30).withDurabilityStat(32));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_HOE, " Pole")
				.withMainStat(1.35).withDurabilityStat(64));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_HOE, " Mace")
				.withMainStat(1.40).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_HOE, " Sceptre")
				.withMainStat(1.45).withDurabilityStat(256));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_HOE, " Soulreaver")
				.withMainStat(1.50).withDurabilityStat(512));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Crude Bow")
				.withMainStat(0.60).withDurabilityStat(64));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Recurve Bow")
				.withMainStat(0.90).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Infractem Bow")
				.withMainStat(1.20).withDurabilityStat(256));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Vest")
				.withMainStat(1.15).withDurabilityStat(64));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.GOLDEN_CHESTPLATE, " Robe")
				.withMainStat(1.30).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Tunic")
				.withMainStat(1.45).withDurabilityStat(192).withLeatherDye(Color.GREEN));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.CHAINMAIL_CHESTPLATE, " Mail")
				.withMainStat(1.30).withDurabilityStat(128));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Guard")
				.withMainStat(1.45).withDurabilityStat(256).withLeatherDye(Color.PURPLE));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Hellscales")
				.withMainStat(1.60).withDurabilityStat(384).withLeatherDye(Color.RED));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.IRON_CHESTPLATE, " Plate")
				.withMainStat(1.45).withDurabilityStat(256));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.DIAMOND_CHESTPLATE, " Herogarb")
				.withMainStat(1.60).withDurabilityStat(512));
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Dragonbones")
				.withMainStat(1.75).withDurabilityStat(768).withLeatherDye(Color.ORANGE));
	}

}
