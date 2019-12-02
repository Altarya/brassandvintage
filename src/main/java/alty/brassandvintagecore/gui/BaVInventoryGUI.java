package alty.brassandvintagecore.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import alty.brassandvintagecore.init.BaVInitialization;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaVInventoryGUI extends BaVBaseGUI {


	public static ResourceLocation texture = new ResourceLocation(BaVInitialization.MODID,"textures/gui/tgplayerinventory.png");
	private final BaVInventory inventory;
	private float sizex;
	private float sizey;
	
	private static final int togglebuttons_xpos= -8;

	public BaVInventoryGUI(Container inventorySlotsIn, EntityPlayer player) {
		super(new BaVInventoryContainer(player));
		this.inventory = null;
		this.tex=texture;
	}

	
	private List<String> getTooltipForToggleButton(int index){
		List<String> tooltip = new ArrayList<String>();
		
		boolean state=false;
		
		return tooltip;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		 int mx = mouseX - (this.width-this.xSize)/2;
	     int my = mouseY - (this.height-this.ySize)/2;
	        
		 for(int i=0;i<5;i++){
	        if (this.isInRect(mx, my, togglebuttons_xpos, 7+i*11, 11, 11)){
	        	this.drawHoveringText(getTooltipForToggleButton(i), mx, my);
	        }
        }
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTickTime, mouseX, mouseY);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		
		
        GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (float)(k + 51) - this.sizex, (float)(l + 75 - 50) - this.sizey, this.mc.player);
	}

	@Override
	public void drawScreen(int i1, int i2, float f1) {
		super.drawScreen(i1, i2, f1);
		this.sizex=(float)i1;
		this.sizey=(float)i2;
	}



	@Override
	public void initGui() {
		super.initGui();
		int index=0;
		
		//GC
		TabRegistry.updateTabValues(this.guiLeft, this.guiTop, BaVTab.class);
		TabRegistry.addTabsToList(this.buttonList);
		/*
	    for(int x =0; x<5;x++){
	    	this.buttonList.add(new GuiToggleButton(++index, this.guiLeft+togglebuttons_xpos, this.guiTop+7+(x*11), x));
	    }*/
	}

}
