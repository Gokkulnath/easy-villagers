package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.core.Direction;

public class EnderTraderRenderState extends BlockEntityRenderState {
    public Direction direction = Direction.NORTH;
    public final EndermanRenderState endermanRenderState = new EndermanRenderState();
}
