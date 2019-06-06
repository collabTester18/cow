package com.smartbear.collaborator.qa.entities.review;

import com.smartbear.collaborator.qa.entities.review.materials.MaterialsFile;
import com.smartbear.collaborator.qa.entities.review.materials.Section;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ReviewSummary {
    private String title;
    private int id;

    private ArrayList<Section> sections;

    public ReviewSummary() {
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MaterialsFile findFile(String fileName, String sectionTitle, String scmId) {
        if (sectionTitle == null && scmId == null) {
            for (Section section : sections) {
                MaterialsFile file = section.findFile(fileName);
                if (file != null) {
                    return file;
                }
            }
            return null;

        } else {
            throw new RuntimeException("STUB");
        }
    }

    public MaterialsFile getFile(String fileName, String sectionTitle, String scmId) {
        MaterialsFile file = this.findFile(fileName, sectionTitle, scmId);
        if (file == null) {
            throw new NoSuchElementException("there is no file with name \"" + fileName + "\" inside review materils");
        }
        return file;
    }

    public boolean isFileExists(String fileName) {
        return this.findFile(fileName, null, null) != null;
    }

    public int getAddedLines(String fileName) {
        MaterialsFile file = getFile(fileName, null, null);
        return file.getAddedLines();
    }

    public int getChangedLines(String fileName) {
        MaterialsFile file = getFile(fileName, null, null);
        return file.getChangedLines();
    }

    public int getDeletedLines(String fileName) {
        MaterialsFile file = getFile(fileName, null, null);
        return file.getDeletedLines();
    }

    public int getStatus(String fileName) {
        MaterialsFile file = getFile(fileName, null, null);
        return file.getStatus();
    }
}
