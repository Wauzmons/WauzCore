//package eu.wauz.wauzcore.legacy;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerMoveEvent;
//
//import eu.wauz.wauzcore.WauzCore;
//import eu.wauz.wauzcore.arcade.ArcadeLobby;
//import eu.wauz.wauzcore.arcade.ArcadeMinigame;
//import eu.wauz.wauzcore.arcade.ArcadeUtils;
//import eu.wauz.wauzcore.building.ShapeCircle;
//import eu.wauz.wauzcore.system.annotations.Minigame;
//
///**
// * A survival minigame, where you have to avoid bars trying to knock you down.
// * 
// * @author Wauzmons
// */
//@Minigame
//public class MinigameJumpShowdown implements ArcadeMinigame {
//
//	/**
//	 * The list of blocks that can break.
//	 */
//	private List<Block> breakingBlocks = new ArrayList<>();
//	
//	/**
//	 * The players who have been eliminated.
//	 */
//	private List<Player> eliminatedPlayers = new ArrayList<>();
//	
//	/**
//	 * The remaining spinners in the arena.
//	 */
//	private List<SpinController> spinners = new ArrayList<>();
//	
//	/**
//	 * The amount of players who can loose the game.
//	 */
//	private int maxLosingPlayers = 1;
//
//	/**
//	 * @return The display name of the minigame.
//	 */
//	@Override
//	public String getName() {
//		return "Jump-Showdown";
//	}
//
//	/**
//	 * @return The scoreboard description of the minigame.
//	 */
//	@Override
//	public List<String> getDescription() {
//		List<String> description = new ArrayList<>();
//		description.add(ChatColor.WHITE + "Avoid the Bars, pushing");
//		description.add(ChatColor.WHITE + "you off the Breaking Tiles");
//		description.add(ChatColor.WHITE + "Keep jumping to Survive!");
//		description.add("   ");
//		description.add(ChatColor.RED + "Eliminated Players: " + ChatColor.GOLD + eliminatedPlayers.size() + " / " + maxLosingPlayers);
//		return description;
//	}
//	
//	/**
//	 * Starts a new game.
//	 * 
//	 * @param players The players who participate.
//	 */
//	@Override
//	public void startGame(List<Player> players) {
//		maxLosingPlayers = players.size() - (players.size() / 2);
//		World world = ArcadeLobby.getWorld();
//		Location floorLocation = new Location(world, 750.5, 85, 1250.5);
//		breakingBlocks.clear();
//		breakingBlocks.addAll(new ShapeCircle(floorLocation, 16, false).create(Material.PINK_CONCRETE));
//		new ShapeCircle(floorLocation, 6, false).create(Material.AIR);
//		spinners.add(new SpinController(floorLocation.clone().add(0, 1, 0), 16, 90));
//		Location spawnLocation = new Location(world, 750.5, 88, 1239.5, 0, 0);
//		ArcadeUtils.placeTeam(players, spawnLocation, 3, 3);
//		for(Player player : ArcadeLobby.getPlayingPlayers()) {
//			player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.BARRIER);
//		}
//		ArcadeUtils.runStartTimer(10, 120);
//	}
//	
//	/**
//	 * Ends the game and decides a winner.
//	 * 
//	 * @return The players wo won the game.
//	 */
//	@Override
//	public List<Player> endGame() {
//		List<Player> winners = new ArrayList<>(ArcadeLobby.getPlayingPlayers());
//		winners.removeAll(eliminatedPlayers);
//		breakingBlocks.clear();
//		eliminatedPlayers.clear();
//		for(SpinController spinner : spinners) {
//			spinner.remove();
//		}
//		spinners.clear();
//		maxLosingPlayers = 1;
//		return winners;
//	}
//	
//	/**
//	 * Handles the start event, that gets fired when the start countdown ends.
//	 */
//	@Override
//	public void handleStartEvent() {
//		for(Player player : ArcadeLobby.getPlayingPlayers()) {
//			player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
//		}
//	}
//	
//	/**
//	 * Handles the given quit event, that occured in the minigame.
//	 * 
//	 * @param player The player who quit.
//	 */
//	@Override
//	public void handleQuitEvent(Player player) {
//		eliminatedPlayers.add(player);
//		if(eliminatedPlayers.size() >= maxLosingPlayers) {
//			ArcadeLobby.endGame();
//		}
//	}
//	
//	/**
//	 * Handles the given move event, that occured in the minigame.
//	 * 
//	 * @param event The move event.
//	 */
//	@Override
//	public void handleMoveEvent(PlayerMoveEvent event) {
//		Player player = event.getPlayer();
//		Location location = player.getLocation().clone().subtract(0, 1, 0);
//		for(double x = -0.3; x <= 0.3; x += 0.3) {
//			for(double z = -0.3; z <= 0.3; z += 0.3) {
//				makeBlockCrumble(location.clone().add(x, 0, z).getBlock());
//			}
//		}
//		if(location.getY() <= 32) {
//			eliminatedPlayers.add(player);
//			player.teleport(new Location(ArcadeLobby.getWorld(), 750.5, 96, 1250.5, 0, 0));
//			player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
//			for(Player playing : ArcadeLobby.getPlayingPlayers()) {
//				playing.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.RED + " has been eliminated!");
//			}
//			if(eliminatedPlayers.size() >= maxLosingPlayers) {
//				ArcadeLobby.endGame();
//			}
//		}
//	}
//	
//	/**
//	 * Makes the given block crumble to the next stage and removes it temporarily from the list.
//	 * 
//	 * @param block The block to crumble.
//	 */
//	public void makeBlockCrumble(Block block) {
//		if(breakingBlocks.contains(block)) {
//			breakingBlocks.remove(block);
//			switch(block.getType()) {
//			case PINK_CONCRETE:
//				block.setType(Material.MAGENTA_CONCRETE);
//				makeBlockCrumbling(block);
//				break;
//			case MAGENTA_CONCRETE:
//				block.setType(Material.PURPLE_CONCRETE);
//				makeBlockCrumbling(block);
//				break;
//			case PURPLE_CONCRETE:
//				block.setType(Material.BLUE_CONCRETE);
//				makeBlockCrumbling(block);
//				break;
//			case BLUE_CONCRETE:
//				block.setType(Material.BLACK_CONCRETE);
//				makeBlockCrumbling(block);
//				break;
//			default:
//				block.setType(Material.AIR);
//				break;
//			}
//			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
//		}
//	}
//
//	/**
//	 * Makes the given block crumbling and re-adds it to the list.
//	 * 
//	 * @param block The block to make crumbling.
//	 */
//	private void makeBlockCrumbling(Block block) {
//		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
//			
//			@Override
//			public void run() {
//				breakingBlocks.add(block);
//			}
//			
//		}, 20);
//	}
//	
//}
