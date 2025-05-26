package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GunWorkbenchBlockEntity extends BaseContainerBlockEntity implements MenuProvider {
    public static final String TAG_MODE = "WorkbenchMode";
    public static final String TAG_INV = "Inventory";
    public static final String TAG_PROGRESS = "CraftProgress";
    private MenuType menuType = MenuType.SKIN_SELECTOR;
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 20 * 20;
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);



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
                return 8;
            }
        };
    }

    public MenuType getMenuType() {
        return this.menuType;
    }

    public void setMenuType(MenuType type) {
        this.menuType = type;
        this.setChanged();
    }

    public @NotNull AbstractContainerMenu createMenu(int pContainerId, Inventory inventory) {
        return menuType.fac.create(pContainerId, inventory, this, this.data, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    public void baseTick(Level level, BlockPos pos, BlockState state) {

            if (hasRecipe()) {

                if (level.getRandom().nextDouble() < 0.05D) {
                    //LevelHelper.playSound(level, pos, PGSounds.WORKBENCH_CRAFT.get(), SoundSource.BLOCKS, 1);
                }
                increaseCraftingProgress();
                setChanged(level, pos, state);

                if (hasProgressFinished()) {
                    craftItem();
                    resetProgress();
                }
            } else {
                resetProgress();

        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {


    }

    private boolean hasRecipe() {

        //System.out.println(Arrays.stream(recipe.get().getBaseItem().getItems()).sequential().toList());


        return true;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        this.menuType = MenuType.values()[pTag.getInt(TAG_MODE)];

        progress = pTag.getInt(TAG_PROGRESS);
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        pTag.putInt(TAG_MODE, this.menuType.ordinal());

        pTag.putInt(TAG_PROGRESS, this.progress);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ((GunWorkbenchBlockEntity) t).baseTick(level, pos, state);
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
        return 3;
    }


    public interface IMenuFactory<T extends AbstractContainerMenu> {
        T create(int id, Inventory inv, GunWorkbenchBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access);
    }

    public enum MenuType {
        WAYPOINT_TRANSFER(WaypointTransferMenu::new),
        SKIN_SELECTOR(SkinSelectorMenu::new),
        CRAFTING(WorkbenchCraftingMenu::new)
        ;

        final IMenuFactory<AbstractContainerMenu> fac;
        MenuType(IMenuFactory<AbstractContainerMenu> factory) {
            this.fac = factory;
        }
    }
}