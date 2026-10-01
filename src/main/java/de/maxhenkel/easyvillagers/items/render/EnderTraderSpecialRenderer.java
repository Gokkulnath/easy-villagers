package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.EnderTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.EnderTraderRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.EnderTraderRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EnderTraderSpecialRenderer extends ItemSpecialRendererBase<EnderTraderTileentity, EnderTraderRenderState> {

    public EnderTraderSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, EnderTraderTileentity.class);
        renderer = new EnderTraderRenderer(modelSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<EnderTraderTileentity> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<EnderTraderTileentity> bake(BakingContext context) {
            return new EnderTraderSpecialRenderer(context.entityModelSet(), () -> ModBlocks.ENDER_TRADER.get().defaultBlockState());
        }

        @Override
        public MapCodec<EnderTraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
