package kr.asmbl.glow_outline_fix.mixin;

import kr.asmbl.glow_outline_fix.GlyphPixelSize;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * 비트맵 글리프의 scale = provider height ÷ 이미지 셀 높이 = 도트 1칸의 크기.
 * 예) 갈무리 hangul.png: height 12 → 도트 ≈ ⅔칸 (GUI 3 에서 2px)
 */
@Mixin(targets = "net.minecraft.client.gui.font.providers.BitmapProvider$Glyph")
public abstract class BitmapGlyphMixin implements GlyphPixelSize {
	@Shadow @Final float scale;

	@Override
	public float glowOutlineFix$pixelSize() {
		return this.scale;
	}
}
