package com.example.examplemod.Mixins.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "updateEntityAfterFallOn(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void updateEntityAfterFallOn(IBlockReader p_176216_1_, Entity entityIn, CallbackInfo ci) {
        if (entityIn.isSuppressingBounce()) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
        } else {
            this.bounceEntity(entityIn);
        }
        ci.cancel();
    }
    private void bounceEntity(Entity entity) {
        Vector3d vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * d0, vector3d.z);
        }
    }
    @Inject(method = "fallOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    public void fallOn(World p_180658_1_, BlockPos p_180658_2_, Entity entityIn, float fallDistance, CallbackInfo ci) {
        if (entityIn.isSuppressingBounce()) {
            entityIn.causeFallDamage(fallDistance, 1.0F);
        } else {
            entityIn.causeFallDamage(fallDistance, 0.0F);

        }
        ci.cancel();
    }
    @Inject(method = "stepOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void stepOn(World p_176199_1_, BlockPos p_176199_2_, Entity entityIn, CallbackInfo ci) {
        double d0 = Math.abs(entityIn.getDeltaMovement().y);
        if (d0 < 0.1D && !entityIn.isSteppingCarefully()) {
            double d1 = 0.4D + d0 * 0.2D;
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(d1, 1.0D, d1));
        }
        ci.cancel();
    }
}
