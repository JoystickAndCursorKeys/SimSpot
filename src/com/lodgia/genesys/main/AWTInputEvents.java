package com.lodgia.genesys.main;

import java.awt.event.KeyEvent;

public class AWTInputEvents {

	public static final int IE_NOKEY 	 =-1;
	
	public static final int IE_QUIT      =KeyEvent.VK_ESCAPE;
	public static final int IE_REALTIME  =KeyEvent.VK_PAUSE;
	public static final int IE_PLAYLASTWINNER  =KeyEvent.VK_P;
	public static final int IE_MOVEUP    =KeyEvent.VK_8;
	public static final int IE_MOVEDOWN  =KeyEvent.VK_2;
	public static final int IE_MOVELEFT  =KeyEvent.VK_LEFT;
	public static final int IE_MOVERIGHT =KeyEvent.VK_RIGHT;
	public static final int IE_TURNLEFT  =KeyEvent.VK_Z;
	public static final int IE_TURNRIGHT =KeyEvent.VK_X;
	public static final int IE_PUSHFORWARD  =KeyEvent.VK_UP;
	public static final int IE_PUSHBACKWARD =KeyEvent.VK_DOWN;
	public static final int IE_DUMP 		=KeyEvent.VK_D;

	public static int convert(int keycode)
	{
		return keycode;
	}
}

