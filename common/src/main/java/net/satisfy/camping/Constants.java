package net.satisfy.camping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "camping";
	public static final String MOD_NAME = "Camping";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	/** Copied from {@link net.minecraft.world.item.DyeColor DyeColor} */
	public static final String[] MC_COLORS = new String[]{
			"white", "orange", "magenta", "light_blue",
			"yellow", "lime", "pink", "gray",
			"light_gray", "cyan", "purple", "blue",
			"brown", "green", "red", "black"
	};
}