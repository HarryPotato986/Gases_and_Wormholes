package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class IntEditBox extends EditBox {
    public IntEditBox(Font font, int width, int height, Component message) {
        this(font, 0, 0, width, height, message);
    }

    public IntEditBox(Font font, int x, int y, int width, int height, Component message) {
        this(font, x, y, width, height, null, message);
    }

    public IntEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(font, x, y, width, height, editBox, message);

        char[] validChars = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        this.setFilter(s -> {
            if(s.isEmpty()) {
                return true;
            }

            boolean isFirstChar = true;
            char[] chars = s.toCharArray();
            for(char c : chars) {
                if (isFirstChar && c == '-') {
                    isFirstChar = false;
                    continue;
                }

                boolean isValid = false;
                for(char validChar : validChars) {
                    if(c == validChar) {
                        isValid = true;
                        break;
                    }
                }
                if(!isValid) {
                    return false;
                }
            }
            return true;
        });
    }

    public int getIntValue() {
        return Integer.parseInt(this.getValue());
    }
}
