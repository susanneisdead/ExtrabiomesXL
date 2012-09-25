/**
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license
 * located in /MMPL-1.0.txt
 */

package extrabiomes;

import java.io.File;
import java.util.logging.Level;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import extrabiomes.biomes.BiomeManagerImpl;
import extrabiomes.features.FeatureGenerator;
import extrabiomes.trees.TreeBlocks;

@Mod(modid = "ExtrabiomesXL", name = "ExtrabiomesXL", version = "3.0.0a")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class Extrabiomes {

	@SidedProxy(clientSide = "extrabiomes.client.ClientProxy", serverSide = "extrabiomes.CommonProxy")
	public static CommonProxy			proxy;
	@Instance("ExtrabiomesXL")
	public static Extrabiomes			instance;

	private static BiomeManagerImpl		biomeManager	= new BiomeManagerImpl();
	private static PluginManagerImpl	pluginManager	= new PluginManagerImpl();

	@Init
	public static void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		proxy.registerWorldGenerator(new FeatureGenerator());

		biomeManager.initialize();
	}

	@PostInit
	public static void postInit(FMLPostInitializationEvent event) {
		pluginManager.activatePlugins();
	}

	@PreInit
	public static void preInit(FMLPreInitializationEvent event) {
		ExtrabiomesLog.configureLogging();
		final ExtrabiomesConfig cfg = new ExtrabiomesConfig(new File(
				event.getModConfigurationDirectory(),
				"/extrabiomes/extrabiomes.cfg"));
		try {
			cfg.load();

			BiomeManagerImpl.loadSettings(cfg);
			BiomeManagerImpl.preInit();

		} catch (final Exception e) {
			ExtrabiomesLog
					.log(Level.SEVERE, e,
							"ExtrabiomesXL had a problem loading it's configuration.");
		} finally {
			cfg.save();
		}

		TreeBlocks.init();
	}
}
