package shadows.click.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import shadows.click.ClickMachine;
import shadows.click.ClickMachineConfig;
import shadows.click.block.gui.GuiAutoClick;
import shadows.placebo.util.PlaceboUtil;

@EventBusSubscriber(modid = ClickMachine.MODID, value = Side.CLIENT)
public class ClickClientProxy extends ClickProxy {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		PlaceboUtil.sMRL(ClickMachine.AUTO_CLICKER, 0, "facing=north");
		for (int i = 0; i < 9; i++) {
			if (ClickMachineConfig.usesRF) GuiAutoClick.setFormatArgs(i, ClickMachineConfig.speeds[i], ClickMachineConfig.powerPerSpeed[i]);
			else GuiAutoClick.setFormatArgs(i, ClickMachineConfig.speeds[i]);
		}
	}

}
