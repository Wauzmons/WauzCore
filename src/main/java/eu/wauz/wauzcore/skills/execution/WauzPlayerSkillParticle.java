package eu.wauz.wauzcore.skills.execution;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

public class WauzPlayerSkillParticle {
	
	Particle particle;
	
	Color color;
	
	public WauzPlayerSkillParticle(Particle particle) {
		this.particle = particle;
	}
	
	public WauzPlayerSkillParticle(Color color) {
		this.color = color;
	}
	
	public void spawn(Location location, int amount) {
		if(particle != null)
			spawnParticles(location, particle, amount);
		else
			spawnParticles(location, color, amount);
	}
	
	public static void spawnParticles(Location location, Particle particle, int amount) {
		location.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, 0, 0, 0, 0.1);
	}
	
	public static void spawnParticles(Location location, Color color, int amount) {
		location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, 0, 0, 0, 0.1, new DustOptions(color, 3));
	}

}
