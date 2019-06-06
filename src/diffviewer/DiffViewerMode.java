package com.smartbear.collaborator.qa.entities.diffviewer;

public enum DiffViewerMode {
    ALL("All"),
    FIRST_VS_LAST("First vs Last"),
    BRANCH_ONLY("Branch only"),
    LAST_COMMIT("Last commit"),
    ACCEPTED("Accepted"),
    COMMITS("Commits");

    private String name;

    public String getName() {
        return this.name;
    }

    private DiffViewerMode(String name) {
        this.name = name;
    }


    public static DiffViewerMode fromValue(String name) throws IllegalArgumentException {
        DiffViewerMode[] allDiffViewerModes = DiffViewerMode.values();
        for (DiffViewerMode mode : allDiffViewerModes) {
            if (mode.getName().equals(name)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown LineState enum value :" + name);
    }
}
