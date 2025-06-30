package expensive.display.display.impl;

import expensive.display.display.ElementRenderer;
import expensive.events.EventDisplay;
import expensive.util.visual.main.color.ColorUtils;
import expensive.util.visual.main.fonts.Fonts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoordsRenderer implements ElementRenderer {


    @Override
    public void render(EventDisplay eventDisplay) {
        float offset = 3;
        float fontSize = 7;
        float fontHeight = Fonts.sfui.getHeight(fontSize);

        float posX = offset;
        float posY = window.getScaledHeight() - offset - fontHeight;

        float stringWidth = Fonts.sfui.getWidth("XYZ: ", fontSize);

        Fonts.sfui.drawTextWithOutline(eventDisplay.getMatrixStack(), "XYZ: ", posX, posY, -1, fontSize, 0.05f);

        Fonts.sfui.drawTextWithOutline(eventDisplay.getMatrixStack(), (int) mc.player.getPosX() + ", "
                + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ(), posX + stringWidth, posY, ColorUtils.rgb(158, 255, 185), fontSize, 0.05f);

        posY -= 12;
        stringWidth = Fonts.sfui.getWidth("BPS: ", fontSize);

        Fonts.sfui.drawTextWithOutline(eventDisplay.getMatrixStack(), "BPS: ", posX, posY, -1, fontSize, 0.05f);

        Fonts.sfui.drawTextWithOutline(eventDisplay.getMatrixStack(), String.format("%.2f", Math.hypot(mc.player.prevPosX - mc.player.getPosX(), mc.player.prevPosZ - mc.player.getPosZ()) * 20), posX + stringWidth, posY, ColorUtils.rgb(158, 255, 185), fontSize, 0.05f);

    }
}
