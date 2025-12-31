package com.jira.board_service.constance;

import java.util.List;

public class DefaultBoardColumns {

    public static final List<ColumnDef> DEFAULT_COLUMNS = List.of(
            new ColumnDef("To_Do", 1, true),
            new ColumnDef("In_Review", 2, false),
            new ColumnDef("IN_PROGRESS", 3, false),
            new ColumnDef("In_Testing", 4, false),
            new ColumnDef("Done", 5, false)
    );

    public record ColumnDef(String name, int position, boolean isDone) {}
}
