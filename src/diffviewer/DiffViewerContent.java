package com.smartbear.collaborator.qa.entities.diffviewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DiffViewerContent {
    public static Log log = LogFactory.getLog(DiffViewerContent.class);

    private List<DiffViewerLine> lines;

    public DiffViewerContent() {
        this.lines = new ArrayList<>();
    }

    public DiffViewerContent(DiffViewerContent content) {
        this.lines = new ArrayList<>(content.lines);
    }

    public void addLine(DiffViewerLine line) {
        this.lines.add(line);
    }

    public void addLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText, LineState state) {
        this.addLine(beforeLineNumber, afterLineNumber, beforeText, afterText, state, null);
    }

    public void addLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText, LineState state, MessageType messageType) {
        this.lines.add(new DiffViewerLine(beforeLineNumber, afterLineNumber, beforeText, afterText, state, messageType));
    }

    public void addChangedLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText) {
        addChangedLine(beforeLineNumber, afterLineNumber, beforeText, afterText, null);
    }

    public void addChangedLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText, MessageType messageType) {
        this.lines.add(DiffViewerLine.changedLine(beforeLineNumber, afterLineNumber, beforeText, afterText, messageType));
    }

    public void addAddedLine(Integer lineNumber, String text) {
        addAddedLine(lineNumber, text, null);
    }

    public void addAddedLine(Integer lineNumber, String text, MessageType messageType) {
        this.lines.add(DiffViewerLine.addedLine(lineNumber, text, messageType));
    }

    public void addDeletedLine(Integer lineNumber, String text) {
        addDeletedLine(lineNumber, text, null);
    }

    public void addDeletedLine(Integer lineNumber, String text, MessageType messageType) {
        this.lines.add(DiffViewerLine.deletedLine(lineNumber, text, messageType));
    }

    public void addContextLine(Integer beforeLineNumber, Integer afterLineNumber, String text) {
        addContextLine(beforeLineNumber, afterLineNumber, text, null);
    }

    public void addContextLine(Integer beforeLineNumber, Integer afterLineNumber, String text, MessageType messageType) {
        this.lines.add(DiffViewerLine.contextLine(beforeLineNumber, afterLineNumber, text, messageType));
    }

    private List<DiffViewerLine> getLines(Predicate<DiffViewerLine> linePredicate) {
        // ArrayList<DiffViewerLine> properLines = new ArrayList<>();
        /*for (DiffViewerLine line : this.lines
                .stream()
                .filter(linePredicate).collect(Collectors.toCollection(Collection<DiffViewerLine>))
                .toArray(DiffViewerLine[]::new)) {
            properLines.add(line);
        }*/

        // !!! this.lines.stream().filter(linePredicate).forEach(line -> properLines.add(line));

        List<DiffViewerLine> properLines = this.lines.stream().filter(linePredicate).collect(Collectors.toList());

        return properLines;
    }

    private List<DiffViewerLine> getAllChangedLines () {
        return getLines(line -> line.getState() != LineState.CONTEXT);
    }

    private boolean checkLine(DiffViewerLine line) {
        Predicate<DiffViewerLine> searchPredicate = getLineComparePredicate(line);
        List<DiffViewerLine> presentedLines = getLines(searchPredicate);
        if (presentedLines.size() == 0) {
            log.error("cant find diff viewer line by pattern " + line);
            return false;
        }
        if (presentedLines.size() > 1) {
            log.error("there are more than one line conform pattern " + line);
            return false;
        }
        return presentedLines.get(0).check(line);
    }

    public boolean checkContextLine(Integer beforeLineNumber, Integer afterLineNumber, String text) {
        DiffViewerLine linePattern = DiffViewerLine.contextLine(beforeLineNumber, afterLineNumber == null ? beforeLineNumber : afterLineNumber, text);
        return checkLine(linePattern);
    }

    public boolean checkChangedLine(Integer beforeLineNumber, Integer afterLineNumber, String beforeText, String afterText) {
        DiffViewerLine linePattern = DiffViewerLine.changedLine(beforeLineNumber, afterLineNumber == null ? beforeLineNumber : afterLineNumber, beforeText, afterText);
        return checkLine(linePattern);
    }

    public boolean checkAddedLine(Integer lineNumber, String text) {
        DiffViewerLine linePattern = DiffViewerLine.addedLine(lineNumber, text);
        return checkLine(linePattern);
    }

    public boolean checkDeletedLine(Integer lineNumber, String text) {
        DiffViewerLine linePattern = DiffViewerLine.deletedLine(lineNumber, text);
        return checkLine(linePattern);
    }

    private static Predicate<DiffViewerLine> getLineComparePredicate(DiffViewerLine line) {
        Predicate<DiffViewerLine> linePredicate = null;
        switch (line.getState()) {
            case CHANGED:
            case CONTEXT:
            case DELETED:
                Integer beforeLineNumber = line.getBeforeLineNumber();
                if (beforeLineNumber == null) {
                    linePredicate = subjectLine -> subjectLine.getBeforeText().equals(line.getBeforeText());
                } else {
                    linePredicate = subjectLine -> subjectLine.getBeforeLineNumber() == beforeLineNumber;
                }
                break;
            case ADDED:
                Integer afterLineNumber = line.getAfterLineNumber();
                if (afterLineNumber == null) {
                    linePredicate = subjectLine -> subjectLine.getAfterText().equals(line.getAfterText());
                } else {
                    linePredicate = subjectLine -> subjectLine.getAfterLineNumber() == afterLineNumber;
                }
        }
        return linePredicate;
    }

    private DiffViewerLine findAndRemove(DiffViewerLine requiredLine) {
        Predicate<DiffViewerLine> linePredicate = DiffViewerContent.getLineComparePredicate(requiredLine);
        Iterator<DiffViewerLine> iterator = this.lines.iterator();
        while (iterator.hasNext()) {
            DiffViewerLine line = iterator.next();
            if (linePredicate.test(line)) {
                iterator.remove();
                return line;
            }
        }
        return null;
    }

    public boolean checkAllChanges(DiffViewerContent changes) {
        DiffViewerContent workingCopy = new DiffViewerContent(this);
        boolean correct = true;
        for (DiffViewerLine change : changes.lines) {
            DiffViewerLine line = workingCopy.findAndRemove(change);
            if (line == null) {
                correct = false;
                log.error("there are no change " + change);
            } else {
                if (!line.check(change)) {
                    correct = false;
                }
            }
        }
        List<DiffViewerLine> leftower = workingCopy.getAllChangedLines();
        if (leftower.size() != 0) {
            log.error("diff viewer contains some excessive changes:");
            leftower.forEach(line -> log.error(line.toString()));
            return false;
        }
        return correct;
    }
}
