package me.odinmain.events.impl

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityBat
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Event

open class SecretPickupEvent : Event() {
    class Interact(val blockPos: BlockPos, val blockState: IBlockState) : SecretPickupEvent()
    class Item(val entity: EntityItem) : SecretPickupEvent()
    class Bat(val entity: EntityBat) : SecretPickupEvent()
}