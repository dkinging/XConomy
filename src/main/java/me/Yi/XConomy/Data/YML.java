package me.Yi.XConomy.Data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class YML {
	public static FileConfiguration pd;
	public static FileConfiguration pdnon;
	public static FileConfiguration pdu;
	public static FileConfiguration top;
	public static HashMap<String, BigDecimal> baltopls = new HashMap<String, BigDecimal>();

	public static void create(Player a, Double ii) {
		if (pd.contains(a.getUniqueId().toString())) {
			pd.set(a.getUniqueId().toString() + ".username", a.getName());
			save(pd, DataCon.userdata);
		} else {
			pd.createSection(a.getUniqueId().toString() + ".username");
			pd.set(a.getUniqueId().toString() + ".username", a.getName());
			pd.createSection(a.getUniqueId().toString() + ".balance");
			pd.set(a.getUniqueId().toString() + ".balance", ii);
			save(pd, DataCon.userdata);
		}
	}

	public static void createu(Player a) {
		pdu.createSection(a.getName() + ".UUID");
		pdu.set(a.getName() + ".UUID", a.getUniqueId().toString());
		save(pdu, DataCon.uiddata);
	}

	public static void getbal(UUID u) {
		String am = pd.getString(u.toString() + ".balance");
		BigDecimal ls = DataFormat.formatsb(am);
		if (ls != null && !ls.equals(null)) {
			Cache.addbal(u, ls);
		}
	}

	public static void getbal_nonp(String u) {
		if (pdnon.contains(u)) {
			String am = pdnon.getString(u + ".balance");
			Cache_NonPlayer.addbal(u, DataFormat.formatsb(am));
		} else {
			Cache_NonPlayer.addbal(u, DataFormat.formatsb("0.0"));
			pdnon.createSection(u + ".balance");
			pdnon.set(u + ".balance", 0.0);
			save(pdnon, DataCon.nonpdata);
		}
	}

	public static void getuid(String name) {
		if (pdu.contains(name)) {
			Cache.adduid(name, UUID.fromString(pdu.getString(name + ".UUID")));
		}

	}

	public static void gettop() {
		if (top.contains("topname") & top.contains("topbal")) {
			if (!top.getString("topname").equals("") && !top.getString("topbal").equals("")) {
				List<String> name = Arrays.asList(top.getString("topname").split("###"));
				List<String> baltop = Arrays.asList(top.getString("topbal").split("###"));
				Integer i = 0;
				for (String xxx : name) {
					Cache.baltop_papi.add(xxx);
					Cache.baltop.put(xxx, DataFormat.formatsb(baltop.get(i)));
					i = i + 1;
				}
			}
		} else {
			top.createSection("topname");
			top.createSection("topbal");
		}
	}

	public static String getsumbal() {
		return "0.0";
	}

	public static void savetop() {
		if (!baltopls.isEmpty()) {
			Cache.baltop_papi.clear();
			for (String e : Cache.baltop.keySet()) {
				if (!baltopls.containsKey(e)) {
					baltopls.put(e, Cache.baltop.get(e));
				}
			}
			Cache.baltop.clear();
			List<Map.Entry<String, BigDecimal>> list = new ArrayList<>(baltopls.entrySet());
			Collections.sort(list, (Map.Entry<String, BigDecimal> o1, Map.Entry<String, BigDecimal> o2) -> o2.getValue()
					.compareTo(o1.getValue()));
			List<String> name = new ArrayList<String>();
			List<String> baltop = new ArrayList<String>();
			int x = 1;
			for (Map.Entry<String, BigDecimal> e : list) {
				if (x > 10) {
					break;
				}
				name.add(e.getKey());
				baltop.add(e.getValue().toString());
				Cache.baltop_papi.add(e.getKey());
				Cache.baltop.put(e.getKey(), e.getValue());
				x = x + 1;
			}
			top.set("topname", String.join("###", name));
			top.set("topbal", String.join("###", baltop));
			baltopls.clear();
			save(top, DataCon.topdata);
		}
	}

	public synchronized static void save(FileConfiguration fc, File x) {
		try {
			fc.save(x);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
