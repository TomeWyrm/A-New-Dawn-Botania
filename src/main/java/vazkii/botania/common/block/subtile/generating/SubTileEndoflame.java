/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 15, 2014, 9:47:56 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileEndoflame extends SubTileGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	private static final int FUEL_CAP = ConfigHandler.endoflameFuelCap;
	private static final int RANGE = 3;

	int burnTime = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(linkedCollector != null) {
			if(burnTime == 0) {
				if(mana < getMaxMana() && !supertile.getWorldObj().isRemote) {
					boolean didSomething = false;

					List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord - RANGE, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + RANGE + 1, supertile.zCoord + RANGE + 1));
					for(EntityItem item : items) {
						if(item.age >= 59 && !item.isDead) {
							ItemStack stack = item.getEntityItem();
							if(stack.getItem().hasContainerItem(stack))
								continue;

							int burnTime = stack == null || stack.getItem() == Item.getItemFromBlock(ModBlocks.spreader) ? 0 : TileEntityFurnace.getItemBurnTime(stack);
							if(burnTime > 0 && stack.stackSize > 0) {
                                if(FUEL_CAP == -1) {
                                    this.burnTime = burnTime /2;
                                }
                                else{
                                    this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;
                                }

								stack.stackSize--;
								supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "botania:endoflame", 0.2F, 1F);

								if(stack.stackSize == 0)
									item.setDead();

								didSomething = true;

								break;
							}
						}
					}

					if(didSomething)
						sync();
				}
			} else {
				if(supertile.getWorldObj().rand.nextInt(8) == 0)
					Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);

				burnTime--;
			}
		}
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 3;
	}

	@Override
	public int getColor() {
		return 0x785000;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.endoflame;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_BURN_TIME, burnTime);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInteger(TAG_BURN_TIME);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 2;
	}

}
