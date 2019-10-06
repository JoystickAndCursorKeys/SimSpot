package com.lodgia.genesys.gfx.interfaces;

import java.awt.Rectangle;

public interface InterfaceGraphicalWorld {

	void setRenderer(InterfaceWorldGenericRenderer  r);
	void callRenderer(Object paintobject, Rectangle bounds, int xoffset, int yoffset );
}
