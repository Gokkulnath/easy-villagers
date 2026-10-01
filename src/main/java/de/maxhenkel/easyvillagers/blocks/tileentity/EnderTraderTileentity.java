package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.inventory.ListAccessItemStacksResourceHandler;
import de.maxhenkel.easyvillagers.inventory.OutputOnlyResourceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.world.level.Level;

public class EnderTraderTileentity extends FakeWorldTileentity implements ITickableBlockEntity {

    protected ListAccessItemStacksResourceHandler inventory;
    protected OutputOnlyResourceHandler outputInventoryDelegate;

    protected long timer;

    public EnderTraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.ENDER_TRADER.get(), ModBlocks.ENDER_TRADER.get().defaultBlockState(), pos, state);
        inventory = new ListAccessItemStacksResourceHandler(4);
        outputInventoryDelegate = new OutputOnlyResourceHandler(inventory);
    }

    public long getTimer() {
        return timer;
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

        timer++;
        setChanged();

        if (timer >= EasyVillagersMod.SERVER_CONFIG.enderTraderSpawnTime.get()) {
            boolean pearl = level.getRandom().nextBoolean();
            ItemStack drop = new ItemStack(pearl ? Items.ENDER_PEARL : Items.BLAZE_ROD);

            try (Transaction transaction = Transaction.open(null)) {
                long inserted = inventory.insert(ItemResource.of(drop), drop.getCount(), transaction);
                if (inserted > 0) {
                    transaction.commit();
                    level.playSound(null, getBlockPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    timer = 0L;
                    sync();
                } else {
                    // if inventory is full, wait a bit
                    timer = EasyVillagersMod.SERVER_CONFIG.enderTraderSpawnTime.get() - 20;
                }
            }
        }
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory.getRaw(), this::setChanged);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ContainerHelper.saveAllItems(valueOutput, inventory.getRaw(), false);
        valueOutput.putLong("Timer", timer);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        ContainerHelper.loadAllItems(valueInput, inventory.getRaw());
        timer = valueInput.getLongOr("Timer", 0L);
        super.loadAdditional(valueInput);
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        return outputInventoryDelegate;
    }
}
