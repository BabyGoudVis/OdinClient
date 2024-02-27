package me.odinmain.features.impl.kuudra

import me.odinmain.events.impl.RenderEntityOutlineEvent
import me.odinmain.features.Category
import me.odinmain.features.Module
import me.odinmain.features.impl.kuudra.FreshTimer.highlightFresh
import me.odinmain.features.impl.kuudra.FreshTimer.highlightFreshColor
import me.odinmain.features.settings.Setting.Companion.withDependency
import me.odinmain.features.settings.impl.BooleanSetting
import me.odinmain.features.settings.impl.ColorSetting
import me.odinmain.utils.addVec
import me.odinmain.utils.distanceSquaredTo
import me.odinmain.utils.render.Color
import me.odinmain.utils.render.world.RenderUtils
import me.odinmain.utils.render.world.RenderUtils.renderVec
import me.odinmain.utils.skyblock.KuudraUtils
import me.odinmain.utils.skyblock.LocationUtils
import net.minecraft.entity.Entity
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TeamHighlight : Module(
    name = "Team Highlight",
    description = "Highlights your teammates in Kuudra.",
    category = Category.KUUDRA
) {
    private val playerOutline: Boolean by BooleanSetting("Player Outline", true, description = "Outlines the player")
    private val highlightName: Boolean by BooleanSetting("Name Highlight", true, description = "Highlights the player name")
    private val outlineColor: Color by ColorSetting("Outline Color", Color.PURPLE, true, description = "Color of the player outline").withDependency { playerOutline }
    private val nameColor: Color by ColorSetting("Name Color", Color.PINK, true, description = "Color of the name highlight").withDependency { highlightName }

    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityOutlineEvent) {
        if (event.type !== RenderEntityOutlineEvent.Type.XRAY || !playerOutline || LocationUtils.currentArea != "Kuudra") return

        event.queueEntitiesToOutline { entity -> getTeammates(entity) }
    }

    @SubscribeEvent
    fun handleNames(event: RenderWorldLastEvent) {
        if (!highlightName || LocationUtils.currentArea != "Kuudra" || KuudraUtils.phase < 1) return
        KuudraUtils.kuudraTeammates.forEach {
             if (it.entity == null || it.playerName == mc.thePlayer.name) return@forEach
            if ((it.entity?.distanceSquaredTo(mc.thePlayer) ?: return@forEach) >= 2333) return@forEach

            RenderUtils.drawStringInWorld(it.entity?.name ?: return@forEach, it.entity?.renderVec?.addVec(y = 2.6) ?: return@forEach,
                if (it.eatFresh) highlightFreshColor.rgba else nameColor.rgba,
                depthTest = false, increase = false, renderBlackBox = false,
                scale = 0.05f
            )
        }
    }

    private fun getTeammates(entity: Entity): Int? {
        val teammates = KuudraUtils.kuudraTeammates.filter { it.playerName != mc.thePlayer.name }
        val teammate = teammates.find { it.entity == entity } ?: return null

        return if (teammate.eatFresh && highlightFresh) highlightFreshColor.rgba else outlineColor.rgba
    }
}