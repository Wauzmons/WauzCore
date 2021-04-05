package eu.wauz.wauzcore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.data.ConfigurationLoader;
import eu.wauz.wauzcore.data.DiscordConfigurator;
import eu.wauz.wauzcore.data.ServerConfigurator;
import eu.wauz.wauzcore.discord.ShiroDiscordBot;
import eu.wauz.wauzcore.discord.WauzLogFilter;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.system.WauzModules;
import eu.wauz.wauzcore.system.WauzRepeatingTasks;
import eu.wauz.wauzcore.system.annotations.AnnotationLoader;
import eu.wauz.wauzcore.system.api.WebServerManager;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.listeners.ArmorEquipEventListener;
import eu.wauz.wauzcore.system.listeners.BaseModuleListener;
import eu.wauz.wauzcore.system.listeners.BlockProtectionListener;
import eu.wauz.wauzcore.system.listeners.CitizenListener;
import eu.wauz.wauzcore.system.listeners.InventoryListener;
import eu.wauz.wauzcore.system.listeners.MythicMobsListener;
import eu.wauz.wauzcore.system.listeners.PetModuleListener;
import eu.wauz.wauzcore.system.listeners.PlayerAmbientListener;
import eu.wauz.wauzcore.system.listeners.PlayerCombatListener;
import eu.wauz.wauzcore.system.listeners.PlayerInteractionListener;
import eu.wauz.wauzcore.system.listeners.ProjectileMovementListener;
import eu.wauz.wauzcore.system.listeners.WauzDiscordListener;

/**
 * The main class of the plugin and holder of system information.
 * Initializes all static data, listerners, API and repeating tasks.
 * When the server stops it will clean it up again.
 * 
 * @author Wauzmons
 */
public class WauzCore extends JavaPlugin {
	
	/**
	 * The maximum level a player can reach in MMORPG mode.
	 */
	public static final int MAX_PLAYER_LEVEL = 60;
	
	/**
	 * The maximum level a player can reach in Survival mode.
	 */
	public static final int MAX_PLAYER_LEVEL_SURVIVAL = 30;
	
	/**
	 * The maximum crafting skill a player can reach in MMORPG mode.
	 */
	public static final int MAX_CRAFTING_SKILL = 40;
	
	/**
	 * The maximum breeding skill a player can reach in MMORPG mode.
	 */
	public static final int MAX_BREEDING_SKILL = 10;
	
	/**
	 * The public IP address of the Minecraft server.
	 */
	public static final String IP = Bukkit.getServer().getIp();
	
	/**
	 * The public IP address and port of the Minecraft server.
	 */
	public static final String IP_AND_PORT = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();
	
	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzCore instance;
	
	/**
	 * The Discord bot running on the server.
	 */
	private static ShiroDiscordBot shiroDiscordBot;
	
	/**
	 * The filter used to listen to log records from Bukkit.
	 */
	private WauzLogFilter logFilter;
	
	/**
	 * The WebServerManager used for the web based API.
	 */
	private static WebServerManager webServerManager;
	
	/**
	 * Gets called when the server is started.
	 */
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pluginManager = getServer().getPluginManager();
		boolean isMainModuleActive = WauzModules.isMainModuleActive();
		if(isMainModuleActive) {
			shiroDiscordBot = new ShiroDiscordBot();
			logFilter = new WauzLogFilter();
			pluginManager.registerEvents(new WauzDiscordListener(), this);
			((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(logFilter);
			getLogger().info("Enabled Discord Integration!");
		}
		
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzCore v" + getDescription().getVersion());
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
		
		if(isMainModuleActive) {
			ConfigurationLoader.init();
			getLogger().info("Finished Loading Data from Files!");
			
			AnnotationLoader.init();
			getLogger().info("Finished Loading Data from Predefined Classes!");
			
			pluginManager.registerEvents(new ArmorEquipEventListener(), this);
			pluginManager.registerEvents(new BlockProtectionListener(), this);
			pluginManager.registerEvents(new CitizenListener(), this);
			pluginManager.registerEvents(new InventoryListener(), this);
			pluginManager.registerEvents(new MythicMobsListener(), this);
			pluginManager.registerEvents(new PlayerAmbientListener(), this);
			pluginManager.registerEvents(new PlayerCombatListener(), this);
			pluginManager.registerEvents(new PlayerInteractionListener(), this);
			pluginManager.registerEvents(new ProjectileMovementListener(), this);
			getLogger().info("Registered EventListeners!");
			
			int port = getWebApiPort();
			webServerManager = new WebServerManager(port);
			getLogger().info("Started WebServerManager on port " + port + "!");
			
			WauzRepeatingTasks.schedule(this);
			getLogger().info("Scheduled Repeating Tasks!");
			
			if(DiscordConfigurator.showStartStopNotification()) {
				shiroDiscordBot.sendEmbedFromMinecraft(null, ":white_check_mark: " + WauzCore.getServerKey()
						+ " has been started!", null, Color.GREEN, false);
			}
		}
		else {
			pluginManager.registerEvents(new BaseModuleListener(), this);
			if(WauzModules.isPetsModuleActive()) {
				pluginManager.registerEvents(new PetModuleListener(), this);
			}
		}
	}

	/**
	 * Gets called when the server is stopped.
	 */
	@Override
	public void onDisable() {
		if(WauzModules.isMainModuleActive()) {
			if(DiscordConfigurator.showStartStopNotification()) {
				shiroDiscordBot.sendEmbedFromMinecraft(null, ":octagonal_sign: " + WauzCore.getServerKey()
						+ " has been stopped!", null, Color.RED, false);
			}
			logFilter.close();
			shiroDiscordBot.stop();
			getLogger().info("Shiro's taking a nap!");
			
			webServerManager.stop();
			getLogger().info("Stopped WebServerManager!");
			
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				WauzPlayerRegistrator.logout(player);
			}
			getLogger().info("Logged Out Players!");
			
			for(World world : Bukkit.getWorlds()) {
				InstanceManager.closeInstance(world);
			}
			getLogger().info("Closed Active Instances!");
		}
	}

	/**
	 * Listens for incoming commands from the plugin.xml.
	 * Redirects the needed informtation to the command executor.
	 * 
	 * @see WauzCommandExecutor
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return WauzCommandExecutor.execute(sender, cmd, args);
	}

	/**
	 * @return The instance of this class, that is created by the Minecraft server.
	 */
	public static WauzCore getInstance() {
		return instance;
	}
	
	/**
	 * @return The Discord bot running on this server.
	 */
	public static ShiroDiscordBot getShiroDiscordBot() {
		return shiroDiscordBot;
	}

	/**
	 * @return The WebServerManager used for the web based API.
	 */
	public static WebServerManager getWebServerManager() {
		return webServerManager;
	}
	
	/**
	 * @return The location of the hub, where players start their adventures.
	 */
	public static Location getHubLocation() {
		return new Location(Bukkit.getWorld("HubNexus"), 0.5, 95, 0.5);
	}
	
	/**
	 * Finds all players with cached player data.
	 * 
	 * @return A list of all registered active players.
	 * 
	 * @see WauzPlayerDataPool
	 */
	public static List<Player> getRegisteredActivePlayers() {
		List<Player> players = new ArrayList<Player>();
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(WauzPlayerDataPool.getPlayer(player) != null) {
				players.add(player);
			}
		}
		return players;
	}
	
	/**
	 * Finds an online player by their name.
	 * 
	 * @param name The name of the player.
	 * 
	 * @return The player if they are online, else null.
	 */
	public static Player getOnlinePlayer(String name) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Finds an offline player by their name.
	 * 
	 * @param name The name of the player.
	 * @return The player if they exist, else null.
	 */
	public static OfflinePlayer getOfflinePlayer(String name) {
		for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
			if(player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * @return The key of the server, defined in the .yml configuration.
	 */
	public static String getServerKey() {
		return ServerConfigurator.getServerKey();
	}
	
	/**
	 * @return The port of the web api, defined in the .yml configuration.
	 */
	public static int getWebApiPort() {
		return ServerConfigurator.getServerApiPort();
	}
	
}
