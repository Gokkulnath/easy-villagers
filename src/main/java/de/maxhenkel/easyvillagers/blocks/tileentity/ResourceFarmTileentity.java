package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
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

        if (level != null) {
            var enchantmentRegistry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            for (Holder.Reference<Enchantment> enchantmentRef : enchantmentRegistry.holders().toList()) {
                if (items.size() >= 54) break;
                Enchantment enchantment = enchantmentRef.value();
                ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantmentRef, enchantment.getMaxLevel()));
                items.add(enchantedBook);
            }
        }

        return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x6, id, playerInventory, new CustomReadOnlyContainer(items), 6) {
            @Override
            public ItemStack quickMoveStack(Player playerIn, int index) {
                // Return empty stack to disable shift-clicking, preventing depletion bug
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
            // Read-only logic from player input, except our initialization which overrides it, wait, super.setItem already worked.
        }

        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            return false;
        }

        @Override
        public void setChanged() {
            // Do nothing
        }
    }
}
