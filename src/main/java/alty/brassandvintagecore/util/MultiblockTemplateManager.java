package alty.brassandvintagecore.util;

import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.objects.IStructure;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * This is a modified version of 1.14 immersive engineering code, all credits goes to them
 * 
 * A special thanks to malte0811 and SkySom, who were patient enough to explain things to me(Alty)
 * 
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/1.14/src/main/java/blusunrize/immersiveengineering/api/multiblocks/TemplateMultiblock.java
 * 
 * */

//TODO rotaion of blocks?
@Slf4j
public abstract class MultiblockTemplateManager implements IMultiblock
{
	private MinecraftServer mcServer = IStructure.worldServer.getMinecraftServer();
	private TemplateManager manager = IStructure.worldServer.getStructureTemplateManager();
	private ResourceLocation loc;
	private BlockPos masterFromOrigin;
	public BlockPos triggerFromOrigin;
	private Map<Block, OreDictionary> tags;
	@Nullable
	private Template template = manager.get(mcServer, loc);

	@Nullable
	private IngredientStack[] materials;
	private IBlockState trigger = Blocks.AIR.getDefaultState();

	public MultiblockTemplateManager(String loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin)
	{
		this(loc, masterFromOrigin, triggerFromOrigin, ImmutableMap.of());
	}

	public MultiblockTemplateManager(String loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, Map<Block, OreDictionary> tags)
	{
		this.loc = new ResourceLocation(BavInitialization.MODID, "multiblocks/"+loc);
		this.masterFromOrigin = masterFromOrigin;
		this.triggerFromOrigin = triggerFromOrigin;
		this.tags = tags;
	}

	@Nonnull
	private Template getTemplate()
	{
		if(template==null)//TODO reset on resource reload
		{
			try
			{
				List<Template.BlockInfo> blocks;
				Field blocksField = template.getClass().getDeclaredField("blocks");
				blocksField.setAccessible(true);
				blocks = (List<BlockInfo>) blocksField.get(template);
				for(int i = 0; i < blocks.size(); i++)
				{
					Template.BlockInfo info = blocks.get(i);
					if(info.pos.equals(triggerFromOrigin))
						trigger = info.blockState;
					if(info.blockState==Blocks.AIR.getDefaultState())
					{
						blocks.remove(i);
						i--;
					}
				}
				materials = null;
			} catch (NoSuchFieldException | IllegalAccessException e) {
			       e.printStackTrace();
			}
		}
		return template;
	}

	
	public ResourceLocation getMultiblockLocation()
	{
		return loc = new ResourceLocation("brassandvintagecore:mutliblocks/"+getUniqueName());
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		getTemplate();
		//TODO facing dependant
		return state.getBlock()==trigger.getBlock();
	}
	
	
	public static Rotation getRotationBetweenFacings(EnumFacing orig, EnumFacing to)
    {
        if(to==orig)
            return Rotation.NONE;
        if(orig.getAxis()==Axis.Y||to.getAxis()==Axis.Y)
            return null;
        orig = orig.rotateY();
        if(orig==to)
            return Rotation.CLOCKWISE_90;
        orig = orig.rotateY();
        if(orig==to)
            return Rotation.CLOCKWISE_180;
        orig = orig.rotateY();
        if(orig==to)
            return Rotation.COUNTERCLOCKWISE_90;
        return null;//This shouldn't ever happen
    }

	public boolean isIn(IBlockState block1, IBlockState block2) {
		ItemStack item1 = new ItemStack(block1.getBlock(), 1, block1.getBlock().getMetaFromState(block1));
		ItemStack item2 = new ItemStack(block2.getBlock(), 1, block2.getBlock().getMetaFromState(block1));
		if(OreDictionary.itemMatches(item2, item1, true)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{
		List<Template.BlockInfo> blocks;
		Field blocksField;
		try {
			blocksField = template.getClass().getDeclaredField("blocks");
			blocksField.setAccessible(true);
			blocks = (List<BlockInfo>) blocksField.get(template);
			
			if(side.getAxis()==Axis.Y)
				side = EnumFacing.fromAngle(player.rotationYaw);
			Rotation rot = getRotationBetweenFacings(EnumFacing.NORTH, side.getOpposite());
			if(rot==null)
				return false;
			Template template = getTemplate();
			List<Mirror> mirrorStates;
			if(canBeMirrored())
				mirrorStates = ImmutableList.of(Mirror.NONE, Mirror.FRONT_BACK);
			else
				mirrorStates = ImmutableList.of(Mirror.NONE);
			mirrorLoop:
			for(Mirror mirror : mirrorStates)
			{
				PlacementSettings placeSet = new PlacementSettings().setMirror(mirror).setRotation(rot);
				BlockPos origin = pos.subtract(Template.transformedBlockPos(placeSet, triggerFromOrigin));
				for(Template.BlockInfo info : blocks)
				{
					BlockPos realRelPos = Template.transformedBlockPos(placeSet, info.pos);
					BlockPos here = origin.add(realRelPos);

					IBlockState expected = info.blockState.withMirror(mirror).withRotation(rot);

					IBlockState inWorld = world.getBlockState(here);
					boolean valid;
					List<String> taglist = new ArrayList<String>(); 
					taglist.add(tags.toString());
					int size = taglist.size();
					ItemStack expectedStack = new ItemStack(expected.getBlock(), 1, expected.getBlock().getMetaFromState(expected));
					for(int r = 0; r<=size; r++) {
						if(Utils.compareToOreName(expectedStack, taglist.get(r)))
							valid = isIn(inWorld, expected);
						else
							valid = inWorld==expected;
						if(!valid)
							continue mirrorLoop;
					}
				}
				form(world, origin, rot, mirror, side);
				return true;
			}
			return false;
		} catch (NoSuchFieldException | IllegalAccessException e) {
			log.error(e.getLocalizedMessage());
		} catch (SecurityException e) {
			log.error(e.getLocalizedMessage());
		}
		return false;
	}

	protected void form(World world, BlockPos pos, Rotation rot, Mirror mirror, EnumFacing sideHit)
	{
		BlockPos masterPos = withSettingsAndOffset(pos, masterFromOrigin, mirror, rot);
		for(BlockInfo block : getStructure())
		{
			BlockPos actualPos = withSettingsAndOffset(pos, block.pos, mirror, rot);
			replaceStructureBlock(block, world, actualPos, mirror!=Mirror.NONE, sideHit,
					actualPos.subtract(masterPos));
		}
	}

	protected abstract void replaceStructureBlock(BlockInfo info, World world, BlockPos actualPos, boolean mirrored, EnumFacing clickDirection, Vec3i offsetFromMaster);

	public final List<BlockInfo> getStructure()
	{
		List<Template.BlockInfo> blocks = new ArrayList<Template.BlockInfo>();
		Field blocksField;
		try {
			blocksField = getTemplate().getClass().getDeclaredField("blocks");
			blocksField.setAccessible(true);
			blocks = (List<BlockInfo>) blocksField.get(template);
		} catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
			log.error(e.getLocalizedMessage());
		}
		return blocks;
	}

	public final Vec3i getSize()
	{
		return getTemplate().getSize();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean overwriteBlockRender(ItemStack state, int iterator)
	{
		return false;
	}

	public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, Mirror mirror, Rotation rot)
	{
		PlacementSettings settings = new PlacementSettings().setMirror(mirror).setRotation(rot);
		return origin.add(Template.transformedBlockPos(settings, relative));
	}

	public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, boolean mirrored, EnumFacing facing)
	{
		Rotation rot = getRotationBetweenFacings(EnumFacing.NORTH, facing);
		if(rot==null)
			return origin;
		return withSettingsAndOffset(origin, relative, mirrored?Mirror.FRONT_BACK: Mirror.NONE,
				rot);
	}

	public void disassemble(World world, BlockPos origin, boolean mirrored, EnumFacing clickDirectionAtCreation)
	{
		Mirror mirror = mirrored?Mirror.FRONT_BACK: Mirror.NONE;
		Rotation rot = getRotationBetweenFacings(EnumFacing.NORTH, clickDirectionAtCreation);
		Preconditions.checkNotNull(rot);
		for(BlockInfo block : getStructure())
		{
			BlockPos actualPos = withSettingsAndOffset(origin, block.pos, mirror, rot);
			prepareBlockForDisassembly(world, actualPos);
			world.setBlockState(actualPos, block.blockState.withMirror(mirror).withRotation(rot));
		}
	}

	protected void prepareBlockForDisassembly(World world, BlockPos pos)
	{
	}
	
	public BlockPos getTriggerOffset()
	{
		return triggerFromOrigin;
	}

	public boolean canBeMirrored()
	{
		return true;
	}
}