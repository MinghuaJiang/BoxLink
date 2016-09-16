package edu.virginia.cs.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ClipboardUtil {
    public static void copyStringtoClipBoard(String text){
        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        board.setContents(tText, null);
    }
}
