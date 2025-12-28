package com.jira.board_service.constance;

import java.util.List;

public class DefaultBoardColumns {

    public static final List<ColumnDef> DEFAULT_COLUMNS = List.of(
            new ColumnDef("To Do", 1, false),
            new ColumnDef("In Review", 2, false),
            new ColumnDef("In Process", 3, false),
            new ColumnDef("In Testing", 4, false),
            new ColumnDef("Done", 5, true)
    );

    public record ColumnDef(String name, int position, boolean isDone) {}
}
