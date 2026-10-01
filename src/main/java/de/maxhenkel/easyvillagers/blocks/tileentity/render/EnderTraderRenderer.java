package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.HorizontalRotatableBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.EnderTraderTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class EnderTraderRenderer extends BlockRendererBase<EnderTraderTileentity, EnderTraderRenderState> {

    private WeakReference<Enderman> endermanCache = new WeakReference<>(null);
    private WeakReference<EndermanRenderer> endermanRendererCache = new WeakReference<>(null);

    public EnderTraderRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public EnderTraderRenderState createRenderState() {
        return new EnderTraderRenderState();
    }

    @Override
    public void extractRenderState(EnderTraderTileentity trader, EnderTraderRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(trader, state, partialTicks, pos, crumblingOverlay);
        Direction dir = trader.getBlockState().getValue(HorizontalRotatableBlock.FACING);
        if (dir != null) {
            state.direction = dir;
        }

        Enderman enderman = endermanCache.get();
        if (enderman == null) {
            enderman = new Enderman(EntityTypes.ENDERMAN, minecraft.level);
            endermanCache = new WeakReference<>(enderman);
        }

        EndermanRenderer endermanRenderer = endermanRendererCache.get();
        if (endermanRenderer == null) {
            endermanRenderer = new EndermanRenderer(createEntityRenderer());
            endermanRendererCache = new WeakReference<>(endermanRenderer);
        }

        endermanRenderer.extractRenderState(enderman, state.endermanRenderState, partialTicks);
    }

    @Override
    public void submit(EnderTraderRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        EndermanRenderer endermanRenderer = endermanRendererCache.get();
        if (endermanRenderer == null) {
            endermanRenderer = new EndermanRenderer(createEntityRenderer());
            endermanRendererCache = new WeakReference<>(endermanRenderer);
        }

        stack.pushPose();
        stack.translate(0.5D, 1D / 16D, 0.5D);
        stack.rotate(Axis.YP.rotationDegrees(-state.direction.toYRot()));
        stack.scale(0.25F, 0.25F, 0.25F);

        endermanRenderer.submit(state.endermanRenderState, stack, collector, cameraRenderState);

        stack.popPose();

        stack.popPose();
    }
}
