package eu.wauz.wauzcore.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import net.md_5.bungee.api.ChatColor;

public class MobModifier {

//	public static void rollRarity(MythicMobSpawnEvent event) {
//		MythicMob mob = event.getMobType();
//		if (Chance.percent(50)) {
//			event.getEntity().setCustomName(mob.getDisplayName().replace("<Prefix>", ChatColor.LIGHT_PURPLE + "Elite "));
//		} else {
//			event.getEntity().setCustomName(mob.getDisplayName().replace("<Prefix>", ""));
//		}
//		Bukkit.getLogger().info("MythicMob Spawned: " + event.getEntity().getCustomName());
//	}
	
	public static void deathEvent(MythicMobDeathEvent event) {
		String mobId = event.getEntity().getUniqueId().toString();
		Player mobOwner = PetOverviewMenu.getOwner(event.getEntity());
		
		if(mobOwner != null)
			PetOverviewMenu.unregPet(mobOwner, mobId);
		if(StringUtils.contains(event.getEntity().getCustomName(), "Strongbox"))
			MobModifier.openStrongbox(event);
		if(WauzCommandExecutor.eventTravelMap.containsKey(mobId))
			WauzCommandExecutor.eventTravelMap.remove(mobId);
	}
	
	private static Map<String, ArmorStand> activeStrongboxMap = new HashMap<>();
	
	private static void openStrongbox(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		if(entity.getMetadata("wzStrongbox").isEmpty()) {
			Location location = entity.getLocation();
			location.setY(location.getY() + 1);
			
			ArmorStand strongbox = (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
			activeStrongboxMap.put(strongbox.getUniqueId().toString(), strongbox);
			
			strongbox.setGravity(false);	
			strongbox.setInvulnerable(true);
			strongbox.setCollidable(false);
			strongbox.setVisible(false);
			
			int enemyAmount = new Random().nextInt(5) + 3;
			strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
					+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			strongbox.setCustomNameVisible(true);
			
			int strongboxTier = Integer.parseInt(StringUtils.substringAfterLast(event.getMobType().getInternalName(), "T"));
			strongbox.setMetadata("wzStrongboxTier", new FixedMetadataValue(WauzCore.getInstance(), strongboxTier));
			
			BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
			try {
				for(int iterator = 1; iterator <= enemyAmount; iterator++) {
					Location offsetLocation = entity.getLocation();
					offsetLocation.setX(offsetLocation.getX() + Chance.negativePositive(1.2f));
					offsetLocation.setZ(offsetLocation.getZ() + Chance.negativePositive(1.2f));
					
					String mobSuffix = Chance.percent(40) ? "-Elite" : "";
					Entity mob = mythicMobs.spawnMythicMob("StrongboxGuardT" + strongboxTier + mobSuffix, offsetLocation);
					mob.setMetadata("wzStrongbox", new FixedMetadataValue(WauzCore.getInstance(), strongbox.getUniqueId().toString()));
				}
				
				for(Player player : strongbox.getWorld().getPlayers())
					player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
				
			} catch (InvalidMobTypeException e) {
				e.printStackTrace();
			}
		}
		else {
			String uuidString = entity.getMetadata("wzStrongbox").get(0).asString();
			ArmorStand strongbox = activeStrongboxMap.get(uuidString);
			int enemyAmount = Integer.parseInt(StringUtils.substringBetween(
					strongbox.getCustomName(), "[" + ChatColor.DARK_RED, ChatColor.GOLD + "]"));
			enemyAmount--;
			
			if(enemyAmount < 1) {
				activeStrongboxMap.remove(uuidString);
				
				String strongboxTierString = strongbox.getMetadata("wzStrongboxTier").get(0).asString();
				DropTable dropTable = MythicMobs.inst().getDropManager().getDropTable("StrongboxDropsT" + strongboxTierString).get();
				LootBag lootBag = dropTable.generate(new DropMetadata(event.getMob(), BukkitAdapter.adapt(event.getKiller())));
				lootBag.drop(BukkitAdapter.adapt(strongbox.getLocation()));
				
				strongbox.getWorld().playEffect(strongbox.getLocation(), Effect.DRAGON_BREATH, 0);
				strongbox.remove();
			}
			else {
				strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
						+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			}
			for(Player player : strongbox.getWorld().getPlayers())
				player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
		}
	}

}
