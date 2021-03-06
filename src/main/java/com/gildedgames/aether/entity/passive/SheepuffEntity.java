package com.gildedgames.aether.entity.passive;

import com.gildedgames.aether.entity.AetherAnimalEntity;
import com.gildedgames.aether.registry.AetherEntityTypes;
import com.gildedgames.aether.entity.ai.EatAetherGrassGoal;
import com.gildedgames.aether.registry.AetherSoundEvents;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("deprecation")
public class SheepuffEntity extends AetherAnimalEntity implements IShearable {
    public static final DataParameter<Byte> FLEECE_COLOR = EntityDataManager.createKey(SheepuffEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Boolean> SHEARED = EntityDataManager.<Boolean>createKey(SheepuffEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> PUFFY = EntityDataManager.createKey(SheepuffEntity.class, DataSerializers.BOOLEAN);

    private int sheepTimer, amountEaten;
    private EatAetherGrassGoal eatGrassGoal;

    private static final Map<DyeColor, IItemProvider> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
        map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        map.put(DyeColor.LIME, Blocks.LIME_WOOL);
        map.put(DyeColor.PINK, Blocks.PINK_WOOL);
        map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        map.put(DyeColor.RED, Blocks.RED_WOOL);
        map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });

    public SheepuffEntity(EntityType<? extends SheepuffEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public SheepuffEntity(World worldIn) {
        this(AetherEntityTypes.SHEEPUFF.get(), worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FLEECE_COLOR, (byte) 0);
        this.dataManager.register(SHEARED, false);
        this.dataManager.register(PUFFY, false);
    }

    @Override
    protected void registerGoals() {
        this.eatGrassGoal = new EatAetherGrassGoal(this);
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, Ingredient.fromItems(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, this.eatGrassGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.eatGrassGoal.getEatingGrassTimer();
        super.updateAITasks();
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return AetherAnimalEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23000000417232513D);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return AetherEntityTypes.SHEEPUFF.get().create(this.world);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getHeadRotationPointY(float p_70894_1_) {
        if (this.sheepTimer <= 0) {
            return 0.0F;
        } else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
            return 1.0F;
        } else {
            return this.sheepTimer < 4 ? (this.sheepTimer - p_70894_1_) / 4.0F : -(this.sheepTimer - 40 - p_70894_1_) / 4.0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getHeadRotationAngleX(float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f = (this.sheepTimer - 4 - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5.0F) + 0.21991149F * MathHelper.sin(f * 28.7F);
        } else {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5.0F) : this.rotationPitch * ((float)Math.PI / 180.0F);
        }
    }

    /**
     * Handles the functionality for when the player attempts to right click the sheepuff. Dyes are handled here, shearing is done in the setSheared method.
     */
    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() instanceof DyeItem && !this.getSheared())
        {
            DyeColor color = ((DyeItem) itemstack.getItem()).getDyeColor();

            if (this.getFleeceColor() != color)
            {
                if (this.getPuffed() && itemstack.getCount() >= 2)
                {
                    this.setFleeceColor(color);
                    itemstack.shrink(2);
                }
                else if (!this.getPuffed())
                {
                    this.setFleeceColor(color);
                    itemstack.shrink(1);
                }
            }
        }

        return super.func_230254_b_(player, hand);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AetherSoundEvents.ENTITY_SHEEPUFF_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AetherSoundEvents.ENTITY_SHEEPUFF_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_SHEEPUFF_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState par4)
    {
        this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_SHEEP_STEP, SoundCategory.NEUTRAL, 0.15F, 1.0F);
    }

    @Override
    public void eatGrassBonus()
    {
        ++this.amountEaten;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Sheared", this.getSheared());
        compound.putBoolean("Puffed", this.getPuffed());
        compound.putByte("Color", (byte)this.getFleeceColor().getId());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setPuffed(compound.getBoolean("Puffed"));
        this.setFleeceColor(DyeColor.byId(compound.getByte("Color")));
    }

    public DyeColor getFleeceColor() {
        return DyeColor.byId(this.dataManager.get(FLEECE_COLOR) & 15);
    }


    public void setFleeceColor(DyeColor color) {
        byte b0 = this.dataManager.get(FLEECE_COLOR);
        this.dataManager.set(FLEECE_COLOR, (byte)(b0 & 240 | color.getId() & 15));
    }

    public boolean getSheared() {
        return this.dataManager.get(SHEARED);
    }

    public void setSheared(boolean flag) {
        this.dataManager.set(SHEARED, flag);
    }

    @Override
    protected void jump()
    {
        if(this.getPuffed())
        {
            this.setMotion(getMotion().x + this.rand.nextGaussian() * 0.5, 1.8,  getMotion().z + rand.nextGaussian() * 0.5);
        }
        else
        {
            this.setMotion(getMotion().x, 0.41999998688697815, getMotion().z);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getPuffed())
        {
            this.fallDistance = 0;

            if(this.getMotion().y < -0.05)
            {
                this.setMotion(getMotion().x, -0.05, getMotion().z);
            }
        }

        if(this.amountEaten >= 2 && !this.getSheared() && !this.getPuffed())
        {
            this.setPuffed(true);
            this.amountEaten = 0;
        }

        if(this.amountEaten == 1 && this.getSheared() && !this.getPuffed())
        {
            this.setSheared(false);
            this.setFleeceColor(DyeColor.WHITE);
            this.amountEaten = 0;
        }
    }

    @Override
    public void livingTick() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.livingTick();
    }


    public boolean getPuffed() {
        return this.dataManager.get(PUFFY);
    }

    public void setPuffed(boolean flag) {
        this.dataManager.set(PUFFY, flag);
    }

    /**
     * Chooses a "vanilla" sheep color based on the provided random.
     */
    public static DyeColor getRandomFleeceColor(Random random) {
        int i = random.nextInt(100);
        if (i < 5) {
            return DyeColor.LIGHT_BLUE;
        } else if (i < 10) {
            return DyeColor.CYAN;
        } else if (i < 15) {
            return DyeColor.LIME;
        } else if (i < 18) {
            return DyeColor.PINK;
        } else {
            return random.nextInt(500) == 0 ? DyeColor.WHITE : DyeColor.PURPLE;
        }
    }

    @Override
    public void shear(SoundCategory category) {
        this.world.playMovingSound(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, category, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.rand.nextInt(3);

        for(int j = 0; j < i; ++j) {
            ItemEntity itementity = this.entityDropItem(WOOL_BY_COLOR.get(this.getFleeceColor()), 1);
            if (itementity != null) {
                itementity.setMotion(itementity.getMotion().add((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F, (this.rand.nextFloat() * 0.05F), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F)));
            }
        }

    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.getSheared() && !this.isChild();
    }

    /*@Override
    public ResourceLocation getLootTable() {
        if (this.getSheared()) {
            return this.getType().getLootTable();
        } else {
            switch(this.getFleeceColor()) {
                case WHITE:
                default:
                    return LootTables.ENTITIES_SHEEP_WHITE;
                case ORANGE:
                    return LootTables.ENTITIES_SHEEP_ORANGE;
                case MAGENTA:
                    return LootTables.ENTITIES_SHEEP_MAGENTA;
                case LIGHT_BLUE:
                    return LootTables.ENTITIES_SHEEP_LIGHT_BLUE;
                case YELLOW:
                    return LootTables.ENTITIES_SHEEP_YELLOW;
                case LIME:
                    return LootTables.ENTITIES_SHEEP_LIME;
                case PINK:
                    return LootTables.ENTITIES_SHEEP_PINK;
                case GRAY:
                    return LootTables.ENTITIES_SHEEP_GRAY;
                case LIGHT_GRAY:
                    return LootTables.ENTITIES_SHEEP_LIGHT_GRAY;
                case CYAN:
                    return LootTables.ENTITIES_SHEEP_CYAN;
                case PURPLE:
                    return LootTables.ENTITIES_SHEEP_PURPLE;
                case BLUE:
                    return LootTables.ENTITIES_SHEEP_BLUE;
                case BROWN:
                    return LootTables.ENTITIES_SHEEP_BROWN;
                case GREEN:
                    return LootTables.ENTITIES_SHEEP_GREEN;
                case RED:
                    return LootTables.ENTITIES_SHEEP_RED;
                case BLACK:
                    return LootTables.ENTITIES_SHEEP_BLACK;
            }
        }
    }*/
}
