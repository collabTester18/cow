package com.smartbear.collaborator.qa.entities.diffviewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiffViewerLine {
    public static Log log = LogFactory.getLog(DiffViewerLine.class);

    private Integer beforeLineNumber;
    private Integer afterLineNumber;
    private String beforeText;
    private String afterText;
    private LineState state;
    private MessageType messageType;

    public DiffViewerLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText, LineState state, MessageType messageType) {
        this.beforeLineNumber = beforeLineNumber;
        this.afterLineNumber = afterLineNumber;
        this.beforeText = beforeText;
        this.afterText = afterText;
        this.state = state;
        this.messageType = messageType;
    }

    public static DiffViewerLine changedLine(Integer lineNumber, String beforeText, String afterText) {
        return changedLine(lineNumber, lineNumber, beforeText, afterText, null);
    }

    public static DiffViewerLine changedLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText) {
        return changedLine(beforeLineNumber, afterLineNumber, beforeText, afterText, null);
    }

    public static DiffViewerLine changedLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText, MessageType messageType) {
        return new DiffViewerLine(beforeLineNumber, afterLineNumber, beforeText, afterText, LineState.CHANGED, messageType);
    }

    public static DiffViewerLine addedLine(Integer lineNumber, String text) {
        return addedLine(lineNumber, text, null);
    }

    public static DiffViewerLine addedLine(Integer lineNumber, String text, MessageType messageType) {
        return new DiffViewerLine(null, lineNumber, null, text, LineState.ADDED, messageType);
    }

    public static DiffViewerLine deletedLine(Integer lineNumber, String text) {
        return deletedLine(lineNumber, text, null);
    }

    public static DiffViewerLine deletedLine(Integer lineNumber, String text, MessageType messageType) {
        return new DiffViewerLine(lineNumber, null, text, null, LineState.DELETED, messageType);
    }

    public static DiffViewerLine contextLine(Integer lineNumber, String text) {
        return contextLine(lineNumber, lineNumber, text, null);
    }

    public static DiffViewerLine contextLine(Integer beforeLineNumber, Integer afterLineNumber, String text) {
        return contextLine(beforeLineNumber, afterLineNumber, text, null);
    }

    public static DiffViewerLine contextLine(Integer beforeLineNumber, Integer afterLineNumber, String text, MessageType messageType) {
        return new DiffViewerLine(beforeLineNumber, afterLineNumber, text, text, LineState.CONTEXT, messageType);
    }

    public Integer getBeforeLineNumber() {
        return beforeLineNumber;
    }

    /*public void setBeforeLineNumber(Integer beforeLineNumber) {
        this.beforeLineNumber = beforeLineNumber;
    }*/

    public Integer getAfterLineNumber() {
        return afterLineNumber;
    }

    /*public void setAfterLineNumber(Integer afterLineNumber) {
        this.afterLineNumber = afterLineNumber;
    }*/

    public String getBeforeText() {
        return beforeText;
    }

    /*public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }*/

    public String getAfterText() {
        return afterText;
    }

    /*public void setAfterText(String afterText) {
        this.afterText = afterText;
    }*/

    public LineState getState() {
        return state;
    }

    /*public void setState(LineState state) {
        this.state = state;
    }*/

    public MessageType getMessage() {
        return messageType;
    }

    /*public void setMessage(MessageType messageType) {
        this.messageType = messageType;
    }*/

    @Override
    public String toString() {
        return this.state.getState().toUpperCase() + ": (" + this.beforeLineNumber + ", \"" + this.beforeText + "\" ,"
                + this.afterLineNumber + ", \"" + this.afterText + "\")";
    }

    private static void logNotEqualLines(String message, DiffViewerLine expected, DiffViewerLine actual) {
        log.error(message + "\r\n" + "EXPECTED: " + expected + "\r\nACTUAL: " + actual);
    }

    public boolean check(DiffViewerLine line) {
        if (line.state != this.state) {
            logNotEqualLines("Diff Viewer line is in different state", line, this);
            return false;
        }
        switch(line.getState()) {
            case CHANGED:
            case CONTEXT:
            case DELETED: {
                if (this.beforeLineNumber != line.beforeLineNumber) {
                    logNotEqualLines("Diff Viewer line has different before line number", line, this);
                    return false;
                }
                if (!this.beforeText.equals(line.beforeText)) {
                    logNotEqualLines("Diff Viewer line has different before text", line, this);
                    return false;
                }
            }
        }
        switch(line.getState()) {
            case CHANGED:
            case CONTEXT:
            case ADDED: {
                if (this.afterLineNumber != line.afterLineNumber) {
                    logNotEqualLines("Diff Viewer line has different after line number", line, this);
                    return false;
                }
                if (!this.afterText.equals(line.afterText)) {
                    logNotEqualLines("Diff Viewer line has different after text", line, this);
                    return false;
                }
            }
        }
        return true;
    }
}