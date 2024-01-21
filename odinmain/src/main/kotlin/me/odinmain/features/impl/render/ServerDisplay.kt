package me.odinmain.features.impl.render

import me.odinmain.features.Category
import me.odinmain.features.Module
import me.odinmain.features.settings.impl.BooleanSetting
import me.odinmain.features.settings.impl.HudSetting
import me.odinmain.ui.hud.HudElement
import me.odinmain.utils.ServerUtils
import me.odinmain.utils.max
import me.odinmain.utils.render.Color
import me.odinmain.utils.render.gui.Fonts
import me.odinmain.utils.render.gui.getTextWidth
import me.odinmain.utils.render.gui.textWithControlCodes
import me.odinmain.utils.round

object ServerDisplay : Module(
    name = "Server Hud",
    category = Category.RENDER,
    description = "Displays your current ping and the server's TPS."
) {
    private val ping: Boolean by BooleanSetting("Ping", true)
    private val tps: Boolean by BooleanSetting("TPS", true)
    private val fps: Boolean by BooleanSetting("FPS", false)


    private val hud: HudElement by HudSetting("Display", 10f, 10f, 1f, false) {
        if (it) {
            if (ping) textWithControlCodes("§6Ping: §a60ms", 1f, 9f, Color.WHITE,16f, Fonts.REGULAR)
            if (tps) textWithControlCodes("§3TPS: §a20.0", 1f, 26f, Color.WHITE,16f, Fonts.REGULAR)
            if (fps) textWithControlCodes("§dFPS: §a240.0", 1f, 43f, Color.WHITE,16f, Fonts.REGULAR)
            max(
                if (ping) getTextWidth("Ping: 60ms", 16f, Fonts.REGULAR) else 0f,
                if (tps) getTextWidth("TPS: 20.0", 16f, Fonts.REGULAR) else 0f,
                if (fps) getTextWidth("§dFPS: §a240.0", 16f, Fonts.REGULAR) else 0f
            ) + 2f to 33f
        } else {
            if (ping) textWithControlCodes("§6Ping: ${colorizePing(ServerUtils.averagePing.toInt())}ms", 1f, 9f, Color.WHITE,16f, Fonts.REGULAR)
            if (tps) textWithControlCodes("§3TPS: ${colorizeTps(ServerUtils.averageTps.round(1))}", 1f, 26f, Color.WHITE,16f, Fonts.REGULAR)
            if (fps) textWithControlCodes("§dFPS: ${colorizeFPS(mc.debug.split(" ")[0].toIntOrNull() ?: 0)}", 1f, 43f, Color.WHITE,16f, Fonts.REGULAR)
            max(
                getTextWidth("§ePing: ${colorizePing(ServerUtils.averagePing.toInt())}ms", 16f, Fonts.REGULAR),
                getTextWidth("§ePing: ${colorizePing(ServerUtils.averagePing.toInt())}ms", 16f, Fonts.REGULAR)
            ) + 2f to 33f
        }
    }

    private fun colorizePing(ping: Int): String {
        return when {
            ping < 150 -> "§a$ping"
            ping < 200 -> "§e$ping"
            ping < 250 -> "§c$ping"
            else -> "§4$ping"
        }
    }

    private fun colorizeTps(tps: Double): String {
        return when {
            tps > 18.0 -> "§a$tps"
            tps > 15.0 -> "§e$tps"
            tps > 10.0 -> "§c$tps"
            else -> "§4$tps"
        }
    }

    private fun colorizeFPS(fps: Int): String {
        return when {
            fps > 200 -> "§a$fps"
            fps > 100.0 -> "§e$fps"
            fps > 60.0 -> "§c$fps"
            else -> "§4$fps"
        }
    }

}