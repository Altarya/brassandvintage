package alty.brassandvintagecore.world.feature.tree;

import alty.brassandvintagecore.blocks.BaVBlockLog;
import alty.brassandvintagecore.blocks.BaVBlockSapling;
import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.util.EnumHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BaVRubberTreeGen extends WorldGenAbstractTree {

    public static final IBlockState LOG = BaVBlocks.RUBBER_TREE_LOG.getDefaultState().withProperty(BaVBlockLog.VARIANT, EnumHandler.EnumType.RUBBER_LOG);
    public static final IBlockState LOG_ROOT = BaVBlocks.RUBBER_TREE_LOG.getDefaultState().withProperty(BaVBlockLog.VARIANT, EnumHandler.EnumType.RUBBER_LOG_ROOT);
    public static final IBlockState LEAF = BaVBlocks.RUBBER_TREE_LEAVES.getDefaultState();

    private int minHeight;

    public BaVRubberTreeGen() {
        super(false);
        this.minHeight = 7;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        int height = this.minHeight;
        boolean flag = true;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int yPos = y; yPos <= y + 1 + height; yPos++) {
            int b0 = 2;
            if (yPos == y) { b0 = 1; }
            if (yPos >= y + 1 + height - 2) { b0 = 2; }

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for (int xPos = x - b0; xPos <= x + b0 && flag; xPos++) {
                for (int zPos = z - b0; zPos <= z + b0 && flag; zPos++) {
                    if (yPos >= 0 && yPos < world.getHeight()) {
                        if (!this.isReplaceable(world, new BlockPos(xPos, yPos, zPos))) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                }
            }
        }

        if (!flag) {
            return false;
        } else {
            BlockPos down = pos.down();
            IBlockState state = world.getBlockState(down);
            boolean isSoil = state.getBlock().canSustainPlant(state, world, down, EnumFacing.UP, (BaVBlockSapling)BaVBlocks.RUBBER_TREE_SAPLING);

            if (isSoil && y < world.getHeight() - height - 1) {
                state.getBlock().onPlantGrow(state, world, down, pos);

                for (int yPos = y - 3 + height; yPos <= y + height; yPos++) {
                    int b1 = yPos - (y + height);
                    int b2 = 1 - b1 / 2;

                    for (int xPos = x - b2; xPos <= x + b2; xPos++) {
                        int b3 = xPos - x;
                        for (int zPos = z - b2; zPos <= z + b2; zPos++) {
                            int b4 = zPos - z;
                            if (Math.abs(b3) != b2 || Math.abs(b4) != b2 || rand.nextInt(2 ) != 0 && b1 != 0) {
                                BlockPos treePos = new BlockPos(xPos, yPos, zPos);
                                IBlockState treeState = world.getBlockState(treePos);
                                if (treeState.getBlock().isAir(treeState, world, treePos) || treeState.getBlock().isAir(treeState, world, treePos)) {
                                    setBlockAndNotifyAdequately(world, treePos, LEAF);
                                }
                            }
                        }
                    }
                }

                for (int logHeight = 0; logHeight < height; logHeight++) {
                    BlockPos up = pos.up(logHeight);
                    IBlockState logState = world.getBlockState(up);

                    if (logState.getBlock().isAir(logState, world, up) || logState.getBlock().isLeaves(logState, world, up)) {
                        setBlockAndNotifyAdequately(world, pos.up(logHeight), LOG);
                        if (logHeight == 0 || logHeight == height - 1) {
                            setBlockAndNotifyAdequately(world, pos.add(1, 0, 0), LOG_ROOT);
                            setBlockAndNotifyAdequately(world, pos.add(0, 0, 1), LOG_ROOT);
                            setBlockAndNotifyAdequately(world, pos.add(-1, 0, 0), LOG_ROOT);
                            setBlockAndNotifyAdequately(world, pos.add(1, 0, -1), LOG_ROOT);
                        }
                    }
                }

                return true;
            }
        }
        return true;
    }
}
