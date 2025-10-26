package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGRecipeTypes;
import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class GunWorkbenchBlockEntity extends RandomizableContainerBlockEntity implements MenuProvider {
    public static final String TAG_MODE = "WorkbenchMode";
    public static final String TAG_PROGRESS = "CraftProgress";
    private MenuType menuType = MenuType.CRAFTING;
    public static final int OUTPUT_SLOT = 4;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = PGHelper.seconds(3);
    private NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

    public GunWorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PGBlockEntities.GUN_WORKBENCH, pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GunWorkbenchBlockEntity.this.progress;
                    case 1 -> GunWorkbenchBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GunWorkbenchBlockEntity.this.progress = pValue;
                    case 1 -> GunWorkbenchBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public List<ItemStack> ingredients() {
        return items.subList(0, 4);
    }

    public AbstractContainerMenu createMenu(int pContainerId, Inventory inventory) {
        return menuType.fac.create(pContainerId, inventory, this, this.data, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.menuType = MenuType.values()[tag.getInt(TAG_MODE)];
        ContainerHelper.loadAllItems(tag, items);
        progress = tag.getInt(TAG_PROGRESS);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_MODE, this.menuType.ordinal());
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt(TAG_PROGRESS, this.progress);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ((GunWorkbenchBlockEntity) t).baseTick(level, pos, state);
    }

    private boolean hasCraftingFinished() {
        return this.progress == this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private void baseTick(Level level, BlockPos pos, BlockState state) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged();

            if (hasCraftingFinished()) {
                if (craftItem()) {
                    resetProgress();
                }
            }
        } else {
            resetProgress();
        }
    }

    private boolean craftItem() {
        Optional<PortalGunWorkbenchRecipe> holder = getCurrentRecipe();
        if (holder.isPresent()) {
            PortalGunWorkbenchRecipe recipe = holder.get();

            ItemStack output = recipe.getResult();
            ItemStack result = output.copy();
            ItemStack inOutputSlot = this.getItem(OUTPUT_SLOT);

            if (inOutputSlot.isEmpty()) {
                lowerInputs(recipe);
                this.setItem(OUTPUT_SLOT, result);
                return true;
            } else {
                if (ItemStack.matches(inOutputSlot, result)) {
                    int i = inOutputSlot.getCount();
                    int j = result.getCount();
                    result.setCount(i + j);
                    lowerInputs(recipe);
                    this.setItem(OUTPUT_SLOT, result);
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    public void setMenuType(int i) {
        this.menuType = MenuType.values()[i];
    }

    private void lowerInputs(PortalGunWorkbenchRecipe recipe) {
        List<ItemStack> stacks = recipe.getInputs();
        for (ItemStack stack : stacks) {
            for (ItemStack invStack : this.items) {
                if (ItemStack.matches(stack, invStack)) {
                    int i = stack.getCount();
                    int j = invStack.getCount();
                    int result = Math.max(j - i, 0);
                    invStack.setCount(result);
                }
            }
        }
    }

    private void clearInputs() {
        for (int i = 0; i < 4; i++) {
            this.setItem(i, ItemStack.EMPTY);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        Optional<PortalGunWorkbenchRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            ItemStack output = recipe.get().getResult();
            return this.canPlaceItem(OUTPUT_SLOT, output);
        }
        return false;
    }

    private Optional<PortalGunWorkbenchRecipe> getCurrentRecipe() {
        return level.getRecipeManager().getRecipeFor(PGRecipeTypes.WORKBENCH_TYPE, this, level);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.ricksportalgun.workbench");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.ricksportalgun.workbench");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    public int getContainerSize() {
        return 10;
    }

    public interface IMenuFactory<T extends AbstractContainerMenu> {
        T create(int id, Inventory inv, GunWorkbenchBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access);
    }

    public enum MenuType {
        WAYPOINT_TRANSFER(WaypointTransferMenu::new),
        SKIN_SELECTOR(SkinSelectorMenu::new),
        CRAFTING(WorkbenchCraftingMenu::new);

        final IMenuFactory<AbstractContainerMenu> fac;

        MenuType(IMenuFactory<AbstractContainerMenu> factory) {
            this.fac = factory;
        }
    }
}