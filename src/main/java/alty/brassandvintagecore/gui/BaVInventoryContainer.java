package alty.brassandvintagecore.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaVInventoryContainer extends Container {

	private static final int DEFAULT_INV_ARMOR_START=BaVInventory.NUMSLOTS;
	private static final int DEFAULT_INV_START=DEFAULT_INV_ARMOR_START+4;
	private static final int DEFAULT_INV_HOTBAR_START=DEFAULT_INV_START+27;
	private static final int DEFAULT_INV_SHIELD=DEFAULT_INV_HOTBAR_START+9;
	
	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	
	private static final int FOOD_SLOTS_COUNT=3;
	private static final int AMMO_SLOTS_COUNT=8;
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	public BaVInventoryContainer(EntityPlayer player){
		
		InventoryPlayer inv = player.inventory;
		/* Imported TG stuff //TODO
		TGPlayerInventory tgplayerinv = TGExtendedPlayer.get(player).tg_inventory;
		
		//custom slots:
		this.addSlotToContainer(new SlotTG(tgplayerinv,TGPlayerInventory.SLOT_FACE, 77, 8,TGSlotType.FACESLOT));

		this.addSlotToContainer(new SlotTG(tgplayerinv, TGPlayerInventory.SLOT_BACK, 77, 26,TGSlotType.BACKSLOT));
		
		this.addSlotToContainer(new SlotTG(tgplayerinv, TGPlayerInventory.SLOT_HAND, 77, 44,TGSlotType.HANDSLOT));

		this.addSlotToContainer(new SlotFood(tgplayerinv,TGPlayerInventory.SLOTS_AUTOFOOD_START,116,24));
		this.addSlotToContainer(new SlotFood(tgplayerinv,TGPlayerInventory.SLOTS_AUTOFOOD_START+1,116+18,24));
		this.addSlotToContainer(new SlotFood(tgplayerinv,TGPlayerInventory.SLOTS_AUTOFOOD_START+2,116+18*2,24));
		
		this.addSlotToContainer(new SlotTG(tgplayerinv,TGPlayerInventory.SLOT_AUTOHEAL,97,24,TGSlotType.HEALSLOT));
		
		for (int i=0;i<2;i++){
			for(int j=0;j<4;j++){
				this.addSlotToContainer(new SlotTG(tgplayerinv,TGPlayerInventory.SLOTS_AMMO_START+(i*4)+j,98+j*18,44+i*18,TGSlotType.AMMOSLOT));
			}
		}*/
		
		
		//Default Slots:
		//Armor
		for (int k = 0; k < 4; ++k) {
			EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
			//this.addSlotToContainer(new SlotArmor(inv, 36 + (3 - k), 8, 8 + k * 18, entityequipmentslot, player));
		}
		int i,j;
		//Inventory:
        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }
		
		//Hotbar:

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }
		
        this.addSlotToContainer(new Slot(inv, 40, 77, 62)
        {
            @Nullable
            @SideOnly(Side.CLIENT)
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

	}

	
	
}
