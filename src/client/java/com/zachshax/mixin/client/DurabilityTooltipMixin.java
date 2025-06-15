package com.zachshax.mixin.client;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class DurabilityTooltipMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void injectDurabilityTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!stack.isDamageable()) return;

        int max = stack.getMaxDamage();
        int current = max - stack.getDamage();
        int percent = (int) ((current / (float) max) * 100);

        List<Text> tooltip = cir.getReturnValue();
        tooltip.add(Text.literal(current + "/" + max).formatted(Formatting.GRAY));
        tooltip.add(Text.literal(percent + "%").formatted(Formatting.DARK_GRAY));
    }
}