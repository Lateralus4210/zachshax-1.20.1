package com.zachshax;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ZachsHaxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			CombinedHudRenderer.render(context, tickDelta);
		});
	}
}
