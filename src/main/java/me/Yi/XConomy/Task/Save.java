package me.Yi.XConomy.Task;

import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.Data.DataCon;

public class Save extends BukkitRunnable {

	private final UUID UID;
	private final BigDecimal amount;
	private final int type;
	
	public Save(UUID UID,BigDecimal amount,Integer type) {
		this.UID = UID;
		this.amount = amount;
		this.type = type;
	}
	
	@Override
	public void run() {
		DataCon.save(UID, amount, type);
	}
}