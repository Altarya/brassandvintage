package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.world.feature.tree.BaVRubberTreeGen;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.Random;

public class BaVBlockSapling extends BlockBush implements IGrowable, IHasModel {

    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BaVBlockSapling(String name, Material material) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(SoundType.PLANT);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(this.getBlockState().getBaseState().withProperty(STAGE, 0));

        BaVBlocks.BLOCKS.add(this);
        BaVItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SAPLING_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, (meta & 8) >> 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(STAGE) << 3;

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return worldIn.rand.nextDouble() < 0.45;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if(state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(worldIn, rand, pos, state);
        }
    }

    public void generateTree(World world, Random rand, BlockPos pos, IBlockState state) {
        if (TerrainGen.saplingGrowTree(world, rand, pos)) {
            WorldGenerator gen = rand.nextInt(10) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
            int i = 0, j = 0;
            boolean flag = false;

            gen = new BaVRubberTreeGen();

            IBlockState blockState = Blocks.AIR.getDefaultState();
            if (flag) {
                world.setBlockState(pos.add(0, 0, 0), blockState, 4);
                world.setBlockState(pos.add(1, 0, 0), blockState, 4);
                world.setBlockState(pos.add(0, 0, 1), blockState, 4);
                world.setBlockState(pos.add(1, 0, 1), blockState, 4);
            } else {
                world.setBlockState(pos, blockState, 4);
            }

            if (!gen.generate(world, rand, pos)) {
                if (flag) {
                    world.setBlockState(pos.add(0, 0, 0), blockState, 4);
                    world.setBlockState(pos.add(1, 0, 0), blockState, 4);
                    world.setBlockState(pos.add(0, 0, 1), blockState, 4);
                    world.setBlockState(pos.add(1, 0, 1), blockState, 4);
                } else {
                    world.setBlockState(pos, blockState, 4);
                }
            }

            System.out.println("Generating...");
            gen.generate(world, rand, pos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    @Override
    public void registerModels() {
        BaVInitialization.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
