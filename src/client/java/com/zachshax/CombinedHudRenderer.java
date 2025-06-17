package com.zachshax;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class CombinedHudRenderer {
    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        int xOffset = 10;
        int yOffset = client.getWindow().getScaledHeight() - 90;

        // Durability Info
        ItemStack stack = client.player.getMainHandStack();
        if (!stack.isEmpty() && stack.isDamageable()) {
            int durability = stack.getMaxDamage() - stack.getDamage();
            int max = stack.getMaxDamage();
            double percent = (durability * 100.0) / max;

            context.drawText(client.textRenderer,
                Text.literal(durability + " / " + max), xOffset, yOffset, 0xFFFFFF, true);
            context.drawText(client.textRenderer,
                Text.literal(String.format("%.1f%%", percent)), xOffset, yOffset + 10, 0xFFFFFF, true);

            yOffset += 30; // Add spacing before location info
        }

        // Location Info
        BlockPos pos = client.player.getBlockPos();
        double x = client.player.getX();
        double y = client.player.getY();
        double z = client.player.getZ();
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        RegistryKey<net.minecraft.world.biome.Biome> biomeKey = client.world
            .getRegistryManager()
            .get(RegistryKeys.BIOME)
            .getKey(client.world.getBiome(pos).value())
            .orElse(null);

        String biomeName = biomeKey != null
            ? biomeKey.getValue().getPath().replace('_', ' ')
            : "unknown";

        context.drawText(client.textRenderer,
            Text.literal(String.format("XYZ: %d / %d / %d", pos.getX(), pos.getY(), pos.getZ()))
                .formatted(Formatting.BOLD),
            xOffset, yOffset, 0xFFFFFF, true);
        context.drawText(client.textRenderer,
            Text.literal(String.format("%.1f / %.1f / %.1f", x, y, z)),
            xOffset, yOffset + 10, 0xAAAAFF, true);
        context.drawText(client.textRenderer,
            Text.literal("Biome: " + biomeName),
            xOffset, yOffset + 20, 0xAAAAFF, true);
        context.drawText(client.textRenderer,
            Text.literal(String.format("Chunk: %d / %d", chunkX, chunkZ)),
            xOffset, yOffset + 30, 0xAAAAFF, true);
        context.drawText(client.textRenderer,
            Text.literal(String.format("Region: r.%d.%d.mca", regionX, regionZ))
                .formatted(Formatting.GRAY),
            xOffset, yOffset + 40, 0x888888, true);
    }
}
