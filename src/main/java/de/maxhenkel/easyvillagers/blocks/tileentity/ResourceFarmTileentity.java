package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ResourceFarmTileentity extends BlockEntity implements MenuProvider {

    public ResourceFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.RESOURCE_FARM.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.easy_villagers.resource_farm");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        List<ItemStack> items = new ArrayList<>();

        items.add(new ItemStack(Items.REDSTONE, 64));
        items.add(new ItemStack(Items.DIAMOND_BLOCK, 64));
        items.add(new ItemStack(Items.NETHERITE_INGOT, 64));
        items.add(new ItemStack(Items.SUGAR_CANE, 64));
        items.add(new ItemStack(Items.BOOK, 64));
        items.add(new ItemStack(Items.ELYTRA, 1));
        items.add(new ItemStack(Items.EMERALD, 64));
        items.add(new ItemStack(Items.SHULKER_BOX, 1));
        items.add(new ItemStack(Items.SHULKER_SHELL, 64));
        items.add(new ItemStack(Items.GUNPOWDER, 64));
        items.add(new ItemStack(Items.PAPER, 64));
        items.add(new ItemStack(Items.FIREWORK_ROCKET, 64));
        items.add(new ItemStack(Items.MAGMA_CREAM, 64));
        items.add(new ItemStack(Items.SPIDER_EYE, 64));
        items.add(new ItemStack(Items.ENDER_PEARL, 16));
        items.add(new ItemStack(Items.BLAZE_ROD, 64));
        items.add(new ItemStack(Items.WATER_BUCKET, 1));
        items.add(new ItemStack(Items.LAVA_BUCKET, 1));
        items.add(new ItemStack(Items.GOLDEN_APPLE, 64));
        items.add(new ItemStack(Items.STRING, 64));
        items.add(new ItemStack(Items.COAL, 64));
        items.add(new ItemStack(Items.REDSTONE_BLOCK, 64));
        items.add(new ItemStack(Items.NETHER_QUARTZ_ORE, 64));
        items.add(new ItemStack(Items.HOPPER, 64));
        items.add(new ItemStack(Items.NETHER_WART, 64));
        items.add(new ItemStack(Items.EMERALD_BLOCK, 64));
        items.add(new ItemStack(Items.GLASS, 64));
        items.add(new ItemStack(Items.LAPIS_BLOCK, 64));
        items.add(new ItemStack(Items.NETHER_BRICK, 64));
        items.add(new ItemStack(Items.SLIME_BALL, 64));
        items.add(new ItemStack(Items.NETHER_WART_BLOCK, 64));
        items.add(new ItemStack(Items.RED_NETHER_BRICKS, 64));
        items.add(new ItemStack(Items.SLIME_BLOCK, 64));

        return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x6, id, playerInventory, new CustomReadOnlyContainer(items), 6) {
            @Override
            public ItemStack quickMoveStack(Player playerIn, int index) {
                return ItemStack.EMPTY;
            }
        };
    }

    private static class CustomReadOnlyContainer extends net.minecraft.world.SimpleContainer {
        public CustomReadOnlyContainer(List<ItemStack> items) {
            super(54);
            for (int i = 0; i < items.size() && i < 54; i++) {
                super.setItem(i, items.get(i).copy());
            }
        }

        @Override
        public ItemStack removeItem(int index, int count) {
            ItemStack stack = getItem(index);
            if (!stack.isEmpty()) {
                return stack.copyWithCount(Math.min(count, stack.getMaxStackSize()));
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int index, ItemStack stack) {
        }

        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            return false;
        }

        @Override
        public void setChanged() {
        }
    }
}
