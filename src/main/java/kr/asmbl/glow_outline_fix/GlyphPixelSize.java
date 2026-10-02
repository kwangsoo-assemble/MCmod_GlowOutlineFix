package kr.asmbl.glow_outline_fix;

/**
 * 글리프 도트 1칸의 크기(폰트 단위). 바닐라 ascii 는 1.0.
 * BitmapProvider$Glyph 가 package-private 이라 Mixin 으로 이 인터페이스를 붙여서 읽는다.
 */
public interface GlyphPixelSize {
	float glowOutlineFix$pixelSize();
}
