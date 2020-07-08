package eu.wauz.wauzcore.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.ShopMenu;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used for mapping interaction events to WauzCore functionalities.
 * 
 * @author Wauzmons
 */
public class EventMapper {
	
	/**
	 * A list of crafting stations to block interactions for, in MMORPG mode.
	 */
	private static List<Material> blockedCraftingStations = Arrays.asList(
			Material.CRAFTING_TABLE, Material.FURNACE, Material.ENCHANTING_TABLE, Material.BREWING_STAND, Material.ANVIL,
			Material.DISPENSER, Material.DROPPER, Material.CAKE, Material.BLAST_FURNACE, Material.CAMPFIRE,
			Material.CARTOGRAPHY_TABLE, Material.COMPOSTER, Material.FLETCHING_TABLE, Material.GRINDSTONE, Material.LOOM,
			Material.SMITHING_TABLE, Material.SMOKER, Material.STONECUTTER);
	
	/**
	 * A map of all coustom items for the MMORPG mode, indexed by trigger materials.
	 */
	private static Map<Material, CustomItem> customItemMap = new HashMap<>();
	
	/**
	 * Registers a custom item for the MMORPG mode.
	 * 
	 * @param customItem The custom item to register.
	 */
	public static void registerCustomItem(CustomItem customItem) {
		for(Material material : customItem.getCustomItemMaterials()) {
			customItemMap.put(material, customItem);
		}
	}
	
	/**
	 * Called when a player interacts with an entity.
	 * Cancels the sit command for pets.
	 * 
	 * @param event The received PlayerInteractEvent.
	 */
	public static void handleEntityInteraction(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if(player.equals(PetOverviewMenu.getOwner(entity)) && entity instanceof Wolf) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	            public void run() {
	            	try {
	            		((Wolf) entity).setSitting(false);
	            	}
	            	catch (NullPointerException e) {
	            		WauzDebugger.catchException(getClass(), e);
	            	}
	            }
			}, 10);
			return;
		}
	}
	
	/**
	 * Called when a player interacts with specific items.
	 * Prevents destruction of farmland and hitting air with a weapon.
	 * Handles opening the main menu, using scrolls, weapons, maps, skills and food.
	 * Also cancels interactions with crafting stations.
	 * Redirects oak sign clicks to the according handler.
	 * 
	 * @param event The received PlayerInteractEvent.
	 * 
	 * @see Cooldown#playerWeaponUse(Player)
	 * @see CustomItem#use(PlayerInteractEvent)
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack)
	 * @see FoodCalculator#tryToConsume(Player, ItemStack)
	 * @see WauzSigns#interact(Player, org.bukkit.block.Block)
	 */
	public static void handleItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND)) {
			event.setCancelled(true);
			return;
		}
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(event.getAction() == Action.LEFT_CLICK_AIR) {
			Cooldown.playerWeaponUse(player);
			event.setCancelled(true);
		}
		else if(itemStack != null) {
			Material type = itemStack.getType();
			CustomItem customItem = customItemMap.get(type);
			
			if(customItem != null) {
				customItem.use(event);
			}
			else if(type.equals(Material.PAPER)) {
				WauzTeleporter.enterInstanceTeleportManual(event);
			}
			else if(event.getAction().toString().contains("RIGHT")) {
				WauzPlayerSkillExecutor.tryToUseSkill(event.getPlayer(), itemStack);
				FoodCalculator.tryToConsume(event.getPlayer(), itemStack);
			}
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			if(blockedCraftingStations.contains(type)) {
				event.setCancelled(true);
			}
			else if(type.equals(Material.OAK_SIGN) || type.equals(Material.OAK_WALL_SIGN)) {
				WauzSigns.interact(player, event.getClickedBlock());
			}
		}
	}
	
	/**
	 * Called when a Survival player interacts with specific items.
	 * Prevents destruction of farmland.
	 * Handles opening the ender chest shop, using maps and pvp protection potions.
	 * Redirects oak sign clicks to the according handler.
	 * 
	 * @param event The received PlayerInteractEvent.
	 * 
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see DamageCalculator#increasePvPProtection(PlayerInteractEvent)
	 * @see WauzSigns#interact(Player, org.bukkit.block.Block)
	 * @see ShopMenu#open(Player, String, String)
	 */
	public static void handleSurvivalItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND)) {
			event.setCancelled(true);
			return;
		}
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(itemStack != null) {
			Material type = itemStack.getType();
			
			if(type.equals(Material.PAPER)) {
				WauzTeleporter.enterInstanceTeleportManual(event);
			}
			else if(type.equals(Material.EXPERIENCE_BOTTLE)) {
				DamageCalculator.increasePvPProtection(event);
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			if(type.equals(Material.ENDER_CHEST)) {
				event.setCancelled(true);
				ShopMenu.open(player, "SurvivalShop", null);
			}
			else if(type.equals(Material.OAK_SIGN) || type.equals(Material.OAK_WALL_SIGN)) {
				WauzSigns.interact(player, event.getClickedBlock());
			}
		}
	}
	
	/**
	 * Called when a player interacts with an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries to select a menu point.
	 * The trashcan and other special MMORPG items are handled here.
	 * 
	 * @param event The received InventoryClickEvent.
	 * 
	 * @see WauzInventoryHolder#selectMenuPoint(InventoryClickEvent)
	 * @see MenuUtils#onSpecialItemInventoryClick(InventoryClickEvent)
	 */
	public static void handleMenuInteraction(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		String inventoryName = ChatColor.stripColor(player.getOpenInventory().getTitle());
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You clicked in Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			WauzDebugger.log(player, "Selected Option in "
					+ StringUtils.substringAfterLast(holder.getInventoryName(), "."));
			holder.selectMenuPoint(event);
		}
		if(WauzMode.isMMORPG(player)) {
			ItemStack clicked = event.getCurrentItem();
			if(ItemUtils.isSpecificItem(clicked, "Trashcan") && ItemUtils.isNotAir(player.getItemOnCursor())) {
				player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
				player.setItemOnCursor(null);
			}
			MenuUtils.onSpecialItemInventoryClick(event);
		}
	}
	
	/**
	 * Called when a player closes an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries properly destroy the menu contents.
	 * 
	 * @param event The received InventoryCloseEvent.
	 * 
	 * @see WauzInventoryHolder#destroyInventory(InventoryCloseEvent)
	 */
	public static void handleMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		String inventoryName = ChatColor.stripColor(player.getOpenInventory().getTitle());
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You closed the Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			holder.destroyInventory(event);
		}
	}
	
}
